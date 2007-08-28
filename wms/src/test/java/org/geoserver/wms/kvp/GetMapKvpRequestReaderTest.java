/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.kvp;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.HashMap;

import org.geoserver.data.test.MockData;
import org.geoserver.ows.Dispatcher;
import org.geoserver.test.ows.KvpRequestReaderTestSupport;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsEqualTo;
import org.vfny.geoserver.config.PaletteManager;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.servlets.GetMap;


public class GetMapKvpRequestReaderTest extends KvpRequestReaderTestSupport {
    GetMapKvpRequestReader reader;
    Dispatcher dispatcher;

    protected void setUp() throws Exception {
        super.setUp();

        GetMap getMap = (GetMap) applicationContext.getBean("wmsGetMap");
        dispatcher = (Dispatcher) applicationContext.getBean("dispatcher");
        WMS wms = (WMS) applicationContext.getBean("wms");
        reader = new GetMapKvpRequestReader(getMap, wms);
    }

    public void testCreateRequest() throws Exception {
        GetMapRequest request = (GetMapRequest) reader.createRequest();
        assertNotNull(request);
    }

    public void testReadMandatory() throws Exception {
        HashMap raw = new HashMap();
        raw.put("layers",
            MockData.BASIC_POLYGONS.getPrefix() + ":" + MockData.BASIC_POLYGONS.getLocalPart());
        raw.put("styles", MockData.BASIC_POLYGONS.getLocalPart());
        raw.put("format", "image/jpeg");
        raw.put("srs", "epsg:3003");
        raw.put("bbox", "-10,-10,10,10");
        raw.put("height", "600");
        raw.put("width", "800");

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        request = (GetMapRequest) reader.read(request, parseKvp(raw), raw);

        String layer = MockData.BASIC_POLYGONS.getLocalPart();
        assertEquals(1, request.getLayers().length);
        assertTrue(request.getLayers()[0].getName().endsWith(layer));

        assertEquals(1, request.getStyles().size());
        assertEquals(getCatalog().getStyle(layer), request.getStyles().get(0));

        assertEquals("image/jpeg", request.getFormat());
        assertEquals(600, request.getHeight());
        assertEquals(800, request.getWidth());

        assertNotNull(request.getBbox());
        assertEquals(-10d, request.getBbox().getMinX(), 0);
        assertEquals(-10d, request.getBbox().getMinY(), 0);
        assertEquals(10d, request.getBbox().getMaxX(), 0);
        assertEquals(10d, request.getBbox().getMaxY(), 0);

        assertEquals("epsg:3003", request.getSRS());
    }

    public void testReadOptional() throws Exception {
        HashMap kvp = new HashMap();
        kvp.put("bgcolor", "000000");
        kvp.put("transparent", "true");
        kvp.put("tiled", "true");
        kvp.put("tilesorigin", "1.2,3.4");
        kvp.put("buffer", "1");
        kvp.put("palette", "SAFE");
        kvp.put("time", "2");
        kvp.put("elevation", "4");

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        request = (GetMapRequest) reader.read(request, parseKvp(kvp), kvp);

        assertEquals(Color.BLACK, request.getBgColor());
        assertTrue(request.isTransparent());
        assertTrue(request.isTiled());

        assertEquals(new Point2D.Double(1.2, 3.4), request.getTilesOrigin());
        assertEquals(1, request.getBuffer());

        assertEquals(PaletteManager.safePalette, request.getPalette().getIcm());
        assertEquals(new Integer(2), request.getTime());
        assertEquals(new Integer(4), request.getElevation());
    }

    public void testFilter() throws Exception {
        HashMap kvp = new HashMap();
        kvp.put("filter", "<Filter><FeatureId id=\"foo\"/></Filter>");

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        request = (GetMapRequest) reader.read(request, parseKvp(kvp), kvp);

        assertNotNull(request.getFilter());
        assertEquals(1, request.getFilter().size());

        Id fid = (Id) request.getFilter().get(0);
        assertEquals(1, fid.getIDs().size());

        assertEquals("foo", fid.getIDs().iterator().next());
    }

    public void testCQLFilter() throws Exception {
        HashMap kvp = new HashMap();
        kvp.put("cql_filter", "foo = bar");

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        request = (GetMapRequest) reader.read(request, parseKvp(kvp), kvp);

        assertNotNull(request.getCQLFilter());
        assertEquals(1, request.getCQLFilter().size());

        PropertyIsEqualTo filter = (PropertyIsEqualTo) request.getCQLFilter().get(0);
    }

    public void testFeatureId() throws Exception {
        HashMap kvp = new HashMap();
        kvp.put("featureid", "foo");

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        request = (GetMapRequest) reader.read(request, parseKvp(kvp), kvp);

        assertNotNull(request.getFeatureId());
        assertEquals(1, request.getFeatureId().size());

        assertEquals("foo", request.getFeatureId().get(0));
    }
    
    public void testSld() throws Exception {
        HashMap kvp = new HashMap();
        URL url = MockData.class.getResource("BasicPolygons.sld");
        kvp.put("sld", url.toString());
        kvp.put("layers", MockData.BASIC_POLYGONS.getPrefix() + ":" + MockData.BASIC_POLYGONS.getLocalPart());

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        request = (GetMapRequest) reader.read(request, parseKvp(kvp), kvp);

        assertNotNull(request.getSld());
        assertEquals(url, request.getSld());
    }
    
    /**
     * One of the cite tests ensures that WMTVER is recognized as VERSION and the server does 
     * not complain
     * @throws Exception 
     */
    public void testWmtVer() throws Exception {
        dispatcher.setCiteCompliant(true);
        String request = "wms?SERVICE=WMS&&WiDtH=200&FoRmAt=image/png&LaYeRs=cite:Lakes&StYlEs=&BbOx=0,-0.0020,0.0040,0&ReQuEsT=GetMap&HeIgHt=100&SrS=EPSG:4326&WmTvEr=1.1.1";
        assertEquals("image/png", getAsServletResponse(request).getContentType());
    }
}
