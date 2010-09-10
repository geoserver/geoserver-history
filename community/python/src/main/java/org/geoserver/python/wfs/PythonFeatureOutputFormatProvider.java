package org.geoserver.python.wfs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.geoserver.platform.ExtensionProvider;
import org.geoserver.python.Python;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import static org.geoserver.python.Python.LOGGER;

public class PythonFeatureOutputFormatProvider implements ExtensionProvider<WFSGetFeatureOutputFormat> {

    Python py;
    
    public PythonFeatureOutputFormatProvider(Python py) {
        this.py = py;
    }
    
    public Class<WFSGetFeatureOutputFormat> getExtensionPoint() {
        return WFSGetFeatureOutputFormat.class;
    }
    
    public List<WFSGetFeatureOutputFormat> getExtensions(
            Class<WFSGetFeatureOutputFormat> extensionPoint) {
        
        List<WFSGetFeatureOutputFormat> formats = new ArrayList();
        
        File dir;
        try {
            dir = py.getWfsRoot();
        } 
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        for (File f : dir.listFiles()) {
            try {
                formats.add(new PythonFeatureOutputFormat(new PythonFeatureOutputFormatAdapter(f, py)));
            }
            catch(Exception e) {
                LOGGER.warning(e.getLocalizedMessage());
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                }
            }
        }
        
        return formats;
        
        
    }

    

}
