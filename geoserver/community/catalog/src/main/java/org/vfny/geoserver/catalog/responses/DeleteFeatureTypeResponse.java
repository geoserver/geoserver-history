/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog.responses;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
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
import org.vfny.geoserver.catalog.requests.DeleteFeatureTypeRequest;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.global.xml.XMLConfigWriter;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public class DeleteFeatureTypeResponse implements Response {
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
    private static final String FOOTER = "\n</DeleteFeatureType>";

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

        if (!(request instanceof DeleteFeatureTypeRequest)) {
            throw new CatalogException(new StringBuffer(
                    "illegal request type, expected DescribeRequest, got ").append(
                    request).toString());
        }

        DeleteFeatureTypeRequest catalogRequest = (DeleteFeatureTypeRequest) request;

        root = (dataDirectory() != null) ? dataDirectory() : new File(".");

        try {
            root = ReaderUtils.checkFile(root, true);
        } catch (Exception e) {
            throw new CatalogException(e);
        }

        try {
            String featureTypeId = catalogRequest.getFeatureTypeId();

            // //
            // Removing the FeatureType
            // //
            // update the data config
            DataConfig dataConfig = ConfigRequests.getDataConfig(req
                    .getHttpServletRequest());

            // //
            // Retrieving FeatureType ...
            // //
            if (LOGGER.isLoggable(Level.CONFIG)) {
                LOGGER.config("Decoding featureType ID: " + featureTypeId);
            }

            FeatureTypeInfoDTO featureType = dataConfig.getFeatureTypeConfig(featureTypeId)
                                                       .toDTO();

            // //
            // Updating the catalog.
            // //
            dataConfig.removeFeatureType(featureTypeId);
            dataConfig.removeDataStore(featureType.getDataStoreId());

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

    private final String generateResponse(
        DeleteFeatureTypeRequest catalogRequest) throws CatalogException {
        // Initialize return information and intermediate return objects
        StringBuffer tempResponse = new StringBuffer();

        tempResponse.append("<?xml version=\"1.0\" encoding=\"")
                    .append(catalogRequest.getGeoServer().getCharSet()
                                          .displayName()).append("\"?>")
                    .append("\n<DeleteFeatureType version=").append(CURR_VER)
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
                    .append("deleteFeatureType.xsd\">\n\n");

        tempResponse.append("Feature Type Succesfully Removed!");
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
}
