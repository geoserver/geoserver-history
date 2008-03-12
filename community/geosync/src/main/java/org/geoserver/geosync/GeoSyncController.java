package org.geoserver.geosync;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.PrintWriter;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndLink;
import com.sun.syndication.feed.synd.SyndLinkImpl;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.propono.atom.common.AtomService;
import com.sun.syndication.propono.atom.common.Workspace;
import com.sun.syndication.propono.atom.common.Collection;
import com.sun.syndication.feed.module.georss.GeoRSSModule;
import com.sun.syndication.feed.module.georss.W3CGeoModuleImpl;
import com.sun.syndication.feed.module.georss.geometries.Position;

import org.geoserver.wfs.TransactionEvent;
import org.geoserver.ows.util.RequestUtils;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.global.GeoServer;

import freemarker.template.Template;
import freemarker.template.Configuration;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class GeoSyncController extends AbstractController {

    static final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");

    private GeoServer myGeoserver;

    private RecordingTransactionListener myListener;

    public RecordingTransactionListener getListener(){
        return myListener;
    }

    public void setListener(RecordingTransactionListener rtl){
        myListener = rtl;
    }

    public void setGeoserver(GeoServer gs){
        myGeoserver = gs;
    }

    public GeoServer getGeoserver(){
        return myGeoserver;
    }

    public ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse resp){
        resp.setContentType("text/xml");
        Map kvPairs = KvpRequestReader.parseKvpSet(req.getQueryString());
        System.out.println("Found kv pairs: " + kvPairs);
        String requestType = (String)kvPairs.get("REQUEST");
        String base = RequestUtils.proxifiedBaseURL(RequestUtils.baseURL(req), myGeoserver.getProxyBaseUrl());

        try{
            if ("GETCAPABILITIES".equalsIgnoreCase(requestType)){
                AtomService service = new AtomService();

                Workspace workspace = new Workspace(
                        "GeoSync Workspace", "application/atom+xml;type=entry");
                service.addWorkspace(workspace);

                Collection entryCol = new Collection(
                        "GeoSync Update Feed", "text",  base + "/geosync/request=feed");
                List accepts = new ArrayList();
                accepts.add("application/atom+xml;type=entry");
                entryCol.setAccepts(accepts);        
                workspace.addCollection( entryCol );
                                                                                        
                resp.setContentType("application/atomsvc+xml");

                //TODO: Categories
                Document d = service.serviceToDocument();
                XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
                outputter.output(d, resp.getOutputStream());
            } else if ("FEED".equalsIgnoreCase(requestType)){
                SyndFeed feed = generateFeed(kvPairs, base);
                SyndFeedOutput out = new SyndFeedOutput();
                PrintWriter writer = resp.getWriter();
                out.output(feed, writer);
            } else if ("DESCRIBESEARCH".equalsIgnoreCase(requestType)){
                Configuration cfg = new Configuration();
                cfg.setClassForTemplateLoading(getClass(), "");
                Map ctx = new HashMap();
                ctx.put("GEOSERVER_URL", base);
                ctx.put("CONTACT_ADDRESS", myGeoserver.getContactEmail());
                Template t = cfg.getTemplate("opensearchdescription.ftl");
                PrintWriter writer = resp.getWriter();
                t.process(ctx, writer);
            } else {
                resp.setStatus(400);
                PrintWriter writer = resp.getWriter();
                writer.println("Bad request");
            }
        } catch (Exception e){
            resp.setStatus(500);
            e.printStackTrace();
        }

        return null;
    }

    public SyndFeed generateFeed(Map params, String base) throws Exception{
        params.remove("REQUEST");
        SyndFeed feed;
        if (params.isEmpty()){
            // do defaulty stuff
            feed = new SyndFeedImpl();
            feed.setFeedType("atom_1.0");
            SyndLink link = new SyndLinkImpl();
            link.setHref(base + "/history?request=DescribeSearch");
            link.setRel("search");
            link.setType("application/opensearchdescription+xml");
            List links = feed.getLinks();
            links.add(link);
            feed.setLinks(links);
        } else {
            feed = generateFeed((String)null);
        }
        return feed;
    }

    public SyndFeed generateFeed(String layername) throws Exception{
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("atom_1.0");

        feed.setTitle("Geoserver History Feed");
        feed.setLink("http://geoserver.org/"); //TODO: get the local url and use that
        // feed.setDescription("Changes for feature type "); // TODO: get the feature type name and use that

        List history;
        if (layername != null){
           history = myListener.getHistoryList(layername); 
        } else {
           history = myListener.getFullHistoryList();
        };

        feed.setEntries(encodeHistory(history));
        return feed;
    }

    public List encodeHistory(List history) throws Exception{
        List entries = new ArrayList();

        Iterator it = history.iterator();
        while(it.hasNext()){
            SyncItem item = (SyncItem)it.next();
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle("Feature A");
            entry.setLink("http://geoserver.org/a");
            entry.setPublishedDate(DATE_PARSER.parse("2004-06-08"));
            SyndContent description = new SyndContentImpl();
            description.setType("text/plain");
            description.setValue(item.toString());

            // GeoRSSModule geoInfo = new W3CGeoModuleImpl();
            // geoInfo.setPosition(new Position(54.2, 12.4));
            // entry.getModules().add(geoInfo);
            // TODO: use real data for Geo output
            List contents = new ArrayList();
            contents.add(description);
            entry.setContents(contents);
            entries.add(entry);
        }

        return entries;
    }
}
