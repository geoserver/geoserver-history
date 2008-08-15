package org.geoserver.wms.responses.map.kml;

import org.vfny.geoserver.wms.responses.map.kml.OWS5MapProducerFactory;

import junit.framework.TestCase;

public class OWS5MapProducerFactoryTest extends TestCase {
    
    private OWS5MapProducerFactory factory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.factory = new OWS5MapProducerFactory();
    }   
    
    public void testCatchAll() {
        // the map producer factory was always answering true to all requests...
        assertFalse(factory.canProduce("bamboo"));
    }
    
    public void testGoodValues() {
        assertTrue(factory.canProduce("application/kml+xml"));
        assertTrue(factory.canProduce("kmlows5"));
    }
}
