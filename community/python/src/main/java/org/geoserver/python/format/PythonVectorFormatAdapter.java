package org.geoserver.python.format;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.opengis.wfs.FeatureCollectionType;

import org.geoserver.platform.FileWatcher;
import org.geoserver.python.Python;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapLayer;
import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyStringMap;
import org.python.util.PythonInterpreter;
import org.vfny.geoserver.wms.WMSMapContext;

public class PythonVectorFormatAdapter {

    Python py;
    FileWatcher<PyObject> fw;
    PyObject pobj;
    
    public PythonVectorFormatAdapter(File module, Python py) {
        this.py = py;
        this.fw = new FileWatcher<PyObject>(module) {
            @Override
            protected PyObject parseFileContents(InputStream in) throws IOException {
                PythonInterpreter pi = PythonVectorFormatAdapter.this.py.interpreter();
                pi.execfile(in);
                
                PyStringMap locals = (PyStringMap) pi.getLocals();
                for (Object o : locals.keys()) {
                    String key = (String) o;
                    PyObject obj = locals.__getitem__(key);
                    if (obj instanceof PyFunction ) {
                        try {
                            if (obj.__getattr__("__vector_format__") != null) {
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
        write(features.getFeature(), output);
    }
    
    public void write(List<FeatureCollection> features, OutputStream output) throws Exception {
        PyObject obj = object();
        obj.__call__(Py.javas2pys(features, output));
        
        output.flush();
    }
    
    public void write(WMSMapContext mapContext, OutputStream output) throws Exception {
        List features = new ArrayList();
        for (int i = 0; i < mapContext.getLayerCount(); i++) {
            MapLayer l = mapContext.getLayer(i);
            if (l.toLayer() instanceof FeatureLayer) {
                FeatureSource source = mapContext.getLayer(i).getFeatureSource();
                features.add(source.getFeatures(l.getQuery()));
            }
        }

        if (features.isEmpty()) {
            throw new IllegalArgumentException("No feature data in map context");
        }

        PyObject obj = object();
        obj.__call__(Py.javas2pys(features, output));
        
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
