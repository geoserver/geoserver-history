package org.geoserver.python;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipFile;

import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.rest.util.IOUtils;
import org.geoserver.rest.util.RESTUtils;
import org.geotools.util.logging.Logging;
import org.python.util.PythonInterpreter;

public class Python {

    static Logger LOGGER = Logging.getLogger("org.geoserver.jython");
    
    GeoServerResourceLoader resourceLoader;
    
    boolean initialized = false;
    
    public Python(GeoServerResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    public PythonInterpreter interpreter() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    initialize();
                    initialized = true;
                }
            }
        }
        
        return new PythonInterpreter();
    }
    
    void initialize() {
        //copy libs into <DATA_DIR>/python/lib
        try {
            initLibs();
        } 
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        //initialize the python path
        ArrayList<String> pythonPath = new ArrayList();
        
        //look for a jython installation on the system
        /*String jythonHome = System.getenv("JYTHON_HOME");
        if (jythonHome != null) {
            pythonPath.add(jythonHome+File.separator+"/Lib");
        }*/
        
        //add <GEOSERVER_DATA_DIR>/jython/lib to path
        try {
            File lib = getLibRoot();
            pythonPath.add(lib.getCanonicalPath());
        } 
        catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unable to access Jython lib directory", e);
        }
        
        StringBuffer path = new StringBuffer();
        for (String loc : pythonPath) {
            path.append(loc).append(File.pathSeparator);
        }
        path.setLength(path.length()-1);
        
        Properties props = new Properties();
        props.put("python.path", path.toString());
        
        PythonInterpreter.initialize(null, props, null);
    }
    
    void initLibs() throws IOException {
        File libRoot = getLibRoot();
        File gsPyRoot = resourceLoader.findOrCreateDirectory(libRoot, "geoserver");
        
        ClassLoader cl = getClass().getClassLoader(); 
        IOUtils.copyStream(cl.getResourceAsStream("geoserver/__init__.py"), 
            new FileOutputStream(new File(gsPyRoot, "__init__.py")), true, true);
        IOUtils.copyStream(cl.getResourceAsStream("geoserver/catalog.py"), 
                new FileOutputStream(new File(gsPyRoot, "catalog.py")), true, true);
        IOUtils.copyStream(cl.getResourceAsStream("geoserver/layer.py"), 
                new FileOutputStream(new File(gsPyRoot, "layer.py")), true, true);
        
        ZipFile f = new ZipFile(new File(cl.getResource("geoscript.zip").getFile()));
        new File(libRoot, "geoscript").mkdir();
        IOUtils.inflate(f, libRoot, null);
        
    }
    
    public File getScriptRoot() throws IOException {
        return resourceLoader.findOrCreateDirectory("python", "scripts");
    }
    
    public String getLibPath() throws IOException {
        return "python" + File.separator + "lib";
    }
    public File getLibRoot() throws IOException {
        return resourceLoader.findOrCreateDirectory(getLibPath());
    }
    
}
