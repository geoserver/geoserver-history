/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.responses;

//import java.io.*;
//import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.Filter;
import org.geotools.filter.FidFilter;
import org.geotools.resources.Geotools;
import org.vfny.geoserver.config.TypeRepository;
import org.vfny.geoserver.config.ConfigInfo;
import org.vfny.geoserver.requests.TransactionRequest;
import org.vfny.geoserver.requests.SubTransactionRequest;
import org.vfny.geoserver.requests.DeleteRequest;
import org.vfny.geoserver.requests.UpdateRequest;
import org.vfny.geoserver.requests.InsertRequest;

/**
 * Tests the Transaction responses.
 *
 * @author Chris Holmes, TOPP
 * @version $VERSION$
 */
public class TransactionSuite extends TestCase {

    /* Initializes the logger. */
    static {
        Geotools.init("Log4JFormatter", Level.INFO);
    }
    
    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.responses");

    /** Unit test data directory */
    private static final String CONFIG_DIR = 
        System.getProperty("user.dir") + "/misc/documents/configuration.xml";

    /** Unit test data directory */
    private static final String TYPE_DIR = 
        System.getProperty("user.dir") + "/misc/testData/featureTypes";


    /** Holds mappings between HTTP and ASCII encodings */
    private static FilterFactory factory = FilterFactory.createFilterFactory();

    private ConfigInfo config;
    private TypeRepository repo;


    /** Constructor with super. */
    public TransactionSuite (String testName) { super(testName); }


    /** Handles test set up details. */
    public void setUp() {
        config = ConfigInfo.getInstance(CONFIG_DIR);
        config.setTypeDir(TYPE_DIR);
        repo = TypeRepository.getInstance();
    }

    /*************************************************************************
     * XML TESTS                                                             *
     *************************************************************************
     * XML GetFeature parsing tests.  Each test reads from a specific XML    *
     * file and compares it to the base request defined in the test itself.  *
     * Tests are run via the static methods in this suite.  The tests        *
     * themselves are quite generic, so documentation is minimal.            *
     *************************************************************************/
    public void test1() throws Exception {        
        // make base comparison objects
        
        TransactionRequest request = new TransactionRequest();
	request.setHandle("test1");
        DeleteRequest delete = new DeleteRequest();
	delete.setFilter(factory.createFidFilter("5"));
	delete.setTypeName("geom_test");
	delete.setHandle("test delete");
	request.addSubRequest(delete);
	
        String response = TransactionResponse.getXmlResponse(request);
        LOGGER.fine("Our response is " + response);
        
	
	// run test
        //assertTrue(runXmlTest(baseRequest, "10", true));
    }

    public void testNoInfo() {
	     TransactionRequest request = new TransactionRequest();
	     request.setHandle("no info");
        DeleteRequest delete = new DeleteRequest();
	delete.setFilter(factory.createFidFilter("33"));
	delete.setTypeName("junk");
	delete.setHandle("test junk");
	request.addSubRequest(delete);
		try {
        String response = TransactionResponse.getXmlResponse(request);
        LOGGER.fine("Our response is " + response);
        } catch (WfsTransactionException e) {
	    LOGGER.fine("Response is " + e.getXmlResponse());
	}
    }


    public void testNoTable() throws Exception {
	     TransactionRequest request = new TransactionRequest();
	     request.setHandle("no table");
        DeleteRequest delete = new DeleteRequest();
	delete.setFilter(factory.createFidFilter("33"));
	delete.setTypeName("no_table");
	delete.setHandle("test this");
	request.addSubRequest(delete);
	try {
        String response = TransactionResponse.getXmlResponse(request);
        LOGGER.fine("Our response is " + response);
        } catch (WfsTransactionException e) {
	    LOGGER.fine("Response is " + e.getXmlResponse());
	}
        
    }
    

}
