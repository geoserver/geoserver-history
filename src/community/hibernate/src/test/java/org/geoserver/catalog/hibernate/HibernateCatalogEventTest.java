package org.geoserver.catalog.hibernate;

import java.util.logging.Logger;

import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.event.CatalogAddEvent;
import org.geoserver.catalog.event.CatalogListener;
import org.geoserver.catalog.event.CatalogModifyEvent;
import org.geoserver.catalog.event.CatalogPostModifyEvent;
import org.geoserver.catalog.event.CatalogRemoveEvent;
import org.geoserver.catalog.impl.WorkspaceInfoImpl;
import org.geoserver.config.hibernate.HibGeoServerImpl;
import org.geoserver.hibernate.HibTestSupport;
import org.geotools.util.logging.Logging;

public class HibernateCatalogEventTest extends HibTestSupport {

    HibCatalogImpl catalog;

    HibGeoServerImpl geoServer;

    // public void setDataSource(org.apache.commons.dbcp.BasicDataSource ds) {
    // System.out.println("SETTING DS " + ds);
    // super.setDataSource(ds);
    // }
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        catalog = (HibCatalogImpl) applicationContext.getBean("catalogTarget");
        catalog.setFireEventsOnCommit(true);

        geoServer = (HibGeoServerImpl) applicationContext.getBean("configTarget");
        // HibBootstrapper bootstrapper = new HibBootstrapper(catalog, geoServer);
        // bootstrapper.createBaseObjects();
    }

    public void testDummy() {
        logger.warn("TODO: event handling has changed in GS2 -- please replace hibernate tests");
    }

    public void _testAddStore() {
        logger.error("------------------> running testAddStore");
        logger.warn("fire on commit? " + catalog.isFireEventsOnCommit());

        Listener listener = new Listener();
        catalog.addListener(listener);

        DataStoreInfo store = catalog.getFactory().createDataStore();
        store.setName("foo" + System.currentTimeMillis()); // we need a unique name
        store.setDescription("testAddStore() "); // just for data debugging

        WorkspaceInfoImpl ws = catalog.getFactory().createWorkspace();
        ws.setName("testAddStore" + System.currentTimeMillis()); // we need a unique name
        catalog.add(ws);

        store.setWorkspace(ws);
        catalog.add(store);

        assertFalse("Listener added, shouldn't", listener.added);

        endTransaction();

        assertTrue("Listener not added", listener.added);
        assertFalse("Listener modified, it should not.", listener.modified);
    }

    public void _testModifyStore() {
        logger.error("------------------> running testModifyStore");

        DataStoreInfo store = catalog.getFactory().createDataStore();
        store.setName("bar");

        WorkspaceInfoImpl ws = catalog.getFactory().createWorkspace();
        ws.setName("testModifyStore" + System.currentTimeMillis()); // we need a unique name
        catalog.add(ws);

        store.setWorkspace(ws);
        catalog.add(store);

        endTransaction();
        startNewTransaction();

        Listener listener = new Listener();
        catalog.addListener(listener);
        store = (DataStoreInfo) catalog.getDataStore(store.getId());
        store.setName("bar2");
        catalog.save(store);

        assertFalse("Listener modified, it should not.", listener.modified);
        endTransaction();
        assertTrue("Listener not modified.", listener.modified);
    }

    // public void testModifyStore() {
    // session.beginTransaction();
    //		
    // Listener listener = new Listener() {
    // public void storeModified(StoreInfo store, List properties, List oldValues, List newValues) {
    // assertEquals( 1, properties.size() );
    // assertEquals( "name", properties.get( 0 ) );
    // assertNull( oldValues.get( 0 ) );
    // assertEquals( "theName", newValues.get( 0 ) );
    // }
    // };
    // catalog.addListener( listener );
    //		
    // DataStoreInfo store = catalog.getFactory().createDataStore();
    // catalog.add( store );
    //		
    // store.setName("theName");
    // catalog.save( store );
    //		
    // session.getTransaction().commit();
    //		
    // }
    //	
    // public void testRemoveStore() {
    // session.beginTransaction();
    //		
    // Listener listener = new Listener();
    // catalog.addListener( listener );
    //		
    // DataStoreInfo store = catalog.getFactory().createDataStore();
    // catalog.add( store );
    // session.getTransaction().commit();
    //		
    // session.beginTransaction();
    //		
    // catalog.remove( store );
    // assertTrue( listener.storeRemoved );
    //		
    // session.getTransaction().commit();
    //	
    // }
    static class Listener implements CatalogListener {
        private static Logger LOGGER = Logging.getLogger(Listener.class);

        boolean added;

        boolean removed;

        boolean modified;

        public void handleAddEvent(CatalogAddEvent event) {
            // LOGGER.warning("EVENT: handleAddEvent " +
            // event.getSource().getClass().getSimpleName());
            added = true;
        }

        public void handleRemoveEvent(CatalogRemoveEvent event) {
            // LOGGER.warning("EVENT: handleRemoveEvent " +
            // event.getSource().getClass().getSimpleName());
            removed = true;
        }

        public void handleModifyEvent(CatalogModifyEvent event) {
            // LOGGER.warning("EVENT: handleModifyEvent " +
            // event.getSource().getClass().getSimpleName());
            modified = true;
        }

        public void reloaded() {
            Logger.getLogger(Listener.class.getName()).warning("reloaded() not implemented");
        }

        public void handlePostModifyEvent(CatalogPostModifyEvent event) {
            Logger.getLogger(Listener.class.getName()).warning(
                    "PostModify " + event.getSource().getClass().getSimpleName());
        }

    }
}
