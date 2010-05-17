package org.geoserver.geosync;

import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.UpdateElementType;
import net.opengis.wfs.WfsFactory;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import com.sun.syndication.feed.atom.Link;
import com.sun.syndication.feed.module.georss.GeoRSSModule;
import com.sun.syndication.feed.module.georss.W3CGeoModuleImpl;
import com.sun.syndication.feed.module.georss.geometries.PositionList;
import com.sun.syndication.feed.module.georss.geometries.LinearRing;
import com.sun.syndication.feed.module.georss.geometries.Polygon;

import org.apache.xml.serialize.OutputFormat;
import org.geoserver.wfs.TransactionEvent;
import org.geoserver.wfs.xml.v1_1_0.WFS;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geotools.xml.Encoder;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;

import freemarker.template.Template;
import freemarker.template.Configuration;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class GeoSyncController extends AbstractController {

    static final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");

    private GeoServer myGeoserver;
    private Data catalog;

    private RecordingTransactionListener myListener;

    private WFSConfiguration xmlConfiguration;
    
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

    public void setCatalog(Data catalog) {
        this.catalog = catalog;
    }
    
    public void setXmlConfiguration(WFSConfiguration xmlConfiguration) {
        this.xmlConfiguration = xmlConfiguration;
    }

    public ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse resp){
        String path = req.getPathInfo();
        
        //strip off query string
        if ( path != null && path.indexOf( '?' ) > -1 ) {
            path = path.substring(0, path.indexOf('?') );
        }
        
        //is this a requst for the serach template?
        boolean search = false;
        if ( path != null && path.endsWith( "/opensearch.xml" ) ) {
            path = path.substring( 0, path.length() - "/opensearch.xml".length() );
            search = true;
        }
        
        //figure out the layer of the request 
        String layer = null;
        if ( path != null && !"".equals( path ) ) {
            //requsting particular layer
            layer = path.substring( 1 );
            try {
                catalog.getFeatureTypeInfo( layer );    
            }
            catch( NoSuchElementException e ) {
                layer = null;
            }
        }
        if ( layer == null ) {
            resp.setStatus( 404 );
            return null;
        }
       
        try {
            String baseUrl = RequestUtils.proxifiedBaseURL(
                RequestUtils.baseURL(req), myGeoserver.getProxyBaseUrl());
            baseUrl = ResponseUtils.appendPath( baseUrl, "history/"+ layer );
            
            //generate the content
            resp.setContentType("text/xml");
            
            if ( search ) {
                //send back the opensearch template
                //write out the opensearch template
                Configuration cfg = new Configuration();
                cfg.setClassForTemplateLoading(getClass(), "");
                
                Map ctx = new HashMap();
                ctx.put("CONTACT_ADDRESS", myGeoserver.getContactEmail());
                ctx.put( "LAYER", layer );
                ctx.put( "BASE_URL", baseUrl );
                
                Template t = cfg.getTemplate("opensearchdescription.ftl");
                PrintWriter writer = resp.getWriter();
                t.process(ctx, writer);
                writer.flush();
            }
            else {
                Map kvp = KvpRequestReader.parseKvpSet( req.getQueryString() );
                SyndFeed feed = null;
                
                if ( kvp.isEmpty() ) {
                     //send back a feed with a link to the opensearch template
                    feed = new SyndFeedImpl();
                    feed.setFeedType("atom_1.0");
                    SyndLink link = new SyndLinkImpl();
                    link.setRel( "search" );
                    link.setHref( ResponseUtils.appendPath(baseUrl, "opensearch.xml"));
                    link.setType( "application/opensearchdescription+xml");
                    feed.getLinks().add( link );
                }
                else {
                    //send back the actual feed
                    feed = myListener.filterFeed(layer,kvp,baseUrl,req);
                }
                
                //write out the feed
                SyndFeedOutput out = new SyndFeedOutput();
                out.output(feed, resp.getWriter());
                resp.getWriter().flush();
            }
           
        } catch (Exception e){
          resp.setStatus(500);
          e.printStackTrace();
        }
            
            
//            if ( search ) {
//               
//            }
//            else {
//                //write out the actual content
//                
//                SyndFeed feed = generateFeed(kvp, baseUrl);
//                SyndFeedOutput out = new SyndFeedOutput();
//                PrintWriter writer = resp.getWriter();
//                out.output(feed, writer);
//            }
//        }
//        catch( Exception e ) {
//            resp.setStatus(500);
//            e.printStackTrace();
//        }
//        
//        Map kvPairs = KvpRequestReader.parseKvpSet(req.getQueryString());
//        String requestType = (String)kvPairs.get("REQUEST");
//        String base = RequestUtils.proxifiedBaseURL(RequestUtils.baseURL(req), myGeoserver.getProxyBaseUrl());
//
//        try{
//            if ("GETCAPABILITIES".equalsIgnoreCase(requestType)){
//                AtomService service = new AtomService();
//
//                Workspace workspace = new Workspace(
//                        "GeoSync Workspace", "application/atom+xml;type=entry");
//                service.addWorkspace(workspace);
//
//                Collection entryCol = new Collection(
//                        "GeoSync Update Feed", "text",  base + "/geosync/request=feed");
//
//                List accepts = new ArrayList();
//                accepts.add("application/atom+xml;type=entry");
//                entryCol.setAccepts(accepts);        
//
//                workspace.addCollection( entryCol );
//                                                                                        
//                resp.setContentType("application/atomsvc+xml");
//
//                //TODO: Categories
//                Document d = service.serviceToDocument();
//                XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
//                outputter.output(d, resp.getOutputStream());
//            } else if ("FEED".equalsIgnoreCase(requestType)){
//                SyndFeed feed = generateFeed(kvPairs, base);
//                SyndFeedOutput out = new SyndFeedOutput();
//                PrintWriter writer = resp.getWriter();
//                out.output(feed, writer);
//            } else if ("DESCRIBESEARCH".equalsIgnoreCase(requestType)){
//                Configuration cfg = new Configuration();
//                cfg.setClassForTemplateLoading(getClass(), "");
//                Map ctx = new HashMap();
//                ctx.put("GEOSERVER_URL", base);
//                ctx.put("CONTACT_ADDRESS", myGeoserver.getContactEmail());
//                Template t = cfg.getTemplate("opensearchdescription.ftl");
//                PrintWriter writer = resp.getWriter();
//                t.process(ctx, writer);
//            } else {
//                resp.setStatus(400);
//                PrintWriter writer = resp.getWriter();
//                writer.println("Bad request");
//            }
//        } catch (Exception e){
//            resp.setStatus(500);
//            e.printStackTrace();
//        }

        return null;
    }

