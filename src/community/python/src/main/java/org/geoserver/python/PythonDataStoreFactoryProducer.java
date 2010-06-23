package org.geoserver.python;

import java.util.List;

import org.geoserver.data.DataStoreFactoryProducer;
import org.geotools.data.DataStoreFactorySpi;

public class PythonDataStoreFactoryProducer implements DataStoreFactoryProducer {

    Python py;
    
    public PythonDataStoreFactoryProducer(Python py) {
        this.py = py;
    }
    
    public List<DataStoreFactorySpi> getDataStoreFactories() {
        return (List) py.getDataStoreFactories();
    }

}
