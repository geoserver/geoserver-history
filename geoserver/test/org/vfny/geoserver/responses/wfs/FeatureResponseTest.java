/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataTestCase;
import org.geotools.data.DefaultRepository;
import org.geotools.data.Repository;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.wfs.requests.FeatureRequest;
import org.vfny.geoserver.wfs.responses.FeatureResponse;


/**
 * This is my attempt at testing FeatureResponse using normal JUnit tests.
 * 
 * <p>
 * Due to the interaction with ModelConfig.getInstance() this may not be
 * possible.
 * </p>
 * 
 * <p></p>
 *
 * @author jgarnett
 */
public class FeatureResponseTest extends DataTestCase {
    Data config;
    FeatureResponse response;

    /**
     * Constructor for FeatureResponseTest.
     *
     * @param arg0
     */
    public FeatureResponseTest(String arg0) {
        super(arg0);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        response = new FeatureResponse();

        Repository cat = new DefaultRepository();
        Map config = new HashMap();

        //GeoServer.load( config, cat );      
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFeatureResponse() {
        FeatureRequest request = new FeatureRequest();
        request.setHandle("FeatureRequest");
    }

    public void testGetContentType() {
    }

    public void testWriteTo() {
    }

    /*
     * Test for void execute(Request)
     */
    public void testExecuteRequest() {
    }

    /*
     * Test for void execute(FeatureRequest)
     */
    public void testExecuteFeatureRequest() {
    }

    public void testAbort() {
    }
}
