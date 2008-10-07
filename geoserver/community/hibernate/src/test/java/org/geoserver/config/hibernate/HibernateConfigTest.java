package org.geoserver.config.hibernate;

import java.util.Collection;
import java.util.Iterator;

import org.geoserver.catalog.hibernate.HibernateCatalog;
import org.geoserver.catalog.hibernate.HibernateTestSupport;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.ServiceInfo;

public class HibernateConfigTest extends HibernateTestSupport {
	
	HibernateCatalog catalog;
	HibernateGeoServer geoServer;
	
	protected void onSetUpBeforeTransaction() throws Exception {
		super.onSetUpBeforeTransaction();
		
		catalog = (HibernateCatalog) applicationContext.getBean("catalog");
		geoServer = (HibernateGeoServer) applicationContext.getBean("configTarget"); 
	}
	
	public void testGeoServer() {
		
		GeoServerInfo info = geoServer.getFactory().createGlobal();
		info.setTitle( "title" );
		geoServer.setGlobal( info );
		
		endTransaction();
		
		startNewTransaction();
		
		GeoServerInfo info2 = geoServer.getGlobal();
		assertFalse( info == info2 );
		
		info = info2;
		assertEquals( "title", info.getTitle() );
		
	}
	
	public void testServices() {
		GeoServerInfo config = geoServer.getFactory().createGlobal();
		geoServer.setGlobal(config);
		
		ServiceInfo info = geoServer.getFactory().createService();
		info.setName( "foo" );
		info.setMetadataLink( catalog.getFactory().createMetadataLink() );
		info.getMetadataLink().setAbout( "about" );
		geoServer.add( info );
		
		info = geoServer.getFactory().createService();
		info.setName( "bar" );
		info.setMetadataLink( catalog.getFactory().createMetadataLink() );
		info.getMetadataLink().setAbout( "about" );
		geoServer.add( info );
		
		endTransaction();
		
		startNewTransaction();
		Collection services = geoServer.getServices();
		assertEquals( 2, services.size() );
		
		Iterator i = services.iterator();
		info = (ServiceInfo) i.next();
		assertEquals( "foo", info.getName() );
		assertNotNull( info.getGeoServer() );
		
		info = (ServiceInfo) i.next();
		assertEquals( "bar", info.getName() );
		assertNotNull( info.getGeoServer() );
	}
	
}
