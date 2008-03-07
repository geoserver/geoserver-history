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
import java.io.Writer;
import java.io.PrintWriter;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.io.SyndFeedOutput;

import org.geoserver.wfs.TransactionEvent;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;

import freemarker.template.Template;
import freemarker.template.Configuration;

public class GeoSyncController extends AbstractController {

    static final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");

    private RecordingTransactionListener myListener;

    public RecordingTransactionListener getListener(){
        return myListener;
    }

    public void setListener(RecordingTransactionListener rtl){
        myListener = rtl;
    }

    public ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse resp){
        resp.setContentType("text/xml");
        Map kvPairs = KvpRequestReader.parseKvpSet(req.getQueryString());
        System.out.println("Found kv pairs: " + kvPairs);
        String requestType = (String)kvPairs.get("SERVICE_DOCUMENT");
        try{
            Writer writer = resp.getWriter();
            if (requestType == null){
                String layer = (String)kvPairs.get("layer");
                SyndFeed feed = generateFeed(layer);
                SyndFeedOutput out = new SyndFeedOutput();
                out.output(feed, writer);
            } else {
                Configuration config = new Configuration();
                config.setClassForTemplateLoading(getClass(), "");
                // todo: get a list of layers
                List l = new ArrayList();
                l.add("notes");
                Map context = new HashMap();
                context.put("SERVER_URL", "http://localhost:8008/geoserver/");
                context.put("LAYERS", l);
                Template t = config.getTemplate("XMLTemplates/service_document.ftl");
                t.process(context, writer);
            }
        } catch (Exception e){
            resp.setStatus(500);
            e.printStackTrace();
        }

        return null;
    }

    public SyndFeed generateFeed(String layername) throws Exception{
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("atom_1.0");

        feed.setTitle("Geoserver History Feed");
        feed.setLink("http://geoserver.org/"); //TODO: get the local url and use that
        feed.setDescription("Changes for feature type "); // TODO: get the feature type name and use that

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
            String event = it.next().toString();
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle("Feature A");
            entry.setLink("http://geoserver.org/a");
            entry.setPublishedDate(DATE_PARSER.parse("2004-06-08"));
            SyndContent description = new SyndContentImpl();
            description.setType("text/xml");
            description.setValue(event);
            List contents = new ArrayList();
            contents.add(description);
            entry.setContents(contents);
            entries.add(entry);
        }

        return entries;
    }
}
