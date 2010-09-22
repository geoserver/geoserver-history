package org.geoserver.wms.map;

import junit.framework.Test;

public class GifMapProducerTest extends DefaultRasterMapProducerTest {
    
    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new GifMapProducerTest());
    }
    
    protected DefaultRasterMapOutputFormat getProducerInstance() {
        return new GIFMapOutputFormat(getWMS()); 
    }

}
