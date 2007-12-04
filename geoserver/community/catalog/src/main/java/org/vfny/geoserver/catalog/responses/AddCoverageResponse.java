/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog.responses;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.geoserver.util.ReaderUtils;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.TransformException;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.catalog.CatalogException;
import org.vfny.geoserver.catalog.requests.AddCoverageRequest;
import org.vfny.geoserver.catalog.requests.CATALOGRequest;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.xml.XMLConfigReader;
import org.vfny.geoserver.global.xml.XMLConfigWriter;
import org.vfny.geoserver.util.CoverageStoreUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public class AddCoverageResponse implements Response {
    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.responses");
    private static final String CURR_VER = "\"1.0\"";
    private static final String CATALOG_URL = "http://www.geo-solutions.it/schemas/catalog";
    private static final String CATALOG_NAMESPACE = new StringBuffer("\n  xmlns=\"").append(CATALOG_URL)
                                                                                    .append("\"")
                                                                                    .toString();
    private static final String XLINK_URL = "\"http://www.w3.org/1999/xlink\"";
    private static final String XLINK_NAMESPACE = new StringBuffer("\n  xmlns:xlink=").append(XLINK_URL)
                                                                                      .toString();
    private static final String OGC_URL = "\"http://www.opengis.net/ogc\"";
    private static final String OGC_NAMESPACE = new StringBuffer("\n  xmlns:ogc=").append(OGC_URL)
                                                                                  .toString();
    private static final String GML_URL = "\"http://www.opengis.net/gml\"";
    private static final String GML_NAMESPACE = new StringBuffer("\n  xmlns:gml=").append(GML_URL)
                                                                                  .toString();
    private static final String SCHEMA_URI = "\"http://www.w3.org/2001/XMLSchema-instance\"";
    private static final String XSI_NAMESPACE = new StringBuffer("\n  xmlns:xsi=").append(SCHEMA_URI)
                                                                                  .toString();

    /** Fixed return footer information */
    private static final String FOOTER = "\n</AddCoverage>";

    /**
     * The default coordinate reference system factory.
     */

    // protected final static CRSFactory crsFactory =
    // FactoryFinder.getCRSFactory(new
    // Hints(Hints.CRS_AUTHORITY_FACTORY,EPSGCRSAuthorityFactory.class));
    protected final static CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(new Hints(
                Hints.CRS_AUTHORITY_FACTORY, CRSAuthorityFactory.class));

    /**
     * The default transformations factory.
     */
    protected final static CoordinateOperationFactory opFactory = ReferencingFactoryFinder
        .getCoordinateOperationFactory(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));

    /** The root directory from which the configuration is loaded. */
    private File root;

    /**
     * AddCoverageRequest request.
     */
    private AddCoverageRequest request;

    /** Main XML class for interpretation and response. */
    private String xmlResponse = new String();

    /**
     * The default datum factory.
     *
     * @uml.property name="datumFactory"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    protected final DatumFactory datumFactory = ReferencingFactoryFinder.getDatumFactory(null);

    /**
     * The default math transform factory.
     *
     * @uml.property name="mtFactory"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    protected final MathTransformFactory mtFactory = ReferencingFactoryFinder.getMathTransformFactory(null);

    /**
     * Returns any extra headers that this service might want to set in the HTTP
     * response object.
     *
     * @see org.vfny.geoserver.Response#getResponseHeaders()
     */
    public HashMap getResponseHeaders() {
        return null;
    }

    public void execute(Request req) throws CatalogException {
        CATALOGRequest request = (CATALOGRequest) req;

        if (!(request instanceof AddCoverageRequest)) {
            throw new CatalogException(new StringBuffer(
                    "illegal request type, expected DescribeRequest, got ").append(request)
                                                                                                               .toString());
        }

        AddCoverageRequest catalogRequest = (AddCoverageRequest) request;
        this.request = catalogRequest;
        LOGGER.finer("processing describe request" + catalogRequest);

        root = (dataDirectory() != null) ? dataDirectory() : new File(".");

        try {
            root = ReaderUtils.checkFile(root, true);
        } catch (Exception e) {
            throw new CatalogException(e);
        }

        try {
            String infoData = catalogRequest.getData();

            StringReader sr = new StringReader(infoData);

            // //
            // Loading Coverages ...
            // //
            CoverageInfoDTO coverage = loadCoverage(sr);
            coverage.setDirName(coverage.getFormatId() + "_" + coverage.getName());

            // //
            // Updating the catalog.
            // //
            String cvName = null;

            // Decode the URL of the FT. This is to catch colons
            // used in filenames
            cvName = URLDecoder.decode(coverage.getKey(), "UTF-8");

            if (LOGGER.isLoggable(Level.CONFIG)) {
                LOGGER.config("Decoding coverage name: " + cvName);
            }

            // //
            // Adding the Coverage
            // //
            // update the data config
            DataConfig dataConfig = ConfigRequests.getDataConfig(req.getHttpServletRequest());
            dataConfig.getCoverages().put(cvName, new CoverageConfig(coverage));

            request.getCATALOG().getData().load(dataConfig.toDTO());

            // //
            // Save the XML ...
            // //
            File rootDir = GeoserverDataDirectory.getGeoserverDataDirectory();

            try {
                XMLConfigWriter.store((DataDTO) request.getCATALOG().getData().toDTO(), rootDir);
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

    private final String generateResponse(AddCoverageRequest catalogRequest)
        throws CatalogException {
        // Initialize return information and intermediate return objects
        StringBuffer tempResponse = new StringBuffer();

        tempResponse.append("<?xml version=\"1.0\" encoding=\"")
                    .append(catalogRequest.getGeoServer().getCharSet().displayName()).append("\"?>")
                    .append("\n<AddCoverage version=").append(CURR_VER).append(" ").toString();

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
        tempResponse.append(" xsi:schemaLocation=\"").append(CATALOG_URL).append(" ")
                    .append("http://www.geo-solutions.it/catalog/1.0/")
                    .append("addCoverage.xsd\">\n\n");

        tempResponse.append("Coverage Succesfully Added!");
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
     * This method loads the coverage information DTO from an info.xml file on
     * the disk.
     *
     * @param infoFile
     *
     * @return
     *
     * @throws ConfigurationException
     * @throws IllegalArgumentException
     *             DOCUMENT ME!
     */
    private CoverageInfoDTO loadCoverage(Reader infoData)
        throws ConfigurationException {
        Element coverageElem = null;

        try {
            if (LOGGER.isLoggable(Level.CONFIG)) {
                LOGGER.config(new StringBuffer("Loading configuration data: ").append(infoData)
                                                                              .toString());
            }

            coverageElem = ReaderUtils.parse(infoData);
            infoData.close();
        } catch (Exception erk) {
            throw new ConfigurationException("Could not parse info file:" + infoData, erk);
        }

        // loding the DTO.
        CoverageInfoDTO dto = loadCoverageDTOFromXML(coverageElem);

        /*
         * File parentDir = infoFile.getParentFile();
         * dto.setDirName(parentDir.getName());
         */
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer(new StringBuffer("added coverageType ").append(dto.getName()).toString());
        }

        return dto;
    }

    /**
     * Creation of a DTo cfron an info.xml file for a coverage.
     *
     * @param coverageRoot
     *
     * @return
     *
     * @throws ConfigurationException
     */
    private CoverageInfoDTO loadCoverageDTOFromXML(Element coverageRoot)
        throws ConfigurationException {
        final CoverageInfoDTO cv = new CoverageInfoDTO();

        try {
            int length = 0;
            List l = null;
            int i = 0;
            String[] ss = null;
            // /////////////////////////////////////////////////////////////////////
            //
            // COVERAGEINFO DTO INITIALIZATION
            //
            // /////////////////////////////////////////////////////////////////////
            cv.setFormatId(ReaderUtils.getAttribute(coverageRoot, "format", true));
            cv.setName(ReaderUtils.getChildText(coverageRoot, "name", true));
            cv.setWmsPath(ReaderUtils.getChildText(coverageRoot, "wmspath" /* , true */));
            cv.setLabel(ReaderUtils.getChildText(coverageRoot, "label", true));
            cv.setDescription(ReaderUtils.getChildText(coverageRoot, "description"));

            // /////////////////////////////////////////////////////////////////////
            //
            // METADATA AND KEYORDS
            //
            // /////////////////////////////////////////////////////////////////////
            final String keywords = ReaderUtils.getChildText(coverageRoot, "keywords");

            if (keywords != null) {
                l = new ArrayList(10);
                ss = keywords.split(",");
                length = ss.length;

                for (i = 0; i < length; i++)
                    l.add(ss[i].trim());

                cv.setKeywords(l);
            }

            cv.setMetadataLink(XMLConfigReader.loadMetaDataLink(ReaderUtils.getChildElement(
                        coverageRoot, "metadataLink")));

            // /////////////////////////////////////////////////////////////////////
            //
            // DEAFULT STYLE
            //
            // /////////////////////////////////////////////////////////////////////
            final Element tmp = ReaderUtils.getChildElement(coverageRoot, "styles");

            if (tmp != null) {
                cv.setDefaultStyle(ReaderUtils.getAttribute(tmp, "default", false));

                final NodeList childrens = tmp.getChildNodes();
                final int numChildNodes = childrens.getLength();
                Node child;

                for (int n = 0; n < numChildNodes; n++) {
                    child = childrens.item(n);

                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().equals("style")) {
                            cv.addStyle(ReaderUtils.getElementText((Element) child));
                        }
                    }
                }
            }

            // /////////////////////////////////////////////////////////////////////
            //
            // CRS
            //
            // /////////////////////////////////////////////////////////////////////
            final Element envelope = ReaderUtils.getChildElement(coverageRoot, "envelope");
            cv.setSrsName(ReaderUtils.getAttribute(envelope, "srsName", true));

            final CoordinateReferenceSystem crs;

            try {
                crs = CRS.parseWKT(ReaderUtils.getAttribute(envelope, "crs", false)
                                              .replaceAll("'", "\""));
            } catch (FactoryException e) {
                throw new ConfigurationException(e);
            } catch (ConfigurationException e) {
                throw new ConfigurationException(e);
            }

            cv.setCrs(crs);
            cv.setSrsWKT(crs.toWKT());

            // /////////////////////////////////////////////////////////////////////
            //
            // ENVELOPE
            //
            // /////////////////////////////////////////////////////////////////////
            GeneralEnvelope gcEnvelope = XMLConfigReader.loadEnvelope(envelope, crs);
            cv.setEnvelope(gcEnvelope);

            try {
                cv.setLonLatWGS84Envelope(CoverageStoreUtils.getWGS84LonLatEnvelope(gcEnvelope));
            } catch (IndexOutOfBoundsException e) {
                throw new ConfigurationException(e);
            } catch (NoSuchAuthorityCodeException e) {
                throw new ConfigurationException(e);
            } catch (FactoryException e) {
                throw new ConfigurationException(e);
            } catch (TransformException e) {
                throw new ConfigurationException(e);
            }

            // /////////////////////////////////////////////////////////////////////
            //
            // GRID GEOMETRY
            //
            // /////////////////////////////////////////////////////////////////////
            final Element grid = ReaderUtils.getChildElement(coverageRoot, "grid");
            cv.setGrid(XMLConfigReader.loadGrid(grid, gcEnvelope, crs));

            // /////////////////////////////////////////////////////////////////////
            //
            // SAMPLE DIMENSIONS
            //
            // /////////////////////////////////////////////////////////////////////
            cv.setDimensionNames(XMLConfigReader.loadDimensionNames(grid));

            final NodeList dims = coverageRoot.getElementsByTagName("CoverageDimension");
            cv.setDimensions(XMLConfigReader.loadDimensions(dims));

            // /////////////////////////////////////////////////////////////////////
            //
            // SUPPORTED/REQUEST CRS
            //
            // /////////////////////////////////////////////////////////////////////
            final Element supportedCRSs = ReaderUtils.getChildElement(coverageRoot, "supportedCRSs");
            final String requestCRSs = ReaderUtils.getChildText(supportedCRSs, "requestCRSs");

            if (requestCRSs != null) {
                l = new LinkedList();
                ss = requestCRSs.split(",");

                length = ss.length;

                for (i = 0; i < length; i++)
                    l.add(ss[i].trim());

                cv.setRequestCRSs(l);
            }

            final String responseCRSs = ReaderUtils.getChildText(supportedCRSs, "responseCRSs");

            if (responseCRSs != null) {
                l = new LinkedList();
                ss = responseCRSs.split(",");
                length = ss.length;

                for (i = 0; i < length; i++)
                    l.add(ss[i].trim());

                cv.setResponseCRSs(l);
            }

            // /////////////////////////////////////////////////////////////////////
            //
            // SUPPORTED FORMATS
            //
            // /////////////////////////////////////////////////////////////////////
            final Element supportedFormats = ReaderUtils.getChildElement(coverageRoot,
                    "supportedFormats");
            cv.setNativeFormat(ReaderUtils.getAttribute(supportedFormats, "nativeFormat", true));

            final String formats = ReaderUtils.getChildText(supportedFormats, "formats");

            if (formats != null) {
                l = new LinkedList();
                ss = formats.split(",");
                length = ss.length;

                for (i = 0; i < length; i++)
                    l.add(ss[i].trim());

                cv.setSupportedFormats(l);
            }

            // /////////////////////////////////////////////////////////////////////
            //
            // SUPPORTED INTERPOLATIONS
            //
            // /////////////////////////////////////////////////////////////////////
            final Element supportedInterpolations = ReaderUtils.getChildElement(coverageRoot,
                    "supportedInterpolations");
            cv.setDefaultInterpolationMethod(ReaderUtils.getAttribute(supportedInterpolations,
                    "default", true));

            final String interpolations = ReaderUtils.getChildText(supportedInterpolations,
                    "interpolationMethods");

            if (interpolations != null) {
                l = new LinkedList();
                ss = interpolations.split(",");
                length = ss.length;

                for (i = 0; i < length; i++)
                    l.add(ss[i].trim());

                cv.setInterpolationMethods(l);
            }

            // /////////////////////////////////////////////////////////////////////
            //
            // READ PARAMETERS
            //
            // /////////////////////////////////////////////////////////////////////
            cv.setParameters(XMLConfigReader.loadConnectionParams(ReaderUtils.getChildElement(
                        coverageRoot, "parameters", false)));
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }

        return cv;
    }
}
