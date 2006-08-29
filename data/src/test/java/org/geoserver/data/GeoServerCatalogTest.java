package org.geoserver.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.geotools.catalog.GeoResource;
import org.geotools.catalog.Service;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;

public class GeoServerCatalogTest extends DataTestSupport {

	public void testServices() throws Exception {
		List services = catalog.services( DataStore.class );
		assertEquals( 3, services.size() );
	}
	
	public void testResolveServices() throws Exception {
		List services = catalog.services( DataStore.class );
		List typeNames = new ArrayList();
		
		for ( Iterator s = services.iterator(); s.hasNext(); ) {
			Service service = (Service) s.next();
			
			DataStore dataStore = 
				(DataStore) service.resolve( DataStore.class, null );
			assertNotNull( dataStore );
			
			typeNames.addAll( Arrays.asList( dataStore.getTypeNames() ) );
		}
		
		String[] citeTypeNames = citeTypeNames();
		for ( int i = 0; i < citeTypeNames.length; i++ ) {
			assertTrue( citeTypeNames[i] + " not found", typeNames.contains( citeTypeNames[i] ) );
		}
	}
	
	public void testResources() throws Exception {
		List resources = catalog.resources( FeatureSource.class );
		assertEquals( citeTypeNames().length, resources.size() );
	}
	
	public void testResolveResources() throws Exception {
		List resources = catalog.resources( FeatureSource.class );
		HashSet citeTypeNames = new HashSet( Arrays.asList( citeTypeNames() ) );
		
		for ( Iterator r = resources.iterator(); r.hasNext(); ) {
			GeoResource resource = (GeoResource) r.next();
			FeatureSource fs = 
				(FeatureSource) resource.resolve( FeatureSource.class, null );
			
			String typeName = fs.getSchema().getTypeName();
			assertTrue( citeTypeNames.contains( typeName ) );
			citeTypeNames.remove( typeName );
		}
	}
	
	public void testFeatureTypes() throws Exception {
		List featureTypes = catalog.featureTypes();
		assertEquals( citeTypeNames().length, featureTypes.size() );
	}
	
	public void testStyles() throws Exception {
		List styles = catalog.styles();
		assertEquals( wmsCiteTypeNames().length, styles.size() );
	}
}
