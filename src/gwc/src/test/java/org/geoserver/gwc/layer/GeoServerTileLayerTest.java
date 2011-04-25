package org.geoserver.gwc.layer;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.createNiceMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.easymock.IAnswer;
import org.easymock.IArgumentMatcher;
import org.easymock.classextension.EasyMock;
import org.easymock.internal.LastControl;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.LayerInfo.Type;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.impl.DataStoreInfoImpl;
import org.geoserver.catalog.impl.FeatureTypeInfoImpl;
import org.geoserver.catalog.impl.LayerGroupInfoImpl;
import org.geoserver.catalog.impl.LayerInfoImpl;
import org.geoserver.catalog.impl.NamespaceInfoImpl;
import org.geoserver.catalog.impl.StyleInfoImpl;
import org.geoserver.gwc.config.GWCConfig;
import org.geoserver.wms.WMSMapContext;
import org.geoserver.wms.WebMap;
import org.geoserver.wms.map.RenderedImageMap;
import org.geoserver.wms.map.RenderedImageMapResponse;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geowebcache.GeoWebCacheException;
import org.geowebcache.conveyor.Conveyor.CacheResult;
import org.geowebcache.conveyor.ConveyorTile;
import org.geowebcache.filter.parameters.ParameterFilter;
import org.geowebcache.filter.parameters.StringParameterFilter;
import org.geowebcache.grid.BoundingBox;
import org.geowebcache.grid.GridSetBroker;
import org.geowebcache.grid.GridSubset;
import org.geowebcache.grid.OutsideCoverageException;
import org.geowebcache.io.ByteArrayResource;
import org.geowebcache.io.Resource;
import org.geowebcache.layer.meta.LayerMetaInformation;
import org.geowebcache.mime.MimeType;
import org.geowebcache.storage.StorageBroker;
import org.geowebcache.storage.TileObject;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;

public class GeoServerTileLayerTest extends TestCase {

    private LayerInfoImpl layerInfo;

    private GeoServerTileLayer layerInfoTileLayer;

    private LayerGroupInfoImpl layerGroup;

    private GeoServerTileLayer layerGroupInfoTileLayer;

    private CatalogConfiguration catalogConfig;

    private GWCConfig defaultSettings;

    @Override
    public void setUp() throws Exception {
        final String layerInfoId = "mock-layer-info";

        NamespaceInfo ns = new NamespaceInfoImpl();
        ns.setPrefix("test");
        ns.setURI("http://goserver.org/test");

        DataStoreInfoImpl storeInfo = new DataStoreInfoImpl(null);
        storeInfo.setId("mock-store-info");
        storeInfo.setEnabled(true);

        FeatureTypeInfoImpl resource = new FeatureTypeInfoImpl((Catalog) null);
        resource.setStore(storeInfo);
        resource.setId("mock-resource-info");
        resource.setName("MockLayerInfoName");
        resource.setNamespace(ns);
        resource.setTitle("Test resource title");
        resource.setAbstract("Test resource abstract");
        resource.setEnabled(true);
        resource.setDescription("Test resource description");
        resource.setLatLonBoundingBox(new ReferencedEnvelope(-180, -90, 0, 0,
                DefaultGeographicCRS.WGS84));
        resource.setNativeBoundingBox(new ReferencedEnvelope(-180, -90, 0, 0,
                DefaultGeographicCRS.WGS84));
        resource.setSRS("EPSG:4326");
        resource.setKeywords(Arrays.asList("kwd1", "kwd2"));

        layerInfo = new LayerInfoImpl();
        layerInfo.setId(layerInfoId);
        layerInfo.setEnabled(true);
        layerInfo.setResource(resource);
        layerInfo.setName("MockLayerInfoName");
        layerInfo.setType(Type.VECTOR);
        StyleInfo defaultStyle = new StyleInfoImpl(null);
        defaultStyle.setName("default_style");

        layerInfo.setDefaultStyle(defaultStyle);

        StyleInfo alternateStyle1 = new StyleInfoImpl(null);
        alternateStyle1.setName("alternateStyle-1");
        StyleInfo alternateStyle2 = new StyleInfoImpl(null);
        alternateStyle2.setName("alternateStyle-2");
        Set<StyleInfo> alternateStyles = new HashSet<StyleInfo>(Arrays.asList(alternateStyle1,
                alternateStyle2));
        layerInfo.setStyles(alternateStyles);

        layerGroup = new LayerGroupInfoImpl();
        final String layerGroupId = "mock-layergroup-id";
        layerGroup.setId(layerGroupId);
        layerGroup.setName("MockLayerGroup");
        layerGroup.setLayers(Collections.singletonList((LayerInfo) layerInfo));

        defaultSettings = GWCConfig.getOldDefaults();
        catalogConfig = EasyMock.createNiceMock(CatalogConfiguration.class);
        expect(catalogConfig.getConfig()).andReturn(defaultSettings).anyTimes();
        expect(catalogConfig.getLayerInfoById(eq(layerInfoId))).andReturn(layerInfo).anyTimes();
        expect(catalogConfig.getLayerGroupById(eq(layerGroupId))).andReturn(layerGroup).anyTimes();
        GridSetBroker gridSetBroker = new GridSetBroker(true, true);
        gridSetBroker.put(gridSetBroker.WORLD_EPSG4326);
        gridSetBroker.put(gridSetBroker.WORLD_EPSG3857);
        expect(catalogConfig.getGridSetBroker()).andStubReturn(gridSetBroker);
    }

