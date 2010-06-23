package org.geoserver.python;

import java.io.File;
import java.io.IOException;

import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInitializer;
import org.geoserver.python.adapter.DataStoreFactory;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.python.core.PyDictionary;
import org.python.core.PyFunction;
import org.python.core.PyJavaType;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyStringMap;
import org.python.core.PyTuple;
import org.python.core.PyType;
import org.python.util.PythonInterpreter;

public class PythonInitializer implements GeoServerInitializer {

    Python py;
    
    public PythonInitializer(Python py) {
        this.py = py;
    }
    
    public void initialize(GeoServer geoServer) throws Exception {
        //initialize data stores
        File f = new File(py.getDataStoreRoot(), "conf.ini");
        if (f.exists()) {
            initDataStores(f);
        }
    }

    void initDataStores(File f) throws IOException {
        Wini ini = new Wini(f);
        for (String section : ini.keySet()) {
            
            String module = section;
            
            PythonInterpreter pi = py.interpreter();
            
            pi.exec("import " + module);
            
            PyObject mod = pi.get(module);
            PyStringMap dict = (PyStringMap) mod.getDict();
            
            PyType type = null;
            for (Object v : dict.values()) {
                if (v instanceof PyType) {
                    type = (PyType)v;
                    break;
                }
            }
            
            if (type == null) {
                Python.LOGGER.warning("Could not find class in data store module " + module);
                continue;
            }
            
            pi.exec("import inspect");
            pi.exec("args = inspect.getargspec(" + module + "." + type.getName() + ".__init__)");
            PyList args = (PyList) ((PyTuple) pi.get("args")).get(0);
            
            String title = ini.get(section, "title");
            if (title == null) {
                title = section;
            }
            
            DataStoreFactory factory = new DataStoreFactory(py, title, type, args);
            py.getDataStoreFactories().add(factory);
            
        }
    }

}
