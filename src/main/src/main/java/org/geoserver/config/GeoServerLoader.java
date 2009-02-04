/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.Wrapper;
import org.geoserver.catalog.event.CatalogListener;
import org.geoserver.catalog.util.LegacyCatalogImporter;
import org.geoserver.config.util.LegacyConfigurationImporter;
import org.geoserver.config.util.LegacyLoggingImporter;
import org.geoserver.logging.LoggingInitializer;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geotools.util.logging.Logging;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * Base class for initializing based on GeoServer configuration.
 * <p>
 * This class should  be subclassed in cases where some sort of system 
 * initialization must be carried out, and aspects of the initialization depend 
 * on GeoServer configuration.
 * </p>
 * <p>
 * Instances of this class should be registered in a spring context. Example:
 * <pre>
 *  &lt;bean id="geoServerLoader" class="GeoServerLoader"/>
 * </pre>
 * </p>
 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public final class GeoServerLoader implements BeanPostProcessor, DisposableBean, 
    ApplicationContextAware {

    static Logger LOGGER = Logging.getLogger( "org.geoserver" );
    
    ApplicationContext applicationContext;
    GeoServerResourceLoader resourceLoader;
    GeoServer geoserver;
    
    public GeoServerLoader( GeoServerResourceLoader resourceLoader ) {
        this.resourceLoader = resourceLoader;
    }
    
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        GeoserverDataDirectory.init((WebApplicationContext)applicationContext);
        this.applicationContext = applicationContext;
    }
    
    public final Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    public final Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        if ( bean instanceof GeoServer ) {
            geoserver = (GeoServer) bean;
            initialize();
        }
        
        return bean;
    }
    
    protected void initialize() {
        
        //first thing to do is load the logging configuration
        LegacyLoggingImporter loggingImporter = new LegacyLoggingImporter( geoserver );
        try {
            loggingImporter.imprt( resourceLoader.getBaseDirectory() );
        } 
        catch (Exception e) {
            throw new RuntimeException( e );
        }
        
        try {
            LoggingInitializer loggingIniter = new LoggingInitializer();
            loggingIniter.setResourceLoader( resourceLoader );
            loggingIniter.initialize( geoserver, applicationContext );
        } 
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        //load catalog
        LegacyCatalogImporter catalogImporter = new LegacyCatalogImporter();
        catalogImporter.setResourceLoader(resourceLoader);
        Catalog catalog = geoserver.getCatalog();
        catalog.setResourceLoader(resourceLoader);

        if(catalog instanceof Wrapper && ((Wrapper) catalog).isWrapperFor(Catalog.class)) {
            catalog = ((Wrapper) catalog).unwrap(Catalog.class);
        }
        catalogImporter.setCatalog(catalog);
        
        try {
            catalogImporter.imprt( resourceLoader.getBaseDirectory() );
            initializeStyles(catalog);
        }
        catch(Exception e) {
            throw new RuntimeException( e );
        }
        
        //load configuration
        LegacyConfigurationImporter importer = new LegacyConfigurationImporter();
        importer.setConfiguration(geoserver);
        
        try {
            importer.imprt( resourceLoader.getBaseDirectory() );
        } 
        catch (Exception e) {
            throw new RuntimeException( e );
        }
        
        //load initializer extensions
        List<GeoServerInitializer> initializers = GeoServerExtensions.extensions( GeoServerInitializer.class );
        for ( GeoServerInitializer initer : initializers ) {
            try {
                initer.initialize( geoserver );
            }
            catch( Throwable t ) {
                //TODO: log this
                t.printStackTrace();
            }
        }
        
        //load listeners
        List<CatalogListener> catalogListeners = GeoServerExtensions.extensions( CatalogListener.class );
        for ( CatalogListener l : catalogListeners ) {
            catalog.addListener( l );
        }
        List<ConfigurationListener> configListeners = GeoServerExtensions.extensions( ConfigurationListener.class );
        for ( ConfigurationListener l : configListeners ) {
            geoserver.addListener( l );
        }
    }
    
    /**
     * Does some post processing on the catalog to ensure that the "well-known" styles
     * are always around.
     */
    void initializeStyles( Catalog catalog ) throws IOException {
        if ( catalog.getStyleByName( StyleInfo.DEFAULT_POINT ) == null ) {
            initializeStyle( catalog, StyleInfo.DEFAULT_POINT, "default_point.sld" );
        }
        if ( catalog.getStyleByName( StyleInfo.DEFAULT_LINE ) == null ) {
            initializeStyle( catalog, StyleInfo.DEFAULT_LINE, "default_line.sld" );
        }
        if ( catalog.getStyleByName( StyleInfo.DEFAULT_POLYGON ) == null ) {
            initializeStyle( catalog, StyleInfo.DEFAULT_POLYGON, "default_line.sld" );
        }
        if ( catalog.getStyleByName( StyleInfo.DEFAULT_RASTER ) == null ) {
            initializeStyle( catalog, StyleInfo.DEFAULT_RASTER, "default_raster.sld" );
        }
    }
    
    /**
     * Copies a well known style out to the data directory and adds a catalog entry for it.
     */
    void initializeStyle( Catalog catalog, String styleName, String sld ) throws IOException {
        
        //copy the file out to the data directory if necessary
        if ( resourceLoader.find( "styles", sld ) == null ) {
            FileUtils.copyURLToFile(getClass().getResource(sld), 
                new File( resourceLoader.find( "styles" ), sld) );
        }
        
        //create a style for it
        StyleInfo s = catalog.getFactory().createStyle();
        s.setName( styleName );
        s.setFilename( sld );
        catalog.add( s );
    }
    
    public void reload() throws Exception {
        destroy();
        initialize();
    }
    
    public void destroy() throws Exception {
        //TODO: persist catalog
        //TODO: persist global

        //persist services
        Collection services = geoserver.getServices();
        List<ServiceLoader> loaders = GeoServerExtensions.extensions( ServiceLoader.class );
        
        for ( Iterator s = services.iterator(); s.hasNext(); ) {
            ServiceInfo service = (ServiceInfo) s.next();
            for ( ServiceLoader loader : loaders ) {
                if ( loader.getServiceId().equals(service.getId()) ) {
                    try {
                        loader.save( service, geoserver );
                        break;
                    }
                    catch( Throwable t ) {
                        LOGGER.warning( "Error persisting service: " + service.getId() );
                        LOGGER.log( Level.INFO, "", t );
                    }
                }
            }
        }
        
        //dispose
        geoserver.dispose();
    }
}
