package org.geoserver.catalog.hibernate;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.MapInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.impl.WorkspaceInfoImpl;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class HibernateCatalogTest extends HibernateTestSupport {

	HibernateCatalog catalog;

	protected void onSetUpBeforeTransaction() throws Exception {
		super.onSetUpBeforeTransaction();
		
		catalog = (HibernateCatalog) applicationContext.getBean("catalogTarget");
	}
	
	public void testDataStore() {
		//ensure no stores
		Iterator stores = catalog.getDataStores().iterator();
		assertFalse( stores.hasNext() );
		
		//store needs a workspace...
		WorkspaceInfo ws = catalog.getFactory().createWorkspace();
		ws.setName("testDataStoreWorkspace");

		assertNull(ws.getId());
		catalog.add(ws);
		assertNotNull(ws.getId());

		endTransaction();
        startNewTransaction();
        		
		//create a new store
		DataStoreInfo store = catalog.getFactory().createDataStore();
		store.setWorkspace(ws);
		store.setName("dataStore");
		store.setDescription( "store description");
		store.setEnabled( true );
		
		store.getConnectionParameters().put( "param1", "value1" );
		store.getConnectionParameters().put( "param2", new Integer( 2 ) );
		
		store.getMetadata().put( "1", "one" );
		store.getMetadata().put( "2", "two" );
		
		catalog.add( store );
		endTransaction();
		
		startNewTransaction();
		stores = catalog.getDataStores().iterator();
		assertTrue( stores.hasNext() );

		store = catalog.getDataStore( store.getId() );
		assertEquals( "dataStore", store.getName() );
		assertEquals( "store description", store.getDescription() );
		assertTrue( store.isEnabled() );
		
		assertEquals( "value1", store.getConnectionParameters().get( "param1" ) );
		assertEquals( new Integer(2), store.getConnectionParameters().get( "param2" ) );
	
		assertEquals( "one", store.getMetadata().get( "1" ) );
		assertEquals( "two", store.getMetadata().get( "2" ) );
	}
	
	public void testCoverageStore() {
		
		//ensure no stores
		Iterator stores = catalog.getCoverageStores().iterator();
		assertFalse( stores.hasNext() );
		
        //store needs a workspace...
        WorkspaceInfo ws = catalog.getFactory().createWorkspace();
        ws.setName("testCoverageStoreWorkspace");
        catalog.add(ws);
		
        endTransaction();
        startNewTransaction();

        //create a new store
		CoverageStoreInfo store = catalog.getFactory().createCoverageStore();
		store.setWorkspace(ws);
		store.setName("coverageStore");
		store.setDescription( "store description");
		store.setEnabled( true );
		store.setType( "type" );
		store.setURL( "url" );
		
		
		store.getMetadata().put( "1", "one" );
		store.getMetadata().put( "2", new Double(2.0) );
		
		catalog.add( store );
		
		endTransaction();
		startNewTransaction();
		
		store = catalog.getCoverageStore( store.getId() );
		assertEquals( "coverageStore", store.getName() );
		assertEquals( "store description", store.getDescription() );
		assertTrue( store.isEnabled() );
		assertEquals( "type", store.getType() );
		
		assertEquals( "url", store.getURL());
	
		assertEquals( "one", store.getMetadata().get( "1" ) );
		assertEquals( new Double(2.0), store.getMetadata().get( "2" ) );
	}
	
	public void testStyle() {
		
		Iterator styles = catalog.getStyles().iterator();
		assertFalse( styles. hasNext() );
		
		StyleInfo style = catalog.getFactory().createStyle();
		style.setName( "style1" );
		catalog.add( style );
		
		style = catalog.getFactory().createStyle();
		style.setName( "style2" );
		catalog.add( style );
		
		endTransaction();
		
		startNewTransaction();
		
		styles = catalog.getStyles().iterator();
		assertTrue( styles.hasNext() );
		StyleInfo s1 = (StyleInfo) styles.next();
	
		assertTrue( styles.hasNext() );
		StyleInfo s2 = (StyleInfo) styles.next();
		
		if ( "style2".equals( s1.getName() ) ) {
			StyleInfo t = s1;
			s1 = s2;
			s2 = t;
		}
		
		assertEquals( "style1", s1.getName() );
		assertEquals( "style2", s2.getName() );
	}
	
	public void testNamespace() {
		
		Iterator namespaces = catalog.getNamespaces().iterator();
		assertFalse( namespaces.hasNext() );
		
		NamespaceInfo namespace = catalog.getFactory().createNamespace();
		namespace.setPrefix( "topp" );
		namespace.setURI("http://topp.openplans.org");
		catalog.add( namespace );
		
		namespace = catalog.getFactory().createNamespace();
		namespace.setPrefix( "gs" );
		namespace.setURI("http://geoserver.org");
		catalog.add( namespace );

		endTransaction();
		startNewTransaction();
		
		namespaces = catalog.getNamespaces().iterator();
		
		assertTrue( namespaces.hasNext() );
		NamespaceInfo ns1 = (NamespaceInfo) namespaces.next();
		
		assertTrue( namespaces.hasNext() );
		NamespaceInfo ns2 = (NamespaceInfo) namespaces.next();
		
		if ( "gs".equals( ns1.getPrefix() ) ) {
			NamespaceInfo t = ns1;
			ns1 = ns2;
			ns2 = t;
		}
		
		assertEquals( "topp", ns1.getPrefix() );
		assertEquals( "http://topp.openplans.org", ns1.getURI() );
		
		assertEquals( "gs", ns2.getPrefix() );
		assertEquals( "http://geoserver.org", ns2.getURI() );
		
        endTransaction();
        startNewTransaction();

        //store needs a workspace...
        WorkspaceInfo ws = catalog.getFactory().createWorkspace();
        ws.setName("testNamespaceWorkspace");
        catalog.add(ws);

        endTransaction();
        startNewTransaction();

        // feature type needs a store
        DataStoreInfo dataStore = catalog.getFactory().createDataStore();
		dataStore.setEnabled(true);
		dataStore.setName("ds1");
		dataStore.setWorkspace(ws);
		catalog.add(dataStore);

        endTransaction();
        startNewTransaction();

        FeatureTypeInfo ft1 = catalog.getFactory().createFeatureType();
		ft1.setName( getName() + "1" );
		ft1.setNamespace( ns1 );
		ft1.setStore(dataStore);
		catalog.add( ft1 );
		
		FeatureTypeInfo ft2 = catalog.getFactory().createFeatureType();
		ft2.setName( getName() + "2" );
		ft2.setNamespace( ns2 );
		ft2.setStore(dataStore);
		catalog.add( ft2 );
		
		List resources = 
			catalog.getResourcesByNamespace( ns1, FeatureTypeInfo.class );
		assertEquals( 1, resources.size() );
	}
	
	public void testFeatureType() throws Exception {
		NamespaceInfo namespace = catalog.getFactory().createNamespace();
		namespace.setPrefix( getName() );
		namespace.setURI( "http://" + getName() + ".openplans.org" );
		catalog.add( namespace );
		
        //store needs a workspace...
        WorkspaceInfo ws = catalog.getFactory().createWorkspace();
        ws.setName("testFeatureTypeWorkspace");
        catalog.add(ws);

        StyleInfo style = catalog.getFactory().createStyle();
        style.setName( "style1" );
		catalog.add( style );
		
		DataStoreInfo dataStore = catalog.getFactory().createDataStore();
		dataStore.setName("dataStore2");
		dataStore.setDescription( "store description");
		dataStore.setEnabled( true );
		dataStore.setWorkspace(ws);
		catalog.add( dataStore );
		
		FeatureTypeInfo featureType = catalog.getFactory().createFeatureType();
		featureType.setName( "featureType" );
		featureType.setTitle( "featureType title");
		featureType.setDescription( "featureType description");
		featureType.setAbstract( "featureType abstract");
		featureType.setSRS( "EPSG:4326" );
		featureType.setNamespace( namespace );
		
		
		FilterFactory factory = CommonFactoryFinder.getFilterFactory( null );
		Filter filter = factory.id( Collections.EMPTY_SET );
		featureType.setFilter( filter );
		
		CoordinateReferenceSystem crsNative = CRS.parseWKT( "PROJCS[\"NAD83 / BC Albers\",GEOGCS[\"NAD83\",DATUM[\"North_American_Datum_1983\",SPHEROID[\"GRS 1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]],TOWGS84[0,0,0],AUTHORITY[\"EPSG\",\"6269\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4269\"]],PROJECTION[\"Albers_Conic_Equal_Area\"],PARAMETER[\"standard_parallel_1\",50],PARAMETER[\"standard_parallel_2\",58.5],PARAMETER[\"latitude_of_center\",45],PARAMETER[\"longitude_of_center\",-126],PARAMETER[\"false_easting\",1000000],PARAMETER[\"false_northing\",0],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AUTHORITY[\"EPSG\",\"3005\"]]");
		BoundingBox boundingBox = new ReferencedEnvelope( 0,0,0,0, crsNative );
		BoundingBox latLonBoundingBox = new ReferencedEnvelope( -180,180,-90,90, DefaultGeographicCRS.WGS84 );
		
		featureType.setNativeBoundingBox( (ReferencedEnvelope)boundingBox );
		featureType.setLatLonBoundingBox( (ReferencedEnvelope)latLonBoundingBox );
		
		featureType.setStore( dataStore );
		
		catalog.add( featureType );
		endTransaction();
		
		startNewTransaction();
		
		FeatureTypeInfo featureType1 = catalog.getFeatureType( featureType.getId() );
		assertNotNull( featureType1 );
		
		//assertFalse( featureType == featureType1 );
		featureType = featureType1;
		
		assertEquals( "featureType" , featureType.getName() );
		assertEquals( "featureType title", featureType.getTitle() );
		assertEquals( "featureType description", featureType.getDescription() );
		assertEquals( "featureType abstract", featureType.getAbstract() );
		assertEquals( "EPSG:4326", featureType.getSRS()  );
		
		BoundingBox box = featureType.getBoundingBox();
		assertNotNull( box );
		assertEquals( 0d, box.getMinX(),0d);
		assertEquals( 0d, box.getMinY(),0d);
		assertEquals( 0d, box.getMaxX(),0d);
		assertEquals( 0d, box.getMaxY(),0d);
		assertTrue( CRS.equalsIgnoreMetadata( crsNative, box.getCoordinateReferenceSystem() ) );
		
		box = featureType.getLatLonBoundingBox();
		assertNotNull( box );
		assertEquals( -180d, box.getMinX(),0d);
		assertEquals( -90d, box.getMinY(),0d);
		assertEquals( 180d, box.getMaxX(),0d);
		assertEquals( 90d, box.getMaxY(),0d);
		assertTrue( CRS.equalsIgnoreMetadata( DefaultGeographicCRS.WGS84, box.getCoordinateReferenceSystem() ) );
		
		assertNotNull( featureType.getNamespace() );
		assertEquals( getName(), featureType.getNamespace().getPrefix() );
		
		assertNotNull( featureType.getStore() );
		assertEquals( "dataStore2", featureType.getStore().getName() );
		endTransaction();
		
		startNewTransaction();
		featureType1 =  catalog.getFeatureTypeByName( namespace.getPrefix(), featureType.getName() );
		assertNotNull( featureType1 );
		
		featureType1 =  catalog.getFeatureTypeByName( namespace.getURI(), featureType.getName() );
		assertNotNull( featureType1 );
	}
	
	public void testCoverage() {
		NamespaceInfo namespace = catalog.getFactory().createNamespace();
		namespace.setPrefix( getName() );
		namespace.setURI( "http://" + getName() + ".openplans.org" );
		catalog.add( namespace );
		
		StyleInfo style = catalog.getFactory().createStyle();
		style.setName( "style1" );
		catalog.add( style );
		
		//store needs a workspace...
        WorkspaceInfo ws = catalog.getFactory().createWorkspace();
        ws.setName("testCoverageWorkspace");
        catalog.add(ws);

        endTransaction();
        startNewTransaction();
        
		CoverageStoreInfo coverageStore = catalog.getFactory().createCoverageStore();
		coverageStore.setName("coverageStore2");
		coverageStore.setWorkspace(ws);
		
		catalog.add( coverageStore );
		
		CoverageInfo coverage = catalog.getFactory().createCoverage();
		coverage.setName( "featureType" );
		coverage.setTitle( "featureType title");
		coverage.setDescription( "featureType description");
		coverage.setAbstract( "featureType abstract");
		coverage.setSRS( "EPSG:4326" );
		coverage.setNamespace( namespace );
		coverage.setStore( coverageStore );
		coverage.setNativeBoundingBox( new ReferencedEnvelope( 0,0,0,0,DefaultGeographicCRS.WGS84 ));
		coverage.setLatLonBoundingBox( new ReferencedEnvelope( 0,0,0,0,DefaultGeographicCRS.WGS84 ));
		
		coverage.setNativeFormat( "nativeFormat" );
		coverage.getSupportedFormats().add( "supportedFormat1" );
		coverage.getSupportedFormats().add( "supportedFormat2" );
		
		coverage.setDefaultInterpolationMethod( "defaultInterpolationMethod" );
		coverage.getInterpolationMethods().add( "interpolationMethod1" );
		coverage.getInterpolationMethods().add( "interpolationMethod2" );
		
		coverage.getRequestSRS().add( "EPSG:3003" );
		coverage.getRequestSRS().add( "EPSG:4326" );
		coverage.getResponseSRS().add( "EPSG:42102" );
	
		catalog.add( coverage );
		
		endTransaction();
		
		startNewTransaction();
		CoverageInfo coverage1 = catalog.getCoverage( coverage.getId() );
		assertNotNull( coverage1 );
		
		//assertFalse( coverage == coverage1 );
		coverage = coverage1;
		
		assertEquals( "nativeFormat", coverage.getNativeFormat() );
		assertEquals( 2, coverage.getSupportedFormats().size() );
		assertTrue( coverage.getSupportedFormats().contains( "supportedFormat1" ) );
		assertTrue( coverage.getSupportedFormats().contains( "supportedFormat2" ) );
		
		assertEquals( "defaultInterpolationMethod", coverage.getDefaultInterpolationMethod() );
		assertEquals( 2, coverage.getInterpolationMethods().size() );
		assertTrue( coverage.getInterpolationMethods().contains( "interpolationMethod1" ));
		assertTrue( coverage.getInterpolationMethods().contains( "interpolationMethod2" ));
		
		assertEquals( 2, coverage.getRequestSRS().size() );
		assertEquals( 1, coverage.getResponseSRS().size() );
		assertTrue( coverage.getRequestSRS().contains( "EPSG:3003" ) );
		assertTrue( coverage.getRequestSRS().contains( "EPSG:4326" ) );
		assertTrue( coverage.getResponseSRS().contains( "EPSG:42102" ) );
		endTransaction();
		
		startNewTransaction();
		coverage1 =  catalog.getCoverageByName( namespace.getPrefix(), coverage.getName() );
		assertNotNull( coverage1 );
		
		coverage1 =  catalog.getCoverageByName( namespace.getURI(), coverage.getName() );
		assertNotNull( coverage1 );
	}
	
	public void testLayer() {
        //store needs a workspace...
        WorkspaceInfo ws = catalog.getFactory().createWorkspace();
        ws.setName("testLayerWorkspace");
        catalog.add(ws);
        
        CoverageStoreInfo coverageStore = catalog.getFactory().createCoverageStore();
        coverageStore.setName("testLayerCoverageStore");
        coverageStore.setWorkspace(ws);
        
        catalog.add( coverageStore );
        
        CoverageInfo coverage = catalog.getFactory().createCoverage();
        coverage.setName( "featureType" );
        coverage.setTitle( "featureType title");
        coverage.setStore( coverageStore );
        coverage.setNativeBoundingBox( new ReferencedEnvelope( 0,0,0,0,DefaultGeographicCRS.WGS84 ));
        coverage.setLatLonBoundingBox( new ReferencedEnvelope( 0,0,0,0,DefaultGeographicCRS.WGS84 ));
        catalog.add( coverage );
	    
		LayerInfo layer1 = catalog.getFactory().createLayer();
		layer1.setName("layer1");
		layer1.setResource(coverage);
		catalog.add( layer1 );
		
		LayerInfo layer2 = catalog.getFactory().createLayer();
		layer2.setName("layer2");
		layer2.setResource(coverage);
		catalog.add( layer2 );
		
		MapInfo map1 = catalog.getFactory().createMap();
                map1.getLayers().add( layer1 );
                map1.getLayers().add( layer2 );
		catalog.add( map1 );
		
		endTransaction();
		startNewTransaction();
		
		MapInfo map2 = catalog.getMap( map1.getId() );
		assertNotNull( map2 );
		
		
		//assertTrue( map1 != map2 );
		
		assertEquals( 2, map2.getLayers().size() );
	}
}
