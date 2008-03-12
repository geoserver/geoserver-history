package org.geoserver.geosync;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.geoserver.wfs.TransactionEventType;
import org.geoserver.wfs.TransactionListener;
import org.geoserver.wfs.WFSException;
import org.geoserver.wfs.TransactionEvent;
import org.geoserver.wfs.xml.v1_0_0.WFSConfiguration;

import org.geotools.xml.Encoder;

import net.opengis.wfs.WfsFactory;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.UpdateElementType;

public class RecordingTransactionListener implements TransactionListener{
    private static Set recordWorthyEvents;
    private List myHistory;
    private WFSConfiguration myWFSConfiguration;

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
            myHistory.add( event );
        }
    }
    
    public WFSConfiguration getWFSConfig(){
        return myWFSConfiguration;
    }

    public void setWFSConfig(WFSConfiguration wfsc){
        myWFSConfiguration = wfsc;
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

    public SyncItem makeDescription(TransactionEvent evt){
        SyncItem item = new SyncItem();
        item.setLayer(evt.getLayerName().toString());
        item.setAction(evt.getType().toString());
        List l = new ArrayList();

        try{
            Iterator it = evt.getAffectedFeatures().iterator();
            while (it.hasNext()){
                l.add(it.next().toString());
            }
        } catch (Exception e){
            // Silence exceptions
        }
        item.setAffectedFeatures(l);
        item.setSource(evt.getSource());

        TransactionType txType = WfsFactory.eINSTANCE.createTransactionType();
        if (evt.getSource() instanceof UpdateElementType){
            txType.getUpdate().add(evt.getSource());
        }
        if (evt.getSource() instanceof DeleteElementType){
            txType.getDelete().add(evt.getSource());
        }
        if (evt.getSource() instanceof InsertElementType){
            txType.getInsert().add(evt.getSource());
        }


        Encoder enc = new Encoder(myWFSConfiguration);
//        enc.encode(tx, WFS.TRANSACTION, );

        return item;
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
