package org.geoserver.python.wfs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.opengis.wfs.FeatureCollectionType;

import org.geoserver.platform.FileWatcher;
import org.geoserver.python.Python;
import org.geotools.feature.FeatureCollection;
import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyStringMap;
import org.python.util.PythonInterpreter;

public class PythonFeatureOutputFormatAdapter {

    Python py;
    FileWatcher<PyObject> fw;
    PyObject pobj;
    
    public PythonFeatureOutputFormatAdapter(File module, Python py) {
        this.py = py;
        this.fw = new FileWatcher<PyObject>(module) {
            @Override
            protected PyObject parseFileContents(InputStream in) throws IOException {
                PythonInterpreter pi = PythonFeatureOutputFormatAdapter.this.py.interpreter();
                pi.execfile(in);
                
                PyStringMap locals = (PyStringMap) pi.getLocals();
                for (Object o : locals.keys()) {
                    String key = (String) o;
                    PyObject obj = locals.__getitem__(key);
                    if (obj instanceof PyFunction ) {
                        try {
                            if (obj.__getattr__("__wfs_outputformat__") != null) {
                                return obj;
                            }
                        }
                        catch(PyException e) {}
                    }
                }
                
                throw new IllegalStateException("No output format function found in " 
                    + fw.getFile().getAbsolutePath());
            }
        };
    }
    
    public String getName() {
        return object().__getattr__("name").toString();
    }
    
    public String getMimeType() {
        return object().__getattr__("mime").toString();
    }
    
    public void write(FeatureCollectionType features, OutputStream output) throws Exception {
        FeatureCollection fc = (FeatureCollection) features.getFeature().get(0);
        
        PyObject obj = object();
        obj.__call__(Py.javas2pys(fc, output));
        
        output.flush();
    }
    
    PyObject object() {
        if (fw.isModified()) {
            synchronized (fw) {
                if (fw.isModified()) {
                    try {
                        pobj = fw.read();
                    } 
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        
        return pobj;
    }
}
