/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.responses;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.Filter;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.FidFilter;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.DefaultExpression;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.LiteralExpression;
import org.geotools.resources.Log4JFormatter;
import org.vfny.geoserver.requests.TransactionRequest;
import org.vfny.geoserver.requests.SubTransactionRequest;
import org.vfny.geoserver.requests.DeleteRequest;
import org.vfny.geoserver.config.TypeRepository;
import org.vfny.geoserver.config.ConfigInfo;
import org.vfny.geoserver.requests.LockRequest;
import org.vfny.geoserver.requests.Query;

/**
 * Tests the get feature request handling.
 *
 * @author Chris Holmes, TOPP
 * @version $VERSION$
 */
public class LockSuite extends TestCase {

    /* Initializes the logger. */
    static {
	Log4JFormatter.init("org.geotools", Level.FINER);
        Log4JFormatter.init("org.vfny.geoserver", Level.FINER);
    }
    
    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.requests");

    /** Unit test data directory */
    private static final String CONFIG_DIR = 
        System.getProperty("user.dir") + "/misc/testData/configuration.xml";

    /** Unit test data directory */
    private static final String TYPE_DIR = 
        System.getProperty("user.dir") + "/misc/testData/featureTypes";


    private String testType = "rail";

    /** Holds mappings between HTTP and ASCII encodings */
    private static FilterFactory factory = FilterFactory.createFilterFactory();

    private ConfigInfo config;
    private TypeRepository repo;


    /** Constructor with super. */
    public LockSuite (String testName) { super(testName); }


    /** Handles test set up details. */
    public void setUp() {
        config = ConfigInfo.getInstance(CONFIG_DIR);
        config.setTypeDir(TYPE_DIR);
        repo = TypeRepository.getInstance();
	repo.clearLocks();
    }

    /*************************************************************************
     * XML TESTS                                                             *
     *************************************************************************
     * XML GetLock parsing tests.  Each test reads from a specific XML    *
     * file and compares it to the base request defined in the test itself.  *
     * Tests are run via the static methods in this suite.  The tests        *
     * themselves are quite generic, so documentation is minimal.            *
     *************************************************************************/
    //TODO: redo all these tests.  Need a backend, as feature level locking
    //means we need some features to work with.  After shapefile support is
    //in we should be good.
    /*    public void testLock() throws Exception {        
        LockRequest request = new LockRequest();
        request.addLock(testType, null);       
        String xml = LockResponse.getXmlResponse(request);
        LOGGER.fine(xml);
	LOGGER.fine("is locked: " + repo.isLocked(testType)); 
	//probably bad to use this as the formatting could change, but
	//that shouldn't really affect the unit test.
	assertTrue(xml.startsWith("<?xml version=\"1.0\" ?>\n" + 
				  "<WFS_LockFeatureResponse"));
	assertTrue(repo.isLocked(testType));
    }

    public void testTwoLocks() throws WfsException {
	LockRequest request = new LockRequest();
        request.addLock(testType, null);     
	String testType2 = "roads";
	request.addLock(testType2, null);
	String xml = LockResponse.getXmlResponse(request);
        LOGGER.fine(xml);
	LOGGER.fine(testType + "is locked: " + repo.isLocked(testType) + ", " +
		    testType2 + "is locked: " + repo.isLocked(testType2)); 
	assertTrue(xml.startsWith("<?xml version=\"1.0\" ?>\n" + 
				  "<WFS_LockFeatureResponse"));
	assertTrue(repo.isLocked(testType));
	assertTrue(repo.isLocked(testType2));
	}*/

    //public void testUnlock(

    /* Need a real db for this to work properly.
    public void testTransaction() throws Exception {
	String lockId = repo.lock(testType);
	TransactionRequest baseRequest = new TransactionRequest();
        DeleteRequest internalRequest = new DeleteRequest();
        internalRequest.setTypeName(testType);
        FidFilter filter = factory.createFidFilter("123");
        internalRequest.setFilter(filter);
        baseRequest.addSubRequest(internalRequest);
	String failLockMsg = "Feature Type: " + testType + " is locked";
	try {
	    TransactionResponse.getXmlResponse(baseRequest);
	    //should never reach here, as it should always throw exception
	    //for not locking right.
	    fail();
	} catch (WfsTransactionException e) {
	    //and message should match the failLock message.
	    assertEquals(failLockMsg, e.getMessage());
	    LOGGER.fine(e.getMessage());
	}

	
	baseRequest.setLockId(lockId);
	try {
	    //this probably will not go through, as db info is not set up right
	    LOGGER.fine(TransactionResponse.getXmlResponse(baseRequest));
	} catch (WfsTransactionException e) {
	    //but the message should not be the failLock message.
	    LOGGER.fine("caught transaction exception: " + e);
	    assertTrue(!(e.getMessage().equals(failLockMsg)));
	}
	}*/
}
