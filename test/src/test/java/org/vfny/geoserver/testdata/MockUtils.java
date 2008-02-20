/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.testdata;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockServletContext;
import org.geoserver.wfs.WFS;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.feature.SchemaException;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.DataStoreInfoDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.global.dto.NameSpaceInfoDTO;
import org.vfny.geoserver.global.dto.ServiceDTO;
import org.vfny.geoserver.global.dto.StyleDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Provides utility methods for the creation of mock objects to help in unit
 * testing specific geoserver classes.
 * <p>
 * Of interest is the {@linkplain #newHttpRequest(Map, boolean)} method which
 * provides a fully functional mocked up GeoServer instance configured with a
 * PropertyDataStore holding CITE FeatureTypes and convenient styles, etc.
 * </p>
 * <p>
 * The PropertyDataStore is created against a temporary directory so it is safe
 * to make transactions on each test. A further improvement would be to create
 * just a MemoryDataStore, but it was easier to reuse the code in
 * AbstractCiteDataTest in the while.
 * </p>
 *
 * @author Gabriel Roldan
 *
 */
public class MockUtils {
    private MockUtils() {
    }

    /**
     *
     * @param includeMockGeoserver
     *            if <code>true</code>, the generated HttpServletRequest
     *            includes a mock geoserver configuration for the cite test
     *            data, so you can use it, for example, for unit testing request
     *            readers.
     * @return
     */
    public static MockHttpServletRequest newHttpRequest(boolean includeMockGeoserver)
        throws ConfigurationException, IOException {
        return newHttpRequest(Collections.EMPTY_MAP, includeMockGeoserver);
    }

    /**
     * Creates a mock HttpServletRequest with the provided set of request
     * parameters, and possibly a fully configured GeoServer with cite test data
     * on the request's HttpServletContext.
     *
     * @param initialParams
     *            a map of request parameters to construct the mock http request
     *            with, where keys are parameter names, and values may be a
     *            single String or a String[] if there are multiple values for
     *            the same request parameter.
     * @param includeMockGeoserver
     *            if <code>true</code>, the generated HttpServletRequest
     *            includes a mock geoserver configuration for the cite test
     *            data, so you can use it, for example, for unit testing request
     *            readers.
     * @return
     */
    public static MockHttpServletRequest newHttpRequest(Map /* <String, String> */ initialParams,
        boolean includeMockGeoServer) throws ConfigurationException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();

        for (Iterator it = initialParams.entrySet().iterator(); it.hasNext();) {
            Map.Entry e = (Map.Entry) it.next();

            String name = (String) e.getKey();
            Object value = e.getValue();

            if ((value != null) && value.getClass().isArray()) {
                if (value.getClass().getComponentType() != String.class) {
                    throw new IllegalArgumentException("provided an illegal parameter for " + name
                        + ": " + value);
                }

                request.setupAddParameter(name, (String[]) value);
            } else {
                request.setupAddParameter(name, (String) value);
            }
        }

        if (includeMockGeoServer) {
            setUpMockGeoServer(request);
        }

        return request;
    }

    /**
     * * public static WMS getWMS(HttpServletRequest request) { ServletRequest
     * req = request; HttpSession session = request.getSession(); ServletContext
     * context = session.getServletContext();
     *
     * return (WMS) context.getAttribute(WMS.WEB_CONTAINER_KEY); }
     *
     * @param request
     */
    private static void setUpMockGeoServer(MockHttpServletRequest request)
        throws ConfigurationException, IOException {
        final GeoServer mockGeoServer = new GeoServer();
        final Data citeData = createTestCiteData(mockGeoServer);
        WMSDTO wmsDto = newWmsDto();
        WMS wms = new WMS(wmsDto) {
                public Data getData() {
                    return citeData;
                }
            };

        WFSDTO wfsDto = newWfsDto();
        WFS wfs = new WFS(wfsDto) {
                public Data getData() {
                    return citeData;
                }
            };

        MockHttpSession session = new MockHttpSession();
        MockServletContext ctx = new MockServletContext();

        ctx.setAttribute(WMS.WEB_CONTAINER_KEY, wms);
        ctx.setAttribute(WFS.WEB_CONTAINER_KEY, wfs);
        ctx.setAttribute(GeoServer.WEB_CONTAINER_KEY, mockGeoServer);

        session.setupServletContext(ctx);
        request.setSession(session);
    }

    /**
     * Creates a new mock WMS config object.
     * <p>
     * The creates WMS config object has no data (i.e. getData() returns null)
     * </p>
     *
     * @return
     */
    public static WMSDTO newWmsDto() {
        WMSDTO dto = new WMSDTO();
        dto.setGmlPrefixing(true);

        ServiceDTO service = new ServiceDTO();
        service.setAbstract("test abstract");
        service.setAccessConstraints("NONE");
        service.setEnabled(true);
        service.setFees("NONE");
        service.setKeywords(Arrays.asList(new String[] { "test", "mock", "service", "config" }));
        service.setMaintainer("Gabriel Roldan");
        service.setName("WMS");

        try {
            service.setOnlineResource(new URL("http://www.axios.es"));
        } catch (Exception e) {
            // no-op
        }

        service.setTitle("My mock WMS");
        dto.setService(service);

        return dto;
    }

    /**
     * Creates a new mock WMS config object.
     * <p>
     * The creates WMS config object has no data (i.e. getData() returns null)
     * </p>
     *
     * @return
     */
    public static WFSDTO newWfsDto() {
        WFSDTO dto = new WFSDTO();
        dto.setCiteConformanceHacks(true);

        ServiceDTO service = new ServiceDTO();
        service.setAbstract("test abstract");
        service.setAccessConstraints("NONE");
        service.setEnabled(true);
        service.setFees("NONE");
        service.setKeywords(Arrays.asList(new String[] { "test", "mock", "service", "config" }));
        service.setMaintainer("Gabriel Roldan");
        service.setName("WMS");

        try {
            service.setOnlineResource(new URL("http://www.axios.es"));
        } catch (Exception e) {
            // no-op
        }

        service.setTitle("My mock WMS");
        dto.setService(service);
        dto.setServiceLevel(WFSDTO.COMPLETE);

        return dto;
    }

    /**
     *
     * @param geoserver
     * @return
     * @throws ConfigurationException
     * @throws IOException
     */
    public static Data createTestCiteData(GeoServer geoserver)
        throws ConfigurationException, IOException {
        DataDTO dataDto = new DataDTO();
        File dir = null;

        URL testDataUrl = MockUtils.class.getResource("test-data");
        System.out.println(testDataUrl);

        if (!"file".equals(testDataUrl.getProtocol())) {
            throw new IOException("unsupported protocol: " + testDataUrl.getProtocol());
        }

        String url = testDataUrl.toExternalForm();
        String testPath = url.substring("file:".length());
        dir = new File(testPath);

        if (!dir.exists() || !dir.isDirectory()) {
            throw new ConfigurationException("Expected cite test dataset directory at " + dir);
        }

        Map formats = new HashMap();
        dataDto.setFormats(formats);

        Map coverages = new HashMap();
        dataDto.setCoverages(coverages);

        Map dataStores = createDataStoresMap();
        dataDto.setDataStores(dataStores);

        Map featureTypes = createFeatureTypes();
        dataDto.setFeaturesTypes(featureTypes);

        Map nameSpaces = createNameSpaces();
        dataDto.setNameSpaces(nameSpaces);
        dataDto.setDefaultNameSpacePrefix("cite");

        Map styles = createStyles(new File(dir, "styles"));
        dataDto.setStyles(styles);

        Data catalog = new Data(dataDto, dir, geoserver);

        return catalog;
    }

    private static Map createDataStoresMap() throws IOException {
        Map map = new HashMap();
        DataStoreInfoDTO dsDto = new DataStoreInfoDTO();
        dsDto.setAbstract("test cite data for unit testing geoserver");
        dsDto.setEnabled(true);
        dsDto.setId("cite");
        dsDto.setNameSpaceId("cite");
        dsDto.setTitle("same as abstract");

        final File envTmpDir = new File(System.getProperty("java.io.tmpdir"));
        File tempDir = new File(envTmpDir, "cite_test_datastore");
        createCiteDataStore(tempDir);

        Map dsConnectionParams = new HashMap();
        dsConnectionParams.put("directory", tempDir);
        dsDto.setConnectionParams(dsConnectionParams);

        map.put("cite", dsDto);

        return map;
    }

    private static Map createFeatureTypes() {
        Map map = new HashMap();

        FeatureTypeInfoDTO ftDto;

        for (int i = 0; i < AbstractCiteDataTest.CITE_TYPE_NAMES.length; i++) {
            String typeName = AbstractCiteDataTest.CITE_TYPE_NAMES[i];
            ftDto = new FeatureTypeInfoDTO();
            ftDto.setAbstract(typeName + " abstract");
            ftDto.setDataStoreId("cite");
            ftDto.setDefaultStyle(typeName);
            ftDto.setDirName(null);
            ftDto.setName(typeName);
            ftDto.setSRS(4326);
            ftDto.setTitle("title for " + typeName);

            map.put(typeName, ftDto);
        }

        return map;
    }

    private static Map createNameSpaces() {
        Map map = new HashMap();
        NameSpaceInfoDTO ns = new NameSpaceInfoDTO();
        ns.setDefault(true);
        ns.setPrefix("cite");
        ns.setUri("http://www.axios.es");
        map.put("cite", ns);

        return map;
    }

    private static Map createStyles(File baseDir) {
        Map map = new HashMap();

        StyleDTO dto = new StyleDTO();
        dto.setDefault(false);
        dto.setFilename(new File(baseDir, "default.sld"));
        dto.setId("default");
        map.put("default", dto);

        for (int i = 0; i < AbstractCiteDataTest.CITE_TYPE_NAMES.length; i++) {
            String typeName = AbstractCiteDataTest.CITE_TYPE_NAMES[i];
            String sldName = typeName + ".sld";
            File sldFile = new File(baseDir, sldName);

            if (!sldFile.exists()) {
                System.err.println("Style file not found, unsing default.sld: " + sldFile);

                continue;
            }

            dto = new StyleDTO();
            dto.setDefault(false);
            dto.setFilename(sldFile);
            dto.setId(typeName);
            map.put(typeName, dto);
        }

        return map;
    }

    /**
     * Returns a <code>DataStore</code> containing CITE feature types.
     *
     * @return a property files backed DataStore which forces all the
     *         FeatureTypes it serves to be in WGS84 CRS.
     *
     * @throws IOException
     *             DOCUMENT ME!
     */
    public static DataStore createCiteDataStore(File tempDir)
        throws IOException {
        writeTempFiles(tempDir);

        DataStore propsDS = new ForceWGS84PropertyDataStore(tempDir);

        return propsDS;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException
     *             DOCUMENT ME!
     */
    private static void writeTempFiles(File tempDir) throws IOException {
        if (tempDir.exists()) {
            tempDir.delete();
        }

        tempDir.mkdir();
        

        if (!tempDir.exists() || !tempDir.isDirectory()) {
            throw new IOException(tempDir.getAbsolutePath() + " is not a writable directory");
        }

        for (int i = 0; i < AbstractCiteDataTest.CITE_TYPE_NAMES.length; i++) {
            writeTempFile(tempDir, AbstractCiteDataTest.CITE_TYPE_NAMES[i]);
        }
        tempDir.deleteOnExit();
    }

    /**
     * DOCUMENT ME!
     *
     * @param typeName
     *            DOCUMENT ME!
     *
     * @throws IOException
     *             DOCUMENT ME!
     * @throws NullPointerException
     *             DOCUMENT ME!
     */
    private static void writeTempFile(File tempDir, final String typeName)
        throws IOException {
        final String fileName = typeName + ".properties";

        File outFile = new File(tempDir, fileName);

        // perhaps it was not deleted in a previous, broken run...
        deleteTempFile(tempDir, typeName);

        // Atomically creates a new, empty file named by this abstract
        // pathname if and only if a file with this name does not yet exist.
        outFile.createNewFile();

        // Request that the file or directory denoted by this abstract
        // pathname be deleted when the virtual machine terminates.
        outFile.deleteOnExit();

        String resourceName = "test-data/featureTypes/" + fileName;

        InputStream in = MockUtils.class.getResourceAsStream(resourceName);

        if (in == null) {
            throw new NullPointerException(resourceName + " not found in classpath");
        }

        OutputStream out = new java.io.FileOutputStream(outFile);
        byte[] buff = new byte[512];
        int count;

        while ((count = in.read(buff)) > -1) {
            out.write(buff, 0, count);
        }

        in.close();
        out.flush();
        out.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param typeName
     *            DOCUMENT ME!
     */
    private static void deleteTempFile(File tempDir, String typeName) {
        deleteTempFile(new File(tempDir, typeName + ".properties"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param f
     *            DOCUMENT ME!
     */
    private static void deleteTempFile(File f) {
        if(f.exists() && !f.delete())
            throw new RuntimeException("Could not delete file " + f);
    }

    /**
     * DOCUMENT ME!
     *
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private static class ForceWGS84PropertyDataStore extends PropertyDataStore {
        /**
         * Creates a new ForceWGS84PropertyDataStore object.
         *
         * @param dir
         *            DOCUMENT ME!
         */
        public ForceWGS84PropertyDataStore(File dir) {
            super(dir);
        }

        /**
         * DOCUMENT ME!
         *
         * @param typeName
         *            DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws IOException
         *             DOCUMENT ME!
         * @throws DataSourceException
         *             DOCUMENT ME!
         */
        public SimpleFeatureType getSchema(String typeName) throws IOException {
            SimpleFeatureType schema = super.getSchema(typeName);

            try {
                return DataUtilities.createSubType(schema, null, DefaultGeographicCRS.WGS84);
            } catch (SchemaException e) {
                throw new DataSourceException(e.getMessage(), e);
            }
        }

        /**
         * DOCUMENT ME!
         */

        /*
         * public FeatureReader getFeatureReader(Query query, Transaction
         * transaction) throws IOException { FeatureReader<SimpleFeatureType, SimpleFeature> reader =
         * super.getFeatureReader(query, transaction); try { return new
         * ForceCoordinateSystemFeatureReader(reader,
         * AbstractCiteDataTest.FORCED_WGS84); } catch (SchemaException e) {
         * throw new DataSourceException(e.getMessage(), e); } }
         */
    }
}
