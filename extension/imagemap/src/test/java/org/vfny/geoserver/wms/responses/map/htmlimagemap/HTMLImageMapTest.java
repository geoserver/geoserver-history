package org.vfny.geoserver.wms.responses.map.htmlimagemap;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.geoserver.platform.GeoServerResourceLoader;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.test.TestData;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;

import com.vividsolutions.jts.geom.Envelope;



/**
 * Test suite for HTMLImageMapMapProducer GetMapProducer
 * 
 * @author Mauro Bartolomeoli
 */
public class HTMLImageMapTest extends TestCase {

	private static final StyleFactory sFac = CommonFactoryFinder.getStyleFactory(null);
	
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(HTMLImageMapTest.class.getPackage().getName());
	
	//GetMapProducerFactorySpi mapFactory=null;
	GetMapProducer mapProducer=null;
	
	CoordinateReferenceSystem WGS84=null;
		
	DataStore testDS = null;
	
	int mapWidth=600;
	int mapHeight=600;
	
	public void setUp() throws Exception {
		// initializes GeoServer Resource Loading (is needed by some tests to not produce exceptions)
		
	    System.setProperty("org.geotools.referencing.forceXY", "true");
		File testdata=TestData.file(this, ".");
		System.setProperty("GEOSERVER_DATA_DIR", testdata.getAbsolutePath());
		GeoServerResourceLoader loader = new GeoServerResourceLoader(testdata);		
        GenericWebApplicationContext context = new GenericWebApplicationContext();
        context.getBeanFactory().registerSingleton("resourceLoader", loader);
        GeoserverDataDirectory.init(context);
		
        // initialized WGS84 CRS (used by many tests)
        WGS84=CRS.decode("EPSG:4326");
        
        
        testDS=getTestDataStore();
        
        // initializes GetMapProducer factory and actual producer
        //this.mapFactory = getProducerFactory();
        this.mapProducer=getProducerInstance();
        super.setUp();
    }
	
	public void tearDown() throws Exception {
        //this.mapFactory = null;
        this.mapProducer=null;
        super.tearDown();
    }
	
	/*protected GetMapProducerFactorySpi getProducerFactory() {
	    return new HTMLImageMapMapProducerFactory();
	}*/
	
	protected GetMapProducer getProducerInstance() {
		/*if(mapFactory!=null)
			return mapFactory.createMapProducer("text/html", null);*/
		return new HTMLImageMapMapProducer();
		
	}
	
	/*public void testGetMapProducerFactory() throws Exception {		
		assertNotNull(mapFactory);		
	}*/
	
	public void testGetMapProducer() throws Exception {
		assertNotNull(mapProducer);				
	}
	
	public DataStore getTestDataStore() throws IOException {
    	File testdata=TestData.file(this, "featureTypes");
	
        return new MyPropertyDataStore(testdata);
            
    }
	
	public DataStore getProjectedTestDataStore() throws IOException {
    	File testdata=TestData.file(this, "featureTypes");
	
        try {
			return new MyPropertyDataStore(testdata,CRS.decode("EPSG:3004"));
		} catch (NoSuchAuthorityCodeException e) {
			e.printStackTrace();
			return null;
		} catch (FactoryException e) {
			// 
			e.printStackTrace();
			return null;
		}
            
    }
	
	protected Style getTestStyle(String styleName) throws Exception {
        SLDParser parser = new SLDParser(sFac);
        File styleRes=TestData.file(this, "styles/"+styleName);
        
        parser.setInput(styleRes);

        Style s = parser.readXML()[0];

        return s;
    }
	
	protected void assertTestResult(String testName, GetMapProducer producer) {
		
		ByteArrayOutputStream out =null;
		StringBuffer testText=new StringBuffer();
        try {
        	        	            
        	out = new ByteArrayOutputStream();
            producer.writeTo(out);
            out.flush();
            out.close();
            File testFile=TestData.file(this, "results/"+testName+".txt");
            BufferedReader reader=new BufferedReader(new FileReader(testFile));
            
            String s=null;
            while((s=reader.readLine())!=null)
            	testText.append(s+"\n");
            
            reader.close();

            
        } catch (Exception e) {
           
            fail(e.getMessage());
        }
        assertNotNull(out);
        assertTrue(out.size()>0);
        String s = new String(out.toByteArray());
        
        assertEquals(testText.toString(), s);
    }
	
