package org.vfny.geoserver.wms.responses.map.worldwind;

import javax.xml.namespace.QName;

import junit.framework.Test;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WMSTestSupport;
import org.vfny.geoserver.wms.responses.featureinfo.GetFeatureInfoTest;
import org.vfny.geoserver.wms.responses.map.png.GetMapTest;
import org.w3c.dom.Document;

/**
 * Test case for producing Raw bil images out of an elevation model.
 * 
 * @author Tishampati Dhar
 * @since 2.0.x
 * 
 */

public class BilTest extends WMSTestSupport {
	/**
     * This is a READ ONLY TEST so we can use one time setup
     */
	
	public static String WCS_PREFIX = "wcs";
    public static String WCS_URI = "http://www.opengis.net/wcs/1.1.1";
    public static QName AUS_DEM = new QName(WCS_URI, "Ausdem", WCS_PREFIX);
    
    
    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new BilTest());
    }
    
    
    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        WMSInfo wmsInfo = getGeoServer().getService(WMSInfo.class);
        wmsInfo.setMaxBuffer(50);
        getGeoServer().save(wmsInfo);
    }
    
    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        super.populateDataDirectory(dataDirectory);
        dataDirectory.addStyle("raster", BilTest.class.getResource("raster.sld"));
        dataDirectory.addCoverage(AUS_DEM, BilTest.class.getResource("aus_dem.tif"),
                "tiff", "raster");
    }
}
