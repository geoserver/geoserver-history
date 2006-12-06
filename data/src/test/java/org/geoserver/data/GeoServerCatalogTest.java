package org.geoserver.data;

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
		assertEquals( 1, services.size() );
		
	}
	
	public void testResolveServices() throws Exception {
		List services = catalog.services( DataStore.class );
		Service service = (Service) services.get( 0 );
		
		DataStore dataStore = 
			(DataStore) service.resolve( DataStore.class, null );
		assertNotNull( dataStore );
		
		List typeNames = Arrays.asList( dataStore.getTypeNames() );
		String[] citeTypeNames = citeTypeNames();
		for ( int i = 0; i < citeTypeNames.length; i++ ) {
			assertTrue( typeNames.contains( citeTypeNames[i] ) );
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
}
