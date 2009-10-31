package org.vfny.geoserver.wms.responses.map.worldwind;

import java.io.File;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.geotools.test.TestData;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geotools.data.DataStore;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.wms.GetMapProducer;

/**
 * Test case for producing Raw bil images out of an elevation model.
 * 
 * @author Tishampati Dhar
 * @since 2.0.x
 * 
 */

public class BilTest extends TestCase {
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(BilTest.class.getPackage().getName());
	
	GetMapProducer mapProducer = null;
	
	DataStore testDS = null;
	
	int mapWidth=512;
	int mapHeight=512;

	private CoordinateReferenceSystem WGS84 = null;
	
	public void setUp() throws Exception {
		System.setProperty("org.geotools.referencing.forceXY", "true");
		File testdata=TestData.file(this, ".");
		System.setProperty("GEOSERVER_DATA_DIR", testdata.getAbsolutePath());
		GeoServerResourceLoader loader = new GeoServerResourceLoader(testdata);		
        GenericWebApplicationContext context = new GenericWebApplicationContext();
        context.getBeanFactory().registerSingleton("resourceLoader", loader);
        GeoserverDataDirectory.init(context);
		
        // initialized WGS84 CRS (used by many tests)
        WGS84 =CRS.decode("EPSG:4326");
        
        
        testDS=getTestDataStore();
        
        // initializes GetMapProducer factory and actual producer
        //this.mapFactory = getProducerFactory();
        this.mapProducer=getProducerInstance();
        super.setUp();
	}

	private DataStore getTestDataStore() {
		// TODO Auto-generated method stub
		return null;
	}

	private GetMapProducer getProducerInstance() {
		// TODO Auto-generated method stub
		return null;
	}
}
