/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import junit.framework.TestCase;
import org.geotools.filter.FilterFactory;
import org.vfny.geoserver.config.ConfigInfo;
import org.vfny.geoserver.config.TypeRepository;
import org.vfny.geoserver.requests.DeleteRequest;
import org.vfny.geoserver.requests.TransactionRequest;
import java.util.logging.Logger;


/**
 * Tests the Transaction responses.  Currently brittle, as it breaks if users
 * move or change road in their main type directory. See notes for
 * FeatureSuite in this package.  Would be nice to  provide a framework to
 * test any transaction datasource that might be plugged into GeoServer.  This
 * is probably worth some good  devel time, to make sure that contributed
 * datasources cover  everything that they need to, without having to rely on
 * them  loading up all the cite data.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: TransactionSuite.java,v 1.4 2003/09/16 16:13:20 cholmesny Exp $
 */
public class TransactionSuite extends TestCase {
    //Initializes the logger. Uncomment to see log messages.
    //static {
    //org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", 
    //java.util.logging.Level.FINE);
    //}

    /** Class logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");

    /** Unit test data directory */
    private static final String CONFIG_DIR = System.getProperty("user.dir")
        + "/misc/unit/config/";

    /** Unit test data directory */
    private static final String TYPE_DIR = System.getProperty("user.dir")
        + "/misc/data/featureTypes";

    /** Holds mappings between HTTP and ASCII encodings */
    private static FilterFactory factory = FilterFactory.createFilterFactory();
    private ConfigInfo config;
    private TypeRepository repo;

    /**
     * Constructor with super.
     *
     * @param testName The name of the test.
     */
    public TransactionSuite(String testName) {
        super(testName);
    }

    /**
     * Handles test set up details.
     */
    public void setUp() {
        config = ConfigInfo.getInstance(CONFIG_DIR);
        config.setTypeDir(TYPE_DIR);
        repo = TypeRepository.getInstance();
    }

    //Need a public postgis database to test against as things stand right
    //now.  Or figure out a way to get responses without actually
    //doing a delete?

    /*    public void test1() throws Exception {
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
    
       }*/
    public void testNoInfo() throws Exception {
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
        } catch (WfsException e) {
            LOGGER.fine("Response is " + e.getXmlResponse());
        }
    }

    public void testNoTransactionSupport() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setHandle("shape-test");

        DeleteRequest delete = new DeleteRequest();
        delete.setFilter(factory.createFidFilter("33"));
        delete.setTypeName("road");
        delete.setHandle("test this");
        request.addSubRequest(delete);

        try {
            String response = TransactionResponse.getXmlResponse(request);
            LOGGER.fine("Our response is " + response);
            fail();
        } catch (WfsTransactionException e) {
            LOGGER.fine("Response is " + e.getXmlResponse());
        }
    }
}
