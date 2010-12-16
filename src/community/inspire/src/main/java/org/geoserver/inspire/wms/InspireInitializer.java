package org.geoserver.inspire.wms;

import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInitializer;
import org.geoserver.platform.GeoServerResourceLoader;

public class InspireInitializer implements GeoServerInitializer {

    public void initialize(GeoServer geoServer) throws Exception {
        //copy over the schema
        GeoServerResourceLoader l = geoServer.getCatalog().getResourceLoader();
        l.copyFromClassPath("inspire_vs.xsd", 
            l.createFile("www", "inspire", "inspire_vs.xsd"), getClass());
    }

}
