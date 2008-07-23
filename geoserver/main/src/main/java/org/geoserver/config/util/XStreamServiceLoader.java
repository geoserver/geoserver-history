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
public abstract class XStreamServiceLoader implements ServiceLoader {
    
    GeoServerResourceLoader resourceLoader;
    
    public XStreamServiceLoader(GeoServerResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    public final ServiceInfo load( GeoServer gs ) throws Exception {
        //look for file matching classname
        String filename = getServiceId() + ".xml";
        File file = resourceLoader.find( filename );
        
        if ( file != null && file.exists() ) {
            //xstream it in
            BufferedInputStream in = 
                new BufferedInputStream( new FileInputStream( file ) );
            try {
                XStream xstream = new XStream();
                return (ServiceInfo) xstream.fromXML(in);
            }
            finally {
                in.close();    
            }
        }
        else {
            //create an 'empty' object
            return createServiceFromScratch( gs );
        }
    }

    public void save(ServiceInfo service, GeoServer gs) throws Exception {
        String filename = getServiceId() + ".xml";
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
    
    protected abstract ServiceInfo createServiceFromScratch(GeoServer gs);
}
