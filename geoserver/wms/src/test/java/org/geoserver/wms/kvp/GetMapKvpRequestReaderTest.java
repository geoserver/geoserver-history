/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.kvp;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;

import org.geoserver.data.test.MockData;
import org.geoserver.ows.Dispatcher;
import org.geoserver.test.ows.KvpRequestReaderTestSupport;
import org.geoserver.wms.RemoteOWSTestSupport;
import org.geotools.styling.Style;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsEqualTo;
import org.vfny.geoserver.config.PaletteManager;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;


public class GetMapKvpRequestReaderTest extends KvpRequestReaderTestSupport {
    GetMapKvpRequestReader reader;
    Dispatcher dispatcher;
    
    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new GetMapKvpRequestReaderTest());
    }

    protected void setUpInternal() throws Exception {
        super.setUpInternal();

        dispatcher = (Dispatcher) applicationContext.getBean("dispatcher");
        WMS wms = (WMS) applicationContext.getBean("wms");
        reader = new GetMapKvpRequestReader(wms);
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
        kvp.put("time", "2006-02-27T22:08:12Z");
        kvp.put("elevation", "4");

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        request = (GetMapRequest) reader.read(request, parseKvp(kvp), kvp);

        assertEquals(Color.BLACK, request.getBgColor());
        assertTrue(request.isTransparent());
        assertTrue(request.isTiled());

        assertEquals(new Point2D.Double(1.2, 3.4), request.getTilesOrigin());
        assertEquals(1, request.getBuffer());

        assertEquals(PaletteManager.safePalette, request.getPalette().getIcm());
        assertEquals(new Integer(4), request.getElevation());
        
        Calendar cal = Calendar.getInstance();
        cal.set(2006, 1, 27, 22, 8, 12);
        List times = request.getTime();
        assertEquals(1, request.getTime().size());
        assertEquals(cal.getTime().toString(), times.get(0).toString());
    }
    
    public void testDefaultStyle() throws Exception {
        HashMap raw = new HashMap();
        raw.put("layers",
            MockData.BASIC_POLYGONS.getPrefix() + ":" + MockData.BASIC_POLYGONS.getLocalPart() + "," +
            MockData.BUILDINGS.getPrefix() + ":" + MockData.BUILDINGS.getLocalPart());
        raw.put("styles", ",");
        raw.put("format", "image/jpeg");
        raw.put("srs", "epsg:3003");
        raw.put("bbox", "-10,-10,10,10");
        raw.put("height", "600");
        raw.put("width", "800");

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        request = (GetMapRequest) reader.read(request, parseKvp(raw), raw);
        assertEquals(2, request.getStyles().size());
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
    
    public void testSldNoDefault() throws Exception {
        // no style name, no default, we should fall back on the server default
        HashMap kvp = new HashMap();
        URL url = GetMapKvpRequestReader.class.getResource("BasicPolygonsLibraryNoDefault.sld");
        kvp.put("sld", url.toString());
        kvp.put("layers", MockData.BASIC_POLYGONS.getPrefix() + ":" + MockData.BASIC_POLYGONS.getLocalPart());

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        reader.setLaxStyleMatchAllowed(false);
        request = (GetMapRequest) reader.read(request, parseKvp(kvp), kvp);

        assertNotNull(request.getSld());
        assertEquals(url, request.getSld());
        final Style style = (Style) request.getStyles().get(0);
        assertNotNull(style);
        assertEquals("BasicPolygons", style.getName());
    }
    
    public void testSldDefault() throws Exception {
        // no style name, but the sld has a default for that layer
        HashMap kvp = new HashMap();
        URL url = GetMapKvpRequestReader.class.getResource("BasicPolygonsLibraryDefault.sld");
        kvp.put("sld", url.toString());
        kvp.put("layers", MockData.BASIC_POLYGONS.getPrefix() + ":" + MockData.BASIC_POLYGONS.getLocalPart());

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        request = (GetMapRequest) reader.read(request, parseKvp(kvp), kvp);

        assertNotNull(request.getSld());
        assertEquals(url, request.getSld());
        final Style style = (Style) request.getStyles().get(0);
        assertNotNull(style);
        assertEquals("TheLibraryModeStyle", style.getName());
    }
    
    public void testSldNamed() throws Exception {
        // style name matching one in the sld
        HashMap kvp = new HashMap();
        URL url = GetMapKvpRequestReader.class.getResource("BasicPolygonsLibraryNoDefault.sld");
        kvp.put("sld", url.toString());
        kvp.put("layers", MockData.BASIC_POLYGONS.getPrefix() + ":" + MockData.BASIC_POLYGONS.getLocalPart());
        kvp.put("styles", "TheLibraryModeStyle");

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        request = (GetMapRequest) reader.read(request, parseKvp(kvp), kvp);

        assertNotNull(request.getSld());
        assertEquals(url, request.getSld());
        final Style style = (Style) request.getStyles().get(0);
        assertNotNull(style);
        assertEquals("TheLibraryModeStyle", style.getName());
    }
    
    public void testSldFailLookup() throws Exception {
        // nothing matches the required style name
        HashMap kvp = new HashMap();
        URL url = GetMapKvpRequestReader.class.getResource("BasicPolygonsLibraryNoDefault.sld");
        kvp.put("sld", url.toString());
        kvp.put("layers", MockData.BASIC_POLYGONS.getPrefix() + ":" + MockData.BASIC_POLYGONS.getLocalPart());
        kvp.put("styles", "ThisStyleDoesNotExists");

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        try {
        	reader.setLaxStyleMatchAllowed(false);
            request = (GetMapRequest) reader.read(request, parseKvp(kvp), kvp);
            fail("The style looked up, 'ThisStyleDoesNotExists', should not have been found");
        } catch(WmsException e) {
            //System.out.println(e);
        }
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
    
    public void testRemoteWFS() throws Exception {
        if(!RemoteOWSTestSupport.isRemoteStatesAvailable())
            return;
        
        HashMap raw = new HashMap();
        raw.put("layers", "topp:states");
        raw.put("styles", MockData.BASIC_POLYGONS.getLocalPart());
        raw.put("format", "image/png");
        raw.put("srs", "epsg:4326");
        raw.put("bbox", "-100,20,-60,50");
        raw.put("height", "300");
        raw.put("width", "300");
        raw.put("remote_ows_type", "WFS");
        raw.put("remote_ows_url", RemoteOWSTestSupport.WFS_SERVER_URL);

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        request = (GetMapRequest) reader.read(request, parseKvp(raw), raw);
        
        assertEquals("WFS", request.getRemoteOwsType()); // TODO: handle case?
        assertEquals(new URL("http://sigma.openplans.org:8080/geoserver/wfs?"), request.getRemoteOwsURL());
        assertEquals(1, request.getLayers().length);
        assertEquals(MapLayerInfo.TYPE_REMOTE_VECTOR, request.getLayers()[0].getType());
        assertEquals("topp:states", request.getLayers()[0].getRemoteFeatureSource().getSchema().getTypeName());
    }
    
    public void testRemoteWFSNoStyle() throws Exception {
        if(!RemoteOWSTestSupport.isRemoteStatesAvailable())
            return;
        
        HashMap raw = new HashMap();
        raw.put("layers", "topp:states");
        raw.put("format", "image/png");
        raw.put("srs", "epsg:4326");
        raw.put("bbox", "-100,20,-60,50");
        raw.put("height", "300");
        raw.put("width", "300");
        raw.put("remote_ows_type", "WFS");
        raw.put("remote_ows_url", RemoteOWSTestSupport.WFS_SERVER_URL);

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        try {
            request = (GetMapRequest) reader.read(request, parseKvp(raw), raw);
            fail("This should have thrown an exception because of the missing style");
        } catch(WmsException e) {
            assertEquals("NoDefaultStyle", e.getCode());
        }
    }
    
    public void testRemoteWFSInvalidURL() throws Exception {
        if(!RemoteOWSTestSupport.isRemoteStatesAvailable())
            return;
        
        HashMap raw = new HashMap();
        raw.put("layers", "topp:states");
        raw.put("format", "image/png");
        raw.put("srs", "epsg:4326");
        raw.put("bbox", "-100,20,-60,50");
        raw.put("height", "300");
        raw.put("width", "300");
        raw.put("remote_ows_type", "WFS");
        raw.put("remote_ows_url", "http://phantom.openplans.org:8080/crapserver/wfs?");

        GetMapRequest request = (GetMapRequest) reader.createRequest();
        try {
            request = (GetMapRequest) reader.read(request, parseKvp(raw), raw);
            fail("This should have thrown an exception because of the non existent layer");
        } catch(WmsException e) {
            e.printStackTrace();
            assertEquals("RemoteOWSFailure", e.getCode());
        }
    }

    public void testSampleDimensions() throws Exception {
        HashMap raw = new HashMap();
        raw.put("layers", "cite:Lakes");
        raw.put("format", "image/png");
        raw.put("srs", "epsg:4326");
        raw.put("bbox", "-100,20,-60,50");
        raw.put("height", "300");
        raw.put("width", "300");

        GetMapRequest request = (GetMapRequest) reader.createRequest();

        request = (GetMapRequest) reader.read(request, parseKvp(raw), raw);
        assertNotNull(request.getSampleDimensions());
        assertEquals(0, request.getSampleDimensions().size());

        raw.put("dim_WaveLength", "1.0e3");
        raw.put("DIM_temp", "32");

        request = (GetMapRequest) reader.read(request, parseKvp(raw), raw);
        Map<String, String> sampleDimensions = request.getSampleDimensions();
        assertEquals(2, request.getSampleDimensions().size());
        //parsed names are upper case, dimension names shall be compared case insensitively
        assertTrue(request.getSampleDimensions().containsKey("WAVELENGTH"));
        assertTrue(request.getSampleDimensions().containsKey("TEMP"));
        assertEquals("1.0e3", request.getSampleDimensions().get("WAVELENGTH"));
        assertEquals("32", request.getSampleDimensions().get("TEMP"));
    }
}
