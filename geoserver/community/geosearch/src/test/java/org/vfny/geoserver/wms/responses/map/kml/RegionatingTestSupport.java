package org.vfny.geoserver.wms.responses.map.kml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import javax.xml.namespace.QName;

import org.geoserver.data.test.LiveData;
import org.geoserver.data.test.MockData;
import org.geoserver.data.test.TestData;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.test.GeoServerTestSupport;
import org.vfny.geoserver.global.Data;

/**
 * Base class for functional testing of the regionating code; sets up a proper testing enviroment
 * with a real data dir and a connection to a postgis data store
 * 
 * @author David Winslow <dwinslow@openplans.org>
 * 
 */
public abstract class RegionatingTestSupport extends GeoServerTestSupport {
    public static QName STACKED_FEATURES = new QName(MockData.SF_URI, "Stacked", MockData.SF_PREFIX);
    public static QName DISPERSED_FEATURES = new QName(MockData.SF_URI, "Dispersed", MockData.SF_PREFIX);

    public void populateDataDirectory(MockData data) throws Exception{
        super.populateDataDirectory(data);

        data.addPropertiesType(
                STACKED_FEATURES,
                getClass().getResource("Stacked.properties"),
                Collections.EMPTY_MAP
                );
        data.addPropertiesType(
                DISPERSED_FEATURES,
                getClass().getResource("Dispersed.properties"),
                Collections.EMPTY_MAP
                );
    }

}

