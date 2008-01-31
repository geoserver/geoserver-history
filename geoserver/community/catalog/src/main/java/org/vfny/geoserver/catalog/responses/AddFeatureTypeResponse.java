/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog.responses;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.geoserver.util.ReaderUtils;
import org.geotools.factory.Hints;
import org.geotools.filter.FilterDOMParser;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransformFactory;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.catalog.CatalogException;
import org.vfny.geoserver.catalog.requests.AddFeatureTypeRequest;
import org.vfny.geoserver.catalog.requests.CATALOGRequest;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.global.dto.LegendURLDTO;
import org.vfny.geoserver.global.xml.XMLConfigReader;
import org.vfny.geoserver.global.xml.XMLConfigWriter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vividsolutions.jts.geom.Envelope;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public class AddFeatureTypeResponse implements Response {
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
    private static final String FOOTER = "\n</AddFeatureType>";

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
    protected final MathTransformFactory mtFactory = ReferencingFactoryFinder
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

        if (!(request instanceof AddFeatureTypeRequest)) {
            throw new CatalogException(new StringBuffer(
                    "illegal request type, expected DescribeRequest, got ").append(request)
                                                                                                               .toString());
        }

        AddFeatureTypeRequest catalogRequest = (AddFeatureTypeRequest) request;

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
            // Loading FeatureTypes ...
            // //
            FeatureTypeInfoDTO featureType = loadFeature(sr);
            featureType.setDirName(featureType.getDataStoreId() + "_" + featureType.getName());

            // //
            // Updating the catalog.
            // //
            String ftName = null;

            // Decode the URL of the FT. This is to catch colons
            // used in filenames
            ftName = URLDecoder.decode(featureType.getKey(), "UTF-8");

            if (LOGGER.isLoggable(Level.CONFIG)) {
                LOGGER.config("Decoding featureType name: " + ftName);
            }

            // //
            // Adding the FeatureType
            // //
            // update the data config
            DataConfig dataConfig = ConfigRequests.getDataConfig(req.getHttpServletRequest());
            
            // //
            // Remove existing Coverages
            // //
            for (Iterator keySetIterator = dataConfig.getFeaturesTypes().keySet().iterator();
                    keySetIterator.hasNext();) {
                String key = (String) keySetIterator.next();

                if (((FeatureTypeConfig) dataConfig.getFeaturesTypes().get(key)).getName()
                         .equals(featureType.getName())) {
                    dataConfig.getFeaturesTypes().put(key, null);
                    dataConfig.getFeaturesTypes().remove(key);
                    request.getCATALOG().getData().load(dataConfig.toDTO());

                    break;
                }
            }            
            
            // //
            // Adding the new Coverage...
            // //
            dataConfig.getFeaturesTypes().put(ftName, new FeatureTypeConfig(featureType));

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

    private final String generateResponse(AddFeatureTypeRequest catalogRequest)
        throws CatalogException {
        // Initialize return information and intermediate return objects
        StringBuffer tempResponse = new StringBuffer();

        tempResponse.append("<?xml version=\"1.0\" encoding=\"")
                    .append(catalogRequest.getGeoServer().getCharSet().displayName()).append("\"?>")
                    .append("\n<AddFeatureType version=").append(CURR_VER).append(" ").toString();

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
                    .append("addFeatureType.xsd\">\n\n");

        tempResponse.append("Feature Type Succesfully Added!");
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
     * loadLatLongBBox purpose.<p>Converts a DOM tree into a Envelope
     * object.</p>
     *
     * @param bboxElem a DOM tree to convert into a Envelope object.
     *
     * @return A complete Envelope object loaded from the DOM tree provided.
     *
     * @throws ConfigurationException When an error occurs.
     */
    protected Envelope loadBBox(Element bboxElem) throws ConfigurationException {
        if (bboxElem == null) {
            return new Envelope();
        }

        try {
            boolean dynamic = ReaderUtils.getBooleanAttribute(bboxElem, "dynamic", false, true);

            if (!dynamic) {
                double minx = ReaderUtils.getDoubleAttribute(bboxElem, "minx", true);
                double miny = ReaderUtils.getDoubleAttribute(bboxElem, "miny", true);
                double maxx = ReaderUtils.getDoubleAttribute(bboxElem, "maxx", true);
                double maxy = ReaderUtils.getDoubleAttribute(bboxElem, "maxy", true);

                return new Envelope(minx, maxx, miny, maxy);
            }
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }

        return new Envelope();
    }

    /**
     * loadDefinitionQuery purpose.
     * <p>
     * Converts a DOM tree into a Filter object.
     * </p>
     *
     * @param typeRoot
     *            a DOM tree to convert into a Filter object.
     *
     * @return A complete Filter object loaded from the DOM tree provided.
     *
     * @throws ConfigurationException
     *             When an error occurs.
     */
    private org.opengis.filter.Filter loadDefinitionQuery(Element typeRoot)
        throws ConfigurationException {
        try {
            Element defQNode = ReaderUtils.getChildElement(typeRoot, "definitionQuery", false);
            org.opengis.filter.Filter filter = null;

            if (defQNode != null) {
                LOGGER.finer("definitionQuery element found, looking for Filter");

                Element filterNode = ReaderUtils.getChildElement(defQNode, "Filter", false);

                if ((filterNode != null)
                        && ((filterNode = ReaderUtils.getFirstChildElement(filterNode)) != null)) {
                    filter = FilterDOMParser.parseFilter(filterNode);

                    return filter;
                }

                LOGGER.finer("No Filter definition query found");
            }

            return filter;
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }

    /**
     * Load FeatureTypeInfoDTO from a directory.
     * <p>
     * Expected directory structure:
     * </p>
     * <ul>
     * <li>info.xml - required</li>
     * <li>schema.xml - optional</li>
     * </ul>
     * <p>
     * If a schema.xml file is not used, the information may be generated from a
     * FeatureType using DataTransferObjectFactory.
     * </p>
     *
     * @param infoFile
     *            a File to convert into a FeatureTypeInfo object. (info.xml)
     *
     * @return A complete FeatureTypeInfo object loaded from the File handle
     *         provided.
     *
     * @throws ConfigurationException
     *             When an error occurs.
     * @throws IllegalArgumentException
     *             DOCUMENT ME!
     *
     * @see loadFeaturePt2(Element)
     */
    private FeatureTypeInfoDTO loadFeature(StringReader infoData)
        throws ConfigurationException {
        Element featureElem = null;

        try {
            if (LOGGER.isLoggable(Level.CONFIG)) {
                LOGGER.config(new StringBuffer("Loading configuration data: ").append(infoData)
                                                                              .toString());
            }

            featureElem = ReaderUtils.parse(infoData);
            infoData.close();
        } catch (Exception erk) {
            throw new ConfigurationException("Could not parse info data:" + infoData, erk);
        }

        FeatureTypeInfoDTO dto = loadFeaturePt2(featureElem);

        /**
         * TODO Load Schema defined by user
         */
        if (LOGGER.isLoggable(Level.CONFIG)) {
            LOGGER.config(new StringBuffer("added featureType ").append(dto.getName()).toString());
        }

        return dto;
    }

    /**
     * loadFeaturePt2 purpose.
     * <p>
     * Converts a DOM tree into a FeatureTypeInfo object.
     * </p>
     *
     * @param fTypeRoot
     *            a DOM tree to convert into a FeatureTypeInfo object.
     *
     * @return A complete FeatureTypeInfo object loaded from the DOM tree
     *         provided.
     *
     * @throws ConfigurationException
     *             When an error occurs.
     */
    protected FeatureTypeInfoDTO loadFeaturePt2(Element fTypeRoot)
        throws ConfigurationException {
        FeatureTypeInfoDTO ft = new FeatureTypeInfoDTO();

        try {
            ft.setName(ReaderUtils.getChildText(fTypeRoot, "name", true));
            ft.setTitle(ReaderUtils.getChildText(fTypeRoot, "title", true));
            ft.setAbstract(ReaderUtils.getChildText(fTypeRoot, "abstract"));
            ft.setWmsPath(ReaderUtils.getChildText(fTypeRoot, "wmspath" /* , true */));

            String keywords = ReaderUtils.getChildText(fTypeRoot, "keywords");

            if (keywords != null) {
                List l = new LinkedList();
                String[] ss = keywords.split(",");

                for (int i = 0; i < ss.length; i++)
                    l.add(ss[i].trim());

                ft.setKeywords(l);
            }

            Element urls = ReaderUtils.getChildElement(fTypeRoot, "metadataLinks");

            if (urls != null) {
                Element[] childs = ReaderUtils.getChildElements(urls, "metadataLink");
                List l = new LinkedList();

                for (int i = 0; i < childs.length; i++) {
                    l.add(XMLConfigReader.getMetaDataLink(childs[i]));
                }

                ft.setMetadataLinks(l);
            }

            ft.setDataStoreId(ReaderUtils.getAttribute(fTypeRoot, "datastore", true));
            ft.setSRS(Integer.parseInt(ReaderUtils.getChildText(fTypeRoot, "SRS", true)));

            Element tmp = ReaderUtils.getChildElement(fTypeRoot, "styles");

            if (tmp != null) {
                ft.setDefaultStyle(ReaderUtils.getAttribute(tmp, "default", false));

                final NodeList childrens = tmp.getChildNodes();
                final int numChildNodes = childrens.getLength();
                Node child;

                for (int n = 0; n < numChildNodes; n++) {
                    child = childrens.item(n);

                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName().equals("style")) {
                            ft.addStyle(ReaderUtils.getElementText((Element) child));
                        }
                    }
                }
            }

            Element cacheInfo = ReaderUtils.getChildElement(fTypeRoot, "cacheinfo");

            if (cacheInfo != null) {
                ft.setCacheMaxAge(ReaderUtils.getAttribute(cacheInfo, "maxage", false)); // not mandatory
                ft.setCachingEnabled((new Boolean(ReaderUtils.getAttribute(cacheInfo, "enabled",
                            true))).booleanValue());
            }

            // Modif C. Kolbowicz - 06/10/2004
            Element legendURL = ReaderUtils.getChildElement(fTypeRoot, "LegendURL");

            if (legendURL != null) {
                LegendURLDTO legend = new LegendURLDTO();
                legend.setWidth(Integer.parseInt(ReaderUtils.getAttribute(legendURL, "width", true)));
                legend.setHeight(Integer.parseInt(ReaderUtils.getAttribute(legendURL, "height", true)));
                legend.setFormat(ReaderUtils.getChildText(legendURL, "Format", true));
                legend.setOnlineResource(ReaderUtils.getAttribute(ReaderUtils.getChildElement(
                            legendURL, "OnlineResource", true), "xlink:href", true));
                ft.setLegendURL(legend);
            }

            Envelope latLonBBox = loadBBox(ReaderUtils.getChildElement(fTypeRoot,
                        "latLonBoundingBox"));
            // -- Modif C. Kolbowicz - 06/10/2004
            ft.setLatLongBBox(latLonBBox);

            Envelope nativeBBox = loadBBox(ReaderUtils.getChildElement(fTypeRoot, "nativeBBox"));
            ft.setNativeBBox(nativeBBox);

            Element numDecimalsElem = ReaderUtils.getChildElement(fTypeRoot, "numDecimals", false);

            if (numDecimalsElem != null) {
                ft.setNumDecimals(ReaderUtils.getIntAttribute(numDecimalsElem, "value", false, 8));
            }

            ft.setDefinitionQuery(loadDefinitionQuery(fTypeRoot));
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }

        return ft;
    }
}
