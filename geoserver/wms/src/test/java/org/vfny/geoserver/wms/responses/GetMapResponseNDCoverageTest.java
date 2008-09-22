package org.vfny.geoserver.wms.responses;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.notNull;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.impl.CatalogImpl;
import org.geoserver.catalog.impl.CoverageInfoImpl;
import org.geoserver.catalog.impl.CoverageStoreInfoImpl;
import org.geoserver.catalog.impl.LayerInfoImpl;
import org.geoserver.catalog.impl.NamespaceInfoImpl;
import org.geoserver.catalog.impl.StyleInfoImpl;
import org.geoserver.catalog.impl.WorkspaceInfoImpl;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageCapabilities;
import org.geotools.coverage.io.CoverageReadRequest;
import org.geotools.coverage.io.CoverageResponse;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.coverage.io.CoverageResponse.Status;
import org.geotools.coverage.io.impl.range.DefaultFieldType;
import org.geotools.coverage.io.impl.range.DefaultRangeType;
import org.geotools.coverage.io.range.FieldType;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.data.Parameter;
import org.geotools.data.ResourceInfo;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.DiscreteCoverage;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.temporal.Instant;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.opengis.util.ProgressListener;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.requests.GetMapRequest;

import com.vividsolutions.jts.geom.Envelope;

public class GetMapResponseNDCoverageTest extends TestCase {

    /**
     * Namespace used for the resources in this test suite
     */
    private static final String TEST_NAMESPACE = "http://geoserver.org";

    private GetMapRequest request;

    private GeneralEnvelope requestedEnvelope;

    private GetMapResponse getMap;

    private CatalogImpl catalog;

    private NamespaceInfoImpl namespaceInfo;

    private WorkspaceInfoImpl workspaceInfo;

    private StyleInfoImpl defaultStyle;

    private GetMapProducer mockMapProducer;

    private CoverageStoreInfoImpl coverageStoreInfo;

    private CoverageInfoImpl coverageInfo;

    private LayerInfoImpl coverageLayerInfo;

    private CoverageInfo coverageInfoOldApi;

    private MapLayerInfo layer;

    private CoverageAccess cvAccess;

    private CoverageSource coverageSource;

    @SuppressWarnings("deprecation")
    protected void setUp() throws Exception {
        mockMapProducer = EasyMock.createMock(GetMapProducer.class);

        request = new GetMapRequest((WMS) null);
        request.setWidth(512);
        request.setHeight(256);
        Envelope envelope = new Envelope(-180, 180, -90, 90);
        request.setBbox(envelope);
        request.setSRS("EPSG:4326");
        request.setCrs(DefaultGeographicCRS.WGS84);
        requestedEnvelope = new GeneralEnvelope(new double[] { envelope.getMinX(),
                envelope.getMinY() }, new double[] { envelope.getMaxX(), envelope.getMaxY() });
        requestedEnvelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);

        getMap = new GetMapResponse(Collections.singleton(mockMapProducer));

        catalog = new CatalogImpl();

        namespaceInfo = new NamespaceInfoImpl(catalog);
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

        coverageStoreInfo = new CoverageStoreInfoImpl(catalog);
        coverageInfo = new CoverageInfoImpl(catalog);
        coverageLayerInfo = new LayerInfoImpl();
        setUpBasicTestCoverage(coverageStoreInfo, coverageInfo, coverageLayerInfo);

        coverageInfoOldApi = new CoverageInfo(coverageLayerInfo, catalog);
        layer = new MapLayerInfo(coverageInfoOldApi);

