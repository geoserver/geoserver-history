package org.geoserver.geosync;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.Set;

import org.geoserver.wfs.TransactionEventType;
import org.geoserver.wfs.TransactionListener;
import org.geoserver.wfs.WFSException;
import org.geoserver.wfs.TransactionEvent;

public class RecordingTransactionListener implements TransactionListener{
    private static Set recordWorthyEvents;
    private List myHistory;

    static {
        recordWorthyEvents = new TreeSet();
        recordWorthyEvents.add(TransactionEventType.POST_INSERT);
        recordWorthyEvents.add(TransactionEventType.POST_UPDATE);
    }

    public RecordingTransactionListener(){
        myHistory = new ArrayList();
    }

    public void dataStoreChange(TransactionEvent event) throws WFSException{
        myHistory.add(makeDescription(event));
    }
    
    public List getHistoryList(String layername){
        List matches = new ArrayList();
        Iterator it = myHistory.iterator();

        while (it.hasNext()){
            SyncItem si = (SyncItem)it.next();
            if (si.getLayer().equals(layername)){
                matches.add(si);
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

        return item;
    }

}
