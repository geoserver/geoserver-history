/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import junit.framework.TestCase;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.Expression;
import org.geotools.filter.FidFilter;
import org.geotools.filter.FilterFactory;
import org.vfny.geoserver.config.ConfigInfo;
import org.vfny.geoserver.config.TypeRepository;
import org.vfny.geoserver.requests.LockRequest;
import org.vfny.geoserver.requests.TransactionRequest;
import org.vfny.geoserver.requests.UpdateRequest;
import java.util.logging.Logger;


/**
 * Tests the locking responses.  This class needs work to generalize, as it
 * currently only works on one database, which unfortunately is not publically
 * available.  To get this class useful we need to not use FidFilters, since
 * they are db specific.  These tests also need a  transactional datasource to
 * be totally useful, but perhaps we can figure out locking with shapefiles.
 * We probably can test most things if TransactionResponse were to check
 * locking before whether the backend datasource could handle transactions,
 * but that seems a bit less efficient.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: LockSuite.java,v 1.3 2003/09/16 16:13:20 cholmesny Exp $
 */
public class LockSuite extends TestCase {
    //Initializes the logger. Uncomment to see log messages.
    //static {
    //org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", 
    //java.util.logging.Level.FINE);
    //}

    /** Class logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** Unit test data directory */
    private static final String CONFIG_DIR = System.getProperty("user.dir")
        + "/misc/unit/config/";

    /** Unit test data directory */
    private static final String TYPE_DIR = System.getProperty("user.dir")
        + "/misc/unit/responses";

    /** Holds mappings between HTTP and ASCII encodings */
    private static FilterFactory filterFac = FilterFactory.createFilterFactory();
    private String testType = "roads";
    private ConfigInfo config;
    private TypeRepository repo;

    /**
     * Constructor with super.
     *
     * @param testName The name of the test.
     */
    public LockSuite(String testName) {
        super(testName);
    }

    /**
     * Handles test set up details.
     */
    public void setUp() {
        config = ConfigInfo.getInstance(CONFIG_DIR);
        config.setTypeDir(TYPE_DIR);
        repo = TypeRepository.getInstance();
        repo.clearLocks();
    }

    public void tearDown() {
        LOGGER.fine("tearing down");
        repo.closeTypeResources();
    }


