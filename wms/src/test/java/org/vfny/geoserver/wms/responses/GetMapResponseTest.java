/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.awt.geom.Point2D;
import java.util.Collections;

import junit.framework.Test;

import org.easymock.EasyMock;
import org.geoserver.data.test.MockData;
import org.geoserver.ows.adapters.ResponseAdapter;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.WMSTestSupport;
import org.springframework.context.ApplicationContext;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.RasterMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.map.metatile.MetatileMapProducer;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Unit test for {@link GetMapResponse}
 * <p>
 * Trying to mock up collaborators lead to 3 direct ones needing to be mocked
 * up, plus {@link WMS} obtained from this test super class. Smells like too
 * much coupling to me, though I'm not going to change that until we have a plan
 * to get rid of the old {@link Response} stuff in favor of the new dispatching
 * system, for which GetMapResponse is being adapted right now ({@link ResponseAdapter})
 * </p>
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 * @source $URL:
 *         https://svn.codehaus.org/geoserver/branches/1.7.x/geoserver/wms/src/test/java/org/vfny/geoserver/wms/responses/GetMapResponseTest.java $
 */
public class GetMapResponseTest extends WMSTestSupport {
    final String mockMapFormat = "mockMapFormat";

    private GetMapResponse response;

    ApplicationContext mockContext;

    RasterMapProducer mockProducer;

    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new GetMapResponseTest());
    }

    /**
     * Per method setup (fixture can be stored in non static fields)
     * 
     * @throws Exception
     */
    @Override
    protected void setUpInternal() throws Exception {
    }

    /**
     * Per method tear down
     * 
     * @throws Exception
     */
    @Override
    protected void tearDownInternal() throws Exception {
    }

    public void testExecuteOutputFormat() {
        response = new GetMapResponse(getWMS(), super.applicationContext);
        GetMapRequest request;
        request = new GetMapRequest(getWMS());
        request.setFormat("non-existent-output-format");
        try {
            response.execute(request);
            fail("Asked for a non existent format, expected ServiceException");
        } catch (ServiceException e) {
            assertTrue(true);
        }
    }

    /**
     * Test method for
     * {@link GetMapResponse#execute(org.vfny.geoserver.Request)}.
     */
    public void testExecuteWrongOutputFormat() {
        response = new GetMapResponse(getWMS(), super.applicationContext);
        GetMapRequest request;
        request = new GetMapRequest(getWMS());
        request.setFormat("non-existent-output-format");
        try {
            response.execute(request);
            fail("Asked for a non existent format, expected ServiceException");
        } catch (ServiceException e) {
            assertTrue(true);
        }
    }

    /**
     * Sets up the mocked up collaborators for an GetMapResponse.execute call.
     * Note the use of EasyMock.expect(...).anyTimes(). Depending on whether a
     * single test case or the whole test suite is being ran, since the number
     * of MapProduces factories returned will differ in both cases when
     * GeoServerExtensions.extensions(MapProducerFactorySpi.class) is called by
     * GetMapResponse.execute
     */
    private void setUpMocksForExecute() {
        final WMS wms = getWMS();

        mockContext = EasyMock.createMock(ApplicationContext.class);
        mockProducer = EasyMock.createNiceMock(RasterMapProducer.class);

        EasyMock.expect(mockContext.getBeanNamesForType(EasyMock.eq(GetMapProducer.class)))
                .andReturn(new String[] { "fakeMapProducer" }).anyTimes();

        EasyMock.expect(mockContext.getBean((String) EasyMock.notNull())).andReturn(mockProducer)
                .anyTimes();

        EasyMock.expect(mockProducer.getOutputFormatNames()).andReturn(
                Collections.singleton(mockMapFormat));

        mockProducer.setMapContext((WMSMapContext) EasyMock.notNull());

        EasyMock.replay(mockContext);
        EasyMock.replay(mockProducer);
    }

    /**
     * Test method for
     * {@link GetMapResponse#execute(org.vfny.geoserver.Request)}.
     */
    public void testExecuteNullExtent() {
        setUpMocksForExecute();
        response = new GetMapResponse(getWMS(), mockContext);
        GetMapRequest request = createGetMapRequest(MockData.BASIC_POLYGONS);
        request.setFormat(mockMapFormat);
        request.setBbox(null);
        try {
            response.execute(request);
            fail("request provided null envelope, expected ServiceException");
        } catch (ServiceException e) {
            assertTrue(true);
        }

        EasyMock.verify(mockContext);
        EasyMock.verify(mockProducer);
    }

    public void testExecuteEmptyExtent() {
        setUpMocksForExecute();
        response = new GetMapResponse(getWMS(), mockContext);
        GetMapRequest request = createGetMapRequest(MockData.BASIC_POLYGONS);
        request.setFormat(mockMapFormat);
        request.setBbox(new Envelope());
        try {
            response.execute(request);
            fail("request provided empty envelope, expected ServiceException");
        } catch (ServiceException e) {
            assertTrue(true);
        }
        EasyMock.verify(mockContext);
        EasyMock.verify(mockProducer);
    }

    public void testExecuteTilingRequested() {
        setUpMocksForExecute();
        response = new GetMapResponse(getWMS(), mockContext);

        GetMapRequest request = createGetMapRequest(MockData.BASIC_POLYGONS);
        request.setFormat(mockMapFormat);
        request.setBbox(new Envelope(-180, -90, 180, 90));
        // request tiling
        request.setTiled(true);
        request.setTilesOrigin(new Point2D.Double(0, 0));
        request.setWidth(256);
        request.setHeight(256);

        try {
            response.execute(request);
        } catch (RuntimeException e) {
            // let execute crash, we're only interested in the delegate
            assertTrue(true);
        }
        GetMapProducer delegate = response.getDelegate();
        assertTrue(delegate instanceof MetatileMapProducer);

        EasyMock.verify(mockContext);
        EasyMock.verify(mockProducer);
    }
}
