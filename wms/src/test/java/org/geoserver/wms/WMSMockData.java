package org.geoserver.wms;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ProjectionPolicy;
import org.geoserver.catalog.ResourcePool;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.impl.CatalogImpl;
import org.geoserver.catalog.impl.DataStoreInfoImpl;
import org.geoserver.catalog.impl.FeatureTypeInfoImpl;
import org.geoserver.catalog.impl.LayerInfoImpl;
import org.geoserver.catalog.impl.NamespaceInfoImpl;
import org.geoserver.catalog.impl.StyleInfoImpl;
import org.geoserver.catalog.impl.WorkspaceInfoImpl;
import org.geoserver.config.GeoServer;
import org.geoserver.config.impl.GeoServerImpl;
import org.geoserver.config.impl.GeoServerInfoImpl;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.map.RasterMapProducer;
import org.geoserver.wms.request.GetMapRequest;
import org.geoserver.wms.response.GetMapResponse;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.vfny.geoserver.wms.WmsException;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockServletContext;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;

/**
 * WMS tests utility class to set up a mocked up catalog and geoserver environment so unit tests
 * does not depend on a fully configured geoserver instance, and also they run fast due to no data
 * directory set up required.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 */
public class WMSMockData {

    /**
     * Namespace used for the resources in this test suite
     */
    public static final String TEST_NAMESPACE = "http://geoserver.org";

    private CatalogImpl catalog;

    private MemoryDataStore dataStore;

    private DataStoreInfo dataStoreInfo;

    private NamespaceInfoImpl namespaceInfo;

    private WorkspaceInfoImpl workspaceInfo;

    private StyleInfoImpl defaultStyle;

    private GetMapOutputFormat mockMapProducer;

    private GeoServer mockGeoServer;

    private WMS mockWMS;

    @SuppressWarnings("deprecation")
    public void setUp() throws Exception {
        mockMapProducer = new DummyRasterMapProducer();

        catalog = new CatalogImpl();

        namespaceInfo = new NamespaceInfoImpl();
        namespaceInfo.setId("testNs");
        namespaceInfo.setPrefix("geos");
        namespaceInfo.setURI(TEST_NAMESPACE);
        catalog.add(namespaceInfo);

        workspaceInfo = new WorkspaceInfoImpl();
        catalog.setDefaultWorkspace(workspaceInfo);

        defaultStyle = new StyleInfoImpl(catalog) {
            /**
             * Override so it does not try to load a file from disk
             */
            @Override
            public Style getStyle() throws IOException {
                StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
                Style style = styleFactory.createStyle();
                style.setName("Default Style");
                return style;
            }
        };
        defaultStyle.setFilename("defaultStyleFileName");
        defaultStyle.setId("defaultStyleId");
        defaultStyle.setName("defaultStyleName");
        catalog.add(defaultStyle);

        // coverageStoreInfo = new CoverageStoreInfoImpl(catalog);
        // coverageInfo = new CoverageInfoImpl(catalog);
        // coverageLayerInfo = new LayerInfoImpl();
        // // setUpBasicTestCoverage(coverageStoreInfo, coverageInfo, coverageLayerInfo);
        //
        // coverageInfoOldApi = new CoverageInfo(coverageLayerInfo, catalog);

        dataStoreInfo = new DataStoreInfoImpl(catalog);
        dataStoreInfo.setName("mockDataStore");
        dataStoreInfo.setEnabled(true);
        dataStoreInfo.setWorkspace(workspaceInfo);

        dataStore = new MemoryDataStore();
        ResourcePool resourcePool = new ResourcePool(catalog) {
            @Override
            public DataStore getDataStore(DataStoreInfo info) throws IOException {
                return dataStore;
            }
        };
        catalog.setResourcePool(resourcePool);

        mockGeoServer = new GeoServerImpl();
        mockGeoServer.setCatalog(catalog);

        GeoServerInfoImpl geoserverInfo = new GeoServerInfoImpl(mockGeoServer);
        geoserverInfo.setId("geoserver");
        mockGeoServer.setGlobal(geoserverInfo);

        WMSInfoImpl wmsInfo = new WMSInfoImpl();
        wmsInfo.setId("wms");
        wmsInfo.setName("WMS");
        wmsInfo.setEnabled(true);
        mockGeoServer.add(wmsInfo);

        mockWMS = new WMS(mockGeoServer);
    }

    /**
     * This dummy producer adds no functionality to DefaultRasterMapProducer, just implements a void
     * formatImageOutputStream
     * 
     * @author Gabriel Roldan
     * @version $Id$
     */
    public static class DummyRasterMapProducer implements RasterMapProducer {

        public static final String MIME_TYPE = "image/dummy";

        public WMSMapContext mapContext;

        public boolean abortCalled;

        public boolean produceMapCalled;

        public String outputFormat;

