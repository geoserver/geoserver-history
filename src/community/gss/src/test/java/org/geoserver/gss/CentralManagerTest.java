package org.geoserver.gss;

import static org.geoserver.gss.GSSCore.*;
import static org.geotools.data.DataUtilities.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.custommonkey.xmlunit.XpathEngine;
import org.geoserver.config.GeoServer;
import org.geoserver.data.test.LiveDbmsData;
import org.geoserver.data.test.TestData;
import org.geoserver.gss.GSSInfo.GSSMode;
import org.geoserver.test.GeoServerAbstractTestSupport;
import org.geotools.data.FeatureStore;
import org.geotools.data.VersioningDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
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

    private SynchronizationManager synch;

    // protected String getLogConfiguration() {
    // return "/DEFAULT_LOGGING.properties";
    // }

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
        synch = (SynchronizationManager) applicationContext.getBeansOfType(SynchronizationManager.class).values().iterator().next();
        
        // disable automated scheduling, we control how does what here
        Timer timer = (Timer) applicationContext.getBean("gssTimerFactory");
        timer.cancel();
        
        // make some tables synchronised
        synchStore = (VersioningDataStore) getCatalog().getDataStoreByName("synch").getDataStore(
                null);
        FeatureStore<SimpleFeatureType, SimpleFeature> fs = (FeatureStore<SimpleFeatureType, SimpleFeature>) synchStore
                .getFeatureSource(SYNCH_TABLES);
        long restrectedId = addFeature(fs , "restricted", "2");
        long roadsId = addFeature(fs, "roads", "2");
        synchStore.setVersioned("restricted", true, null, null);
        synchStore.setVersioned("roads", true, null, null);
        
        // add some units
        fs = (FeatureStore<SimpleFeatureType, SimpleFeature>) synchStore.getFeatureSource(SYNCH_UNITS);
        long mangoId = addFeature(fs, "unit-mango", "http://localhost:8081/geoserver/ows", null, null, null, null, 60, 10, false);
        
        // link units and tables
        fs = (FeatureStore<SimpleFeatureType, SimpleFeature>) synchStore.getFeatureSource(SYNCH_UNIT_TABLES);
        addFeature(fs, mangoId, restrectedId, null, null, null, null);
    }
    
    long addFeature(FeatureStore<SimpleFeatureType, SimpleFeature> fs, Object... attributes) throws IOException {
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(fs.getSchema());
        List<FeatureId> ids = fs.addFeatures(collection((fb.buildFeature(null, attributes))));
        String id = ids.get(0).getID();
        return Long.parseLong(id.substring(fs.getSchema().getTypeName().length() + 1));
    }
    
    public void testVoid() throws Exception {
        synch.synchronizeOustandlingLayers();
    }

}
