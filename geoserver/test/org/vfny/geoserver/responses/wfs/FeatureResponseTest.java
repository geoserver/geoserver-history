package org.vfny.geoserver.responses.wfs;

import java.util.HashMap;
import java.util.Map;

import org.geotools.data.Catalog;
import org.geotools.data.DataTestCase;
import org.geotools.data.DefaultCatalog;
import org.vfny.geoserver.global.CatalogConfig;
import org.vfny.geoserver.global.ServerConfig;
import org.vfny.geoserver.requests.wfs.FeatureRequest;
/**
 * This is my attempt at testing FeatureResponse using
 * normal JUnit tests.
 * <p>
 * Due to the interaction with ModelConfig.getInstance() this
 * may not be possible.
 * </p>
 * <p>
 * 
 * @author jgarnett
 */
public class FeatureResponseTest extends DataTestCase {
    CatalogConfig config;
    FeatureResponse response;
    
    /**
     * Constructor for FeatureResponseTest.
     * @param arg0
     */
    public FeatureResponseTest(String arg0) {
        super(arg0);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        response = new FeatureResponse();
        Catalog cat = new DefaultCatalog();
        Map config = new HashMap();
        ServerConfig.load( config, cat );      
        
        
    }
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFeatureResponse() {
        FeatureRequest request = new FeatureRequest();
        request.setHandle( "FeatureRequest" );
                
    }

    public void testGetContentType() {
    }

    public void testWriteTo() {
    }

    /*
     * Test for void execute(Request)
     */
    public void testExecuteRequest() {
    }

    /*
     * Test for void execute(FeatureRequest)
     */
    public void testExecuteFeatureRequest() {
    }

    public void testAbort() {
    }

}
