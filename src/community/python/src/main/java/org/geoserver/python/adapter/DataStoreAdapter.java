package org.geoserver.python.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.Query;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;
import org.python.core.PyList;
import org.python.core.PyMethod;
import org.python.core.PyObject;

/**
 * A DataStore implementation that adapts a geoscript workspace.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class DataStoreAdapter extends ContentDataStore {

    PyObject workspace;
    
    public DataStoreAdapter(PyObject workspace) {
        this.workspace = workspace;
    }
    
    @Override
    protected FeatureStoreAdapter createFeatureSource(ContentEntry entry) throws IOException {
        return new FeatureStoreAdapter(entry, Query.ALL);
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        PyMethod layers = (PyMethod) workspace.__findattr__("keys");
        PyList result = (PyList) layers.__call__();
        
        List<Name> typeNames = new ArrayList<Name>();
        for (Object o : result.toArray()) {
            typeNames.add(new NameImpl(o.toString()));
        }
        return typeNames;
    }

}
