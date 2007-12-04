package org.geoserver.wfs;

import java.util.ArrayList;
import java.util.List;

public class TransactionListenerTester implements TransactionListener {
    List events = new ArrayList();
    List features = new ArrayList();
    
    public void clear() {
        events.clear();
    }

    public void dataStoreChange(TransactionEvent event) throws WFSException {
        events.add(event);
        features.addAll(event.getAffectedFeatures());
    }
    
    
    
}