/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.util.logging.Logger;

import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.servlet.ServletContext;

import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServer;
import org.geoserver.util.ReaderUtils;
import org.geotools.factory.Hints;
import org.geotools.resources.image.ImageUtilities;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.DataTransferObject;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.vfny.geoserver.global.dto.WCSDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;
import org.vfny.geoserver.global.xml.XMLConfigReader;


/**
 * The application configuratoin facade.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class Config implements ApplicationContextAware {
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.global");
    WebApplicationContext context;
    XMLConfigReader reader;

    GeoServer configuration;
    
//    public XMLConfigReader getXMLReader() throws ConfigurationException {
//        return reader;
//    }

    public File dataDirectory() {
        return GeoserverDataDirectory.getGeoserverDataDirectory();
    }

    public void setConfiguration(GeoServer configuration) {
        this.configuration = configuration;
    }
    
    public void setApplicationContext(ApplicationContext context)
        throws BeansException {
        
        this.context = (WebApplicationContext) context;

        ServletContext sc = this.context.getServletContext();
        //try {
            GeoserverDataDirectory.init(this.context);
            //before proceeding perform some configuration sanity checks
            try {
                doConfigSanityCheck();
            } catch (ConfigurationException ce) {
                LOGGER.severe(ce.getMessage());
                throw new BeanInitializationException(ce.getMessage(),ce);
            }
            
            //reader = new XMLConfigReader(dataDirectory(), sc, catalog);
        //} catch (ConfigurationException e) {
        //    String msg = "Error creating xml config reader";
        //    throw new BeanInitializationException(msg, e);
        //}
    }

    /**
     * Performs a sanity check on the configuration files to allow for
     * the early failure of the bean initialization and providing
     * a meaningful failure message.
     * <p>
     * For the time being, it only ensures that the data dir exists.
     * </p>
     */
    private void doConfigSanityCheck() throws ConfigurationException {
        File dataDirectory = dataDirectory();
        try {
            ReaderUtils.checkFile(dataDirectory, true);
        } catch (FileNotFoundException e) {
            throw new ConfigurationException("Can't access the configuration directory. Reason: "
                    + e.getMessage(), e);
        }
    }

    public WebApplicationContext getApplictionContext() {
        return context;
    }

    public DataDTO getData() {
        //return reader.getData();
        return new Data( configuration ).toDTO();
    }

    public GeoServerDTO getGeoServer() {
        //return reader.getGeoServer();
        return new org.vfny.geoserver.global.GeoServer( configuration ).toDTO();
    }

    public WMSDTO getWms() {
        return (WMSDTO) dto( "org.vfny.geoserver.global.WMS");
        //return reader.getWms();
    }

    public WFSDTO getWfs() {
        return (WFSDTO) dto( "org.geoserver.wfs.WFS" );
        //return reader.getWfs();
    }

    public WCSDTO getWcs() {
        return (WCSDTO) dto( "org.vfny.geoserver.global.WCS");
        //return reader.getWcs();
    }
    
    DataTransferObject dto( String className ) {
        try {
            Class clazz = Class.forName( className );
            Constructor c = clazz.getConstructor(GeoServer.class);
            Service s = (Service) c.newInstance(configuration);
            return (DataTransferObject) s.toDTO();
        } 
        catch (Exception e) {
            LOGGER.warning( "Error loading service: " + className );
            return null;
        }
    }
}