    //TODO: redo all these tests.  Need a backend, as feature level locking
    //means we need some features to work with.
    public void testLock() throws Exception {
        LockRequest request = new LockRequest();
        request.addLock(testType, null);

        String xml = LockResponse.getXmlResponse(request);
        LOGGER.fine(xml);
        LOGGER.fine("is locked: " + repo.isLocked(testType, null, null));

        //probably bad to use this as the formatting could change, but
        //that shouldn't really affect the unit test.
        assertTrue(xml.startsWith(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<WFS_LockFeatureResponse"));
        assertTrue(repo.isLocked(testType));
    }

    public void testTwoLocks() throws WfsException {
        LockRequest request = new LockRequest();
        request.addLock(testType, null);

        String testType2 = "geom_test";
        request.addLock(testType2, null);

        String xml = LockResponse.getXmlResponse(request);
        LOGGER.fine(xml);
        LOGGER.fine(testType + "is locked: " + repo.isLocked(testType) + ", "
            + testType2 + "is locked: " + repo.isLocked(testType2));
        assertTrue(xml.startsWith(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<WFS_LockFeatureResponse"));
        assertTrue(repo.isLocked(testType));
        assertTrue(repo.isLocked(testType2));
    }

    //public void testUnlock(
    public void testTransaction() throws Exception {
        String lockId = repo.lock(testType);
        TransactionRequest baseRequest = new TransactionRequest();
        UpdateRequest internalRequest = new UpdateRequest();
        internalRequest.setTypeName(testType);
        internalRequest.addProperty("nlanes", "1");

        FidFilter filter = filterFac.createFidFilter("roads.48686");
        LOGGER.fine("roads.48686 is locked: "
            + repo.isLocked(testType, filter, null));
        internalRequest.setFilter(filter);
        baseRequest.addSubRequest(internalRequest);

        String failLockMsg = "attempting to modify locked features"; //Feature Type: " + testType + " is locked";

        try {
            String transResponse = TransactionResponse.getXmlResponse(baseRequest);

            //should never reach here, as it should always throw exception
            //for not locking right.
            LOGGER.fine("response is " + transResponse);
            fail();
        } catch (WfsTransactionException e) {
            LOGGER.fine("fail message is " + e.getMessage());

            //and message should match the failLock message.
            assertEquals(e.getMessage(), failLockMsg);

            //LOGGER.fine(e.getMessage());
        }

        LOGGER.fine("roads.48686 is locked :"
            + repo.isLocked(testType, filter, null));
        assertTrue(repo.isLocked(testType, filter, null));
        baseRequest.setLockId(lockId);
        baseRequest.setReleaseAll(false);

        try {
            //this probably will not go through, as db info is not set up right
            String response = TransactionResponse.getXmlResponse(baseRequest);
            LOGGER.fine("response from transaction is " + response);
            assertTrue(response.indexOf("SUCCESS") != -1);
        } catch (WfsTransactionException e) {
            //but the message should not be the failLock message.
            LOGGER.fine("caught transaction exception: " + e);
            fail();

            //assertTrue(!(e.getMessage().equals(failLockMsg)));
        }

        LOGGER.fine("roads.48686 is locked :"
            + repo.isLocked(testType, filter, null));
        assertTrue(!repo.isLocked(testType, filter, null));

        FidFilter fidFilter2 = filterFac.createFidFilter("roads.48692");
        LOGGER.fine("roads.48692 is locked :"
            + repo.isLocked(testType, fidFilter2, null));
        assertTrue(repo.isLocked(testType, fidFilter2, null));

        baseRequest.setReleaseAll(true);

        try {
            //this probably will not go through, as db info is not set up right
            String response = TransactionResponse.getXmlResponse(baseRequest);
            LOGGER.fine("response from transaction is " + response);
            assertTrue(response.indexOf("SUCCESS") != -1);
        } catch (WfsTransactionException e) {
            //but the message should not be the failLock message.
            LOGGER.fine("caught transaction exception: " + e);
            fail();

            //assertTrue(!(e.getMessage().equals(failLockMsg)));
        }

        LOGGER.fine("roads.48686 is locked :"
            + repo.isLocked(testType, filter, null));
        assertTrue(!repo.isLocked(testType, filter, null));

        //FidFilter fidFilter2 = filterFac.createFidFilter("roads.48692");
        LOGGER.fine("roads.48692 is locked :"
            + repo.isLocked(testType, fidFilter2, null));
        assertTrue(!repo.isLocked(testType, fidFilter2, null));
    }

    public void testLockSome() throws Exception {
        LockRequest request = new LockRequest();
        CompareFilter tFilter = filterFac.createCompareFilter(AbstractFilter.COMPARE_LESS_THAN);
        Expression nlanes = filterFac.createAttributeExpression(null, "nlanes");
        Integer testInt = new Integer(5);
        Expression testLiteral = filterFac.createLiteralExpression(testInt);
        tFilter.addLeftValue(nlanes);
        tFilter.addRightValue(testLiteral);
        request.addLock(testType, tFilter);

        String xml = LockResponse.getXmlResponse(request);
        LOGGER.fine("first response is " + xml);
        LOGGER.fine(testType + "is locked: " + repo.isLocked(testType));
        assertTrue(xml.startsWith(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<WFS_LockFeatureResponse"));
        assertTrue(repo.isLocked(testType));

        LockRequest request2 = new LockRequest();
        CompareFilter tFilter2 = filterFac.createCompareFilter(AbstractFilter.COMPARE_GREATER_THAN);
        Integer testInt2 = new Integer(3);
        Expression testLiteral2 = filterFac.createLiteralExpression(testInt2);
        tFilter2.addLeftValue(nlanes);
        tFilter2.addRightValue(testLiteral2);
        request2.addLock(testType, tFilter2);

        FidFilter fidFilter = filterFac.createFidFilter("roads.48692");
        LOGGER.fine("roads.48692 is locked :"
            + repo.isLocked(testType, fidFilter, null));
        assertTrue(!repo.isLocked(testType, fidFilter, null));

        try {
            String xml2 = LockResponse.getXmlResponse(request2);
            LOGGER.fine("2nd response is " + xml2);

            //should throw exception as all were not locked.  Or is that 
            //correct behavior?
            fail();
        } catch (WfsException e) {
            LOGGER.fine("Caught exception " + e.getMessage());
            assertTrue(e.getMessage().startsWith("all features were not"));
        }

        LOGGER.fine("roads.48692 is locked :"
            + repo.isLocked(testType, fidFilter, null));
        assertTrue(!repo.isLocked(testType, fidFilter, null));
        request2.setLockAll(false);

        String xml3 = LockResponse.getXmlResponse(request2);
        LOGGER.fine("3rd response is " + xml3);

        //assertTrue(xml3.matches(".*<FeaturesNotLocked>.*roads.48689"));
        LOGGER.fine("roads.48692 is locked :"
            + repo.isLocked(testType, fidFilter, null));
        assertTrue(repo.isLocked(testType, fidFilter, null));
    }
}
