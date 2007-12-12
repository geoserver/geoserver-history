/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog.responses;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.geoserver.util.ReaderUtils;
import org.geotools.factory.Hints;
import org.geotools.referencing.FactoryFinder;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransformFactory;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.catalog.CatalogException;
import org.vfny.geoserver.catalog.requests.CATALOGRequest;
import org.vfny.geoserver.catalog.requests.UpdateRequest;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.NameSpaceConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.dto.CoverageStoreInfoDTO;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.DataStoreInfoDTO;
import org.vfny.geoserver.global.dto.NameSpaceInfoDTO;
import org.vfny.geoserver.global.dto.StyleDTO;
import org.vfny.geoserver.global.xml.XMLConfigWriter;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public class UpdateResponse implements Response {
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");
    private static final String CURR_VER = "\"1.0\"";
    private static final String CATALOG_URL = "http://www.geo-solutions.it/schemas/catalog";
    private static final String CATALOG_NAMESPACE = new StringBuffer(
            "\n  xmlns=\"").append(CATALOG_URL).append("\"").toString();
    private static final String XLINK_URL = "\"http://www.w3.org/1999/xlink\"";
    private static final String XLINK_NAMESPACE = new StringBuffer(
            "\n  xmlns:xlink=").append(XLINK_URL).toString();
    private static final String OGC_URL = "\"http://www.opengis.net/ogc\"";
    private static final String OGC_NAMESPACE = new StringBuffer(
            "\n  xmlns:ogc=").append(OGC_URL).toString();
    private static final String GML_URL = "\"http://www.opengis.net/gml\"";
    private static final String GML_NAMESPACE = new StringBuffer(
            "\n  xmlns:gml=").append(GML_URL).toString();
    private static final String SCHEMA_URI = "\"http://www.w3.org/2001/XMLSchema-instance\"";
    private static final String XSI_NAMESPACE = new StringBuffer(
            "\n  xmlns:xsi=").append(SCHEMA_URI).toString();

    /** Fixed return footer information */
    private static final String FOOTER = "\n</UpdateCatalog>";

    /**
     * The default coordinate reference system factory.
     */

    // protected final static CRSFactory crsFactory =
    // FactoryFinder.getCRSFactory(new
    // Hints(Hints.CRS_AUTHORITY_FACTORY,EPSGCRSAuthorityFactory.class));
    protected final static CRSFactory crsFactory = FactoryFinder.getCRSFactory(new Hints(
                Hints.CRS_AUTHORITY_FACTORY, CRSAuthorityFactory.class));

    /**
     * The default transformations factory.
     */
    protected final static CoordinateOperationFactory opFactory = FactoryFinder
        .getCoordinateOperationFactory(new Hints(Hints.LENIENT_DATUM_SHIFT,
                Boolean.TRUE));

    /** The root directory from which the configuration is loaded. */
    private File root;

    /**
     * DataConfig
     */
    private DataConfig dataConfig;

    /** Main XML class for interpretation and response. */
    private String xmlResponse = new String();

    /**
     * The default datum factory.
     *
     * @uml.property name="datumFactory"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    protected final DatumFactory datumFactory = FactoryFinder.getDatumFactory(null);

    /**
     * The default math transform factory.
     *
     * @uml.property name="mtFactory"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    protected final MathTransformFactory mtFactory = FactoryFinder
        .getMathTransformFactory(null);

    /**
     * Returns any extra headers that this service might want to set in the HTTP
     * response object.
     *
     * @see org.vfny.geoserver.Response#getResponseHeaders()
     */
    public HashMap getResponseHeaders() {
        return null;
    }

    public synchronized void execute(Request req) throws CatalogException {
        CATALOGRequest request = (CATALOGRequest) req;

        if (!(request instanceof UpdateRequest)) {
            throw new CatalogException(new StringBuffer(
                    "illegal request type, expected DescribeRequest, got ").append(
                    request).toString());
        }

        UpdateRequest catalogRequest = (UpdateRequest) request;

        root = (dataDirectory() != null) ? dataDirectory() : new File(".");

        try {
            root = ReaderUtils.checkFile(root, true);
        } catch (Exception e) {
            throw new CatalogException(e);
        }

        try {
            File styleDir = GeoserverDataDirectory.findConfigDir(root, "styles/");

            dataConfig = ConfigRequests.getDataConfig(req.getHttpServletRequest());

            String catalogData = catalogRequest.getData();

            Element catalogElem = null;

            StringReader sr = new StringReader(catalogData);
            catalogElem = ReaderUtils.parse(sr);
            sr.close();

            // //
            // Loading Namespaces ...
            // //
            Map namepsaces = loadNameSpaces(ReaderUtils.getChildElement(
                        catalogElem, "namespaces", true));

            // //
            // Loading Styles ...
            // //
            Map styles = loadStyles(ReaderUtils.getChildElement(catalogElem,
                        "styles", false), styleDir);

            // //
            // Loading DataStores ...
            // //
            Element datastoresElem = ReaderUtils.getChildElement(catalogElem,
                    "datastores", false);
            Map datastores = new HashMap();

            if (datastoresElem != null) {
                datastores = loadDataStores(datastoresElem, namepsaces);
            }

            // //
            // Loading CoverageStores ...
            // //
            Element coveragestoresElem = ReaderUtils.getChildElement(catalogElem,
                    "formats", false);
            Map coveragestores = new HashMap();

            if (coveragestoresElem != null) {
                coveragestores = loadCoverageStores(coveragestoresElem);
            }

            // //
            // Updating the catalog.
            // //
            // update the data config
            dataConfig.getNameSpaces().putAll(namepsaces);
            dataConfig.getDataStores().putAll(datastores);
            dataConfig.getDataFormats().putAll(coveragestores);
            dataConfig.getStyles().putAll(styles);

            request.getCATALOG().getData().load(dataConfig.toDTO());

            // //
            // Save the XML ...
            // //
            File rootDir = GeoserverDataDirectory.getGeoserverDataDirectory();

            try {
                XMLConfigWriter.store((DataDTO) request.getCATALOG().getData()
                                                       .toDTO(), rootDir);
            } catch (ConfigurationException e) {
                e.printStackTrace();
                throw new ServletException(e);
            }
        } catch (ConfigurationException e) {
            throw new CatalogException(e, "", getClass().getName());
        } catch (Exception e) {
            throw new CatalogException(e, "", getClass().getName());
        }

        // generates response, using general function
        xmlResponse = generateResponse(catalogRequest);

        if (!request.getCATALOG().getGeoServer().isVerbose()) {
            xmlResponse = xmlResponse.replaceAll(">\n[ \\t\\n]*", ">");
            xmlResponse = xmlResponse.replaceAll("\n[ \\t\\n]*", " ");
        }
    }

    public String getContentType(GeoServer gs) {
        return gs.getMimeType();
    }

    public String getContentEncoding() {
        return null;
    }

    public String getContentDisposition() {
        return null;
    }

    public void writeTo(OutputStream out) throws CatalogException {
        try {
            byte[] content = xmlResponse.getBytes();
            out.write(content);
        } catch (IOException ex) {
            throw new CatalogException(ex, "", getClass().getName());
        }
    }

    private final String generateResponse(UpdateRequest catalogRequest)
        throws CatalogException {
        // Initialize return information and intermediate return objects
        StringBuffer tempResponse = new StringBuffer();

        tempResponse.append("<?xml version=\"1.0\" encoding=\"")
                    .append(catalogRequest.getGeoServer().getCharSet()
                                          .displayName()).append("\"?>")
                    .append("\n<UpdateCatalog version=").append(CURR_VER)
                    .append(" ").toString();

        tempResponse.append(CATALOG_NAMESPACE);
        tempResponse.append(XLINK_NAMESPACE);
        tempResponse.append(OGC_NAMESPACE);
        tempResponse.append(GML_NAMESPACE);
        tempResponse.append(XSI_NAMESPACE);
        /*
         * tempResponse.append("
         * xsi:schemaLocation=\"").append(CATALOG_URL).append( "
         * ").append(request.getSchemaBaseUrl()).append(
         * "catalog/1.0.0/describeCoverage.xsd\">\n\n");
         */
        tempResponse.append(" xsi:schemaLocation=\"").append(CATALOG_URL)
                    .append(" ")
                    .append("http://www.geo-solutions.it/catalog/1.0/")
                    .append("updateCatalog.xsd\">\n\n");

        tempResponse.append("Catalog Succesfully Updated!");
        tempResponse.append(FOOTER);

        return tempResponse.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.vfny.geoserver.responses.Response#abort()
     */
    public void abort(Service gs) {
        // nothing to undo
    }

    // ------------------------------------------------------------------------
    // HELPER METHODS
    // ------------------------------------------------------------------------
    /**
     * returns the GeoServer Data Dir
     */
    private File dataDirectory() {
        // ServletContext sc =
        // this.request.getServiceRef().getApplicationContext().getServletContext();
        return GeoserverDataDirectory.getGeoserverDataDirectory();
    }

    /**
     * loadNameSpaces purpose.
     * <p>
     * Converts a DOM tree into a Map of NameSpaces.
     * </p>
     *
     * @param nsRoot
     *            a DOM tree to convert into a Map of NameSpaces.
     *
     * @return A complete Map of NameSpaces loaded from the DOM tree provided.
     *
     * @throws ConfigurationException
     *             When an error occurs.
     */
    private Map loadNameSpaces(Element nsRoot) throws ConfigurationException {
        NodeList nsList = nsRoot.getElementsByTagName("namespace");
        Element elem;
        int nsCount = nsList.getLength();
        Map nameSpaces = new HashMap(nsCount);

        try {
            for (int i = 0; i < nsCount; i++) {
                elem = (Element) nsList.item(i);

                NameSpaceInfoDTO ns = new NameSpaceInfoDTO();
                ns.setUri(ReaderUtils.getAttribute(elem, "uri", true));
                ns.setPrefix(ReaderUtils.getAttribute(elem, "prefix", true));
                ns.setDefault(ReaderUtils.getBooleanAttribute(elem, "default",
                        false, false) || (nsCount == 1));

                if (LOGGER.isLoggable(Level.CONFIG)) {
                    LOGGER.config(new StringBuffer("added namespace ").append(
                            ns).toString());
                }

                nameSpaces.put(ns.getPrefix(), new NameSpaceConfig(ns));
            }
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }

        return nameSpaces;
    }

    /**
     * loadCoverageStores purpose.
     * <p>
     * Converts a DOM tree into a Map of Formats.
     * </p>
     *
     * @param fmRoot
     *            a DOM tree to convert into a Map of Formats.
     *
     * @return A complete Map of Formats loaded from the DOM tree provided.
     *
     * @throws ConfigurationException
     *             When an error occurs.
     */
    private Map loadCoverageStores(Element fmRoot)
        throws ConfigurationException {
        Map formats = new HashMap();

        if (fmRoot == null) { // if there are no formats (they are using
                              // v.1.4)

            return formats; // just return the empty list
        }

        NodeList fmElements = fmRoot.getElementsByTagName("format");
        int fmCnt = fmElements.getLength();
        CoverageStoreInfoDTO fmConfig;
        Element fmElem;

        for (int i = 0; i < fmCnt; i++) {
            fmElem = (Element) fmElements.item(i);

            try {
                fmConfig = loadFormat(fmElem);

                if (formats.containsKey(fmConfig.getId())) {
                    LOGGER.warning("Ignored duplicated format.");
                } else {
                    formats.put(fmConfig.getId(),
                        new CoverageStoreConfig(fmConfig));
                }
            } catch (ConfigurationException e) {
                LOGGER.log(Level.WARNING, "Ignored a misconfigured coverage.", e);
            }
        }

        return formats;
    }

    /**
     * loadFormat purpose.
     * <p>
     * Converts a DOM tree into a CoverageStoreInfo object.
     * </p>
     *
     * @param fmElem
     *            a DOM tree to convert into a CoverageStoreInfo object.
     *
     * @return A complete CoverageStoreInfo object loaded from the DOM tree
     *         provided.
     *
     * @throws ConfigurationException
     *             When an error occurs.
     */
    private CoverageStoreInfoDTO loadFormat(Element fmElem)
        throws ConfigurationException {
        CoverageStoreInfoDTO fm = new CoverageStoreInfoDTO();

        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("creating a new FormatDTO configuration");
        }

        try {
            fm.setId(ReaderUtils.getAttribute(fmElem, "id", true));

            String namespacePrefix = ReaderUtils.getAttribute(fmElem,
                    "namespace", true);

            if (dataConfig.getNameSpaces().containsKey(namespacePrefix)) {
                fm.setNameSpaceId(namespacePrefix);
            } else {
                LOGGER.warning("Could not find namespace " + namespacePrefix
                    + " defaulting to "
                    + dataConfig.getDefaultNameSpace().getPrefix());
                fm.setNameSpaceId(dataConfig.getDefaultNameSpace().getPrefix());
            }

            fm.setType(ReaderUtils.getChildText(fmElem, "type", true));
            fm.setUrl(ReaderUtils.getChildText(fmElem, "url", false));
            fm.setEnabled(ReaderUtils.getBooleanAttribute(fmElem, "enabled",
                    false, true));
            fm.setTitle(ReaderUtils.getChildText(fmElem, "title", false));
            fm.setAbstract(ReaderUtils.getChildText(fmElem, "description", false));

            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer(new StringBuffer(
                        "loading parameters for FormatDTO ").append(fm.getId())
                                                                                  .toString());
            }
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }

        return fm;
    }

    /**
     * loadDataStores purpose.
     * <p>
     * Converts a DOM tree into a Map of DataStores.
     * </p>
     *
     * @param dsRoot
     *            a DOM tree to convert into a Map of DataStores.
     *
     * @return A complete Map of DataStores loaded from the DOM tree provided.
     *
     * @throws ConfigurationException
     *             When an error occurs.
     */
    private Map loadDataStores(Element dsRoot, Map namespaces)
        throws ConfigurationException {
        Map dataStores = new HashMap();

        NodeList dsElements = dsRoot.getElementsByTagName("datastore");
        int dsCnt = dsElements.getLength();
        DataStoreInfoDTO dsConfig = null;
        Element dsElem;

        for (int i = 0; i < dsCnt; i++) {
            dsElem = (Element) dsElements.item(i);

            try {
                dsConfig = loadDataStore(dsElem, namespaces);

                if (dataStores.containsKey(dsConfig.getId())) {
                    LOGGER.warning("Ignored duplicated datastore with id "
                        + dsConfig.getId());
                } else {
                    dataStores.put(dsConfig.getId(),
                        new DataStoreConfig(dsConfig));
                }
            } catch (ConfigurationException e) {
                LOGGER.log(Level.WARNING, "Ignored a misconfigured datastore.",
                    e);
            }
        }

        return dataStores;
    }

    /**
     * loadDataStore purpose.
     * <p>
     * Converts a DOM tree into a DataStoreInfo object.
     * </p>
     *
     * @param dsElem
     *            a DOM tree to convert into a DataStoreInfo object.
     *
     * @return A complete DataStoreInfo object loaded from the DOM tree
     *         provided.
     *
     * @throws ConfigurationException
     *             When an error occurs.
     */
    private DataStoreInfoDTO loadDataStore(Element dsElem, Map namespaces)
        throws ConfigurationException {
        DataStoreInfoDTO ds = new DataStoreInfoDTO();

        try {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("creating a new DataStoreDTO configuration");
            }

            ds.setId(ReaderUtils.getAttribute(dsElem, "id", true));

            String namespacePrefix = ReaderUtils.getAttribute(dsElem,
                    "namespace", true);

            if (namespaces.containsKey(namespacePrefix)) {
                ds.setNameSpaceId(namespacePrefix);
            } else {
                LOGGER.warning("Could not find namespace " + namespacePrefix
                    + " defaulting to "
                    + dataConfig.getDefaultNameSpace().getPrefix());
                ds.setNameSpaceId(dataConfig.getDefaultNameSpace().getPrefix());
            }

            ds.setEnabled(ReaderUtils.getBooleanAttribute(dsElem, "enabled",
                    false, true));
            ds.setTitle(ReaderUtils.getChildText(dsElem, "title", false));
            ds.setAbstract(ReaderUtils.getChildText(dsElem, "description", false));

            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer(new StringBuffer(
                        "loading connection parameters for DataStoreDTO ").append(
                        ds.getNameSpaceId()).toString());
            }

            ds.setConnectionParams(loadConnectionParams(
                    ReaderUtils.getChildElement(dsElem, "connectionParams", true)));
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }

        if (LOGGER.isLoggable(Level.CONFIG)) {
            LOGGER.config(new StringBuffer("Loaded datastore ").append(
                    ds.getId()).toString());
        }

        return ds;
    }

    /**
     * loadConnectionParams purpose.
     * <p>
     * Converts a DOM tree into a Map of Strings which represent connection
     * parameters.
     * </p>
     *
     * @param connElem
     *            a DOM tree to convert into a Map of Strings which represent
     *            connection parameters.
     *
     * @return A complete Map of Strings which represent connection parameters
     *         loaded from the DOM tree provided.
     *
     * @throws ConfigurationException
     *             When an error occurs.
     */
    private Map loadConnectionParams(Element connElem)
        throws ConfigurationException {
        Map connectionParams = new HashMap();

        if (connElem == null) {
            return connectionParams;
        }

        NodeList paramElems = connElem.getElementsByTagName("parameter");
        int pCount = paramElems.getLength();
        Element param;
        String paramKey;
        String paramValue;

        try {
            for (int i = 0; i < pCount; i++) {
                param = (Element) paramElems.item(i);
                paramKey = ReaderUtils.getAttribute(param, "name", true);
                paramValue = ReaderUtils.getAttribute(param, "value", false);
                connectionParams.put(paramKey, paramValue);

                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer(new StringBuffer("added parameter ").append(
                            paramKey).append(": '")
                                                                     .append(paramValue
                            .replaceAll("'", "\"")).append("'").toString());
                }
            }
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }

        return connectionParams;
    }

    /**
     * loadStyles purpose.
     * <p>
     * Converts a DOM tree into a Map of Styles.
     * </p>
     *
     * @param stylesElem
     *            a DOM tree to convert into a Map of Styles.
     * @param baseDir
     *            DOCUMENT ME!
     *
     * @return A complete Map of Styles loaded from the DOM tree provided.
     *
     * @throws ConfigurationException
     *             When an error occurs.
     */
    private Map loadStyles(Element stylesElem, File baseDir)
        throws ConfigurationException {
        Map styles = new HashMap();

        NodeList stylesList = null;

        if (stylesElem != null) {
            stylesList = stylesElem.getElementsByTagName("style");
        }

        if ((stylesList == null) || (stylesList.getLength() == 0)) {
            // no styles where defined, just add a default one
            StyleDTO s = new StyleDTO();
            s.setId("normal");
            s.setFilename(new File(baseDir, "normal.sld"));
            s.setDefault(true);
            styles.put("normal", new StyleConfig(s));
        }

        int styleCount = stylesList.getLength();
        Element styleElem;

        for (int i = 0; i < styleCount; i++) {
            try {
                styleElem = (Element) stylesList.item(i);

                StyleDTO s = new StyleDTO();
                s.setId(ReaderUtils.getAttribute(styleElem, "id", true));
                s.setFilename(new File(baseDir,
                        ReaderUtils.getAttribute(styleElem, "filename", true)));
                s.setDefault(ReaderUtils.getBooleanAttribute(styleElem,
                        "default", false, false));
                styles.put(s.getId(), new StyleConfig(s));

                if (LOGGER.isLoggable(Level.CONFIG)) {
                    LOGGER.config(new StringBuffer("Loaded style ").append(
                            s.getId()).toString());
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Ignored misconfigured style", e);
            }
        }

        return styles;
    }
}
