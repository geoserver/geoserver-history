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
import java.util.Date;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.StringReader;

import org.geoserver.wfs.TransactionEventType;
import org.geoserver.wfs.TransactionListener;
import org.geoserver.wfs.WFSException;
import org.geoserver.wfs.TransactionEvent;
import org.geoserver.wfs.xml.v1_1_0.WFS;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.ConfigurationException;

import org.geotools.xml.Encoder;
import org.geotools.xml.Parser;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;

import org.apache.xml.serialize.OutputFormat;

import net.opengis.wfs.WfsFactory;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.UpdateElementType;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

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
import com.sun.syndication.feed.module.georss.SimpleModuleImpl;
import com.sun.syndication.feed.module.georss.geometries.Position;
import com.sun.syndication.feed.module.georss.geometries.Envelope;

public class RecordingTransactionListener implements TransactionListener{
    private static Set recordWorthyEvents;
    private List myHistory;
    private WFSConfiguration xmlConfiguration;

    private Map filterHandling;

    static {
        recordWorthyEvents = new TreeSet();
        recordWorthyEvents.add(TransactionEventType.PRE_INSERT);
        recordWorthyEvents.add(TransactionEventType.PRE_UPDATE);
        recordWorthyEvents.add(TransactionEventType.PRE_DELETE);
    }

    public RecordingTransactionListener(){
        myHistory = new ArrayList();
        filterHandling = new HashMap();
        // NOTE: Keys in this map should be ALL CAPS to play nice with the KvpParser
        filterHandling.put("LAYER", new LayerNameFilter());
        filterHandling.put("DATE", new DateFilter());
    }

    public void dataStoreChange(TransactionEvent event) throws WFSException{
        if ( recordWorthyEvents.contains( event.getType() ) ) {
            try {
                SyndFeed feed = getCurrentFeed();
                List l = feed.getEntries();
                SyndEntry entry = eventToEntry(event);
                l.add(entry);
                feed.setEntries(l);
                saveFeed(feed);
            } catch (Exception e){
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
        return new File(f, "history.xml");
    }

    public SyndFeed getCurrentFeed() throws Exception{
        try{ 
            SyndFeedInput input = new SyndFeedInput();
            return input.build(new XmlReader(getFile()));
       } catch (Exception e){
            // System.out.println("Feed requested but no stored data exists; generating template.");
            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType("atom_1.0");
            feed.setTitle("Geoserver History Feed");
            SyndLink link = new SyndLinkImpl();
            link.setHref("http://geoserver.org/");
            feed.setLink(link);
            return feed;
        }
    }

    public SyndFeed filterFeed(Map params) throws Exception{
        SyndFeed feed = getCurrentFeed();
        HistoryFilter filter = getFilter(params);
        List entries = feed.getEntries();
        for (int i = 0; i < entries.size(); i++){
            SyndEntry entry = (SyndEntry)entries.get(i);
            if (!filter.pass(entry)){
                entries.remove(i);
                i--;
            }
        }
        return feed;
    }

    public void saveFeed(SyndFeed feed) throws Exception{
        File f = getFile();
        PrintWriter writer = new PrintWriter(new FileWriter(f));
        SyndFeedOutput out = new SyndFeedOutput();
        out.output(feed, writer);
    }

    public SyndEntry eventToEntry(TransactionEvent evt) throws Exception{
            
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle("Feature A");
            entry.setLink("http://geoserver.org/a");
            entry.setPublishedDate(new Date());
            
            //encode the content as the wfs transcation
            SyndContent description = new SyndContentImpl();
            description.setType("text/xml");
            description.setValue(encodeTransaction( evt ));

            // attach the content to the entry
            List contents = new ArrayList();
            contents.add(description);
            entry.setContents(contents);

            // Add the georss info
            ReferencedEnvelope refenv = evt.getAffectedFeatures().getBounds();

            GeoRSSModule geoInfo = new SimpleModuleImpl();
            double minLat = refenv.getMinimum(0),
                   minLong = refenv.getMinimum(1),
                   maxLat = refenv.getMaximum(0),
                   maxLong = refenv.getMaximum(1);
            Envelope bounds = new Envelope(minLat, minLong, maxLat, maxLong);
            geoInfo.setGeometry(bounds);
            List modules = entry.getModules();
            modules.add(geoInfo);
            entry.setModules(modules);
            System.out.println("Entry with georss: " + entry);

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

    public interface HistoryFilter {
        public void initialize(String param);
        public boolean pass(SyndEntry entry);
    }

    public class CompositeFilter implements HistoryFilter {
        private List myFilters;

        public CompositeFilter(List filters){
            myFilters = filters;
        }

        public void initialize(String param){
            // no need
        }

        public boolean pass(SyndEntry entry){
            Iterator it = myFilters.iterator();
            
            while (it.hasNext()){
                HistoryFilter f = (HistoryFilter)it.next();
                if (f != null && !f.pass(entry)){
                    return false;
                }
            }

            return true;
        }
    }

    public class DateFilter implements HistoryFilter{
        Date myDate;
        final DateFormat DATEPARSER = new SimpleDateFormat("yyyy-MM-dd");

        public void initialize(String param){
            try{
                myDate = DATEPARSER.parse(param);
            } catch (Exception e){
                myDate = null;
            }
        }

        public boolean pass(SyndEntry entry){
            if (myDate != null){
                return myDate.before(entry.getPublishedDate());
            } else return true;
        }
    }

    public class LayerNameFilter implements HistoryFilter{
        String myLayer;

        public void initialize(String param){
            myLayer = param;
        }

        public boolean pass(SyndEntry entry){
            try{
                String xmlBlob = ((SyndContent)entry.getContents().get(0)).getValue();
                Parser parser = new Parser(xmlConfiguration);
                TransactionType tx = (TransactionType) parser.parse(new StringReader(xmlBlob));

                Set qnames = new TreeSet();

                Iterator it = tx.getInsert().iterator();
                while (it.hasNext()){
                    InsertElementType iet = (InsertElementType)it.next();
                    Iterator iter = iet.getFeature().iterator();
                    while (iter.hasNext()){
                        qnames.add(((SimpleFeature)iter.next()).getFeatureType().getTypeName());
                    }
                }

                it = tx.getDelete().iterator();
                while (it.hasNext()){
                    DeleteElementType det = (DeleteElementType)it.next();
                    qnames.add(det.getTypeName().getLocalPart());
                }

                it = tx.getUpdate().iterator();
                while (it.hasNext()){
                    UpdateElementType uet = (UpdateElementType)it.next();
                    qnames.add(uet.getTypeName().getLocalPart());
                }

                return qnames.contains(myLayer);// evt.getLayerName().toString());
            } catch (Exception e){
                e.printStackTrace();
                // pass since we didn't understand, I guess
                // TODO: revisit and decide whether unparsable stuff should stay in the feed
                return true; 
            }
        }
    }

}