        cvAccess = EasyMock.createMock(CoverageAccess.class);
        coverageSource = EasyMock.createMock(CoverageSource.class);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * If the {@link CoverageSource#read()} operation returns a {@link CoverageResponse} with non
     * {@link Status#SUCCESS} an IOException shall be thrown
     */
    public void testGetCoverageRequestFailed() throws IOException {
        expect(
                cvAccess.access(new NameImpl("testCoverageName"), null, AccessType.READ_ONLY, null,
                        null)).andReturn(coverageSource);

        CoverageResponse response = createMock(CoverageResponse.class);
        expect(
                coverageSource.read((CoverageReadRequest) notNull(), (ProgressListener) EasyMock
                        .anyObject())).andReturn(response);
        // record the dispose() expectation, it should always be called
        coverageSource.dispose();

        expect(response.getStatus()).andReturn(Status.FAILURE);
        expect(response.getExceptions()).andStubReturn(
                Collections.singleton(new Exception("Fake exception")));
        replay(response);
        replay(cvAccess);
        replay(coverageSource);

        try {
            GetMapResponse.getCoverage(request, layer, requestedEnvelope, cvAccess);
            fail("Expected IOException if the coverage response has non SUCCESS status");
        } catch (IOException e) {
            assertTrue(true);
        }
        verify(response);
        verify(cvAccess);
        verify(coverageSource);
    }

    /**
     * If the response {@link CoverageSource#read()} contains no coverage (
     * {@link CoverageResponse#getResults(ProgressListener)} is null or empty) an IOException shall
     * be thrown
     */
    public void testGetCoverageEmptyResults() throws IOException {
        expect(
                cvAccess.access(new NameImpl("testCoverageName"), null, AccessType.READ_ONLY, null,
                        null)).andReturn(coverageSource);

        CoverageResponse response = createMock(CoverageResponse.class);
        expect(
                coverageSource.read((CoverageReadRequest) notNull(), (ProgressListener) EasyMock
                        .anyObject())).andReturn(response);
        // record the dispose() expectation, it should always be called
        coverageSource.dispose();

        expect(response.getStatus()).andReturn(Status.SUCCESS);
        expect(response.getResults((ProgressListener) anyObject())).andReturn(null);
        replay(response);
        replay(cvAccess);
        replay(coverageSource);

        try {
            GetMapResponse.getCoverage(request, layer, requestedEnvelope, cvAccess);
            fail("Expected IOException if the coverage response has no coverages");
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Requests returned no coverage"));
        }
        verify(response);
        verify(cvAccess);
        verify(coverageSource);
    }

    /**
     * If the response {@link CoverageSource#read()} contains more than one coverage (
     * {@code CoverageResponse.getResults(ProgressListener).size() > 1} ) an IOException shall be
     * thrown
     */
    public void testGetCoverageMultipleResults() throws IOException {
        expect(
                cvAccess.access(new NameImpl("testCoverageName"), null, AccessType.READ_ONLY, null,
                        null)).andReturn(coverageSource);

        CoverageResponse response = createMock(CoverageResponse.class);
        expect(
                coverageSource.read((CoverageReadRequest) notNull(), (ProgressListener) EasyMock
                        .anyObject())).andReturn(response);
        // record the dispose() expectation, it should always be called
        coverageSource.dispose();

        expect(response.getStatus()).andReturn(Status.SUCCESS);
        Collection<? extends Coverage> results = Arrays.asList(new Coverage[] {
                createMock(GridCoverage2D.class), createMock(GridCoverage2D.class) });
        expect(response.getResults((ProgressListener) anyObject())).andStubReturn(results);
        replay(response);
        replay(cvAccess);
        replay(coverageSource);

        try {
            GetMapResponse.getCoverage(request, layer, requestedEnvelope, cvAccess);
            fail("Expected IOException if the coverage response has more than one coverage");
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Request returned 2 coverages"));
        }
        verify(response);
        verify(cvAccess);
        verify(coverageSource);
    }

    /**
     * If the resulting coverage is not a {@link GridCoverage2D} an io exception shall be thrown
     */
    public void testGetCoverageResultTypeMissmatch() throws IOException {
        expect(
                cvAccess.access(new NameImpl("testCoverageName"), null, AccessType.READ_ONLY, null,
                        null)).andReturn(coverageSource);

        CoverageResponse response = createMock(CoverageResponse.class);
        expect(
                coverageSource.read((CoverageReadRequest) notNull(), (ProgressListener) EasyMock
                        .anyObject())).andReturn(response);
        // record the dispose() expectation, it should always be called
        coverageSource.dispose();

        expect(response.getStatus()).andReturn(Status.SUCCESS);
        // note result is not a GridCoverage2D
        Collection<? extends Coverage> results;
        results = Collections.singleton(createMock(DiscreteCoverage.class));

        expect(response.getResults((ProgressListener) anyObject())).andStubReturn(results);

        replay(response);
        replay(cvAccess);
        replay(coverageSource);

        try {
            GetMapResponse.getCoverage(request, layer, requestedEnvelope, cvAccess);
            fail("Expected IOException if the resulting coverage is not a GridCoverage2D");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("not a GridCoverage2D"));
        }
        verify(response);
        verify(cvAccess);
        verify(coverageSource);
    }

    /**
     * If the layer's field id matches more than one coverage {@link FieldType} an IOException shall
     * be thrown, we don't support multi field coverages yet
     */
    public void testGetCoverageMultipleRangeSubset() throws IOException {
        // now mock up the layer fieldId and coverage FieldTypes...
        layer.setFieldId("testFieldName");

        Set<FieldType> fieldTypes = new HashSet<FieldType>();
        // make sure there are more than one match for the layer field id
        fieldTypes.add(new DefaultFieldType(new NameImpl("testFieldName"), null, null, null, null));
        fieldTypes.add(new DefaultFieldType(new NameImpl("anotherFieldName"), null, null, null,
                null));
        fieldTypes.add(new DefaultFieldType(new NameImpl("testFieldName"), null, null, null, null));

        RangeType fields = new DefaultRangeType("testRange", "range desc", fieldTypes);
        coverageInfo.setFields(fields);

        expect(
                cvAccess.access(new NameImpl("testCoverageName"), null, AccessType.READ_ONLY, null,
                        null)).andReturn(coverageSource);

        // record the dispose() expectation, it should always be called
        coverageSource.dispose();

        replay(cvAccess);
        replay(coverageSource);

        try {
            GetMapResponse.getCoverage(request, layer, requestedEnvelope, cvAccess);
            fail("Expected IOException if there are more than one range matching the layer field id");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Multi field coverages are not supported yet"));
        }
        verify(cvAccess);
        verify(coverageSource);
    }

    /**
     * Check getCoverage builds up the correct {@link CoverageReadRequest} for
     * {@link CoverageSource#read(CoverageReadRequest, ProgressListener)}
     */
    public void testGetCoverageIssuesTheCorrectRequest() throws IOException {
        // now mock up the layer fieldId and coverage FieldTypes...
        layer.setFieldId("testFieldName");

        // set the request values for the temporal subset (note the dates are unsorted in the
        // request)
        Date time1 = new Date(1000L);
        Date time2 = new Date(0L);
        Date time3 = new Date(2000L);
        request.setTime(Arrays.asList(new Date[] { time1, time2, time3 }));

        Set<FieldType> fieldTypes = new HashSet<FieldType>();
        // make sure there's a matching FieldType in the coverage for the layer field id
        fieldTypes.add(new DefaultFieldType(new NameImpl("testFieldName"), null, null, null, null));
        fieldTypes.add(new DefaultFieldType(new NameImpl("anotherFieldName"), null, null, null,
                null));
        coverageInfo.setFields(new DefaultRangeType("testRange", "range desc", fieldTypes));

        //Set the request value for the elevation param
        request.setElevation(new Integer(1500));
        
        MockCoverageSource source = new MockCoverageSource(coverageSource);

        expect(
                cvAccess.access(new NameImpl("testCoverageName"), null, AccessType.READ_ONLY, null,
                        null)).andReturn(source);

        CoverageResponse response = createMock(CoverageResponse.class);
        expect(
                coverageSource.read((CoverageReadRequest) anyObject(),
                        (ProgressListener) anyObject())).andReturn(response);

        // record the dispose() expectation, it should always be called
        coverageSource.dispose();

        // return status failure so it does not advances further than issuing the read request
        expect(response.getStatus()).andReturn(Status.FAILURE);
        expect(response.getExceptions()).andStubReturn(
                Collections.singleton(new Exception("Fake exception")));
        replay(response);
        replay(cvAccess);
        replay(coverageSource);

        try {
            GetMapResponse.getCoverage(request, layer, requestedEnvelope, cvAccess);
            fail("Expected IOException if there are more than one range matching the layer field id");
        } catch (IOException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("FAILURE"));
        }
        verify(cvAccess);
        verify(coverageSource);

        // no assert the correct request were sent
        final CoverageReadRequest issuedRequest = source.issuedRequest;

        // was the request envelope set?
        BoundingBox geographicArea = issuedRequest.getGeographicArea();
        assertNotNull(geographicArea);
        assertSame(DefaultGeographicCRS.WGS84, geographicArea.getCoordinateReferenceSystem());
        assertEquals(-180D, geographicArea.getMinX());
        assertEquals(-90D, geographicArea.getMinY());
        assertEquals(180D, geographicArea.getMaxX());
        assertEquals(90D, geographicArea.getMaxY());

        // was the range subset set based on the layer's field id?
        RangeType rangeSubset = issuedRequest.getRangeSubset();
        assertNotNull(rangeSubset);
        assertEquals(1, rangeSubset.getFieldTypes().size());
        assertNotNull(rangeSubset.getFieldType("testFieldName"));

        // was the temporal subset set and in the correct order?
        SortedSet<TemporalGeometricPrimitive> temporalSubset = issuedRequest.getTemporalSubset();
        assertNotNull(temporalSubset);
        assertEquals(3, temporalSubset.size());
        Iterator<TemporalGeometricPrimitive> iterator = temporalSubset.iterator();
        TemporalGeometricPrimitive t1 = iterator.next();
        TemporalGeometricPrimitive t2 = iterator.next();
        TemporalGeometricPrimitive t3 = iterator.next();
        assertTrue(t1 instanceof Instant);
        assertTrue(t2 instanceof Instant);
        assertTrue(t3 instanceof Instant);

        // TODO: this backward order is due to what I think is a bug in
        // DefaultTemporalPrimitive.compareTo. If that's fixed, just change the expectation order
        // bellow
        assertEquals(2000L, ((Instant) t1).getPosition().getDate().getTime());
        assertEquals(1000L, ((Instant) t2).getPosition().getDate().getTime());
        assertEquals(0L, ((Instant) t3).getPosition().getDate().getTime());
        
        //assert the CoverageReadRequest was set with the elevation parameters
        Set<org.opengis.geometry.Envelope> verticalSubset = issuedRequest.getVerticalSubset();
        assertNotNull(verticalSubset);
        assertEquals(1, verticalSubset.size());
        org.opengis.geometry.Envelope verticalRange = verticalSubset.iterator().next();
        assertTrue(verticalRange instanceof GeneralEnvelope);
        assertEquals(1, ((GeneralEnvelope)verticalRange).getDimension());        
        assertEquals(Double.valueOf(1500), ((GeneralEnvelope)verticalRange).getMinimum(0));
        assertEquals(Double.valueOf(1500), ((GeneralEnvelope)verticalRange).getMaximum(0));
    }

