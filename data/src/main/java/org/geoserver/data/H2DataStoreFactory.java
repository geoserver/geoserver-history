package org.geoserver.data;

import org.geoserver.platform.GeoServerResourceLoader;
import org.springframework.beans.factory.InitializingBean;

/**
 * Extension of geotools h2 data store factory which is geoserver data directory 
 * aware.
 * <p>
 * This datastore factory ensures that h2 database files are stored under the 
 * geoserver data directory. It needs to be declared in the spring context.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class H2DataStoreFactory extends org.geotools.data.h2.H2DataStoreFactory 
    implements InitializingBean {

    GeoServerResourceLoader resourceLoader;
    
    public H2DataStoreFactory( GeoServerResourceLoader resourceLoader ) {
        this.resourceLoader = resourceLoader;
    }
    
    public void afterPropertiesSet() throws Exception {
        //set the base directory for storing database files
        setBaseDirectory(resourceLoader.getBaseDirectory());
    }
}
