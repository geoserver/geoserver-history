/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.filter.AbstractFilter;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.FidFilter;
import org.geotools.filter.LiteralExpression;
import org.vfny.geoserver.requests.wfs.TransactionRequest;
import org.vfny.geoserver.requests.wfs.UpdateRequest;


/**
 * Tests the Update request handling.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: UpdateSuite.java,v 1.4.2.4 2004/01/06 22:05:11 dmzwiers Exp $
 *
 * @task TODO: Bring back tests 4 and 6, they are broken due to reading of the
 *       same literals in different ways - string/double/int.
 */
public class UpdateSuite extends TransactionSuite {
    // Initializes the logger. Uncomment to see log messages.
    //static {
    //    org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", Level.FINEST);
    //}

    /**
     * Constructor with super.
     *
     * @param testName The name of the test.
     */
    public UpdateSuite(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Update tests");
        suite.addTestSuite(UpdateSuite.class);

        return suite;
    }

    public void testXml1() throws Exception {
        // make base comparison objects        
        UpdateRequest update = new UpdateRequest();
        update.setTypeName("BUILTUPA_1M");
        update.setFilter(factory.createFidFilter("10131"));
        update.addProperty("POPULATION", "4070000");

        TransactionRequest baseRequest = new TransactionRequest();
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

        TransactionRequest baseRequest = new TransactionRequest();
        baseRequest.addSubRequest(update);

        // run test       
        assertTrue(runXmlTest(baseRequest, "update2", true));
    }

    public void testXml3() throws Exception {
        // make base comparison objects        
        UpdateRequest update = new UpdateRequest();
        update.setTypeName("BUILTUPA_1M");

        FidFilter tempFilter = factory.createFidFilter("1031");
        tempFilter.addFid("34");
        tempFilter.addFid("24256");
        update.setFilter(tempFilter);
        update.addProperty("NAME", "somestring");

        UpdateRequest update2 = new UpdateRequest();
        update2.setTypeName("BUILTUPA_1M");

        CompareFilter compFilter = factory.createCompareFilter(AbstractFilter.COMPARE_GREATER_THAN);
        AttributeExpression tempLeftExp = factory.createAttributeExpression(null);
        tempLeftExp.setAttributePath("TILE_ID");

        LiteralExpression tempRightExp = factory.createLiteralExpression(1000);
        compFilter.addLeftValue(tempLeftExp);
        compFilter.addRightValue(tempRightExp);
        update2.setFilter(compFilter);
        update2.addProperty("FAC_ID", "100");

        TransactionRequest baseRequest = new TransactionRequest();
        baseRequest.addSubRequest(update);
        baseRequest.addSubRequest(update2);

        // run test       
        assertTrue(runXmlTest(baseRequest, "update3", true));
    }

    /* public void testXml4() throws Exception {
       // make base comparison objects
       UpdateRequest update = new UpdateRequest();
       update.setTypeName("TREESA_1M");
       FidFilter tempFilter = factory.createFidFilter("1010");
       update.setFilter(tempFilter);
       update.addProperty("TREETYPE", "CONIFEROUS");
       UpdateRequest update2 = new UpdateRequest();
       update2.setTypeName("OCEANSA_1M");
       CompareFilter compFilter = factory.createCompareFilter(AbstractFilter.COMPARE_GREATER_THAN);
       AttributeExpression tempLeftExp = factory.createAttributeExpression(null);
       tempLeftExp.setAttributePath("DEPTH");
       LiteralExpression tempRightExp = factory.createLiteralExpression(2400);
       compFilter.addLeftValue(tempLeftExp);
       compFilter.addRightValue(tempRightExp);
       update2.setFilter(compFilter);
       update2.addProperty("DEPTH", new Integer(2400));
       TransactionRequest baseRequest = new TransactionRequest();
       baseRequest.addSubRequest(update2);
       baseRequest.addSubRequest(update);
       baseRequest.setVersion("1.0.0");
       baseRequest.setService("WFS");
       // run test
       assertTrue(runXmlTest(baseRequest, "update4", true));
       }*/
    public void testXml5() throws Exception {
        // make base comparison objects        
        UpdateRequest update = new UpdateRequest();
        update.setTypeName("BUILTUPA_1M");
        update.setFilter(factory.createFidFilter("10131"));
        update.addProperty("POPULATION", "4070000");

        TransactionRequest baseRequest = new TransactionRequest();
        baseRequest.addSubRequest(update);

        // run test       
        assertTrue(runXmlTest(baseRequest, "update5", true));
    }

    //TODO: figure out why equals is false.

    /* public void testXml6() throws Exception {
       UpdateRequest update2 = new UpdateRequest();
       update2.setTypeName("OCEANSA_1M");
       CompareFilter compFilter = factory.createCompareFilter(AbstractFilter.COMPARE_GREATER_THAN);
       AttributeExpression tempLeftExp = factory.createAttributeExpression(null);
       tempLeftExp.setAttributePath("DEPTH");
       LiteralExpression tempRightExp = factory.createLiteralExpression(new Integer(2400));
       compFilter.addLeftValue(tempLeftExp);
       compFilter.addRightValue(tempRightExp);
       update2.setFilter(compFilter);
       update2.addProperty("DEPTH", new Integer(2400));
       update2.addProperty("TREASURE", "Booty");
       update2.setHandle("update_booty");
       TransactionRequest baseRequest = new TransactionRequest();
       baseRequest.addSubRequest(update2);
       baseRequest.setVersion("1.0.0");
       baseRequest.setService("WFS");
       baseRequest.setHandle("oceans");
       // run test
       assertTrue(runXmlTest(baseRequest, "update6", true));
       }*/
}
