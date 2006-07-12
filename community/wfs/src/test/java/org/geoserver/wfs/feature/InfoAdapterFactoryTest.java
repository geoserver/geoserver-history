package org.geoserver.wfs.feature;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.geoserver.wfs.WFSTestSupport;
import org.geotools.catalog.GeoResource;
import org.geotools.catalog.Service;

public class InfoAdapterFactoryTest extends WFSTestSupport {

	public void testAdaptToDataStoreInfo() throws Exception {
		List services = catalog.services( DataStoreInfo.class );
		assertEquals( 1, services.size() );
		
		Service service = (Service) services.get( 0 );
		assertNotNull( service );
		
		DataStoreInfo dsInfo = 
			(DataStoreInfo) service.resolve( DataStoreInfo.class, null ); 
		assertNotNull( dsInfo );
		
	}
	
	public void testAdaptToFeautreStoreInfo() throws Exception {
		List resources = catalog.resources( FeatureTypeInfo.class );
		assertEquals( citeTypeNames().length, resources.size() );
		
		HashSet names = new HashSet(Arrays.asList( citeTypeNames() ) );
		for ( Iterator r = resources.iterator(); r.hasNext(); ) {
			GeoResource resource = (GeoResource) r.next();
			FeatureTypeInfo ftInfo = 
				(FeatureTypeInfo) resource.resolve( FeatureTypeInfo.class, null );
			assertNotNull( ftInfo );
			
			names.remove( ftInfo.getTypeName() );
		}
		
		assertTrue( names.isEmpty() );
	}
}
