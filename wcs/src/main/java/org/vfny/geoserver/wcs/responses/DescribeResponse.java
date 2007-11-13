/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses;

import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.util.InternationalString;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.global.CoverageDimension;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.WCS;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.requests.DescribeRequest;
import org.vfny.geoserver.wcs.requests.WCSRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Logger;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public class DescribeResponse implements Response {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.responses");
    private static final String CURR_VER = "\"1.0.0\"";
    private static final String WCS_URL = "http://www.opengis.net/wcs";
    private static final String WCS_NAMESPACE = new StringBuffer("\n  xmlns=\"").append(WCS_URL)
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
    private static final String FOOTER = "\n</CoverageDescription>";

    /**
     *
     * @uml.property name="request"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    private DescribeRequest request;

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
     * Returns any extra headers that this service might want to set in the HTTP response object.
     * @see org.vfny.geoserver.Response#getResponseHeaders()
     */
    public HashMap getResponseHeaders() {
        return null;
    }

    /**
     * The default coordinate reference system factory.
     */

    // protected final static CRSFactory crsFactory =
    // FactoryFinder.getCRSFactory(new
    // Hints(Hints.CRS_AUTHORITY_FACTORY,EPSGCRSAuthorityFactory.class));
    protected final static CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(new Hints(
                Hints.CRS_AUTHORITY_FACTORY, CRSAuthorityFactory.class));

    /**
     * The default math transform factory.
     *
     * @uml.property name="mtFactory"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    protected final MathTransformFactory mtFactory = ReferencingFactoryFinder
        .getMathTransformFactory(null);

    /**
     * The default transformations factory.
     */
    protected final static CoordinateOperationFactory opFactory = ReferencingFactoryFinder
        .getCoordinateOperationFactory(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));

    public void execute(Request req) throws WcsException {
        WCSRequest request = (WCSRequest) req;

        if (!(request instanceof DescribeRequest)) {
            throw new WcsException(new StringBuffer(
                    "illegal request type, expected DescribeRequest, got ").append(request)
                                                                                                           .toString());
        }

        DescribeRequest wcsRequest = (DescribeRequest) request;
        this.request = wcsRequest;
        LOGGER.finer("processing describe request" + wcsRequest);

        String outputFormat = wcsRequest.getOutputFormat();

        if (!outputFormat.equalsIgnoreCase("XMLSCHEMA")) {
            throw new WcsException(new StringBuffer("output format: ").append(outputFormat)
                                                                      .append(" not ")
                                                                      .append("supported by geoserver")
                                                                      .toString());
        }

        // generates response, using general function
        xmlResponse = generateCoverages(wcsRequest);

        if (!request.getWCS().getGeoServer().isVerbose()) {
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

    public void writeTo(OutputStream out) throws WcsException {
        try {
            byte[] content = xmlResponse.getBytes();
            out.write(content);
        } catch (IOException ex) {
            throw new WcsException(ex, "", getClass().getName());
        }
    }

    private final String generateCoverages(DescribeRequest wcsRequest)
        throws WcsException {
        List requestedTypes = wcsRequest.getCoverages();

        // Initialize return information and intermediate return objects
        StringBuffer tempResponse = new StringBuffer();

        // ComplexType table = new ComplexType();
        if (requestedTypes.size() == 0) {
            // if there are no specific requested types then get all.
            requestedTypes = new ArrayList(wcsRequest.getWCS().getData().getCoverageInfos().keySet());
        }

        tempResponse.append("<?xml version=\"1.0\" encoding=\"")
                    .append(wcsRequest.getGeoServer().getCharSet().displayName()).append("\"?>")
                    .append("\n<CoverageDescription version=").append(CURR_VER).append(" ")
                    .toString();

        tempResponse.append(WCS_NAMESPACE);
        tempResponse.append(XLINK_NAMESPACE);
        tempResponse.append(OGC_NAMESPACE);
        tempResponse.append(GML_NAMESPACE);
        tempResponse.append(XSI_NAMESPACE);
        /*tempResponse.append(" xsi:schemaLocation=\"").append(WCS_URL).append(
                        " ").append(request.getSchemaBaseUrl()).append(
                        "wcs/1.0.0/describeCoverage.xsd\">\n\n");*/
        tempResponse.append(" xsi:schemaLocation=\"").append(WCS_URL).append(" ")
                    .append("http://schemas.opengis.net/wcs/1.0.0/")
                    .append("describeCoverage.xsd\">\n\n");

        tempResponse.append(generateSpecifiedCoverages(requestedTypes, wcsRequest.getWCS()));

        tempResponse.append(FOOTER);

        return tempResponse.toString();
    }

    private String generateSpecifiedCoverages(List requestedTypes, WCS gs)
        throws WcsException {
        String tempResponse = new String();
        String curCoverageName = new String();

        final int length = requestedTypes.size();
        CoverageInfo meta;

        for (int i = 0; i < length; i++) {
            curCoverageName = requestedTypes.get(i).toString();

            meta = gs.getData().getCoverageInfo(curCoverageName);

            if (meta == null) {
                throw new WcsException(new StringBuffer("Coverage ").append(curCoverageName)
                                                                    .append(" does ")
                                                                    .append("not exist on this server")
                                                                    .toString());
            }

            tempResponse = tempResponse + printElement(meta);
        }

        tempResponse = tempResponse + "\n\n";

        return tempResponse;
    }

    private static String printElement(CoverageInfo cv) {
        StringBuffer tempResponse = new StringBuffer();

        tempResponse.append("\n <CoverageOffering>");

        if (cv.getMetadataLink() != null) {
            tempResponse.append("\n  <metadataLink about=\"").append(cv.getMetadataLink().getAbout())
                        .append("\" metadataType=\"").append(cv.getMetadataLink().getMetadataType())
                        .append("\"/>");
        }

        String tmp = cv.getDescription();

        if ((tmp != null) && (tmp != "")) {
            tempResponse.append("\n  <description>").append(tmp).append("</description>");
        }

        tmp = cv.getName();

        if ((tmp != null) && (tmp != "")) {
            tempResponse.append("\n  <name>").append(tmp).append("</name>");
        }

        tmp = cv.getLabel();

        if ((tmp != null) && (tmp != "")) {
            tempResponse.append("\n  <label>").append(tmp).append("</label>");
        }

        final GeneralEnvelope envelope = cv.getWGS84LonLatEnvelope();

        tempResponse.append("\n  <lonLatEnvelope" + " srsName=\"WGS84(DD)\"") /*urn:ogc:def:crs:OGC:1.3:CRS84*/
                    .append(">");
        tempResponse.append("\n   <gml:pos>").append(envelope.getLowerCorner().getOrdinate(0))
                    .append(" ").append(envelope.getLowerCorner().getOrdinate(1))
                    .append("</gml:pos>");
        tempResponse.append("\n   <gml:pos>").append(envelope.getUpperCorner().getOrdinate(0))
                    .append(" ").append(envelope.getUpperCorner().getOrdinate(1))
                    .append("</gml:pos>");
        /*tempResponse.append("\n   <gml:timePosition></gml:timePosition>");
        tempResponse.append("\n   <gml:timePosition></gml:timePosition>");*/
        tempResponse.append("\n  </lonLatEnvelope>");

        if ((cv.getKeywords() != null) && (cv.getKeywords().size() > 0)) {
            tempResponse.append("\n  <keywords>");

            for (int i = 0; i < cv.getKeywords().size(); i++)
                tempResponse.append("\n   <keyword>" + cv.getKeywords().get(i) + "</keyword>");

            tempResponse.append("\n  </keywords>");
        }

        // TODO we need to signal somehow that something went wrong
        GeneralEnvelope cvEnvelope = cv.getEnvelope();
        // try {
        // cvEnvelope =
        // CoverageStoreUtils.adjustEnvelopeLongitudeFirst(cv.getEnvelope()
        // .getCoordinateReferenceSystem(), cv.getEnvelope());
        // } catch (MismatchedDimensionException e) {
        // LOGGER.logp(Level.SEVERE, DescribeResponse.class.toString(),
        // "private static String printElement(CoverageInfo cv)", e
        // .getLocalizedMessage(), e);
        //
        // } catch (IndexOutOfBoundsException e) {
        // LOGGER.logp(Level.SEVERE, DescribeResponse.class.toString(),
        // "private static String printElement(CoverageInfo cv)", e
        // .getLocalizedMessage(), e);
        // } catch (NoSuchAuthorityCodeException e) {
        // LOGGER.logp(Level.SEVERE, DescribeResponse.class.toString(),
        // "private static String printElement(CoverageInfo cv)", e
        // .getLocalizedMessage(), e);
        //		}
        tempResponse.append("\n  <domainSet>");
        tempResponse.append("\n   <spatialDomain>");
        // Envelope
        tempResponse.append("\n    <gml:Envelope")
                    .append((((cv.getSrsName() != null) && (cv.getSrsName() != ""))
            ? new StringBuffer(" srsName=\"").append(cv.getSrsName()).append("\"").toString() : ""))
                    .append(">");
        tempResponse.append("\n       <gml:pos>")
                    .append((cvEnvelope != null)
            ? new StringBuffer(Double.toString(cvEnvelope.getLowerCorner().getOrdinate(0))).append(
                " ").append(cvEnvelope.getLowerCorner().getOrdinate(1)).toString() : "")
                    .append("</gml:pos>");
        tempResponse.append("\n       <gml:pos>")
                    .append((cvEnvelope != null)
            ? new StringBuffer(Double.toString(cvEnvelope.getUpperCorner().getOrdinate(0))).append(
                " ").append(cvEnvelope.getUpperCorner().getOrdinate(1)).toString() : "")
                    .append("</gml:pos>");
        tempResponse.append("\n    </gml:Envelope>");

        // Grid
        GridGeometry g = cv.getGrid();
        InternationalString[] dimNames = cv.getDimensionNames();
        final int gridDimension = g.getGridRange().getDimension();

        // RectifiedGrid
        tempResponse.append("\n    <gml:RectifiedGrid")
                    .append((g != null)
            ? new StringBuffer(" dimension=\"").append(gridDimension).append("\"").toString() : "")
                    .append(">");

        String lowers = "";
        String upers = "";

        for (int r = 0; r < gridDimension; r++) {
            lowers += (g.getGridRange().getLower(r) + " ");
            upers += (g.getGridRange().getUpper(r) + " ");
        }

        tempResponse.append("\n       <gml:limits>");
        tempResponse.append("\n         <gml:GridEnvelope>");
        tempResponse.append("\n         <gml:low>" + ((cvEnvelope != null) ? lowers : "")
            + "</gml:low>");
        tempResponse.append("\n         <gml:high>" + ((cvEnvelope != null) ? upers : "")
            + "</gml:high>");
        tempResponse.append("\n         </gml:GridEnvelope>");
        tempResponse.append("\n       </gml:limits>");

        if (dimNames != null) {
            for (int dn = 0; dn < dimNames.length; dn++)
                tempResponse.append("\n       <gml:axisName>" + dimNames[dn] + "</gml:axisName>");
        }

        tempResponse.append("\n       <gml:origin>");
        tempResponse.append("\n       <gml:pos>"
            + ((cvEnvelope != null)
            ? (cvEnvelope.getLowerCorner().getOrdinate(0) + " "
            + cvEnvelope.getUpperCorner().getOrdinate(1)) : "") + "</gml:pos>");
        tempResponse.append("\n       </gml:origin>");
        tempResponse.append("\n       <gml:offsetVector>"
            + ((cvEnvelope != null)
            ? ((cvEnvelope.getUpperCorner().getOrdinate(0)
            - cvEnvelope.getLowerCorner().getOrdinate(0)) / (g.getGridRange().getUpper(0)
            - g.getGridRange().getLower(0))) : 0.0) + " 0.0</gml:offsetVector>");
        tempResponse.append("\n       <gml:offsetVector>0.0 "
            + ((cvEnvelope != null)
            ? ((cvEnvelope.getLowerCorner().getOrdinate(1)
            - cvEnvelope.getUpperCorner().getOrdinate(1)) / (g.getGridRange().getUpper(1)
            - g.getGridRange().getLower(1))) : (-0.0)) + "</gml:offsetVector>");
        tempResponse.append("\n    </gml:RectifiedGrid>");
        tempResponse.append("\n   </spatialDomain>");
        tempResponse.append("\n  </domainSet>");

        // rangeSet
        CoverageDimension[] dims = cv.getDimensions();
        TreeSet nodataValues = new TreeSet();

        try {
            if (dims != null) {
                int numSampleDimensions = dims.length;
                tempResponse.append("\n  <rangeSet>");
                tempResponse.append("\n   <RangeSet>");
                //tempResponse.append("\n    <!--  WARNING: Mandatory metadata '..._rangeset_name' was missing in this context.  --> ");
                tempResponse.append("\n    <name>" + cv.getName() + "</name>");
                tempResponse.append("\n    <label>" + cv.getLabel() + "</label>");
                tempResponse.append("\n      <axisDescription>");
                tempResponse.append("\n        <AxisDescription>");
                tempResponse.append("\n          <name>Band</name>");
                tempResponse.append("\n          <label>Band</label>");
                tempResponse.append("\n          <values>");

                if (numSampleDimensions == 1) {
                    tempResponse.append("\n            <singleValue>").append("1")
                                .append("</singleValue>");
                } else {
                    tempResponse.append("\n            <interval>");
                    tempResponse.append("\n              <min>1</min>");
                    tempResponse.append("\n              <max>" + numSampleDimensions + "</max>");
                    tempResponse.append("\n            </interval>");
                }

                tempResponse.append("\n          </values>");
                tempResponse.append("\n        </AxisDescription>");
                tempResponse.append("\n      </axisDescription>");

                for (int sample = 0; sample < numSampleDimensions; sample++) {
                    Double[] nodata = dims[sample].getNullValues();

                    if (nodata != null) {
                        for (int nd = 0; nd < nodata.length; nd++) {
                            if (!nodataValues.contains(nodata[nd])) {
                                nodataValues.add(nodata[nd]);
                            }
                        }
                    }
                }

                tempResponse.append("\n      <nullValues>");

                if (nodataValues.size() > 0) {
                    if (nodataValues.size() == 1) {
                        tempResponse.append("\n        <singleValue>"
                            + (Double) nodataValues.first() + "</singleValue>");
                    } else {
                        tempResponse.append("\n        <interval>");
                        tempResponse.append("\n          <min>" + (Double) nodataValues.first()
                            + "</min>");
                        tempResponse.append("\n          <max>" + (Double) nodataValues.last()
                            + "</max>");
                        tempResponse.append("\n        <interval>");
                    }
                } else {
                    tempResponse.append("\n        <singleValue>0</singleValue>");
                }

                tempResponse.append("\n      </nullValues>");

                tempResponse.append("\n   </RangeSet>");
                tempResponse.append("\n  </rangeSet>");
            }
        } catch (Exception e) {
            // TODO Handle this exceptions ...
            e.printStackTrace();
        }

        if (((cv.getRequestCRSs() != null) && (cv.getRequestCRSs().size() > 0))
                || ((cv.getResponseCRSs() != null) && (cv.getResponseCRSs().size() > 0))) {
            tempResponse.append("\n  <supportedCRSs>");

            if ((cv.getResponseCRSs() != null) && (cv.getResponseCRSs().size() > 0)
                    && (cv.getRequestCRSs() != null) && (cv.getRequestCRSs().size() > 0)) {
                tempResponse.append("\n    <requestResponseCRSs>");

                ArrayList CRSs = new ArrayList();

                for (int i = 0; i < cv.getRequestCRSs().size(); i++)
                    if (!CRSs.contains(cv.getRequestCRSs().get(i))) {
                        CRSs.add(cv.getRequestCRSs().get(i));
                    }

                for (int i = 0; i < cv.getResponseCRSs().size(); i++)
                    if (!CRSs.contains(cv.getResponseCRSs().get(i))) {
                        CRSs.add(cv.getResponseCRSs().get(i));
                    }

                for (int i = 0; i < CRSs.size(); i++)
                    tempResponse.append(CRSs.get(i) + " ");

                tempResponse.append("\n    </requestResponseCRSs>");
            } else {
                if ((cv.getRequestCRSs() != null) && (cv.getRequestCRSs().size() > 0)) {
                    for (int i = 0; i < cv.getRequestCRSs().size(); i++)
                        tempResponse.append("\n    <requestCRSs>" + cv.getRequestCRSs().get(i)
                            + "</requestCRSs>");
                }

                if ((cv.getResponseCRSs() != null) && (cv.getResponseCRSs().size() > 0)) {
                    for (int i = 0; i < cv.getResponseCRSs().size(); i++)
                        tempResponse.append("\n    <responseCRSs>" + cv.getResponseCRSs().get(i)
                            + "</responseCRSs>");
                }
            }

            tempResponse.append("\n  </supportedCRSs>");
        }

        final String nativeFormat = (((cv.getNativeFormat() != null)
            && cv.getNativeFormat().equalsIgnoreCase("GEOTIFF")) ? "GeoTIFF" : cv.getNativeFormat());
        String supportedFormat = "";

        if (((cv.getSupportedFormats() != null) && (cv.getSupportedFormats().size() > 0))) {
            tempResponse.append("\n  <supportedFormats"
                + (((nativeFormat != null) && (nativeFormat != ""))
                ? (" nativeFormat=\"" + nativeFormat + "\"") : "") + ">");

            for (int i = 0; i < cv.getSupportedFormats().size(); i++) {
                supportedFormat = (String) cv.getSupportedFormats().get(i);
                supportedFormat = (supportedFormat.equalsIgnoreCase("GEOTIFF") ? "GeoTIFF"
                                                                               : supportedFormat);
                tempResponse.append("\n    <formats>" + supportedFormat + "</formats>");
            }

            tempResponse.append("\n  </supportedFormats>");
        }

        if (((cv.getInterpolationMethods() != null) && (cv.getInterpolationMethods().size() > 0))) {
            tempResponse.append("\n  <supportedInterpolations"
                + (((cv.getDefaultInterpolationMethod() != null)
                && (cv.getDefaultInterpolationMethod() != ""))
                ? (" default=\"" + cv.getDefaultInterpolationMethod() + "\"") : "") + ">");

            for (int i = 0; i < cv.getInterpolationMethods().size(); i++)
                tempResponse.append("\n    <interpolationMethod>"
                    + cv.getInterpolationMethods().get(i) + "</interpolationMethod>");

            tempResponse.append("\n  </supportedInterpolations>");
        }

        tempResponse.append("\n </CoverageOffering>");

        return tempResponse.toString();
    } /*
    * (non-Javadoc)
    *
    * @see org.vfny.geoserver.responses.Response#abort()
    */
    public void abort(Service gs) {
        // nothing to undo
    }
}
