/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms;

import junit.framework.TestCase;

import org.vfny.geoserver.responses.wms.map.GetMapProducer;


/**
 * DOCUMENT ME!
 *
 * @author gabriel TODO To change the template for this generated type comment
 *         go to Window - Preferences - Java - Code Style - Code Templates
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
        this.getMapResponse = new GetMapResponse();
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
    public void testExecute() throws Exception{
        GetMapProducer producer;
    	producer = GetMapResponse.getDelegate(TestMapProducerFactory.TESTING_MIME_TYPE);
    	assertEquals(TestMapProducerFactory.class, producer.getClass());
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
