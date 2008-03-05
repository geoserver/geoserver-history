package org.geoserver.geosync;

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

       // if (recordWorthyEvents.contains(event.getType())){
            // Something was inserted!!
        	myHistory.add(event.getType().name() + " on " + event.getLayerName());
       // }
    }
    
    public List getHistoryList(){
        return myHistory;
    }
}
