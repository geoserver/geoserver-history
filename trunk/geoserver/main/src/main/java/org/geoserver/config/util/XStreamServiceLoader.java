package org.geoserver.config.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.ServiceLoader;
import org.geoserver.platform.GeoServerResourceLoader;

import com.thoughtworks.xstream.XStream;

/**
 * Service loader which loads and saves a service configuration with xstream.
 *   
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public abstract class XStreamServiceLoader<T extends ServiceInfo> implements ServiceLoader<T> {
    
    GeoServerResourceLoader resourceLoader;
    String filenameBase;
    
    public XStreamServiceLoader(GeoServerResourceLoader resourceLoader, String filenameBase) {
        this.resourceLoader = resourceLoader;
        this.filenameBase = filenameBase;
    }
    
    public final T load( GeoServer gs ) throws Exception {
        //look for file matching classname
        String filename = this.filenameBase + ".xml";
        File file = resourceLoader.find( filename );
        
        if ( file != null && file.exists() ) {
            //xstream it in
            BufferedInputStream in = 
                new BufferedInputStream( new FileInputStream( file ) );
            try {
                XStream xstream = new XStream();
                return (T) xstream.fromXML(in);
            }
            finally {
                in.close();    
            }
        }
        else {
            //create an 'empty' object
            ServiceInfo service = createServiceFromScratch( gs );
            return (T) service;
        }
    }

    public void save(T service, GeoServer gs) throws Exception {
        String filename = filenameBase + ".xml";
        File file = resourceLoader.find( filename );
        if ( file == null ) {
            file = resourceLoader.createFile(filename);
        }
        
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        try {
            XStream xstream = new XStream();
            xstream.toXML( service, out );
            
            out.flush();
        }
        finally {
            out.close();
        }
    }
    
    protected abstract T createServiceFromScratch(GeoServer gs);
}
