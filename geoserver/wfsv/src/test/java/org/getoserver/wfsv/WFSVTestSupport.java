package org.getoserver.wfsv;

import java.io.File;

import org.geoserver.data.test.LiveData;
import org.geoserver.data.test.LiveDbmsData;
import org.geoserver.data.test.TestData;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.test.GeoServerAbstractTestSupport;
import org.vfny.geoserver.global.Data;

public class WFSVTestSupport extends GeoServerAbstractTestSupport {

    LiveData testData;
    
//    protected String getLogConfiguration() {
//        return "/DEFAULT_LOGGING.properties";
//    }

    @Override
    public TestData getTestData() throws Exception {
        if (testData == null) {
            File base = new File("./src/test/resources/");
            testData = new LiveDbmsData(new File(base, "versioning"), "wfsv", 
                    new File(base, "versioning.sql"));
        }
        return testData;
    }

    public void testCatalogLoaded() {
        Data data = (Data) GeoServerExtensions.bean("catalog");
        assertTrue(data.getFeatureTypeInfo("topp:archsites").isEnabled());
        assertTrue(data.getFeatureTypeInfo("topp:restricted").isEnabled());
        assertTrue(data.getFeatureTypeInfo("topp:roads").isEnabled());
    }

}