    /**
     * Ties up the provided coverage store, coverage and layer together and adds them to the catalog
     * in order to assert the encoding of a coverage in the GetCapabilities response.
     * <p>
     * This method only sets up the simplest and minimal properties needed for each of the
     * arguments. Callers could enhance them after this method returns in order to set up the
     * objects properties of interest for the specific test.
     * </p>
     * 
     * @param coverageStoreInfo
     *                the coverage store info to add to the catalog and where to configure the
     *                coverage
     * @param coverageInfo
     *                the coverage to configure with basic resource properties, add to the catalog
     *                as a component of the {@code coverageStoreInfo}
     * @param coverageLayerInfo
     *                the layer to configure for {@code coverageInfo} and add to the catalog with
     *                {@link #defaultStyle} and some default simple properties
     */
    private void setUpBasicTestCoverage(CoverageStoreInfoImpl coverageStoreInfo,
            CoverageInfoImpl coverageInfo, LayerInfoImpl coverageLayerInfo) {

        coverageInfo.setStore(coverageStoreInfo);

        coverageInfo.setId("testCoverageId");
        coverageInfo.setEnabled(true);
        coverageInfo.setName("testCoverageName");
        coverageInfo.setNamespace(namespaceInfo);
        coverageInfo.setId("testCoverage");
        coverageInfo.setTitle("testCoverageTitle");
        coverageInfo.setAbstract("testCoverageAbstract");
        coverageInfo.setDescription("testCoverageDescription");
        coverageInfo.setDefaultInterpolationMethod("defaultInterpolationMethod");
        ReferencedEnvelope latLonBbox = new ReferencedEnvelope(-180, 180, -90, 90,
                DefaultGeographicCRS.WGS84);
        coverageInfo.setLatLonBoundingBox(latLonBbox);
        coverageInfo.setNativeCRS(DefaultGeographicCRS.WGS84);
        coverageInfo.setNativeBoundingBox(latLonBbox);
        coverageInfo.setSRS("EPSG:4326");

        coverageLayerInfo.setResource(coverageInfo);
        coverageLayerInfo.setEnabled(true);
        coverageLayerInfo.setId("testLayerId");
        coverageLayerInfo.setName("testLayerName");

        // no path, top level layer
        // coverageLayerInfo.setPath("testLayerPath");
        coverageLayerInfo.setType(LayerInfo.Type.RASTER);
        coverageLayerInfo.setDefaultStyle(defaultStyle);

        catalog.add(coverageLayerInfo);
        catalog.add(coverageStoreInfo);
        catalog.add(coverageInfo);
    }

