package org.geoserver.wfs;

import java.math.BigInteger;

import javax.xml.namespace.QName;

import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.LockFeatureResponseType;
import net.opengis.wfs.LockFeatureType;
import net.opengis.wfs.LockType;
import net.opengis.wfs.WFSFactory;

import org.geoserver.data.test.MockGeoServerDataDirectory;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;

public class LockFeatureTest extends WFSTestSupport {

    private static final QName BASIC_POLYGON_TYPE_QNAME = new QName( 
		MockGeoServerDataDirectory.CITE_URI, MockGeoServerDataDirectory.BASIC_POLYGONS_TYPE,
		MockGeoServerDataDirectory.CITE_PREFIX
	);
    private static final QName BRIDGES_TYPE_QNAME = new QName(
		MockGeoServerDataDirectory.CITE_URI, MockGeoServerDataDirectory.BRIDGES_TYPE,
		MockGeoServerDataDirectory.CITE_PREFIX
	);

    public void testLockFailNoFeatureType() throws Exception {
        FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();

        LockFeatureType request = WFSFactory.eINSTANCE.createLockFeatureType();
        LockType lock = WFSFactory.eINSTANCE.createLockType();
        lock.setTypeName( new QName( 
    		MockGeoServerDataDirectory.CITE_URI, MockGeoServerDataDirectory.BASIC_POLYGONS_TYPE + "garbage", 
    		MockGeoServerDataDirectory.CITE_PREFIX)
		);
        lock.setFilter(Filter.NONE);
        request.getLock().add(lock);

        try {
            LockFeatureResponseType results = webFeatureService.lockFeature(request);
            fail("Feature type unkown");
        } catch (WFSException e) {
            LOGGER.info("Trying to get an unknow feature type, the expected exception is: "
                    + e.getMessage());
        }
    }

    public void testLockFailNoLockElements() throws Exception {
        FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();

        LockFeatureType request = WFSFactory.eINSTANCE.createLockFeatureType();
        LockType lock = WFSFactory.eINSTANCE.createLockType();
        lock.setTypeName(BASIC_POLYGON_TYPE_QNAME);
        lock.setFilter(Filter.NONE);

        try {
            LockFeatureResponseType results = webFeatureService.lockFeature(request);
            fail("No locks, should have failed");
        } catch (WFSException e) {
            LOGGER.info("Trying to issue a lockFeature without locks, the expected exception is: "
                    + e.getMessage());
        }
    }

    public void testLockAll() throws Exception {
        FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();

        LockFeatureType request = WFSFactory.eINSTANCE.createLockFeatureType();
        LockType lock = WFSFactory.eINSTANCE.createLockType();
        lock.setTypeName(BASIC_POLYGON_TYPE_QNAME);
        // I don't specify a filter, the spec does not require it
        request.getLock().add(lock);

        LockFeatureResponseType results = webFeatureService.lockFeature(request);
        assertEquals(0, results.getFeaturesNotLocked().getFeatureId().size());
        assertEquals(3, results.getFeaturesLocked().getFeatureId().size());
        results.getFeaturesLocked().getFeatureId().contains("BasicPolygons.1107531493630");
        results.getFeaturesLocked().getFeatureId().contains("BasicPolygons.1107531493643");
        results.getFeaturesLocked().getFeatureId().contains("BasicPolygons.1107531493644");
    }
    
    public void testLockAllMulti() throws Exception {
        FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();

        LockFeatureType request = WFSFactory.eINSTANCE.createLockFeatureType();
        LockType lock1 = WFSFactory.eINSTANCE.createLockType();
        lock1.setTypeName(BASIC_POLYGON_TYPE_QNAME);
        request.getLock().add(lock1);
        LockType lock2 = WFSFactory.eINSTANCE.createLockType();
        lock2.setTypeName(BRIDGES_TYPE_QNAME);
        request.getLock().add(lock2);

        LockFeatureResponseType results = webFeatureService.lockFeature(request);
        assertEquals(0, results.getFeaturesNotLocked().getFeatureId().size());
        assertEquals(4, results.getFeaturesLocked().getFeatureId().size());
        results.getFeaturesLocked().getFeatureId().contains("BasicPolygons.1107531493630");
        results.getFeaturesLocked().getFeatureId().contains("BasicPolygons.1107531493643");
        results.getFeaturesLocked().getFeatureId().contains("BasicPolygons.1107531493644");
        results.getFeaturesLocked().getFeatureId().contains("Bridges.1107531599613");
    }

