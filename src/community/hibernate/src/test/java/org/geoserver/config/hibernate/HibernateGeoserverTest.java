package org.geoserver.config.hibernate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import java.util.logging.Logger;
import org.geoserver.catalog.hibernate.HibCatalogImpl;
import org.geoserver.config.LoggingInfo;
import org.geoserver.hibernate.HibTestSupport;
import org.geoserver.config.ConfigurationListener;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.hibernate.beans.GeoServerInfoImplHb;
import org.geoserver.config.impl.ServiceInfoImpl;
import org.geoserver.hibernate.HibBootstrapper;
import org.geoserver.services.hibernate.beans.GMLInfoImplHb;
import org.geoserver.wfs.GMLInfo;
import org.geoserver.wfs.GMLInfo.SrsNameStyle;
import org.geoserver.wfs.WFSInfo;
import org.geotools.util.logging.Logging;

public class HibernateGeoserverTest extends HibTestSupport {

    HibCatalogImpl catalog;

    HibGeoServerImpl geoServer;

    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();

        catalog = (HibCatalogImpl) applicationContext.getBean("catalogTarget");
        geoServer = (HibGeoServerImpl) applicationContext.getBean("configTarget");

    }

    private void removeServices() {
        for (ServiceInfo serviceInfo : geoServer.getServices()) {
            geoServer.remove(serviceInfo);
        }
    }

    public void testGeoServer() {

        GeoServerInfo info = geoServer.getFactory().createGlobal();
        info.setTitle("title");
        geoServer.setGlobal(info);
        assertEquals("Lost title attribute when persisting", "title", info.getTitle());

        GeoServerInfo reloaded = geoServer.getGlobal();
        assertEquals("Lost title attribute when reloading", "title", reloaded.getTitle());

        endTransaction();

        startNewTransaction();

        GeoServerInfo info2 = geoServer.getGlobal();
        assertFalse(info == info2);

        // info = info2;
        assertEquals("Lost title attribute inter transactions", "title", info2.getTitle());

    }

    public void testServices() {
        GeoServerInfo config = geoServer.getFactory().createGlobal();
        geoServer.setGlobal(config);

        removeServices();

        ServiceInfo info = geoServer.getFactory().createService();
        info.setName("foo");
        ((ServiceInfoImpl) info).setId("foo");
        info.setMetadataLink(catalog.getFactory().createMetadataLink());
        info.getMetadataLink().setAbout("about");
        geoServer.add(info);

        info = geoServer.getFactory().createService();
        ((ServiceInfoImpl) info).setId("bar");
        info.setName("bar");
        info.setMetadataLink(catalog.getFactory().createMetadataLink());
        info.getMetadataLink().setAbout("about");
        geoServer.add(info);

        endTransaction();

        startNewTransaction();
        Collection services = geoServer.getServices();
        assertEquals(2, services.size());

        Iterator i = services.iterator();
        info = (ServiceInfo) i.next();
        assertEquals("foo", info.getName());
        assertNotNull(info.getGeoServer());

        info = (ServiceInfo) i.next();
        assertEquals("bar", info.getName());
        assertNotNull(info.getGeoServer());
    }

    public void testGlobal() throws Exception {
        GeoServerInfo global1 = geoServer.getFactory().createGlobal();
        global1.setTitle("test global "
                + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.FULL).format(
                        new Date()));

        geoServer.setGlobal(global1);

        endTransaction();
        startNewTransaction();

        assertEquals(global1, geoServer.getGlobal());

        endTransaction();
        startNewTransaction();

        GeoServerInfo global2 = geoServer.getFactory().createGlobal();
        global2.setTitle("subst'ed global "
                + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.FULL).format(
                        new Date()));

        global2.setAdminPassword("secret");
        ContactInfo contactInfo = geoServer.getFactory().createContact();
        contactInfo.setAddress("the address");
        contactInfo.setAddressCity("the city");
        global2.setContact(contactInfo);
        geoServer.setGlobal(global2);

        endTransaction();
        startNewTransaction();

        GeoServerInfo globalZ = geoServer.getGlobal();

        String globalDebugDiff = ((GeoServerInfoImplHb) global2).getFirstDiff(globalZ);
        if (globalDebugDiff != null)
            Logging.getLogger(this.getClass()).severe("Globals do not match: " + globalDebugDiff);

        assertEquals(global2, globalZ);

        ContactInfo contactInfoZ = globalZ.getContact();
        assertEquals("Contacts differ", global2.getContact(), contactInfoZ);
        // assertEquals(global2, globalZ);
    }

    public void testModifyGlobal() throws Exception {
        GeoServerInfo global = geoServer.getFactory().createGlobal();
        geoServer.setGlobal(global);

        GeoServerInfo g1 = geoServer.getGlobal();
        assertNull(g1.getAdminPassword());

        g1.setAdminPassword("newAdminPassword");
        geoServer.save(g1);

        GeoServerInfo g2 = geoServer.getGlobal();
        assertEquals("newAdminPassword", g2.getAdminPassword());
    }

    public void testAddService() throws Exception {
        ServiceInfo service = geoServer.getFactory().createService();
        service.setName("testAddService");

        try {
            geoServer.add(service);
            fail("adding without id should throw exception");
        } catch (Exception e) {
            assertTrue(true);
        }

        ((ServiceInfoImpl) service).setId("id");
        geoServer.add(service);

        ServiceInfo s2 = geoServer.getFactory().createService();
        ((ServiceInfoImpl) s2).setId("id");
        s2.setName("testAddService2");

        try {
            geoServer.add(s2);
            fail("adding service with duplicate id should throw exception");
        } catch (Exception e) {
            assertTrue(true);
        }

        ServiceInfo s = geoServer.getServiceByName("testAddService", ServiceInfo.class);
        assertNotNull(s);
        assertEquals(service, s);
    }

    public void testModifyService() throws Exception {
        ServiceInfo service = geoServer.getFactory().createService();
        ((ServiceInfoImpl) service).setId("testModifyServiceID");
        service.setName("testModifyService");
        service.setTitle("bar");

        geoServer.add(service);

        ServiceInfo s1 = geoServer.getServiceByName("testModifyService", ServiceInfo.class);
        assertEquals("bar", s1.getTitle());

        s1.setTitle("changed");
        geoServer.save(s1);

        ServiceInfo s2 = geoServer.getServiceByName("testModifyService", ServiceInfo.class);

        assertEquals("changed", s2.getTitle());
    }


    public void testWFS() throws Exception {
        removeServices();

        HibBootstrapper bootstrapper = new HibBootstrapper(catalog, geoServer.getProxy());
        bootstrapper.createDefaultNamespace();
        bootstrapper.createDefaultWorkspace();
        bootstrapper.createBaseObjects();

        
        // WS should already be loaded
        WFSInfo wfs = (WFSInfo)geoServer.getService("wfs");
        assertNotNull(wfs);

        Map<WFSInfo.Version,GMLInfo> gml = wfs.getGML();
//
//
//        gml.setSrsNameStyle(SrsNameStyle.NORMAL);
//        wfs.getGML().put(WFSInfo.Version.V_10, gml);
//
//        // gml3
//        gml = new GMLInfoImplHb();
//        gml.setSrsNameStyle(SrsNameStyle.URN);
//        wfs.getGML().put(WFSInfo.Version.V_11, gml);
//        wfs.setGeoServer(serviceCatalog);

    }

    /**
     * todo: we're not firing events
     * 
     * @throws Exception
     */
    public void _testGlobalEvents() throws Exception {
        TestListener tl = new TestListener();
        geoServer.addListener(tl);

        GeoServerInfo global = geoServer.getFactory().createGlobal();
        geoServer.setGlobal(global);

        global = geoServer.getGlobal();
        global.setAdminPassword("foo");
        // global.setMaxFeatures(100);
        global.setOnlineResource("bar");

        assertEquals(0, tl.gPropertyNames.size());
        geoServer.save(global);

        assertEquals(3, tl.gPropertyNames.size());
        assertTrue(tl.gPropertyNames.contains("adminPassword"));
        assertTrue(tl.gPropertyNames.contains("maxFeatures"));
        assertTrue(tl.gPropertyNames.contains("onlineResource"));
    }

    static class TestListener implements ConfigurationListener {

        List<String> gPropertyNames = new ArrayList<String>();

        List<Object> gOldValues = new ArrayList<Object>();

        List<Object> gNewValues = new ArrayList<Object>();

        List<String> sPropertyNames = new ArrayList<String>();

        List<Object> sOldValues = new ArrayList<Object>();

        List<Object> sNewValues = new ArrayList<Object>();

        public void handleGlobalChange(GeoServerInfo global, List<String> propertyNames,
                List<Object> oldValues, List<Object> newValues) {
            gPropertyNames.addAll(propertyNames);
            gOldValues.addAll(oldValues);
            gNewValues.addAll(newValues);
        }

        public void handleServiceChange(ServiceInfo service, List<String> propertyNames,
                List<Object> oldValues, List<Object> newValues) {

            sPropertyNames.addAll(propertyNames);
            sOldValues.addAll(oldValues);
            sNewValues.addAll(newValues);
        }

        public void reloaded() {
            Logger.getLogger(this.getClass().getName()).warning("reloaded() not implemented");
        }

        public void handlePostGlobalChange(GeoServerInfo global) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void handleLoggingChange(LoggingInfo logging, List<String> propertyNames,
                List<Object> oldValues, List<Object> newValues) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void handlePostLoggingChange(LoggingInfo logging) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void handlePostServiceChange(ServiceInfo service) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}
