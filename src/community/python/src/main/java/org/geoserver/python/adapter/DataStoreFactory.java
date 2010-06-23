package org.geoserver.python.adapter;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geoserver.python.Python;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyType;

public class DataStoreFactory implements DataStoreFactorySpi {

    
    Python py;
    String title;
    PyType type;
    List<String> args;
    
    public DataStoreFactory(Python py, String title, PyType type, PyList args) {
        this.py = py;
        this.title = title;
        this.type = type;
        
        this.args = new ArrayList();
        for (int i = 1; i < args.size(); i++) {
            this.args.add((String)args.get(i));
        }
    }
    
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        PyObject[] values = new PyObject[args.size()];
        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);
            values[i] = new PyString(params.get(arg).toString());
        }

        PyObject instance = this.type.__call__(values);
        return new DataStoreAdapter(instance);
    }

    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        return null;
    }

    public boolean canProcess(Map<String, Serializable> params) {
        for (String arg : args) {
            if (!params.containsKey(arg)) {
                return false;
            }
        }
        
        return "python".equals(params.get("__type"));
    }

    public String getDescription() {
        return title;
    }

    public String getDisplayName() {
        return title;
    }

    public Param[] getParametersInfo() {
        Param[] params = new Param[args.size()+1];
        for (int i = 0; i < args.size(); i++) {
            params[i] = new Param(args.get(i));
        }
        params[args.size()] = new Param("__type", String.class, "", true, "python");
        return params;
    }

    public boolean isAvailable() {
        return true;
    }

    public Map<Key, ?> getImplementationHints() {
        return null;
    }

}