//    public SyndFeed generateFeed(Map params, String base) throws Exception{
//        params.remove("REQUEST");
//        SyndFeed feed;
//        if (params.isEmpty()){
//            // do defaulty stuff
//            feed = new SyndFeedImpl();
//            feed.setFeedType("atom_1.0");
//            
//            SyndLink link = new SyndLinkImpl();
//            link.setHref(base + "/history?request=DescribeSearch");
//            link.setRel("search");
//            link.setType("application/opensearchdescription+xml");
//            List links = feed.getLinks();
//            links.add(link);
//            feed.setLinks(links);
//        } else {
//            feed = generateFeed(params);
//        }
//        return feed;
//    }

   public List encodeHistory(List history) throws Exception{
        List entries = new ArrayList();

        Iterator it = history.iterator();
        while(it.hasNext()){
            TransactionEvent e = (TransactionEvent) it.next();
            //SyncItem item = (SyncItem)it.next();
            
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle("Feature A");
            entry.setLink("http://geoserver.org/a");
            entry.setPublishedDate(DATE_PARSER.parse("2004-06-08"));
            
            //encode the content as the wfs transcation
            SyndContent description = new SyndContentImpl();
            description.setType("text/xml");
            description.setValue(encodeTransaction( e ));

            // GeoRSSModule geoInfo = new W3CGeoModuleImpl();
            // Polygon bounds = new Polygon();
            // PositionList exterior = new PositionList();
            // double minLat = 0, minLong = 0, maxLat = 0, maxLong = 0;
            // exterior.add(minLat, minLong);
            // exterior.add(minLat, maxLong);
            // exterior.add(maxLat, maxLong);
            // exterior.add(maxLat, minLong);
            // bounds.setExterior(new LinearRing(exterior));
            // geoInfo.setGeometry(bounds);
            // entry.getModules().add(geoInfo);
            // TODO: use real data for Geo output
            List contents = new ArrayList();
            contents.add(description);
            entry.setContents(contents);
            entries.add(entry);
        }

        return entries;
    }
    
    String encodeTransaction( TransactionEvent e ) throws IOException {
        TransactionType tx = WfsFactory.eINSTANCE.createTransactionType();
        Object source = e.getSource();
        
        if ( source instanceof InsertElementType ) {
            tx.getInsert().add( source );
        }
        else if ( source instanceof UpdateElementType ) {
            tx.getUpdate().add( source );
        }
        else if ( source instanceof DeleteElementType ) {
            tx.getDelete().add( source );
        }
        
        Encoder encoder = new Encoder( xmlConfiguration );
        OutputFormat of = new OutputFormat();
        of.setOmitXMLDeclaration( true );
        encoder.setOutputFormat( of );
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encoder.encode( tx, WFS.TRANSACTION, out );
        return new String( out.toByteArray() );
    }
}