    public void testEnabled() {
        catalogConfig.save((GeoServerTileLayer) anyObject());

        replay(catalogConfig);
        layerInfo.setEnabled(true);
        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);
        assertTrue(layerInfoTileLayer.isEnabled());

        layerInfo.setEnabled(false);
        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);
        assertFalse(layerInfoTileLayer.isEnabled());

        layerInfo.setEnabled(true);
        layerInfoTileLayer.setEnabled(true);
        assertTrue(layerInfoTileLayer.isEnabled());
        assertTrue(layerInfoTileLayer.getInfo().isEnabled());

        // this is the only call to layerInfoTileLayer that will call catalogConfig.save
        layerInfoTileLayer.setEnabled(false);
        assertFalse(layerInfoTileLayer.isEnabled());
        assertFalse(layerInfoTileLayer.getInfo().isEnabled());

        verify(catalogConfig);
    }

    public void testGetMetaTilingFactors() {
        replay(catalogConfig);

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);

        int[] metaTilingFactors = layerInfoTileLayer.getMetaTilingFactors();
        assertEquals(defaultSettings.getMetaTilingX(), metaTilingFactors[0]);
        assertEquals(defaultSettings.getMetaTilingY(), metaTilingFactors[1]);

        GeoServerTileLayerInfo info = layerInfoTileLayer.getInfo();
        info.setMetaTilingX(1 + defaultSettings.getMetaTilingX());
        info.setMetaTilingY(2 + defaultSettings.getMetaTilingY());
        info.saveTo(layerInfo.getMetadata());

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);
        metaTilingFactors = layerInfoTileLayer.getMetaTilingFactors();
        assertEquals(1 + defaultSettings.getMetaTilingX(), metaTilingFactors[0]);
        assertEquals(2 + defaultSettings.getMetaTilingY(), metaTilingFactors[1]);
    }

    public void testIsQueryable() {
        expect(catalogConfig.isQueryable((GeoServerTileLayer) anyObject())).andReturn(true);
        expect(catalogConfig.isQueryable((GeoServerTileLayer) anyObject())).andReturn(false);
        replay(catalogConfig);

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);
        assertTrue(layerInfoTileLayer.isQueryable());
        assertFalse(layerInfoTileLayer.isQueryable());
        verify(catalogConfig);
    }

    public void testGetName() {
        replay(catalogConfig);

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);
        assertEquals(layerInfo.getResource().getPrefixedName(), layerInfoTileLayer.getName());

        layerGroupInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerGroup);
        assertEquals(layerGroup.getName(), layerGroupInfoTileLayer.getName());

        verify(catalogConfig);
    }

    public void testGetParameterFilters() {
        replay(catalogConfig);

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);
        List<ParameterFilter> parameterFilters = layerInfoTileLayer.getParameterFilters();
        assertNotNull(parameterFilters);
        assertEquals(1, parameterFilters.size());
        assertTrue(parameterFilters.get(0) instanceof StringParameterFilter);
        StringParameterFilter styleFilter = (StringParameterFilter) parameterFilters.get(0);
        assertEquals("STYLES", styleFilter.getKey());
        assertEquals("default_style", styleFilter.getDefaultValue());
        assertEquals(
                new HashSet<String>(Arrays.asList("default_style", "alternateStyle-1",
                        "alternateStyle-2")), new HashSet<String>(styleFilter.getLegalValues()));

        layerInfoTileLayer.getInfo().setCachedStyles(Collections.singleton("alternateStyle-2"));

        verify(catalogConfig);
    }

    public void testGetDefaultParameterFilters() {
        replay(catalogConfig);

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);
        Map<String, String> defaultFilters = layerInfoTileLayer.getDefaultParameterFilters();
        assertEquals(1, defaultFilters.size());
        assertEquals("default_style", defaultFilters.get("STYLES"));

        StyleInfo newDefaultStyle = new StyleInfoImpl(null);
        newDefaultStyle.setName("newDefault");
        layerInfo.setDefaultStyle(newDefaultStyle);

        layerInfoTileLayer.resetParameterFilters();

        defaultFilters = layerInfoTileLayer.getDefaultParameterFilters();
        assertEquals(1, defaultFilters.size());
        assertEquals("newDefault", defaultFilters.get("STYLES"));

        verify(catalogConfig);
    }

    public void testResetParameterFilters() {
        replay(catalogConfig);

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);

        layerInfoTileLayer.getInfo().setCachedStyles(Collections.singleton("alternateStyle-2"));

        layerInfoTileLayer.resetParameterFilters();
        List<ParameterFilter> parameterFilters = layerInfoTileLayer.getParameterFilters();
        StringParameterFilter styleFilter = (StringParameterFilter) parameterFilters.get(0);
        assertEquals(new HashSet<String>(Arrays.asList("default_style", "alternateStyle-2")),
                new HashSet<String>(styleFilter.getLegalValues()));

        verify(catalogConfig);
    }

    public void testGetModifiableParameters() throws GeoWebCacheException {
        replay(catalogConfig);

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);
        ParameterFilter stylesParamFilter = layerInfoTileLayer.getParameterFilters().get(0);
        List<String> legalValues = stylesParamFilter.getLegalValues();

        Map<String, String> requestParams;
        Map<String, String> modifiedParams;

        requestParams = Collections.singletonMap("sTyLeS", "");
        modifiedParams = layerInfoTileLayer.getModifiableParameters(requestParams, "UTF-8");
        assertEquals(0, modifiedParams.size());

        for (String legalStyle : legalValues) {
            requestParams = new HashMap<String, String>();
            requestParams.put("sTyLeS", legalStyle);
            modifiedParams = layerInfoTileLayer.getModifiableParameters(requestParams, "UTF-8");
            if (legalStyle.equals(stylesParamFilter.getDefaultValue())) {
                assertEquals(0, modifiedParams.size());
            } else {
                assertEquals(Collections.singletonMap("STYLES", legalStyle), modifiedParams);
            }
        }

        verify(catalogConfig);
    }

    public void testGetMetaInformation() {
        replay(catalogConfig);

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);
        layerGroupInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerGroup);

        LayerMetaInformation metaInformation = layerInfoTileLayer.getMetaInformation();
        assertNotNull(metaInformation);
        String title = metaInformation.getTitle();
        String description = metaInformation.getDescription();
        List<String> keywords = metaInformation.getKeywords();
        assertEquals(layerInfo.getResource().getTitle(), title);
        assertEquals(layerInfo.getResource().getAbstract(), description);
        assertEquals(layerInfo.getResource().getKeywords(), keywords);

        metaInformation = layerGroupInfoTileLayer.getMetaInformation();
        assertNotNull(metaInformation);
        title = metaInformation.getTitle();
        description = metaInformation.getDescription();
        keywords = metaInformation.getKeywords();
        // these properties are missing from LayerGroupInfo interface
        assertEquals(layerGroup.getName(), title);
        assertEquals("", description);
        assertEquals(0, keywords.size());

        verify(catalogConfig);
    }

    public void testGetStyles() {
        replay(catalogConfig);

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);
        layerGroupInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerGroup);

        assertEquals("default_style", layerInfoTileLayer.getStyles());
        assertNull(layerGroupInfoTileLayer.getStyles());

        StyleInfo newDefaultStyle = new StyleInfoImpl(null);
        newDefaultStyle.setName("newDefault");
        layerInfo.setDefaultStyle(newDefaultStyle);

        assertEquals("newDefault", layerInfoTileLayer.getStyles());

        verify(catalogConfig);
    }

    public void testGetGridSubsets() {
        replay(catalogConfig);

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);
        Map<String, GridSubset> gridSubsets = layerInfoTileLayer.getGridSubsets();
        assertNotNull(gridSubsets);
        assertEquals(2, gridSubsets.size());

        layerInfoTileLayer.getInfo().setCachedGridSetIds(Collections.singleton("EPSG:900913"));
        layerInfoTileLayer.getInfo().saveTo(layerInfo.getMetadata());
        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);

        gridSubsets = layerInfoTileLayer.getGridSubsets();
        assertNotNull(gridSubsets);
        assertEquals(1, gridSubsets.size());

        verify(catalogConfig);
    }

    public void testGetFeatureInfo() throws Exception {

        // TODO: use org.easymock.Capture instead when we upgrade to EasyMock 2.4+
        final Map<String, String> capturedParams = new HashMap<String, String>();
        final Resource stubResponse = new ByteArrayResource("it's ok".getBytes());
        IArgumentMatcher m = null;

        expect(catalogConfig.dispatchOwsRequest((Map<String, String>) anyObject(),
                (Cookie[]) anyObject()));

        EasyMock.expectLastCall().andAnswer(new IAnswer<Object>() {

            public Object answer() throws Throwable {
                Object[] currentArguments = LastControl.getCurrentArguments();
                Map<String, String> params = (Map<String, String>) currentArguments[0];
                capturedParams.putAll(params);
                return stubResponse;
            }
        });
        replay(catalogConfig);

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);

        ConveyorTile convTile = new ConveyorTile(null, null, null, null);
        convTile.setTileLayer(layerInfoTileLayer);
        convTile.setMimeType(MimeType.createFromFormat("image/png"));
        convTile.setGridSetId("EPSG:4326");
        BoundingBox bbox = new BoundingBox(0, 0, 10, 10);
        Resource result = layerInfoTileLayer.getFeatureInfo(convTile, bbox, 100, 100, 50, 50);
        assertNotNull(result);
        assertEquals("it's ok", new String(((ByteArrayResource) result).getContents()));

        assertEquals("image/png", capturedParams.get("INFO_FORMAT"));
        assertEquals("0.0,0.0,10.0,10.0", capturedParams.get("BBOX"));
        assertEquals("test:MockLayerInfoName", capturedParams.get("QUERY_LAYERS"));
        assertEquals("WMS", capturedParams.get("SERVICE"));
        assertEquals("100", capturedParams.get("HEIGHT"));
        assertEquals("100", capturedParams.get("WIDTH"));
        assertEquals("GetFeatureInfo", capturedParams.get("REQUEST"));
        assertEquals("default_style", capturedParams.get("STYLES"));
        assertEquals("SE_XML", capturedParams.get("EXCEPTIONS"));
        assertEquals("1.1.1", capturedParams.get("VERSION"));
        assertEquals("image/png", capturedParams.get("FORMAT"));
        assertEquals("test:MockLayerInfoName", capturedParams.get("LAYERS"));
        assertEquals("EPSG:4326", capturedParams.get("SRS"));
        assertEquals("50", capturedParams.get("X"));
        assertEquals("50", capturedParams.get("Y"));

        verify(catalogConfig);
    }

    public void testGetTilePreconditions() throws Exception {
        replay(catalogConfig);

        StorageBroker storageBroker = createNiceMock(StorageBroker.class);
        replay(storageBroker);

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);

        MockHttpServletRequest servletReq = new MockHttpServletRequest();
        HttpServletResponse servletResp = new MockHttpServletResponse();

        ConveyorTile tile = new ConveyorTile(storageBroker, layerInfoTileLayer.getName(),
                servletReq, servletResp);
        tile.setMimeType(MimeType.createFromFormat("image/gif"));
        try {
            layerInfoTileLayer.getTile(tile);
            fail("Expected exception, requested mime is invalid for the layer");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("is not a supported format"));
        }

        tile.setMimeType(MimeType.createFromFormat("image/png"));
        tile.setGridSetId("EPSG:2003");
        try {
            layerInfoTileLayer.getTile(tile);
            fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("gridset not found"));
        }

        long[] tileIndex = { 0, 0, 1 };
        MimeType mimeType = MimeType.createFromFormat("image/png");
        tile = new ConveyorTile(storageBroker, layerInfoTileLayer.getName(), "EPSG:900913",
                tileIndex, mimeType, null, servletReq, servletResp);

        try {
            layerInfoTileLayer.getTile(tile);
            fail("Expected outside coverage exception");
        } catch (OutsideCoverageException e) {
            assertTrue(true);
        }

        verify(storageBroker);
        verify(catalogConfig);
    }

    public void testGetTile() throws Exception {

        Resource stubHttpresponse = new ByteArrayResource();
        expect(
                catalogConfig.dispatchOwsRequest((Map<String, String>) anyObject(),
                        (Cookie[]) anyObject())).andStubReturn(stubHttpresponse);

        BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
        RenderedImageMap fakeDispatchedMap = new RenderedImageMap(new WMSMapContext(), image,
                "image/png");

        RenderedImageMapResponse fakeResponseEncoder = createMock(RenderedImageMapResponse.class);

        expect(
                catalogConfig.getResponseEncoder(eq(MimeType.createFromFormat("image/png")),
                        (WebMap) anyObject())).andStubReturn(fakeResponseEncoder);
        replay(catalogConfig);

        StorageBroker storageBroker = createNiceMock(StorageBroker.class);
        expect(storageBroker.get((TileObject) anyObject())).andReturn(false);
        replay(storageBroker);

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);

        MockHttpServletRequest servletReq = new MockHttpServletRequest();
        HttpServletResponse servletResp = new MockHttpServletResponse();
        long[] tileIndex = { 0, 0, 0 };
        MimeType mimeType = MimeType.createFromFormat("image/png");
        ConveyorTile tile = new ConveyorTile(storageBroker, layerInfoTileLayer.getName(),
                "EPSG:4326", tileIndex, mimeType, null, servletReq, servletResp);

        GeoServerTileLayer.WEB_MAP.set(fakeDispatchedMap);
        ConveyorTile returned = layerInfoTileLayer.getTile(tile);
        assertNotNull(returned);
        assertNotNull(returned.getBlob());
        assertEquals(CacheResult.MISS, returned.getCacheResult());
        assertEquals(200, returned.getStatus());

        verify(storageBroker);
        verify(catalogConfig);
    }

    public void testGetMimeTypes() throws Exception {
        replay(catalogConfig);

        layerInfoTileLayer = new GeoServerTileLayer(catalogConfig, layerInfo);
        List<MimeType> mimeTypes = layerInfoTileLayer.getMimeTypes();
        GWCConfig defaults = catalogConfig.getConfig();
        assertEquals(defaults.getDefaultOtherCacheFormats().size(), mimeTypes.size());

        layerInfoTileLayer.getInfo().setMimeFormats(Collections.singleton("image/gif"));

        mimeTypes = layerInfoTileLayer.getMimeTypes();
        assertEquals(1, mimeTypes.size());
        assertEquals(MimeType.createFromFormat("image/gif"), mimeTypes.get(0));

        verify(catalogConfig);
    }

}
