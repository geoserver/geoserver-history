/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import junit.framework.TestCase;



/**
 * DOCUMENT ME!
 *
 * @author gabriel 
 */
public class GetMapResponseTest extends TestCase {
    /** DOCUMENT ME!  */
    private GetMapResponse getMapResponse;

    /**
     * Constructor for GetMapResponseTest.
     *
     * @param arg0
     */
    public GetMapResponseTest(String arg0) {
        super(arg0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(GetMapResponseTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.getMapResponse = new GetMapResponse(null, null);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        this.getMapResponse = null;
        super.tearDown();
    }

    /**
     * DOCUMENT ME!
     */
    public void testGetContentType() {
        //TODO Implement getContentType().
    }

    /**
     * DOCUMENT ME!
     */
    public void testGetContentEncoding() {
        //TODO Implement getContentEncoding().
    }

    /**
     * DOCUMENT ME!
     */
    public void testAbort() {
        //TODO Implement abort().
    }

    /**
     * DOCUMENT ME!
     */
    public void testWriteTo() {
        //TODO Implement writeTo().
    }

    /**
     * DOCUMENT ME!
     */
    public void testGetMapFormats() {
        //TODO Implement getMapFormats().
    }
}
