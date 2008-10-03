/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;

import junit.framework.TestCase;

import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.ows.adapters.ResponseAdapter;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.WMSMockData;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.map.metatile.MetatileMapProducer;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;

/**
 * Unit test for {@link GetMapResponse}
 * <p>
 * Trying to mock up collaborators lead to 3 direct ones needing to be mocked up, plus {@link WMS}
 * obtained from this test super class. Smells like too much coupling to me, though I'm not going to
 * change that until we have a plan to get rid of the old {@link Response} stuff in favor of the new
 * dispatching system, for which GetMapResponse is being adapted right now ({@link ResponseAdapter})
 * </p>
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 * @source $URL:
 *         https://svn.codehaus.org/geoserver/branches/1.7.x/geoserver/wms/src/test/java/org/vfny/geoserver/wms/responses/GetMapResponseTest.java $
 */
public class GetMapResponseTest extends TestCase {

    private WMSMockData mockData;

    private GetMapRequest request;

    private GetMapResponse response;

    @Override
    protected void setUp() throws Exception {
        GetMapResponse.LOGGER.setLevel(Level.FINEST);
        mockData = new WMSMockData();
        mockData.setUp();

        request = mockData.createRequest();
        response = mockData.createResponse();
    }

    @Override
    protected void tearDown() throws Exception {
        GetMapResponse.LOGGER.setLevel(Level.INFO);
    }

    public void testConstructor() {
        try {
            new GetMapResponse(null);
            fail("should fail on null list of available producers");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        try {
            Collection<GetMapProducer> producers = Collections.emptyList();
            new GetMapResponse(producers);
            fail("should fail on empty list of available producers");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Test method for {@link GetMapResponse#execute(org.vfny.geoserver.Request)}.
     */
    public void testDelegateLookup() {
        request.setFormat("non-existent-output-format");
        try {
            response.execute(request);
            fail("Asked for a non existent format, expected ServiceException");
        } catch (WmsException e) {
            assertEquals("InvalidFormat", e.getCode());
        }

        GetMapProducer producer = new WMSMockData.DummyRasterMapProducer();
        response = new GetMapResponse(Collections.singleton(producer));
        request.setFormat(WMSMockData.DummyRasterMapProducer.MIME_TYPE);
        try {
            response.execute(request);
            fail("should have failed by any reason, we just want to check it looked up the map producer");
        } catch (Exception e) {
            assertTrue(true);
        }
        GetMapProducer delegate = response.getDelegate();
        assertSame(producer, delegate);
    }

    /**
     * Test method for {@link GetMapResponse#execute(org.vfny.geoserver.Request)}.
     */
    public void testExecuteNullExtent() {
        request.setBbox(null);
        try {
            response.execute(request);
            fail("request provided null envelope, expected ServiceException");
        } catch (ServiceException e) {
            assertEquals("MissingBBox", e.getCode());
        }
    }

    public void testExecuteEmptyExtent() {
        request.setBbox(new Envelope());
        try {
            response.execute(request);
            fail("request provided empty envelope, expected ServiceException");
        } catch (ServiceException e) {
            assertTrue(true);
        }
    }

    public void testExecuteTilingRequested() {
        request.setBbox(new Envelope(-180, -90, 180, 90));
        // request tiling
        request.setTiled(true);
        request.setTilesOrigin(new Point2D.Double(0, 0));
        request.setWidth(256);
        request.setHeight(256);

        try {
            response.execute(request);
            fail("Expected failure");
        } catch (RuntimeException e) {
            // let execute crash, we're only interested in the delegate
            assertTrue(true);
        }
        GetMapProducer delegate = response.getDelegate();
        assertTrue(delegate instanceof MetatileMapProducer);
    }

    public void testSingleVectorLayer() throws IOException {
        MapLayerInfo layer = mockData.addFeatureTypeLayer("testType", Point.class);
        request.setLayers(new MapLayerInfo[] { layer });

        response.execute(request);
    }
}