    /**
     * An empty CoverageSource implementation for tests to use in asserting the operation arguments,
     * etc
     */
    private static class MockCoverageSource implements CoverageSource {

        private CoverageSource original;

        boolean disposeCalled = false;

        public CoverageReadRequest issuedRequest;

        public MockCoverageSource() {
            // will return null for all operations
            original = EasyMock.createNiceMock(CoverageSource.class);
        }

        public MockCoverageSource(CoverageSource wrapped) {
            this.original = wrapped;
        }

        public void dispose() {
            original.dispose();
            disposeCalled = true;
        }

        public EnumSet<CoverageCapabilities> getCapabilities() {
            return original.getCapabilities();
        }

        public CoordinateReferenceSystem getCoordinateReferenceSystem(ProgressListener listener)
                throws IOException {
            return original.getCoordinateReferenceSystem(listener);
        }

        public MathTransform2D getGridToWorldTransform(boolean brief, ProgressListener listener)
                throws IOException {
            return original.getGridToWorldTransform(brief, listener);
        }

        public List<BoundingBox> getHorizontalDomain(boolean overall, ProgressListener listener)
                throws IOException {
            return original.getHorizontalDomain(overall, listener);
        }

        public ResourceInfo getInfo(ProgressListener listener) {
            return original.getInfo(listener);
        }

