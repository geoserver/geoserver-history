/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vfny.geoserver.responses.wms;

import junit.framework.TestCase;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.responses.wms.map.GetMapProducer;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;


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
