package org.geoserver.gss;

import static org.easymock.EasyMock.*;
import static org.geoserver.gss.GSSCore.*;
import static org.geotools.data.DataUtilities.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.xml.namespace.QName;

import org.custommonkey.xmlunit.XpathEngine;
import org.geoserver.config.GeoServer;
import org.geoserver.data.test.LiveDbmsData;
import org.geoserver.data.test.TestData;
import org.geoserver.gss.GSSInfo.GSSMode;
import org.geoserver.test.GeoServerAbstractTestSupport;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.VersioningDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;

/**
 * Tests the central manager
 */
public class CentralManagerTest extends GeoServerAbstractTestSupport {

    static XpathEngine xpath;

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    DefaultGeoServerSynchronizationService gss;

    VersioningDataStore synchStore;

    SynchronizationManager synch;

    FeatureStore<SimpleFeatureType, SimpleFeature> fsUnits;

    FeatureStore<SimpleFeatureType, SimpleFeature> fsUnitTables;

    @Override
    public TestData buildTestData() throws Exception {
        File base = new File("./src/test/resources/");
        LiveDbmsData data = new LiveDbmsData(new File(base, "data_dir"), "unit", new File(base,
                "unit.sql"));
        List<String> filteredPaths = data.getFilteredPaths();
        filteredPaths.clear();
        filteredPaths.add("workspaces/topp/synch/datastore.xml");
        return data;
    }

    @Override
    protected void setUpInternal() throws Exception {
        // configure the GSS service
        GeoServer gs = getGeoServer();
        GSSInfo gssInfo = gs.getService(GSSInfo.class);
        gssInfo.setMode(GSSMode.Central);
        gssInfo.setVersioningDataStore(getCatalog().getDataStoreByName("synch"));
        gs.save(gssInfo);

        // initialize the GSS service
        Map gssBeans = applicationContext
                .getBeansOfType(DefaultGeoServerSynchronizationService.class);
        gss = (DefaultGeoServerSynchronizationService) gssBeans.values().iterator().next();
        gss.core.ensureCentralEnabled();

        // grab the synch manager
        synch = (SynchronizationManager) applicationContext.getBeansOfType(
                SynchronizationManager.class).values().iterator().next();

        // disable automated scheduling, we control how does what here
        Timer timer = (Timer) applicationContext.getBean("gssTimerFactory");
        timer.cancel();

        // make some tables synchronised
        synchStore = (VersioningDataStore) getCatalog().getDataStoreByName("synch").getDataStore(
                null);
        FeatureStore<SimpleFeatureType, SimpleFeature> fs = (FeatureStore<SimpleFeatureType, SimpleFeature>) synchStore
                .getFeatureSource(SYNCH_TABLES);
        long restrectedId = addFeature(fs, "restricted", "2");
        long roadsId = addFeature(fs, "roads", "2");
        synchStore.setVersioned("restricted", true, null, null);
        synchStore.setVersioned("roads", true, null, null);

        // add some units
        fsUnits = (FeatureStore<SimpleFeatureType, SimpleFeature>) synchStore
                .getFeatureSource(SYNCH_UNITS);
        long mangoId = addFeature(fsUnits, "unit1", "http://localhost:8081/geoserver/ows",
                null, null, null, null, 60, 10, false);

        // link units and tables
        fsUnitTables = (FeatureStore<SimpleFeatureType, SimpleFeature>) synchStore
                .getFeatureSource(SYNCH_UNIT_TABLES);
        addFeature(fsUnitTables, mangoId, restrectedId, null, null, null, null);
        addFeature(fsUnitTables, mangoId, roadsId, null, null, null, null);
    }

    /**
     * Utility method to add a feature in a store and get back the generated id (which is supposed
     * to be a integer/long number)
     * 
     * @param fs
     * @param attributes
     * @return
     * @throws IOException
     */
    long addFeature(FeatureStore<SimpleFeatureType, SimpleFeature> fs, Object... attributes)
            throws IOException {
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(fs.getSchema());
        List<FeatureId> ids = fs.addFeatures(collection((fb.buildFeature(null, attributes))));
        String id = ids.get(0).getID();
        return Long.parseLong(id.substring(fs.getSchema().getTypeName().length() + 1));
    }
    
    SimpleFeature getSingleFeature(FeatureSource<SimpleFeatureType, SimpleFeature> fs, Filter f) throws IOException {
        FeatureIterator<SimpleFeature> fi = null;
        try {
            fi = fs.getFeatures(f).features();
            return fi.next();
        } finally {
            if(fi != null) {
                fi.close();
            }
        }
    }

    public void testConnectionFailure() throws Exception {
        // create mock objects that will simulate a connection failure
        GSSClient client = createNiceMock(GSSClient.class);
        expect(client.getCentralRevision((QName) anyObject())).andThrow(
                new IOException("Host unreachable"));
        replay(client);
        GSSClientFactory factory = createNiceMock(GSSClientFactory.class);
        expect(factory.createClient(new URL("http://localhost:8081/geoserver/ows"), null, null))
                .andReturn(client);
        replay(factory);

        synch.clientFactory = factory;

        // perform synch
        Date start = new Date();
        synch.synchronizeOustandlingLayers();
        Date end = new Date();

        // check we stored the last failure marker
        SimpleFeature f = getSingleFeature(fsUnitTables, ff.equal(ff.property("table_id"), ff.literal(1), false));
        Date lastFailure = (Date) f.getAttribute("last_failure");
        assertNotNull(lastFailure);
        assertTrue(lastFailure.compareTo(start) >= 0 && lastFailure.compareTo(end) <= 0);
        
        // check we marked the unit as failed
        f = getSingleFeature(fsUnits, ff.equal(ff.property("unit_name"), ff.literal("unit1"), false));
        assertTrue((Boolean) f.getAttribute("errors"));
    }
    
    public void testEmptyUpdates() throws Exception {
        // create mock objects that will simulate a connection failure
        GSSClient client = createNiceMock(GSSClient.class);
        expect(client.getCentralRevision((QName) anyObject())).andReturn(new Long(-1));
        client.postDiff((PostDiffType) anyObject());
        expect(client.getDiff((GetDiffType) anyObject())).andReturn(new GetDiffResponseType());
        replay(client);
        GSSClientFactory factory = createNiceMock(GSSClientFactory.class);
        expect(factory.createClient(new URL("http://localhost:8081/geoserver/ows"), null, null))
                .andReturn(client);
        replay(factory);
        
        synch.clientFactory = factory;
        
        // perform synch
        Date start = new Date();
        synch.synchronizeOustandlingLayers();
        Date end = new Date();

        // check we stored the last synch marker
        SimpleFeature f = getSingleFeature(fsUnitTables, ff.equal(ff.property("table_id"), ff.literal(1), false));
        Date lastSynch = (Date) f.getAttribute("last_synchronization");
        assertNotNull(lastSynch);
        assertTrue(lastSynch.compareTo(start) >= 0 && lastSynch.compareTo(end) <= 0);
        assertNull(f.getAttribute("last_failure"));
        
        // check we marked the unit as succeded
        f = getSingleFeature(fsUnits, ff.equal(ff.property("unit_name"), ff.literal("unit1"), false));
        assertFalse((Boolean) f.getAttribute("errors"));

    }

}