        public String[] getMetadataNames(ProgressListener listener) throws IOException {
            return original.getMetadataNames(listener);
        }

        public String getMetadataValue(String name, ProgressListener listener) throws IOException {
            return original.getMetadataValue(name, listener);
        }

        public Name getName(ProgressListener listener) {
            return original.getName(listener);
        }

        public int[] getOptimalDataBlockSizes() {
            return original.getOptimalDataBlockSizes();
        }

        public RangeType getRangeType(ProgressListener listener) throws IOException {
            return original.getRangeType(listener);
        }

        public List<Rectangle> getRasterDomain(boolean overall, ProgressListener listener)
                throws IOException {
            return original.getRasterDomain(overall, listener);
        }

        public Map<String, Parameter<?>> getReadParameterInfo() {
            return original.getReadParameterInfo();
        }

        public Set<TemporalGeometricPrimitive> getTemporalDomain(ProgressListener listener)
                throws IOException {
            return original.getTemporalDomain(listener);
        }

        public Set<org.opengis.geometry.Envelope> getVerticalDomain(boolean overall,
                ProgressListener listener) throws IOException {
            return original.getVerticalDomain(overall, listener);
        }

        public CoverageResponse read(CoverageReadRequest request, ProgressListener listener)
                throws IOException {
            this.issuedRequest = request;
            return original.read(request, listener);
        }
    }
}
