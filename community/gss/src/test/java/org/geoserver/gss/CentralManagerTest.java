package org.geoserver.gss;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.custommonkey.xmlunit.XpathEngine;
import org.geoserver.config.GeoServer;
import org.geoserver.data.test.LiveDbmsData;
import org.geoserver.data.test.TestData;
import org.geoserver.gss.GSSInfo.GSSMode;
import org.geoserver.test.GeoServerAbstractTestSupport;
import org.geotools.data.VersioningDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;

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
    }
    
    public void testVoid() {
        synch.run();
    }

}
