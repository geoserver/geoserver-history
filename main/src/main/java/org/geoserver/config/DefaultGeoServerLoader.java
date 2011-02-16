package org.geoserver.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.MetadataMap;
import org.geoserver.catalog.impl.CatalogImpl;
import org.geoserver.config.impl.CoverageAccessInfoImpl;
import org.geoserver.config.impl.GeoServerInfoImpl;
import org.geoserver.config.util.LegacyConfigurationImporter;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.config.util.XStreamServiceLoader;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.GeoServerResourceLoader;

/**
 * Default GeoServerLoader which loads and persists configuration from the classic GeoServer data 
 * directory structure.
 *  
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class DefaultGeoServerLoader extends GeoServerLoader {

    public DefaultGeoServerLoader(GeoServerResourceLoader resourceLoader) {
        super(resourceLoader);
    }
    
    protected void loadCatalog(Catalog catalog, XStreamPersister xp) throws Exception {
        catalog.setResourceLoader(resourceLoader);

        readCatalog(catalog, xp);
        
        if ( !legacy ) {
            //add the listener which will persist changes
            catalog.addListener( new GeoServerPersister( resourceLoader, xp ) );
        }
    }
    
    protected void loadGeoServer(final GeoServer geoServer, XStreamPersister xp) throws Exception {
        
        //add event listener which persists changes
        final List<XStreamServiceLoader> loaders = 
            GeoServerExtensions.extensions( XStreamServiceLoader.class );
        geoServer.addListener( 
            new ConfigurationListenerAdapter() {
                @Override
                public void handlePostServiceChange(ServiceInfo service) {
                    for ( XStreamServiceLoader<ServiceInfo> l : loaders  ) {
                        if ( l.getServiceClass().isInstance( service ) ) {
                            try {
                                l.save( service, geoServer );
                            } catch (Throwable t) {
                                //TODO: log this
                                t.printStackTrace();
                            }
                        }
                    }
                }
            }
        );
        
        readConfiguration(geoServer, xp);
        
        geoServer.addListener( new GeoServerPersister( resourceLoader, xp ) );
    }
    
    @Override
    protected void initializeStyles(Catalog catalog, XStreamPersister xp) throws IOException {
        //add a persister temporarily in case the styles don't exist on disk
        GeoServerPersister p = new GeoServerPersister(resourceLoader, xp);
        catalog.addListener(p);
        
        super.initializeStyles(catalog, xp);
        
        catalog.removeListener(p);
    }

}
