package org.geoserver.catalog.hibernate;

import org.geoserver.hibernate.HibTestSupport;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import java.util.logging.Logger;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.MapInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.impl.ModificationProxy;
import org.geoserver.hibernate.dao.CatalogDAO;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.style.Style;

public class HibernateCatalogTest extends HibTestSupport {
    private static final Logger LOGGER = Logging.getLogger(HibernateCatalogTest.class);

    HibCatalogImpl catalog;

    CatalogDAO catalogDAO;

    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();

        catalog = (HibCatalogImpl) applicationContext.getBean("catalogTarget");
    }

    private void clearCatalog() {
        // ensure no stores

        for (MapInfo mapInfo : ModificationProxy.unwrap(catalog.getMaps())) {
            catalog.remove(mapInfo);
        }
        
        for (LayerGroupInfo layergroup : ModificationProxy.unwrap(catalog.getLayerGroups())) {
            catalog.remove(layergroup);
        }

        for (LayerInfo layerInfo : ModificationProxy.unwrap(catalog.getLayers())) {
            catalog.remove(layerInfo);
        }

        for (FeatureTypeInfo featureTypeInfo : ModificationProxy.unwrap(catalog.getFeatureTypes())) {
            catalog.remove(featureTypeInfo);
        }

        for (CoverageInfo coverageInfo : ModificationProxy.unwrap(catalog.getCoverages())) {
            catalog.remove(coverageInfo);
        }

        for (DataStoreInfo dataStoreInfo : ModificationProxy.unwrap(catalog.getDataStores())) {
            catalog.remove(dataStoreInfo);
        }

        for (CoverageStoreInfo covStoreInfo : ModificationProxy.unwrap(catalog.getCoverageStores())) {
            catalog.remove(covStoreInfo);
        }

        assertTrue("LayerGroups in the DB, can't proceed.", catalog.getLayerGroups().isEmpty());
        assertTrue("Layers in the DB, can't proceed.", catalog.getLayers().isEmpty());
        assertTrue("Datastores in the DB, can't proceed.", catalog.getDataStores().isEmpty());
        assertTrue("Coveragestores in the DB, can't proceed.", catalog.getCoverageStores()
                .isEmpty());
        assertTrue("Coverages in the DB, can't proceed.", catalog.getCoverages().isEmpty());
        assertTrue("FeatureTypes in the DB, can't proceed.", catalog.getFeatureTypes().isEmpty());
        assertTrue("Maps in the DB, can't proceed.", catalog.getMaps().isEmpty());

    }

    private void removeExistingStyles() {
        // ensure no styles
        for (StyleInfo styleInfo : catalog.getStyles()) {
            catalog.remove(styleInfo);
        }

        List<StyleInfo> list = catalog.getStyles();
        assertTrue("Styles in the DB, can't proceed.", list.isEmpty());
    }

    private void removeExistingNS() {
        // for (NamespaceInfo namespaceInfo : ModificationProxy.unwrap(catalog.getNamespaces())) {
        for (NamespaceInfo namespaceInfo : catalogDAO.getNamespaces()) {
            // namespaceInfo = ModificationProxy.unwrap(namespaceInfo);
            catalog.remove(namespaceInfo);
        }

        List<NamespaceInfo> list = catalogDAO.getNamespaces();
        StringBuilder sb = new StringBuilder();
        for (NamespaceInfo nsi : list) {
            sb.append(nsi.getName()).append("(").append(nsi.getId()).append(") ");
        }
        if (!list.isEmpty())
            LOGGER.severe("!!! Namespaces in the DB (" + sb + ")");

        assertTrue("Namespaces in the DB (" + sb + "), can't proceed.", list.isEmpty());

        for (WorkspaceInfo ws : ModificationProxy.unwrap(catalog.getWorkspaces())) {
            catalog.remove(ws);
        }

        assertTrue("Workspaces in the DB, can't proceed.", catalog.getWorkspaces().isEmpty());

    }

    public void testDataStore() {
        System.out.println("========== testDataStore()");

        clearCatalog();
        removeExistingNS();

        // store needs a workspace...
        WorkspaceInfo ws = catalog.getFactory().createWorkspace();
        ws.setName("testDataStoreWorkspace");

        assertNull(ws.getId());
        catalog.add(ws);
        assertNotNull(ws.getId());

        endTransaction();
        startNewTransaction();

        // create a new store
        DataStoreInfo store = catalog.getFactory().createDataStore();
        store.setWorkspace(ws);
        store.setName("dataStore");
        store.setDescription("store description");
        store.setEnabled(true);

        store.getConnectionParameters().put("param1", "value1");
        store.getConnectionParameters().put("param2", new Integer(2));

        store.getMetadata().put("1", "one");
        store.getMetadata().put("2", "two");

        assertEquals("one", store.getMetadata().get("1"));

        catalog.add(store);

        assertEquals("one", store.getMetadata().get("1"));

        endTransaction();

        assertEquals("one", store.getMetadata().get("1"));

        startNewTransaction();
        Iterator<DataStoreInfo> stores = catalog.getDataStores().iterator();
        assertTrue("Datastore not properly stored.", stores.hasNext());

        store = catalog.getDataStore(store.getId());
        assertEquals("dataStore", store.getName());
        assertEquals("store description", store.getDescription());
        assertTrue(store.isEnabled());

        assertEquals("value1", store.getConnectionParameters().get("param1"));
        assertEquals(new Integer(2), store.getConnectionParameters().get("param2"));

        assertEquals("one", store.getMetadata().get("1"));
        assertEquals("two", store.getMetadata().get("2"));
    }

    public void testCoverageStore() {
        System.out.println("========== testCoverageStore()");

        // ensure no stores
        clearCatalog();
        removeExistingNS();

        endTransaction();
        // ----------------------------------------------------------------------
        startNewTransaction();

        // store needs a workspace...
        WorkspaceInfo ws = catalog.getFactory().createWorkspace();
        ws.setName("testCoverageStoreWorkspace");
        catalog.add(ws);

        endTransaction();
        // ----------------------------------------------------------------------
        startNewTransaction();

        // create a new store
        CoverageStoreInfo store = catalog.getFactory().createCoverageStore();
        store.setWorkspace(ws);
        store.setName("coverageStore");
        store.setDescription("store description");
        store.setEnabled(true);
        store.setType("type");
        store.setURL("url");

        store.getMetadata().put("1", "one");
        store.getMetadata().put("2", new Double(2.0));

        catalog.add(store);

        endTransaction();
        // Logging.getLogger(this.getClass()).warning("##################### id:" + store.getId() +
        // " name:"+store.getName() );

        // ----------------------------------------------------------------------
        startNewTransaction();

        List<CoverageStoreInfo> stores = catalog.getCoverageStores();
        // for (CoverageStoreInfo storex : stores) {
        // Logging.getLogger(this.getClass()).warning(":::::::::: id:" + storex.getId() +
        // " name:"+storex.getName() );
        // }
        assertEquals(1, stores.size());
        // Logging.getLogger(this.getClass()).warning("::::::::::::::::::::: id:" +
        // stores.get(0).getId() + " name:"+stores.get(0).getName() );

        CoverageStoreInfo loadedStore = catalog.getCoverageStore(store.getId());
        assertNotNull("Store " + store.getId() + " was not persisted properly.", loadedStore);
        assertEquals("coverageStore", loadedStore.getName());
        assertEquals("store description", loadedStore.getDescription());
        assertTrue(loadedStore.isEnabled());
        assertEquals("type", loadedStore.getType());

        assertEquals("url", loadedStore.getURL());

        assertEquals("one", loadedStore.getMetadata().get("1"));
        assertEquals(new Double(2.0), loadedStore.getMetadata().get("2"));
    }

    public void testStyle() {
        System.out.println("========== testStyle()");

        removeExistingStyles();

        StyleInfo style = catalog.getFactory().createStyle();
        style.setName("style1");
        style.setFilename("style1");
        catalog.add(style);

        style = catalog.getFactory().createStyle();
        style.setName("style2");
        style.setFilename("style2");
        catalog.add(style);

        endTransaction();

        startNewTransaction();

        List<StyleInfo> styles = catalog.getStyles();
        assertFalse(styles.isEmpty());
        StyleInfo s1 = styles.get(0);

        assertTrue(styles.size() == 2);
        StyleInfo s2 = styles.get(1);

        if ("style2".equals(s1.getName())) {
            StyleInfo t = s1;
            s1 = s2;
            s2 = t;
        }

        assertEquals("style1", s1.getName());
        assertEquals("style2", s2.getName());
    }

    public void testNamespace() {
        System.out.println("========== testNamespace()");
        // ensure no stores
        clearCatalog();
        removeExistingNS();

        NamespaceInfo namespace = catalog.getFactory().createNamespace();
        namespace.setPrefix("topp");
        namespace.setURI("http://topp.openplans.org");
        catalog.add(namespace);

        namespace = catalog.getFactory().createNamespace();
        namespace.setPrefix("gs");
        namespace.setURI("http://geoserver.org");
        catalog.add(namespace);

        endTransaction();
        startNewTransaction();

        Iterator<NamespaceInfo> namespaces = catalog.getNamespaces().iterator();

        assertTrue(namespaces.hasNext());
        NamespaceInfo ns1 = namespaces.next();
        ns1 = ModificationProxy.unwrap(ns1);

        assertTrue(namespaces.hasNext());
        NamespaceInfo ns2 = namespaces.next();
        ns2 = ModificationProxy.unwrap(ns2);

        if ("gs".equals(ns1.getPrefix())) {
            NamespaceInfo t = ns1;
            ns1 = ns2;
            ns2 = t;
        }

        assertEquals("topp", ns1.getPrefix());
        assertEquals("http://topp.openplans.org", ns1.getURI());

        assertEquals("gs", ns2.getPrefix());
        assertEquals("http://geoserver.org", ns2.getURI());

        endTransaction();
        startNewTransaction();

        // store needs a workspace...
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
        ft1.setName(getName() + "1");
        ft1.setNativeName("native_" + ft1.getName());
        ft1.setNamespace(ns1);
        ft1.setStore(dataStore);
        catalog.add(ft1);

        FeatureTypeInfo ft2 = catalog.getFactory().createFeatureType();
        ft2.setName(getName() + "2");
        ft2.setNativeName("native_" + ft2.getName());
        ft2.setNamespace(ns2);
        ft2.setStore(dataStore);
        catalog.add(ft2);

        List resources = catalog.getResourcesByNamespace(ns1, FeatureTypeInfo.class);
        assertEquals(1, resources.size());
    }

    public void testDefaultNamespace() {
        System.out.println("========== testDefaultNamespace()");
        // ensure no stores
        clearCatalog();
        removeExistingNS();

        NamespaceInfo toppNamespace = catalog.getFactory().createNamespace();
        toppNamespace.setPrefix("topp2");
        toppNamespace.setURI("http://topp.openplans.org/default");

        catalog.add(toppNamespace);
        catalog.setDefaultNamespace(toppNamespace);

        NamespaceInfo gsNamespace = catalog.getFactory().createNamespace();
        gsNamespace.setPrefix("gs2");
        gsNamespace.setURI("http://geoserver.org/default");
        catalog.add(gsNamespace);

        endTransaction();
        startNewTransaction();

        NamespaceInfo defaultNs = catalog.getDefaultNamespace();
        assertEquals(toppNamespace, defaultNs);

        catalog.setDefaultNamespace(gsNamespace);

        endTransaction();
        startNewTransaction();

        defaultNs = catalog.getDefaultNamespace();
        assertEquals(gsNamespace, defaultNs);
    }

    public void testFeatureType() throws Exception {
        System.out.println("========== testFeatureType()");

        // ensure no stores
        clearCatalog();
        removeExistingNS();

        NamespaceInfo namespace = catalog.getFactory().createNamespace();
        namespace.setPrefix(getName());
        namespace.setURI("http://" + getName() + ".openplans.org");
        catalog.add(namespace);

        // store needs a workspace...
        WorkspaceInfo ws = catalog.getFactory().createWorkspace();
        ws.setName("testFeatureTypeWorkspace");
        catalog.add(ws);

        StyleInfo style = catalog.getFactory().createStyle();
        style.setName("style_testFeatureType");
        style.setFilename("style_testFeatureType");
        catalog.add(style);

        DataStoreInfo dataStore = catalog.getFactory().createDataStore();
        dataStore.setName("dataStore2");
        dataStore.setDescription("store description");
        dataStore.setEnabled(true);
        dataStore.setWorkspace(ws);
        catalog.add(dataStore);

        FeatureTypeInfo refFeatureType = catalog.getFactory().createFeatureType();
        refFeatureType.setName("featureType");
        refFeatureType.setNativeName("nativefeatureType");
        refFeatureType.setTitle("featureType title");
        refFeatureType.setDescription("featureType description");
        refFeatureType.setAbstract("featureType abstract");
        refFeatureType.setSRS("EPSG:4326");
        refFeatureType.setNamespace(namespace);

        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        Filter filter = factory.id(Collections.EMPTY_SET);
        refFeatureType.setFilter(filter);

        CoordinateReferenceSystem crsNative = CRS
                .parseWKT("PROJCS[\"NAD83 / BC Albers\",GEOGCS[\"NAD83\",DATUM[\"North_American_Datum_1983\",SPHEROID[\"GRS 1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]],TOWGS84[0,0,0],AUTHORITY[\"EPSG\",\"6269\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4269\"]],PROJECTION[\"Albers_Conic_Equal_Area\"],PARAMETER[\"standard_parallel_1\",50],PARAMETER[\"standard_parallel_2\",58.5],PARAMETER[\"latitude_of_center\",45],PARAMETER[\"longitude_of_center\",-126],PARAMETER[\"false_easting\",1000000],PARAMETER[\"false_northing\",0],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AUTHORITY[\"EPSG\",\"3005\"]]");
        BoundingBox nativeBoundingBox = new ReferencedEnvelope(0, 0, 0, 0, crsNative);
        BoundingBox latLonBoundingBox = new ReferencedEnvelope(-180, 180, -90, 90,
                DefaultGeographicCRS.WGS84);

        refFeatureType.setNativeBoundingBox((ReferencedEnvelope) nativeBoundingBox);
        refFeatureType.setLatLonBoundingBox((ReferencedEnvelope) latLonBoundingBox);

        refFeatureType.setStore(dataStore);

        catalog.add(refFeatureType);

        endTransaction();

        startNewTransaction();

        FeatureTypeInfo loadedFeatureType = catalog.getFeatureType(refFeatureType.getId());
        assertNotNull(loadedFeatureType);

        assertFalse(refFeatureType == loadedFeatureType);
        refFeatureType = loadedFeatureType;

        assertEquals("featureType", refFeatureType.getName());
        assertEquals("featureType title", refFeatureType.getTitle());
        assertEquals("featureType description", refFeatureType.getDescription());
        assertEquals("featureType abstract", refFeatureType.getAbstract());
        assertEquals("EPSG:4326", refFeatureType.getSRS());

        BoundingBox box = refFeatureType.getNativeBoundingBox();
        assertNotNull("Native BBox is null", box);
        assertEquals(0d, box.getMinX(), 0d);
        assertEquals(0d, box.getMinY(), 0d);
        assertEquals(0d, box.getMaxX(), 0d);
        assertEquals(0d, box.getMaxY(), 0d);
        assertTrue(CRS.equalsIgnoreMetadata(crsNative, box.getCoordinateReferenceSystem()));

        box = refFeatureType.getLatLonBoundingBox();
        assertNotNull(box);
        assertEquals(-180d, box.getMinX(), 0d);
        assertEquals(-90d, box.getMinY(), 0d);
        assertEquals(180d, box.getMaxX(), 0d);
        assertEquals(90d, box.getMaxY(), 0d);
        assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, box
                .getCoordinateReferenceSystem()));

        assertNotNull(refFeatureType.getNamespace());
        assertEquals(getName(), refFeatureType.getNamespace().getPrefix());

        assertNotNull(refFeatureType.getStore());
        assertEquals("dataStore2", refFeatureType.getStore().getName());
        endTransaction();

        startNewTransaction();
        loadedFeatureType = catalog.getFeatureTypeByName(namespace.getPrefix(), refFeatureType
                .getName());
        assertNotNull(loadedFeatureType);

        loadedFeatureType = catalog.getFeatureTypeByName(namespace.getURI(), refFeatureType
                .getName());
        assertNotNull(loadedFeatureType);
    }

    public void testCoverage() {
        // ensure no stores
        clearCatalog();
        removeExistingNS();

        NamespaceInfo namespace = catalog.getFactory().createNamespace();
        namespace.setPrefix(getName());
        namespace.setURI("http://" + getName() + ".openplans.org");
        catalog.add(namespace);

        StyleInfo style = catalog.getFactory().createStyle();
        style.setName("style_testCoverage");
        style.setFilename("style_testCoverage");
        catalog.add(style);

        // store needs a workspace...
        WorkspaceInfo ws = catalog.getFactory().createWorkspace();
        ws.setName("testCoverageWorkspace");
        catalog.add(ws);

        endTransaction();
        startNewTransaction();

        CoverageStoreInfo coverageStore = catalog.getFactory().createCoverageStore();
        coverageStore.setName("coverageStore2");
        coverageStore.setWorkspace(ws);

        catalog.add(coverageStore);

        CoverageInfo coverage = catalog.getFactory().createCoverage();
        coverage.setName("coverage");
        coverage.setNativeName("nativename");
        coverage.setTitle("coverage title");
        coverage.setDescription("coverage description");
        coverage.setAbstract("coverage abstract");
        coverage.setSRS("EPSG:4326");
        coverage.setNamespace(namespace);
        coverage.setStore(coverageStore);
        coverage
                .setNativeBoundingBox(new ReferencedEnvelope(0, 0, 0, 0, DefaultGeographicCRS.WGS84));
        coverage
                .setLatLonBoundingBox(new ReferencedEnvelope(0, 0, 0, 0, DefaultGeographicCRS.WGS84));

        coverage.setNativeFormat("nativeFormat");
        coverage.getSupportedFormats().add("supportedFormat1");
        coverage.getSupportedFormats().add("supportedFormat2");

        coverage.setDefaultInterpolationMethod("defaultInterpolationMethod");
        coverage.getInterpolationMethods().add("interpolationMethod1");
        coverage.getInterpolationMethods().add("interpolationMethod2");

        coverage.getRequestSRS().add("EPSG:3003");
        coverage.getRequestSRS().add("EPSG:4326");
        coverage.getResponseSRS().add("EPSG:42102");

        catalog.add(coverage);

        endTransaction();

        startNewTransaction();
        CoverageInfo coverage1 = catalog.getCoverage(coverage.getId());
        assertNotNull(coverage1);

        // assertFalse( coverage == coverage1 );
        coverage = ModificationProxy.unwrap(coverage1);

        assertEquals("nativeFormat", coverage.getNativeFormat());
        assertEquals(2, coverage.getSupportedFormats().size());
        assertTrue(coverage.getSupportedFormats().contains("supportedFormat1"));
        assertTrue(coverage.getSupportedFormats().contains("supportedFormat2"));

        assertEquals("defaultInterpolationMethod", coverage.getDefaultInterpolationMethod());
        assertEquals(2, coverage.getInterpolationMethods().size());
        assertTrue(coverage.getInterpolationMethods().contains("interpolationMethod1"));
        assertTrue(coverage.getInterpolationMethods().contains("interpolationMethod2"));

        assertEquals(2, coverage.getRequestSRS().size());
        assertEquals(1, coverage.getResponseSRS().size());
        assertTrue(coverage.getRequestSRS().contains("EPSG:3003"));
        assertTrue(coverage.getRequestSRS().contains("EPSG:4326"));
        assertTrue(coverage.getResponseSRS().contains("EPSG:42102"));
        endTransaction();

        startNewTransaction();
        coverage1 = catalog.getCoverageByName(namespace.getPrefix(), coverage.getName());
        assertNotNull(coverage1);

        coverage1 = catalog.getCoverageByName(namespace.getURI(), coverage.getName());
        assertNotNull(coverage1);
    }

    public void testMap() {
        // ensure no stores
        clearCatalog();
        removeExistingNS();

        // store needs a workspace...
        WorkspaceInfo ws = catalog.getFactory().createWorkspace();
        ws.setName("testLayerWorkspace");
        catalog.add(ws);
        NamespaceInfo ns = catalog.getFactory().createNamespace();
        ns.setPrefix("testLayerWorkspace");
        ns.setURI("http://testLayerWorkspace.org");
        catalog.add(ns);

        LayerInfo layer1 = createLayer(ws, "cs1", "cov1", "ncov1", "test coverage 1", "testlayer1");
        LayerInfo layer2 = createLayer(ws, "cs2", "cov2", "ncov2", "test coverage 2", "testlayer2");

        MapInfo map1 = catalog.getFactory().createMap();
        map1.setName("map_testLayer");
        map1.getLayers().add(layer1);
        map1.getLayers().add(layer2);
        catalog.add(map1);

        endTransaction();
        startNewTransaction();

        MapInfo map2 = catalog.getMap(map1.getId());
        assertNotNull(map2);
        map2 = ModificationProxy.unwrap(map2);

        // assertTrue( map1 != map2 );

        assertEquals(2, map2.getLayers().size());
    }

    public void testLayerGroup() {
        // ensure no stores
        clearCatalog();
        removeExistingNS();

        // store needs a workspace...
        WorkspaceInfo ws = catalog.getFactory().createWorkspace();
        ws.setName("testLayerWorkspace");
        catalog.add(ws);
        NamespaceInfo ns = catalog.getFactory().createNamespace();
        ns.setPrefix("testLayerWorkspace");
        ns.setURI("http://testLayerWorkspace.org");
        catalog.add(ns);

        LayerInfo layer1 = createLayer(ws, "cs1", "cov1", "ncov1", "test coverage 1", "testlayer1");
        LayerInfo layer2 = createLayer(ws, "cs2", "cov2", "ncov2", "test coverage 2", "testlayer2");

        LayerGroupInfo layerGroupInfo = catalog.getFactory().createLayerGroup();
        layerGroupInfo.setName("TestLayerGroup");
        layerGroupInfo.getLayers().add(layer1);
        layerGroupInfo.getLayers().add(layer2);

        ReferencedEnvelope re = new ReferencedEnvelope(10, 20, -20, -10, DefaultGeographicCRS.WGS84);
        layerGroupInfo.setBounds(re);

        catalog.add(layerGroupInfo);

        endTransaction();
        startNewTransaction();

        LayerGroupInfo reloaded = catalog.getLayerGroup(layerGroupInfo.getId());
        assertNotNull(reloaded);

        ReferencedEnvelope reReloaded = reloaded.getBounds();
        assertNotNull(reReloaded);
        assertEquals(re.getMinX(), reReloaded.getMinX());
    }

    public void testLayerGroupUpdate() {
        // ensure no stores
        clearCatalog();
        removeExistingNS();

        // store needs a workspace...
        WorkspaceInfo ws = catalog.getFactory().createWorkspace();
        ws.setName("testLayerWorkspace");
        catalog.add(ws);
        NamespaceInfo ns = catalog.getFactory().createNamespace();
        ns.setPrefix("testLayerWorkspace");
        ns.setURI("http://testLayerWorkspace.org");
        catalog.add(ns);

        LayerInfo layer1 = createLayer(ws, "cs1", "cov1", "ncov1", "test coverage 1", "testlayer1");
        LayerInfo layer2 = createLayer(ws, "cs2", "cov2", "ncov2", "test coverage 2", "testlayer2");

        LayerGroupInfo layerGroupInfo = catalog.getFactory().createLayerGroup();
        layerGroupInfo.setName("TestLayerGroup_SAVE_UPDATE");
        layerGroupInfo.getLayers().add(layer1);
        layerGroupInfo.getLayers().add(layer2);

        ReferencedEnvelope re = new ReferencedEnvelope(10, 20, -20, -10, DefaultGeographicCRS.WGS84);
        layerGroupInfo.setBounds(re);

        StyleInfo style = catalog.getFactory().createStyle();
        style.setName("style_test");
        style.setFilename("style_test");
        catalog.add(style);

        layerGroupInfo.getStyles().add(style);
        layerGroupInfo.getStyles().add(null);

        catalog.add(layerGroupInfo);

        assertEquals(2, layerGroupInfo.getStyles().size());

        endTransaction();

        {
            startNewTransaction();

            LayerGroupInfo reloaded = catalog.getLayerGroup(layerGroupInfo.getId());
            assertNotNull(reloaded);

            assertEquals("Styles has not been saved", 2, reloaded.getStyles().size());

            reloaded.getLayers().remove(1);
            reloaded.getStyles().remove(1);

            assertEquals("GroupedLayer is broken", 1, reloaded.getStyles().size());
            assertEquals("GroupedLayer is broken", 1, reloaded.getLayers().size());

            LOGGER.info("A Layer was removed from the group, total should be 1 - SAVING");
            catalog.save(reloaded);
            LOGGER.info("After saving, IN transaction, 1 expected:  " + reloaded);
            endTransaction();
            LOGGER.info("After saving, OUT transaction, 1 expected: " + reloaded);

            assertEquals(layerGroupInfo.getId(), reloaded.getId());
            assertFalse(layerGroupInfo == reloaded);
        }


        {
            startNewTransaction();
            LayerGroupInfo reloaded2 = catalog.getLayerGroup(layerGroupInfo.getId());
            assertNotNull(reloaded2);

            assertEquals("GroupedLayer remove/update is broken", 1, reloaded2.getStyles().size());
            assertEquals("GroupedLayer remove/update is broken", 1, reloaded2.getLayers().size());

            endTransaction();
            startNewTransaction();

            reloaded2.getLayers().add(layer2);
            reloaded2.getStyles().add(null);

            LOGGER.info("A Layer was added to the group, total should be 2 - SAVING");
            catalog.save(reloaded2);
            LOGGER.info("After saving, IN transaction, 2 expected:  " + reloaded2);
            endTransaction();
            LOGGER.info("After saving, OUT transaction, 2 expected: " + reloaded2);
        }

        {
            startNewTransaction();

            LayerGroupInfo reloaded3 = catalog.getLayerGroup(layerGroupInfo.getId());
            assertNotNull(reloaded3);

            assertEquals("GroupedLayer add/update is broken", 2, reloaded3.getStyles().size());
            assertEquals("GroupedLayer add/update is broken", 2, reloaded3.getLayers().size());
        }

    }

    private LayerInfo createLayer(WorkspaceInfo ws, String csname, String covname, String covnname,
            String covtitle, String lname) {
        LayerInfo layer1;

        CoverageStoreInfo coverageStore = catalog.getFactory().createCoverageStore();
        coverageStore.setName(csname);
        coverageStore.setWorkspace(ws);

        catalog.add(coverageStore);

        CoverageInfo coverage = catalog.getFactory().createCoverage();
        coverage.setName(covname);
        coverage.setNativeName(covnname);
        coverage.setTitle(covtitle);
        coverage.setStore(coverageStore);
        // coverage.setNativeBoundingBox(new ReferencedEnvelope(0, 0, 0, 0,
        // DefaultGeographicCRS.WGS84));
        // coverage.setLatLonBoundingBox(new ReferencedEnvelope(0, 0, 0, 0,
        // DefaultGeographicCRS.WGS84));
        catalog.add(coverage);

        layer1 = catalog.getFactory().createLayer();
        layer1.setResource(coverage);
        layer1.setName(lname);

        logger.warn("LYID:     " + layer1.getId());
        logger.warn("COVERAGE: " + layer1.getResource());
        logger.warn("COVSTORE: " + ((CoverageInfo) layer1.getResource()).getStore());

        catalog.add(layer1);

        return layer1;
    }

    public void setCatalogDAO(CatalogDAO catalogDAO) {
        this.catalogDAO = catalogDAO;
    }

}
