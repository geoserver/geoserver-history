/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
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
    XStreamPersisterFactory xpf = new XStreamPersisterFactory();
    
    public XStreamServiceLoader(GeoServerResourceLoader resourceLoader, String filenameBase) {
        this.resourceLoader = resourceLoader;
        this.filenameBase = filenameBase;
    }
    
    public void setXStreamPeristerFactory(XStreamPersisterFactory xpf) {
        this.xpf = xpf;
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
                XStreamPersister xp = xpf.createXMLPersister();
                initXStreamPersister(xp, gs);
                return initialize( xp.load( in, getServiceClass() ) );
            }
            finally {
                in.close();    
            }
        }
        else {
            //create an 'empty' object
            ServiceInfo service = createServiceFromScratch( gs );
            return initialize( (T) service );
        }
    }

    protected T initialize( T service ) {
        return service;
    }
    
    public void save(T service, GeoServer gs) throws Exception {
        String filename = filenameBase + ".xml";
        File file = resourceLoader.find( filename );
        if ( file == null ) {
            file = resourceLoader.createFile(filename);
        }
        
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        try {
            XStreamPersister xp = xpf.createXMLPersister();
            initXStreamPersister(xp, gs);
            xp.save( service, out );
            
            out.flush();
        }
        finally {
            out.close();
        }
    }
    
    /**
     * Hook for subclasses to configure the xstream.
     * <p>
     * The most common use is to do some aliasing or omit some fields. 
     * </p>
     */
    protected void initXStreamPersister( XStreamPersister xp, GeoServer gs ) {
        xp.setGeoServer( gs );
        xp.getXStream().alias( filenameBase, getServiceClass() );
    }
    
    protected abstract T createServiceFromScratch(GeoServer gs);
}
