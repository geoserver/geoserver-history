package org.geoserver.wfs;

import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.opengis.feature.simple.SimpleFeature;

public class TransactionListenerTester implements TransactionListener {
    List events = new ArrayList();
    List<List<SimpleFeature>> features = new ArrayList();
    
    public void clear() {
        events.clear();
    }

    public void dataStoreChange(TransactionEvent event) throws WFSException {
        events.add(event);
        features.add(DataUtilities.list(event.getAffectedFeatures()));
    }
    
    
    
}
