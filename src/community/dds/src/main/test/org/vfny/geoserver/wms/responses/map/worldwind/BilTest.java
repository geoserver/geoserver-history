package org.vfny.geoserver.wms.responses.map.worldwind;

import java.util.logging.Logger;

import javax.xml.namespace.QName;

import junit.framework.Test;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WMSTestSupport;


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


	public void testStandardRequest() throws Exception {
	    String layer = getLayerId(AUS_DEM);
	    String request = "wms?service=wms&request=GetMap&version=1.1.1" +
	    		"&layers=" + layer + "&styles=&bbox=108.3,-46.3,160.3,-4.2&width=64&height=64" + 
	    		"&format=application/bil8&srs=EPSG:4326";
	    String response = getAsString(request);
	    // Check response length in bytes
	    assertEquals("testStandardRequest",4092,response.getBytes().length);
	    
	    request = "wms?service=wms&request=GetMap&version=1.1.1" +
		"&layers=" + layer + "&styles=&bbox=108.3,-46.3,160.3,-4.2&width=64&height=64" + 
		"&format=application/bil16&srs=EPSG:4326";
	    response = getAsString(request);
	    // Check response length in bytes
	    assertEquals("testStandardRequest",8178,response.getBytes().length);
	    
	    request = "wms?service=wms&request=GetMap&version=1.1.1" +
		"&layers=" + layer + "&styles=&bbox=108.3,-46.3,160.3,-4.2&width=64&height=64" + 
		"&format=application/bil32&srs=EPSG:4326";
	    response = getAsString(request);
	    // Check response length in bytes
	    assertEquals("testStandardRequest",16361,response.getBytes().length);
	}
	
	public void testLargeRequest() throws Exception {
	    String layer = getLayerId(AUS_DEM);
	    String request = "wms?service=wms&request=GetMap&version=1.1.1" +
	    		"&layers=" + layer + "&styles=&bbox=108.3,-46.3,160.3,-4.2&width=600&height=600" + 
	    		"&format=image/bil&srs=EPSG:4326";
	    
	    String exceptstr  = getAsString(request);
	    assertTrue("testLargeRequest",exceptstr.contains("ServiceException"));
	}
}
