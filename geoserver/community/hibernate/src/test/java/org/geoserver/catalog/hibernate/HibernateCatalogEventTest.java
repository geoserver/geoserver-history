package org.geoserver.catalog.hibernate;

import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.event.CatalogAddEvent;
import org.geoserver.catalog.event.CatalogListener;
import org.geoserver.catalog.event.CatalogModifyEvent;
import org.geoserver.catalog.event.CatalogRemoveEvent;

public class HibernateCatalogEventTest extends HibernateTestSupport {

	HibernateCatalog catalog;
	
	protected void onSetUpBeforeTransaction() throws Exception {
		super.onSetUpBeforeTransaction();
		catalog = (HibernateCatalog) applicationContext.getBean("catalogTarget");
		catalog.setFireEventsOnCommit( true );
	}
	
	public void testAddStore() {
		
		Listener listener = new Listener();
		catalog.addListener( listener );
		
		DataStoreInfo store = catalog.getFactory().createDataStore();
		store.setName( "foo" );
		catalog.add( store );
		
		assertFalse( listener.added );
		
		endTransaction();
		
		assertTrue( listener.added );
		assertFalse( listener.modified );
	}
	
	public void testModifyStore() {
		DataStoreInfo store = catalog.getFactory().createDataStore();
		store.setName( "bar" );
		catalog.add( store );
	
		endTransaction();
		startNewTransaction();
		
		Listener listener = new Listener();
		catalog.addListener( listener );
		store = (DataStoreInfo) catalog.getDataStore( store.getId() );
		store.setName( "bar2" );
		catalog.save( store );
		
		assertFalse( listener.modified );
		endTransaction();
		assertTrue( listener.modified );
	}
	
	
//	public void testModifyStore() {
//		session.beginTransaction();
//		
//		Listener listener = new Listener() {
//			public void storeModified(StoreInfo store, List properties, List oldValues, List newValues) {
//				assertEquals( 1, properties.size() );
//				assertEquals( "name", properties.get( 0 ) );
//				assertNull( oldValues.get( 0 ) );
//				assertEquals( "theName", newValues.get( 0 ) );
//			}
//		};
//		catalog.addListener( listener );
//		
//		DataStoreInfo store = catalog.getFactory().createDataStore();
//		catalog.add( store );
//		
//		store.setName("theName");
//		catalog.save( store );
//		
//		session.getTransaction().commit();
//		
//	}
//	
//	public void testRemoveStore() {
//		session.beginTransaction();
//		
//		Listener listener = new Listener();
//		catalog.addListener( listener );
//		
//		DataStoreInfo store = catalog.getFactory().createDataStore();
//		catalog.add( store );
//		session.getTransaction().commit();
//		
//		session.beginTransaction();
//		
//		catalog.remove( store );
//		assertTrue( listener.storeRemoved );
//		
//		session.getTransaction().commit();
//	
//	}
	
	static class Listener implements CatalogListener {

		boolean added;
		boolean removed;
		boolean modified;
		
		public void handleAddEvent(CatalogAddEvent event) {
			added = true;
		}
		
		public void handleRemoveEvent(CatalogRemoveEvent event) {
			removed = true;
		}
		
		public void handleModifyEvent(CatalogModifyEvent event) {
			modified = true;
		}
	}
}
