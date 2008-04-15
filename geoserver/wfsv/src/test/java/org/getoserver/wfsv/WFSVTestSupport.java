package org.getoserver.wfsv;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.geoserver.data.test.LiveData;
import org.geoserver.data.test.LiveDbmsData;
import org.geoserver.data.test.MockData;
import org.geoserver.data.test.TestData;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.test.GeoServerAbstractTestSupport;
import org.vfny.geoserver.global.Data;

/**
 * Base class for WFSV functional testing, sets up a proper testing enviroment
 * for WFSV test with a real data dir and a connection to a postgis data store
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public class WFSVTestSupport extends GeoServerAbstractTestSupport {
    
    // protected String getLogConfiguration() {
    // return "/DEFAULT_LOGGING.properties";
    // }

    @Override
    public TestData buildTestData() throws Exception {
        File base = new File("./src/test/resources/");
        return new LiveDbmsData(new File(base, "versioning"), "wfsv", new File(base,
                "versioning.sql"));
    }
    
    @Override
    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
        
        // init xmlunit
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wfs", "http://www.opengis.net/wfs");
        namespaces.put("wfsv", "http://www.opengis.net/wfsv");
        namespaces.put("ows", "http://www.opengis.net/ows");
        namespaces.put("ogc", "http://www.opengis.net/ogc");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put("topp", "http://www.openplans.org/topp"); 
        namespaces.put("xs", "http://www.w3.org/2001/XMLSchema");
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
    }
    
    /**
     * Returns the url of the WFSV entry point
     * @return
     */
    protected String root() {
        return "wfsv";
    }

//    public void testCatalogLoaded() {
//        Data data = (Data) GeoServerExtensions.bean("catalog");
//        assertTrue(data.getFeatureTypeInfo("topp:archsites").isEnabled());
//        assertTrue(data.getFeatureTypeInfo("topp:restricted").isEnabled());
//        assertTrue(data.getFeatureTypeInfo("topp:roads").isEnabled());
//    }

    
}
