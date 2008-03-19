/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import org.geoserver.data.test.MockData;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.servlets.WMService;

import com.vividsolutions.jts.geom.Envelope;

public class DefaultWebMapServiceTest extends WMSTestSupport {
    
    /** 
     * This is just a very basic test, mostly testing defaults
     * 
     * @throws Exception
     */
    public void test1() throws Exception {
        GetMapRequest mockGMR = createGetMapRequest(MockData.BASIC_POLYGONS);
        WMService wmsHandle = mockGMR.getWMService();

        /* Create a request */
        GetMapRequest request = new GetMapRequest(wmsHandle);

        /* Create the reflector */
        DefaultWebMapService reflector = new DefaultWebMapService();

        /* Run the reflector */
        reflector.getMap = request;
        request.setLayers(mockGMR.getLayers());
        request.setFormat(DefaultWebMapService.FORMAT);
        reflector.autoSetBoundsAndSize();
        
        CoordinateReferenceSystem crs = reflector.getMap.getCrs();
        String srs = reflector.getMap.getSRS();
        Envelope bbox = reflector.getMap.getBbox();
        String format = reflector.getMap.getFormat();
        int width = reflector.getMap.getWidth();
        int height = reflector.getMap.getHeight();
        
        String crsString = crs.getName().toString();
        assertTrue("EPSG:WGS 84".equalsIgnoreCase(crsString));
        assertTrue("EPSG:4326".equalsIgnoreCase(srs));
        //mockGMR.getBbox() actually returns (-180 , 90 , -90 , 180 ) <- foo
        assertTrue(bbox.getMinX() == -180.0 && bbox.getMaxX() == 180.0
                && bbox.getMinY() == -90.0 && bbox.getMaxY() == 90.0);
        assertEquals("image/png",format);
        assertEquals(width, 512);
        assertEquals(height, 256);
    }
    
    /**
     * Tests basic reprojection
     * 
     * @throws Exception
     */
    public void test2() throws Exception {
        GetMapRequest mockGMR = createGetMapRequest(MockData.BASIC_POLYGONS);
        WMService wmsHandle = mockGMR.getWMService();

        /* Create a request */
        GetMapRequest request = new GetMapRequest(wmsHandle);

        /* Create the reflector */
        DefaultWebMapService reflector = new DefaultWebMapService();

        /* Run the reflector */
        reflector.getMap = request;
        reflector.getMap.setSRS("EPSG:41001");
        reflector.getMap.setCrs(CRS.decode("EPSG:41001"));
        request.setLayers(mockGMR.getLayers());
        request.setFormat("image/gif");
        reflector.autoSetBoundsAndSize();
        
        CoordinateReferenceSystem crs = reflector.getMap.getCrs();
        String srs = reflector.getMap.getSRS();
        Envelope bbox = reflector.getMap.getBbox();
        String format = reflector.getMap.getFormat();
        int width = reflector.getMap.getWidth();
        int height = reflector.getMap.getHeight();
        
        String crsString = crs.getName().toString();
        assertTrue("WGS84 / Simple Mercator".equalsIgnoreCase(crsString));
        assertTrue("EPSG:41001".equalsIgnoreCase(srs));
        //mockGMR.getBbox() actually returns (-180 , 90 , -90 , 180 ) <- foo
        assertTrue(
                Math.abs(bbox.getMinX() + 1.9236008009077676E7) < 1E-4
                && Math.abs(bbox.getMinY() + 2.2026354993694823E7) < 1E-4
                && Math.abs(bbox.getMaxX() - 1.9236008009077676E7) < 1E-4
                && Math.abs(bbox.getMaxY() - 2.2026354993694823E7) < 1E-4 );
        assertEquals("image/gif",format);
        assertEquals(width, 512);
        assertEquals(height, 586);
    }
    
    /**
     * This test is incomplete because I (arneke) had trouble finding
     * mock data with proper bounding boxes
     * 
     * @throws Exception
     */
    public void test3() throws Exception {
        GetMapRequest mockStreams = createGetMapRequest(MockData.BRIDGES);
        GetMapRequest mockBridges = createGetMapRequest(MockData.STREAMS);
        WMService wmsHandle = mockStreams.getWMService();
        
        MapLayerInfo[] mls = new MapLayerInfo[2];
        mls[0] = mockBridges.getLayers()[0];
        mls[1] = mockStreams.getLayers()[0];
        
        /* Create a request */
        GetMapRequest request = new GetMapRequest(wmsHandle);

        /* Create the reflector */
        DefaultWebMapService reflector = new DefaultWebMapService();

        /* Run the reflector */
        reflector.getMap = request;
        reflector.getMap.setSRS("EPSG:41001");
        reflector.getMap.setCrs(CRS.decode("EPSG:41001"));
        request.setLayers(mls);
        request.setFormat("image/gif");
        reflector.autoSetBoundsAndSize();
        
        CoordinateReferenceSystem crs = reflector.getMap.getCrs();
        String srs = reflector.getMap.getSRS();
        Envelope bbox = reflector.getMap.getBbox();
        String format = reflector.getMap.getFormat();
        int width = reflector.getMap.getWidth();
        int height = reflector.getMap.getHeight();
        
        String crsString = crs.getName().toString();
        assertTrue("WGS84 / Simple Mercator".equalsIgnoreCase(crsString));
        assertTrue("EPSG:41001".equalsIgnoreCase(srs));
        assertTrue(
                Math.abs(bbox.getMinX() + 1.9236008009077676E7) < 1E-4
                && Math.abs(bbox.getMinY() + 2.2026354993694823E7) < 1E-4
                && Math.abs(bbox.getMaxX() - 1.9236008009077676E7) < 1E-4
                && Math.abs(bbox.getMaxY() - 2.2026354993694823E7) < 1E-4 );
        assertEquals("image/gif",format);
        assertEquals(width, 512);
        assertEquals(height, 586);
    }
    
}