	protected void assertNotBlank(String testName, GetMapProducer producer) {
		
		ByteArrayOutputStream out =null;

        try {
        	out = new ByteArrayOutputStream();
            producer.writeTo(out);
            out.flush();
            out.close();            
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertNotNull(out);
        assertTrue(out.size()>0);
        String s=new String(out.toByteArray());
        System.out.println(s);
    }
	
	public void testStates() throws Exception {
		File shapeFile=TestData.file(this, "featureTypes/states.shp");		
		ShapefileDataStore ds=new ShapefileDataStore(shapeFile.toURL());
		
		final FeatureSource<SimpleFeatureType,SimpleFeature> fs = ds.getFeatureSource("states");
		final ReferencedEnvelope env = new ReferencedEnvelope(fs.getBounds(),WGS84);
		
		final WMSMapContext map = new WMSMapContext();
        map.setAreaOfInterest(env);
        map.setMapWidth(mapWidth);
        map.setMapHeight(mapHeight);
        map.setTransparent(false);

        Style basicStyle = getTestStyle("Population.sld");
        map.addLayer(fs, basicStyle);

        this.mapProducer.setOutputFormat("text/html");
        this.mapProducer.setMapContext(map);
        this.mapProducer.produceMap();
        assertTestResult("States", this.mapProducer);
	}
	
	public void testMapProduceBasicPolygons() throws Exception {
		
        final FeatureSource<SimpleFeatureType,SimpleFeature> fs = testDS.getFeatureSource("BasicPolygons");
        final ReferencedEnvelope env = new ReferencedEnvelope(fs.getBounds(),WGS84);
        
        LOGGER.info("about to create map ctx for BasicPolygons with bounds " + env);

        final WMSMapContext map = new WMSMapContext();
        map.setAreaOfInterest(env);
        map.setMapWidth(mapWidth);
        map.setMapHeight(mapHeight);
        map.setTransparent(false);

        Style basicStyle = getTestStyle("default.sld");
        map.addLayer(fs, basicStyle);

        this.mapProducer.setOutputFormat("text/html");
        this.mapProducer.setMapContext(map);
        this.mapProducer.produceMap();
        assertTestResult("BasicPolygons", this.mapProducer);

	}	
	
	public void testMapProducePolygonsWithHoles() throws Exception {
		
		final FeatureSource<SimpleFeatureType,SimpleFeature> fs = testDS.getFeatureSource("PolygonWithHoles");
        final ReferencedEnvelope env = new ReferencedEnvelope(fs.getBounds(),WGS84);
        
        LOGGER.info("about to create map ctx for BasicPolygons with bounds " + env);

        final WMSMapContext map = new WMSMapContext();
        map.setAreaOfInterest(env);
        map.setMapWidth(mapWidth);
        map.setMapHeight(mapHeight);
        map.setTransparent(false);

        Style basicStyle = getTestStyle("default.sld");
        map.addLayer(fs, basicStyle);

        this.mapProducer.setOutputFormat("text/html");
        this.mapProducer.setMapContext(map);
        this.mapProducer.produceMap();
        assertTestResult("PolygonWithHoles", this.mapProducer);
	}
	
	public void testMapProducePolygonsWithSkippedHoles() throws Exception {
		
		final FeatureSource<SimpleFeatureType,SimpleFeature> fs = testDS.getFeatureSource("PolygonWithSkippedHoles");
        final ReferencedEnvelope env = new ReferencedEnvelope(fs.getBounds(),WGS84);
        
        LOGGER.info("about to create map ctx for BasicPolygons with bounds " + env);

        final WMSMapContext map = new WMSMapContext();
        map.setAreaOfInterest(env);
        map.setMapWidth(mapWidth);
        map.setMapHeight(mapHeight);
        map.setTransparent(false);

        Style basicStyle = getTestStyle("default.sld");
        map.addLayer(fs, basicStyle);

        this.mapProducer.setOutputFormat("text/html");
        this.mapProducer.setMapContext(map);
        this.mapProducer.produceMap();
        assertTestResult("PolygonWithSkippedHoles", this.mapProducer);
	}
	
	public void testMapProduceReproject() throws Exception {
		final DataStore ds = getProjectedTestDataStore();
        final FeatureSource<SimpleFeatureType,SimpleFeature> fs = ds.getFeatureSource("ProjectedPolygon");
        final ReferencedEnvelope env = new ReferencedEnvelope(fs.getBounds(),CRS.decode("EPSG:3004"));
        
        LOGGER.info("about to create map ctx for ProjectedPolygon with bounds " + env);

        final WMSMapContext map = new WMSMapContext();
        
        CoordinateReferenceSystem sourceCrs=CRS.decode("EPSG:3004");
        CoordinateReferenceSystem targetCrs=CRS.decode("EPSG:3003");
        
        MathTransform transform=CRS.findMathTransform(sourceCrs, targetCrs,true);
        Envelope projEnv=JTS.transform(env, transform);
        ReferencedEnvelope refEnv=new ReferencedEnvelope(projEnv,targetCrs);
        
        map.setAreaOfInterest(refEnv);
        map.setMapWidth(mapWidth);
        map.setMapHeight(mapHeight);
        map.setBgColor(Color.red);
        map.setTransparent(false);
       
        
        map.setCoordinateReferenceSystem(targetCrs);
        Style basicStyle=getTestStyle("BasicPolygons.sld");
        
        map.addLayer(fs, basicStyle);

        this.mapProducer.setOutputFormat("text/html");
        this.mapProducer.setMapContext(map);
        this.mapProducer.produceMap();
        assertTestResult("ProjectedPolygon", this.mapProducer);
	}
	
	public void testMapProduceLines() throws Exception {
		
        final FeatureSource<SimpleFeatureType,SimpleFeature> fs = testDS.getFeatureSource("RoadSegments");
        final ReferencedEnvelope env = new ReferencedEnvelope(fs.getBounds(),WGS84);

        LOGGER.info("about to create map ctx for RoadSegments with bounds " + env);

        final WMSMapContext map = new WMSMapContext();
        map.setAreaOfInterest(env);
        map.setMapWidth(mapWidth);
        map.setMapHeight(mapHeight);
        
        map.setTransparent(false);
                
        Style basicStyle = getTestStyle("RoadSegments.sld");
        map.addLayer(fs, basicStyle);

        this.mapProducer.setOutputFormat("text/html");
        this.mapProducer.setMapContext(map);
        this.mapProducer.produceMap();
        assertTestResult("RoadSegments", this.mapProducer);

	}
	
	public void testMapRuleWithFilters() throws Exception {
		/*Filter f=filterFactory.equals(filterFactory.property("NAME"),filterFactory.literal("Route 5"));
		Query q=new Query("RoadSegments",f);*/
        final FeatureSource<SimpleFeatureType,SimpleFeature> fs = testDS.getFeatureSource("RoadSegments");
        final ReferencedEnvelope env = new ReferencedEnvelope(fs.getBounds(),WGS84);

        LOGGER.info("about to create map ctx for RoadSegments with filter on name and bounds " + env);

        final WMSMapContext map = new WMSMapContext();
        map.setAreaOfInterest(env);
        map.setMapWidth(mapWidth);
        map.setMapHeight(mapHeight);
        
        map.setTransparent(false);
                
        Style basicStyle = getTestStyle("RoadSegmentsFiltered.sld");
        map.addLayer(fs, basicStyle);

        this.mapProducer.setOutputFormat("text/html");
        this.mapProducer.setMapContext(map);
        this.mapProducer.produceMap();
        assertTestResult("RoadSegmentsFiltered", this.mapProducer);

	}
	
	public void testMapProducePoints() throws Exception {
		
        final FeatureSource<SimpleFeatureType,SimpleFeature> fs = testDS.getFeatureSource("BuildingCenters");
        final ReferencedEnvelope env = new ReferencedEnvelope(fs.getBounds(),WGS84);

        LOGGER.info("about to create map ctx for BuildingCenters with bounds " + env);

        final WMSMapContext map = new WMSMapContext();
        map.setAreaOfInterest(env);
        map.setMapWidth(mapWidth);
        map.setMapHeight(mapHeight);
        
        map.setTransparent(false);
                
        Style basicStyle = getTestStyle("BuildingCenters.sld");
        map.addLayer(fs, basicStyle);

        this.mapProducer.setOutputFormat("text/html");
        this.mapProducer.setMapContext(map);
        this.mapProducer.produceMap();
        assertTestResult("BuildingCenters", this.mapProducer);

	}
	
	public void testMapProduceMultiPoints() throws Exception {
		
        final FeatureSource<SimpleFeatureType,SimpleFeature> fs = testDS.getFeatureSource("BuildingCentersMultiPoint");
        final ReferencedEnvelope env = new ReferencedEnvelope(fs.getBounds(),WGS84);

        LOGGER.info("about to create map ctx for BuildingCentersMultiPoint with bounds " + env);

        final WMSMapContext map = new WMSMapContext();
        map.setAreaOfInterest(env);
        map.setMapWidth(mapWidth);
        map.setMapHeight(mapHeight);
        
        map.setTransparent(false);
                
        Style basicStyle = getTestStyle("BuildingCenters.sld");
        map.addLayer(fs, basicStyle);

        this.mapProducer.setOutputFormat("text/html");
        this.mapProducer.setMapContext(map);
        this.mapProducer.produceMap();
        assertTestResult("BuildingCentersMultiPoint", this.mapProducer);

	}
	
	public void testMapProduceCollection() throws Exception {
		
        final FeatureSource<SimpleFeatureType,SimpleFeature> fs = testDS.getFeatureSource("CollectionSample");
        final ReferencedEnvelope env = new ReferencedEnvelope(fs.getBounds(),WGS84);

        LOGGER.info("about to create map ctx for RoadSegments with bounds " + env);

        final WMSMapContext map = new WMSMapContext();
        map.setAreaOfInterest(env);
        map.setMapWidth(mapWidth);
        map.setMapHeight(mapHeight);
        
        map.setTransparent(false);
                
        Style basicStyle = getTestStyle("CollectionSample.sld");
        map.addLayer(fs, basicStyle);

        this.mapProducer.setOutputFormat("text/html");
        this.mapProducer.setMapContext(map);
        this.mapProducer.produceMap();
        assertTestResult("CollectionSample", this.mapProducer);

	}
	
	public void testMapProduceNoCoords() throws Exception {
		final FeatureSource<SimpleFeatureType,SimpleFeature> fs = testDS.getFeatureSource("NoCoords");
        final ReferencedEnvelope env = new ReferencedEnvelope(2.0,6.0,2.0,6.0,WGS84);
     
        LOGGER.info("about to create map ctx for NamedPlaces with bounds " + env);

        final WMSMapContext map = new WMSMapContext();
        map.setAreaOfInterest(env);
        map.setMapWidth(mapWidth);
        map.setMapHeight(mapHeight);
        
        map.setTransparent(false);
                
        Style basicStyle = getTestStyle("NamedPlaces.sld");
        map.addLayer(fs, basicStyle);

        this.mapProducer.setOutputFormat("text/html");
        this.mapProducer.setMapContext(map);
        this.mapProducer.produceMap();
        assertTestResult("NoCoords", this.mapProducer);
	}
	
	
	
	public static void main(String[] args) {	
		junit.textui.TestRunner.run(HTMLImageMapTest.class);
	}
	
	static class MyPropertyDataStore extends PropertyDataStore {
		
		CoordinateReferenceSystem myCRS=DefaultGeographicCRS.WGS84;
	    /**
         * Creates a new MyPropertyDataStore object.
         *
         * @param dir DOCUMENT ME!
         */
	    public MyPropertyDataStore(File dir) {
	        super(dir);
	    }
	    
	    /**
         * Creates a new MyPropertyDataStore object.
         *
         * @param dir DOCUMENT ME!
         */
	    public MyPropertyDataStore(File dir,CoordinateReferenceSystem coordinateSystem) {
	        super(dir);
	        this.myCRS=coordinateSystem;
	    }

	    /**
	     * DOCUMENT ME!
	     *
	     * @param typeName DOCUMENT ME!
	     *
	     * @return DOCUMENT ME!
	     *
	     * @throws IOException DOCUMENT ME!
	     * @throws DataSourceException DOCUMENT ME!
	     */
	    public SimpleFeatureType getSchema(String typeName) throws IOException {
	    	SimpleFeatureType schema = super.getSchema(typeName);

	        try {
	            return DataUtilities.createSubType(schema, null, myCRS);
	        } catch (SchemaException e) {
	            throw new DataSourceException(e.getMessage(), e);
	        }
	    }
	    
	}
	
}

