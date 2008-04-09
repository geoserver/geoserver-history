package org.vfny.geoserver.wms.responses.map.gif;

import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;
import org.vfny.geoserver.wms.responses.map.DefaultRasterMapProducerTest;

public class GifMapProducerTest extends DefaultRasterMapProducerTest {
    
    protected DefaultRasterMapProducer getProducerInstance() {
        return new GIFMapProducer("image/gif", null); 
    }

}
