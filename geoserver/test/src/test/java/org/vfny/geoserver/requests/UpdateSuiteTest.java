/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.geotools.filter.FidFilter;
import org.vfny.geoserver.wfs.requests.TransactionRequest;
import org.vfny.geoserver.wfs.requests.UpdateRequest;


/**
 * Tests the Update request handling.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: UpdateSuite.java,v 1.7 2004/01/31 00:17:52 jive Exp $
 *
 * @task TODO: Bring back tests 4 and 6, they are broken due to reading of the
 *       same literals in different ways - string/double/int.
 */
public class UpdateSuiteTest extends TransactionSuiteTest {
    // Initializes the logger. Uncomment to see log messages.
    //static {
    //    org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", Level.FINEST);
    //}

    /**
     * Constructor with super.
     *
     * @param testName The name of the test.
     */
    public UpdateSuiteTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Update tests");
        suite.addTestSuite(UpdateSuiteTest.class);

        return suite;
    }

    public void testXml1() throws Exception {
        // make base comparison objects        
        UpdateRequest update = new UpdateRequest();
        update.setTypeName("BUILTUPA_1M");
        update.setFilter(factory.createFidFilter("10131"));
        update.addProperty("POPULATION", "4070000");

        TransactionRequest baseRequest = new TransactionRequest(service);
        baseRequest.addSubRequest(update);

        // run test       
        assertTrue(runXmlTest(baseRequest, "update1", true));
    }

    public void testXml2() throws Exception {
        // make base comparison objects        
        UpdateRequest update = new UpdateRequest();
        update.setTypeName("BUILTUPA_1M");

        FidFilter tempFilter = factory.createFidFilter("1031");
        tempFilter.addFid("34");
        tempFilter.addFid("24256");
        update.setFilter(tempFilter);
        update.addProperty("POPULATION_TYPE", "CITY");

        TransactionRequest baseRequest = new TransactionRequest(service);
        baseRequest.addSubRequest(update);

        // run test       
        assertTrue(runXmlTest(baseRequest, "update2", true));
    }

    public void testXml5() throws Exception {
        // make base comparison objects        
        UpdateRequest update = new UpdateRequest();
        update.setTypeName("BUILTUPA_1M");
        update.setFilter(factory.createFidFilter("10131"));
        update.addProperty("POPULATION", "4070000");

        TransactionRequest baseRequest = new TransactionRequest(service);
        baseRequest.addSubRequest(update);

        // run test       
        assertTrue(runXmlTest(baseRequest, "update5", true));
    }
}