        public boolean writeToCalled;

        public void formatImageOutputStream(RenderedImage image, OutputStream outStream)
                throws WmsException, IOException {
            // do nothing
        }

        public RenderedImage getImage() {
            // do nothing
            return null;
        }

        public void abort() {
            this.abortCalled = true;
        }

        public String getContentDisposition() {
            // do nothing
            return null;
        }

        public String getContentType() throws IllegalStateException {
            return MIME_TYPE;
        }

        public String getOutputFormat() {
            return MIME_TYPE;
        }

        public void setMapContext(WMSMapContext mapContext) {
            this.mapContext = mapContext;
        }

        public WMSMapContext getMapContext() {
            return mapContext;
        }

        public Set<String> getOutputFormatNames() {
            return Collections.singleton(MIME_TYPE);
        }

        public void produceMap() throws WmsException {
            this.produceMapCalled = true;
        }

        public void setOutputFormat(String format) {
            this.outputFormat = format;
        }

        public void writeTo(OutputStream out) throws ServiceException, IOException {
            this.writeToCalled = true;
        }
    }

    public StyleInfo getDefaultStyle() {
        return defaultStyle;
    }

    public GetMapRequest createRequest() {
        GetMapRequest request;

        request = new GetMapRequest();
        request.setFormat(DummyRasterMapProducer.MIME_TYPE);
        request.setWidth(512);
        request.setHeight(256);
        Envelope envelope = new Envelope(-180, 180, -90, 90);
        request.setBbox(envelope);
        request.setSRS("EPSG:4326");
        request.setCrs(DefaultGeographicCRS.WGS84);
        try {
            request.setStyles(Collections.singletonList(defaultStyle.getStyle()));
        } catch (IOException e) {
            throw new RuntimeException("shouldn't happen", e);
        }
        request.setRawKvp(new HashMap<String, Serializable>());

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        request.setHttpRequest(servletRequest);
        MockHttpSession session = new MockHttpSession();
        servletRequest.setSession(session);
        MockServletContext context = new MockServletContext();
        session.setupServletContext(context);

        return request;
    }

    public GetMapResponse createResponse() {
        return createResponse(Collections.singletonList(mockMapProducer));
    }

    public GetMapResponse createResponse(List<GetMapOutputFormat> availableProducers) {
        GetMapResponse getMap;
        getMap = new GetMapResponse(availableProducers);
        return getMap;
    }

    /**
     * Creates a vector layer with associated FeatureType in the internal MemoryDataStore with the
     * given type and two attributes: name:String and geom:geometryType
     */
    public MapLayerInfo addFeatureTypeLayer(final String name,
            Class<? extends Geometry> geometryType) throws IOException {
        org.geoserver.catalog.FeatureTypeInfo featureTypeInfo = new FeatureTypeInfoImpl(catalog);
        featureTypeInfo.setName(name);
        featureTypeInfo.setNativeName(name);
        featureTypeInfo.setEnabled(true);

        final DefaultGeographicCRS wgs84 = DefaultGeographicCRS.WGS84;

        ReferencedEnvelope bbox = new ReferencedEnvelope(-180, 180, -90, 90, wgs84);
        featureTypeInfo.setLatLonBoundingBox(bbox);
        featureTypeInfo.setNamespace(namespaceInfo);
        featureTypeInfo.setNativeBoundingBox(bbox);
        featureTypeInfo.setNativeCRS(wgs84);
        featureTypeInfo.setSRS("EPSG:4326");
        featureTypeInfo.setProjectionPolicy(ProjectionPolicy.FORCE_DECLARED);
        featureTypeInfo.setStore(dataStoreInfo);
        catalog.add(featureTypeInfo);

        LayerInfo layerInfo = new LayerInfoImpl();
        layerInfo.setResource(featureTypeInfo);
        layerInfo.setName(name);
        layerInfo.setEnabled(true);
        layerInfo.setDefaultStyle(defaultStyle);
        layerInfo.setType(LayerInfo.Type.VECTOR);
        catalog.add(layerInfo);

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setNamespaceURI(TEST_NAMESPACE);
        ftb.setName(name);
        ftb.add("name", String.class);
        ftb.add("geom", geometryType, wgs84);
        SimpleFeatureType featureType = ftb.buildFeatureType();
        dataStore.createSchema(featureType);

        return new MapLayerInfo(layerInfo);
    }

    public SimpleFeature addFeature(final SimpleFeatureType featureType, final Object[] values)
            throws IOException, ParseException {
        SimpleFeatureStore fs;
        fs = (SimpleFeatureStore) dataStore.getFeatureSource(featureType.getName());

        SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(featureType);
        sfb.addAll(values);
        SimpleFeature feature = sfb.buildFeature(null);
        fs.addFeatures(DataUtilities.collection(feature));

        return feature;
    }

}
