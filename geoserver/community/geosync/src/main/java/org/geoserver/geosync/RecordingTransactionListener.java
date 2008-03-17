package org.geoserver.geosync; 

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

import org.geoserver.wfs.TransactionEventType;
import org.geoserver.wfs.TransactionListener;
import org.geoserver.wfs.WFSException;
import org.geoserver.wfs.TransactionEvent;
import org.geoserver.wfs.xml.v1_1_0.WFS;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.ConfigurationException;

import org.geotools.xml.Encoder;

import org.apache.xml.serialize.OutputFormat;

import net.opengis.wfs.WfsFactory;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.UpdateElementType;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndLink;
import com.sun.syndication.feed.synd.SyndLinkImpl;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.propono.atom.common.AtomService;
import com.sun.syndication.propono.atom.common.Workspace;
import com.sun.syndication.propono.atom.common.Collection;
import com.sun.syndication.feed.module.georss.GeoRSSModule;
import com.sun.syndication.feed.module.georss.W3CGeoModuleImpl;
import com.sun.syndication.feed.module.georss.geometries.PositionList;
import com.sun.syndication.feed.module.georss.geometries.LinearRing;
import com.sun.syndication.feed.module.georss.geometries.Polygon;

public class RecordingTransactionListener implements TransactionListener{
    private static Set recordWorthyEvents;
    private List myHistory;
    private WFSConfiguration xmlConfiguration;
    static final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");

    private static Map filterHandling;

    static {
        recordWorthyEvents = new TreeSet();
        recordWorthyEvents.add(TransactionEventType.PRE_INSERT);
        recordWorthyEvents.add(TransactionEventType.PRE_UPDATE);
        recordWorthyEvents.add(TransactionEventType.PRE_DELETE);

        filterHandling = new HashMap();
        // NOTE: Keys in this map should be ALL CAPS to play nice with the KvpParser
        filterHandling.put("LAYER", new LayerNameFilter());
    }

    public RecordingTransactionListener(){
        myHistory = new ArrayList();
    }

    public void dataStoreChange(TransactionEvent event) throws WFSException{
        if ( recordWorthyEvents.contains( event.getType() ) ) {
            try {
                SyndFeed feed = getCurrentFeed();
                List l = feed.getEntries();
                l.add(eventToEntry(event));
                feed.setEntries(l);
                saveFeed(feed);
            } catch (Exception e){
                System.out.println("Failure while trying to update history.");
                e.printStackTrace();
                // LOG ERROR HERE!!
            }
        }
    }
    
    public WFSConfiguration getWFSConfig(){
        return xmlConfiguration;
    }

    public void setWFSConfig(WFSConfiguration wfsc){
        xmlConfiguration = wfsc;
    }

    public File getFile() throws ConfigurationException{
        File f = GeoserverDataDirectory.findCreateConfigDir("geosync");
        System.out.println("Created " + f);
        return new File(f, "history.xml");
    }

    public SyndFeed getCurrentFeed() throws Exception{
        try{ 
            SyndFeedInput input = new SyndFeedInput();
            return input.build(new XmlReader(getFile()));
        } catch (Exception e){
            System.out.println("Feed requested but no stored data exists; generating template.");
            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType("atom_1.0");
            feed.setTitle("Geoserver History Feed");
            SyndLink link = new SyndLinkImpl();
            link.setHref("http://geoserver.org/");
            feed.setLink(link);
            return feed;
        }
    }

    public void saveFeed(SyndFeed feed) throws Exception{
        File f = getFile();
        PrintWriter writer = new PrintWriter(new FileWriter(f));
        SyndFeedOutput out = new SyndFeedOutput();
        out.output(feed, writer);
        out.output(feed, new PrintWriter(System.out));
    }

    public SyndEntry eventToEntry(TransactionEvent evt) throws Exception{
            
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle("Feature A");
            entry.setLink("http://geoserver.org/a");
            entry.setPublishedDate(DATE_PARSER.parse("2004-06-08"));
            
            //encode the content as the wfs transcation
            SyndContent description = new SyndContentImpl();
            description.setType("text/xml");
            description.setValue(encodeTransaction( evt ));

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
            return entry;
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


    public List getHistoryList(String layername){
        List matches = new ArrayList();
        Iterator it = myHistory.iterator();

        while (it.hasNext()){
            TransactionEvent e = (TransactionEvent) it.next();
            if ( e.getLayerName().equals( layername ) ) {
                matches.add( e );
            }
        }
        
        return matches;
    }

    public List getHistoryList(Map filterParams){
        List matches = new ArrayList();
        Iterator it = myHistory.iterator();
        HistoryFilter filter = getFilter(filterParams);

        while (it.hasNext()){
            TransactionEvent e = (TransactionEvent) it.next();
            if (filter.pass(e)){
                matches.add(e);
            }
        }

        return matches;
    }

    public List getFullHistoryList(){
        return myHistory;
    }

    private HistoryFilter getFilter(Map filterParams){
        Iterator it = filterParams.entrySet().iterator();
        List filters = new ArrayList();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            HistoryFilter f = (HistoryFilter)filterHandling.get(entry.getKey());
            if (f != null){
                f.initialize((String)entry.getValue());
                filters.add(f);
            }
        }
        return new CompositeFilter(filters);
    }

    public static interface HistoryFilter {
        public void initialize(String param);
        public boolean pass(TransactionEvent evt);
    }

    public static class CompositeFilter implements HistoryFilter {
        private List myFilters;

        public CompositeFilter(List filters){
            myFilters = filters;
        }

        public void initialize(String param){
            // no need
        }

        public boolean pass(TransactionEvent evt){
            Iterator it = myFilters.iterator();
            
            while (it.hasNext()){
                HistoryFilter f = (HistoryFilter)it.next();
                if (f != null && !f.pass(evt)){
                    return false;
                }
            }

            return true;
        }
    }

    public static class LayerNameFilter implements HistoryFilter{
        String myLayer;
        public void initialize(String param){
            myLayer = param;
        }

        public boolean pass(TransactionEvent evt){
            return myLayer.equals(evt.getLayerName().toString());
        }
    }

}