    public void testLockFiltered() throws Exception {
        FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();

        LockFeatureType request = WFSFactory.eINSTANCE.createLockFeatureType();
        LockType lock = WFSFactory.eINSTANCE.createLockType();
        lock.setTypeName(BASIC_POLYGON_TYPE_QNAME);
        lock.setFilter(filterFactory.createFidFilter("BasicPolygons.1107531493630"));
        request.getLock().add(lock);

        LockFeatureResponseType results = webFeatureService.lockFeature(request);
        assertEquals(0, results.getFeaturesNotLocked().getFeatureId().size());
        assertEquals(1, results.getFeaturesLocked().getFeatureId().size());
        results.getFeaturesLocked().getFeatureId().contains("BasicPolygons.1107531493630");
    }

    /**
     * Lock one, then try to lock the rest with a different request and check we're
     * able to lock only the remaning features
     * @throws Exception
     */
    public void testLockSome() throws Exception {
        FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();

        LockFeatureType request1 = WFSFactory.eINSTANCE.createLockFeatureType();
        LockType lock1 = WFSFactory.eINSTANCE.createLockType();
        lock1.setTypeName(BASIC_POLYGON_TYPE_QNAME);
        lock1.setFilter(filterFactory.createFidFilter("BasicPolygons.1107531493630"));
        request1.getLock().add(lock1);

        // this should work fine and it's tested somewhere else
        webFeatureService.lockFeature(request1);
        
        // now let's try to lock the rest
        LockFeatureType request2 = WFSFactory.eINSTANCE.createLockFeatureType();
        request2.setLockAction(AllSomeType.SOME_LITERAL);
        LockType lock2 = WFSFactory.eINSTANCE.createLockType();
        lock2.setTypeName(BASIC_POLYGON_TYPE_QNAME);
        lock2.setFilter(Filter.NONE);
        request2.getLock().add(lock2);
        
        LockFeatureResponseType results2 = webFeatureService.lockFeature(request2);
        assertEquals(1, results2.getFeaturesNotLocked().getFeatureId().size());
        assertEquals(2, results2.getFeaturesLocked().getFeatureId().size());
    }
    
    /**
     * Lock all, then check we can't lock anymore those
     */
    public void testLockFailRepeated() throws Exception {
        FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();

        LockFeatureType request1 = WFSFactory.eINSTANCE.createLockFeatureType();
        LockType lock1 = WFSFactory.eINSTANCE.createLockType();
        lock1.setTypeName(BASIC_POLYGON_TYPE_QNAME);
        lock1.setFilter(Filter.NONE);
        request1.getLock().add(lock1);

        // this should work fine and it's tested somewhere else
        webFeatureService.lockFeature(request1);
        
        // now let's try to lock the rest
        LockFeatureType request2 = WFSFactory.eINSTANCE.createLockFeatureType();
        LockType lock2 = WFSFactory.eINSTANCE.createLockType();
        lock2.setTypeName(BASIC_POLYGON_TYPE_QNAME);
        lock2.setFilter(filterFactory.createFidFilter("BasicPolygons.1107531493630"));
        request2.getLock().add(lock2);
        
        try {
            webFeatureService.lockFeature(request2);
            fail("This should not work because stuff it's already locked");
        } catch(WFSException e) {
            LOGGER.info("The expected exception message is " + e.getMessage());
        }
    }
    
    // COMMENTED OUT BECAUSE WAITING ONE MINUTE DURING UNIT TESTS IS WAY TOO MUCH...
//    public void testExpiry() throws Exception {
//        FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();
//
//        // first lock all feature in the type
//        LockFeatureType request1 = WFSFactory.eINSTANCE.createLockFeatureType();
//        request1.setExpiry(new BigInteger("1"));
//        LockType lock1 = WFSFactory.eINSTANCE.createLockType();
//        lock1.setTypeName(BASIC_POLYGON_TYPE_QNAME);
//        lock1.setFilter(Filter.NONE);
//        request1.getLock().add(lock1);
//
//        // this should work fine and it's tested somewhere else
//        webFeatureService.lockFeature(request1);
//        
//        // then try a failing request 
//        LockFeatureType request2 = WFSFactory.eINSTANCE.createLockFeatureType();
//        LockType lock2 = WFSFactory.eINSTANCE.createLockType();
//        lock2.setTypeName(BASIC_POLYGON_TYPE_QNAME);
//        lock2.setFilter(Filter.NONE);
//        request2.getLock().add(lock2);
//
//        try {
//            webFeatureService.lockFeature(request2);
//            fail("We are not suppose to be able and lock those features");
//        } catch(WFSException e) {
//            LOGGER.info("Ok, we were not supposed to be able and lock the features: " + e);
//        }
//        
//        // sleep for one minute... good night!
//        Thread.sleep(60 * 1000);
//        
//        // wake up! and try again!
//        LockFeatureResponseType results = webFeatureService.lockFeature(request2);
//        assertEquals(0, results.getFeaturesNotLocked().getFeatureId().size());
//        assertEquals(3, results.getFeaturesLocked().getFeatureId().size());
//    }

}
