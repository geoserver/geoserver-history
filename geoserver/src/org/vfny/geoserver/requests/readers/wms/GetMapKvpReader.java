/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers.wms;

import com.vividsolutions.jts.geom.Envelope;

import org.geotools.feature.FeatureType;
import org.geotools.filter.Filter;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.readers.WmsKvpRequestReader;
import org.vfny.geoserver.requests.wms.GetMapRequest;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * Mandatory parameters:
 *
 * <ul>
 * <li>
 * LAYERS
 * </li>
 * <li>
 * STYLES
 * </li>
 * <li>
 * BBOX
 * </li>
 * <li>
 * FORMAT
 * </li>
 * <li>
 * WIDTH
 * </li>
 * <li>
 * HEIGHT
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * Optional parameters:
 *
 * <ul>
 * <li>
 * SRS
 * </li>
 * <li>
 * TRANSPARENT
 * </li>
 * <li>
 * EXCEPTIONS
 * </li>
 * <li>
 * BGCOLOR
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * Customized parameters:
 *
 * <ul>
 * <li>
 * FILTER if present, must contain a list of filters, exactly one per feature
 * type requested, in the same format as for the <i>FILTER</i> parameter in
 * WFS's GetFeature request.
 * </li>
 * <li>
 * ATTRIBUTES wich attributes of each layer will be sent as XML attributes in a
 * SVG map response. The format of this parameter is:
 * <code>ATTRIBUTES=attName1,attName2,...,attNameN|attName1,
 * attName2,...,attNameN</code>. Wich means that if it is pressent, a list of
 * attributes for each layer requested must be specified, separated by "|"
 * (pipe), and each attribute separated by "," (comma). The following special
 * attributes are allowed to be queried:
 *
 * <ul>
 * <li>
 * <b>#FID</b>: a map producer capable of handling attributes will write the
 * feature id of each feature. For example, SVGMapResponse will write a
 * polygon by this way: <code>&lt;path id="&lt;featureId&gt;"
 * d="..."/&gt;</code>
 * </li>
 * <li>
 * <b>#BOUNDS</b>: a map producer capable of handling attributes will write the
 * bounding box of each feature. For example, SVGMapResponse will write a
 * polygon by this way: <code>&lt;path bounds="minx miny maxx maxy"
 * d="..."/&gt;</code>
 * </li>
 * </ul>
 *
 * </li>
 * <li>
 * SVGHEADER expected <code>"true"</code> or <code>"false"</code>, tells wether
 * the xml header and SVG element must be printed when generating a SVG map
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>NOTE:</strong> if you want to request one of the special attributes
 * (#FID or #BOUNDS), and you're making the request through HTTP GET method
 * (such as writing the request in the address text box of your web browser),
 * be sure to no write the <code>'#'</code> character in it's URL encoded
 * format, wich is the <code>"<b>%23</b>"</code> literal. For example, instead
 * of writting <code>ATTRIBUTES=<b>#</b>FID,<b>#</b>BOUNDS</code> you should
 * write <code>ATTRIBUTES=<b>%23</b>FID,<b>%23</b>BOUNDS</code>
 * </p>
 *
 * @author Gabriel Roldán
 * @version $Id: GetMapKvpReader.java,v 1.9 2004/03/14 16:01:32 groldan Exp $
 */
public class GetMapKvpReader extends WmsKvpRequestReader {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.readers.wms");

    /** the request wich will be built by getRequest method */
    private GetMapRequest request;

    /**
     * Creates a new GetMapKvpReader object.
     *
     * @param kvpPairs DOCUMENT ME!
     */
    public GetMapKvpReader(Map kvpPairs) {
        super(kvpPairs);
    }

    /**
     * Produces a <code>GetMapRequest</code> instance by parsing the GetMap
     * mandatory, optional and custom parameters.
     *
     * @param httpRequest the servlet request who's application object holds
     *        the server configuration
     *
     * @return a <code>GetMapRequest</code> completely setted up upon the
     *         parameters passed to this reader
     *
     * @throws ServiceException DOCUMENT ME!
     */
    public Request getRequest(HttpServletRequest httpRequest)
        throws ServiceException {
        request = new GetMapRequest();
        request.setHttpServletRequest(httpRequest);

        String version = getRequestVersion();
        request.setVersion(version);

        FeatureTypeInfo[] layers = parseMandatoryParameters(request);
        parseOptionalParameters(request);
        parseCustomParameters(request, layers);

        return request;
    }

    /**
     * Parses the optional parameters:
     *
     * <ul>
     * <li>
     * SRS
     * </li>
     * <li>
     * TRANSPARENT
     * </li>
     * <li>
     * EXCEPTIONS
     * </li>
     * <li>
     * BGCOLOR
     * </li>
     * </ul>
     *
     *
     * @param request DOCUMENT ME!
     *
     * @task TODO: implement parsing of transparent, exceptions and bgcolor
     */
    private void parseOptionalParameters(GetMapRequest request) {
        String crs = getValue("SRS");

        if (crs != null) {
            request.setCrs(crs);
        }
    }

    /**
     * Parses the mandatory GetMap request parameters:
     *
     * <p>
     * Mandatory parameters:
     *
     * <ul>
     * <li>
     * LAYERS
     * </li>
     * <li>
     * STYLES
     * </li>
     * <li>
     * BBOX
     * </li>
     * <li>
     * FORMAT
     * </li>
     * <li>
     * WIDTH
     * </li>
     * <li>
     * HEIGHT
     * </li>
     * </ul>
     * </p>
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws WmsException DOCUMENT ME!
     */
    private FeatureTypeInfo[] parseMandatoryParameters(GetMapRequest request)
        throws WmsException {
        FeatureTypeInfo[] layers = parseLayers();
        request.setLayers(layers);

        List styles = parseStyles(layers.length);
        request.setStyles(styles);

        try {
            int width = Integer.parseInt(getValue("WIDTH"));
            int height = Integer.parseInt(getValue("HEIGHT"));
            request.setWidth(width);
            request.setHeight(height);
        } catch (NumberFormatException ex) {
            throw new WmsException("WIDTH and HEIGHT incorrectly specified");
        }

        String format = getValue("FORMAT");

        if (format == null) {
            throw new WmsException("parameter FORMAT is required");
        }

        request.setFormat(format);

        Envelope bbox = parseBbox();
        request.setBbox(bbox);

        return layers;
    }

    /**
     * parses the following custom parameters for the GetMap request handling:
     *
     * <ul>
     * <li>
     * FILTER if present, must contain a list of filters, exactly one per
     * feature type requested, in the same format as for the <i>FILTER</i>
     * parameter in WFS's GetFeature request.
     * </li>
     * <li>
     * ATTRIBUTES wich attributes of each layer will be sent as XML attributes
     * in a SVG map response. The format of this parameter is:
     * <code>ATTRIBUTES=attName1,attName2,...,attNameN|attName1,
     * attName2,...,attNameN</code>. Wich means that if it is pressent, a list
     * of attributes for each layer requested must be specified, separated by
     * "|" (pipe), and each attribute separated by "," (comma).
     * </li>
     * <li>
     * SVGHEADER expected <code>"true"</code> or <code>"false"</code>, tells
     * wether the xml header and SVG element must be printed when generating a
     * SVG map
     * </li>
     * </ul>
     *
     *
     * @param request DOCUMENT ME!
     * @param layers DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     */
    private void parseCustomParameters(GetMapRequest request,
        FeatureTypeInfo[] layers) throws ServiceException {
        Filter[] filters = parseFilters(layers.length);
        request.setFilters(filters);

        List attributes = parseAttributes(layers);
        request.setAttributes(attributes);

        String svgHeader = super.getValue("SVGHEADER");
        boolean writeSvgHeader = (svgHeader == null) ? true
                                                     : Boolean.valueOf(svgHeader)
                                                              .booleanValue();
        request.setWriteSvgHeader(writeSvgHeader);

        String collectParam = super.getValue("COLLECT");

        if (collectParam != null) {
            boolean collect = Boolean.valueOf(collectParam).booleanValue();
            LOGGER.finer("Request sets collect geometries to: " + collect);
            request.setCollectGeometries(collect);
        }

        String genFactorParam = super.getValue("GENERALIZATIONFACTOR");

        if (genFactorParam != null) {
            double gfactor = 0;
            LOGGER.finest("Requested generalization factor: " + genFactorParam);

            try {
                gfactor = Double.parseDouble(genFactorParam);
                request.setGeneralizationFactor(gfactor);
            } catch (NumberFormatException ex) {
                LOGGER.warning(
                    "parameter GENERALIZATIONFACTOR mus be parseable "
                    + "as double, got " + genFactorParam);
            }
        }
    }

    /**
     * creates a list of requested attributes, wich must be a valid attribute
     * name or one of the following special attributes:
     *
     * <ul>
     * <li>
     * <b>#FID</b>: a map producer capable of handling attributes (such as
     * SVGMapResponse), will write the feature id of each feature
     * </li>
     * <li>
     * <b>#BOUNDS</b>: a map producer capable of handling attributes (such as
     * SVGMapResponse), will write the bounding box of each feature
     * </li>
     * </ul>
     *
     *
     * @param layers info about the requested map layers
     *
     * @return an empty list if no attributes was requested, or a
     *         <code>List&lt;List&lt;String&gt;&gt;</code> with an entry for
     *         each requested layer, where each of them consists of a List of
     *         the attribute names requested
     *
     * @throws WmsException if: <ul><li>the number of attribute sets requested
     *         is not equal to the number of layers requested.</li> <li>an
     *         illegal attribute name was requested</li> <li>an IOException
     *         occurs while fetching a FeatureType schema to ask it for
     *         propper attribute names</li> </ul>
     */
    private List parseAttributes(FeatureTypeInfo[] layers)
        throws WmsException {
        String rawAtts = getValue("ATTRIBUTES");
        LOGGER.finer("parsing attributes " + rawAtts);

        if ((rawAtts == null) || "".equals(rawAtts)) {
            return Collections.EMPTY_LIST;
        }

        //raw list of attributes for each feature type requested
        List byFeatureTypes = super.readFlat(rawAtts, "|");
        int nLayers = layers.length;

        if (byFeatureTypes.size() != nLayers) {
            throw new WmsException(byFeatureTypes.size()
                + " lists of attributes specified, expected " + layers.length,
                getClass().getName() + "::parseAttributes()");
        }

        //fill byFeatureTypes with the split of its raw attributes requested
        //separated by commas, and check for the validity of each att name
        for (int i = 0; i < nLayers; i++) {
            rawAtts = (String) byFeatureTypes.get(i);

            List atts = readFlat(rawAtts, ",");
            byFeatureTypes.set(i, atts);

            //FeatureType schema = layers[i].getSchema();
            try {
                FeatureType schema = layers[i].getFeatureType();

                //verify that propper attributes has been requested
                for (Iterator attIt = atts.iterator(); attIt.hasNext();) {
                    String attName = (String) attIt.next();

                    if (attName.length() > 0) {
                        LOGGER.finer("checking that " + attName + " is valid");

                        if ("#FID".equalsIgnoreCase(attName)
                                || "#BOUNDS".equalsIgnoreCase(attName)) {
                            LOGGER.finer("special attribute name requested: "
                                + attName);

                            continue;
                        }

                        if (schema.getAttributeType(attName) == null) {
                            throw new WmsException("Attribute '" + attName
                                + "' requested for layer "
                                + schema.getTypeName() + " does not exists");
                        }
                    } else {
                        LOGGER.finest(
                            "removing empty attribute name from request");
                        attIt.remove();
                    }
                }

                LOGGER.finest("attributes requested for "
                    + schema.getTypeName() + " checked: " + rawAtts);
            } catch (java.io.IOException e) {
                throw new WmsException(e);
            }
        }

        return byFeatureTypes;
    }

    /**
     * parses the BBOX parameter, wich must be a String of the form
     * <code>minx,miny,maxx,maxy</code> and returns a corresponding
     * <code>Envelope</code> object
     *
     * @return the <code>Envelope</code> represented by the request BBOX
     *         parameter
     *
     * @throws WmsException if the value of the BBOX request parameter can't be
     *         parsed as four <code>double</code>'s
     */
    private Envelope parseBbox() throws WmsException {
        Envelope bbox = null;
        String bboxParam = getValue("BBOX");
        Object[] bboxValues = readFlat(bboxParam, INNER_DELIMETER).toArray();

        if (bboxValues.length != 4) {
            throw new WmsException(bboxParam
                + " is not a valid pair of coordinates", getClass().getName());
        }

        try {
            double minx = Double.parseDouble(bboxValues[0].toString());
            double miny = Double.parseDouble(bboxValues[1].toString());
            double maxx = Double.parseDouble(bboxValues[2].toString());
            double maxy = Double.parseDouble(bboxValues[3].toString());
            bbox = new Envelope(minx, maxx, miny, maxy);
        } catch (NumberFormatException ex) {
            throw new WmsException(ex,
                "Illegal value for BBOX parameter: " + bboxParam,
                getClass().getName() + "::parseBbox()");
        }

        return bbox;
    }

    /**
     * DOCUMENT ME!
     *
     * @param numLayers DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws WmsException DOCUMENT ME!
     */
    private List parseStyles(int numLayers) throws WmsException {
        List styles = readFlat(getValue("STYLES"), INNER_DELIMETER);

        if (numLayers != styles.size()) {
            String msg = numLayers + " layers requested, but found "
                + styles.size() + " styles specified. "
                + "Since SLD parameter is not yet implemented, the STYLES parameter "
                + "is mandatory and MUST have exactly one value per requested layer";
            throw new WmsException(msg, getClass().getName());
        }

        Map configuredStyles = null;
        configuredStyles = request.getWMS().getData().getStyles();

        String st;

        for (Iterator it = styles.iterator(); it.hasNext();) {
            st = (String) it.next();

            if ((st != null) && !("".equals(st))) {
                if (!configuredStyles.containsKey(st)) {
                    throw new WmsException(st
                        + " style not recognized by this server",
                        getClass().getName());
                }
            }
        }

        return styles;
    }

    /**
     * DOCUMENT ME!
     *
     * @param numLayers DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws WmsException DOCUMENT ME!
     */
    private Filter[] parseFilters(int numLayers) throws ServiceException {
        List filtersList = Collections.EMPTY_LIST;
        String rawFilters = getValue("FILTER");

        if (rawFilters != null) {
            filtersList = super.readFilters(null, rawFilters, null);

            if ((filtersList.size() > 0) && (filtersList.size() != numLayers)) {
                throw new WmsException("Number of layers and filters do not match",
                    "GetMapKvpReader.parseFilters()");
            }
        }

        Filter[] filters = new Filter[filtersList.size()];

        return (Filter[]) filtersList.toArray(filters);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws WmsException DOCUMENT ME!
     */
    private FeatureTypeInfo[] parseLayers() throws WmsException {
        List layers = layers = readFlat(getValue("LAYERS"), INNER_DELIMETER);
        int layerCount = layers.size();

        if (layerCount == 0) {
            throw new WmsException("No LAYERS has been requested",
                getClass().getName());
        }

        FeatureTypeInfo[] featureTypes = new FeatureTypeInfo[layerCount];
        Data catalog = null;
        catalog = request.getWMS().getData();

        String layerName = null;
        FeatureTypeInfo ftype = null;

        try {
            for (int i = 0; i < layerCount; i++) {
                layerName = (String) layers.get(i);
                ftype = catalog.getFeatureTypeInfo(layerName);
                featureTypes[i] = ftype;
            }
        } catch (NoSuchElementException ex) {
            throw new WmsException(ex,
                layerName + ": no such layer on this server",
                getClass().getName());
        }

        return featureTypes;
    }
}
