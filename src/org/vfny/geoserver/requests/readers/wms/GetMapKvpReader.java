/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers.wms;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.feature.FeatureType;
import org.geotools.filter.Filter;
import org.geotools.styling.Style;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.readers.WmsKvpRequestReader;
import org.vfny.geoserver.requests.wms.GetMapRequest;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;


/**
 * Builds a GetMapRequest object given by a set of CGI parameters supplied in
 * the constructor.
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
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id: GetMapKvpReader.java,v 1.12 2004/09/16 22:20:54 cholmesny Exp $
 */
public class GetMapKvpReader extends WmsKvpRequestReader {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.readers.wms");

    /** the request wich will be built by getRequest method */
    private GetMapRequest request;

    /**
     * Indicates wether STYLES parameter must be parsed. Defaults to
     * <code>true</code>, but can be set to false, for example, when parsing a
     * GetFeatureInfo request
     */
    private boolean stylesRequired = true;

    /**
     * Creates a new GetMapKvpReader object.
     *
     * @param kvpPairs DOCUMENT ME!
     */
    public GetMapKvpReader(Map kvpPairs) {
        super(kvpPairs);
    }

    /**
     * Sets wether the STYLES parameter must be parsed
     *
     * @param parseStyles
     */
    public void setStylesRequired(boolean parseStyles) {
        this.stylesRequired = parseStyles;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isStylesRquired() {
        return this.stylesRequired;
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
     * @throws WmsException DOCUMENT ME!
     *
     * @task TODO: implement parsing of transparent, exceptions and bgcolor
     */
    private void parseOptionalParameters(GetMapRequest request)
        throws WmsException {
        String crs = getValue("SRS");

        if (crs != null) {
            request.setCrs(crs);
        }

        String transparentValue = getValue("TRANSPARENT");
        boolean transparent = (transparentValue == null) ? false
                                                         : Boolean.valueOf(transparentValue)
                                                                  .booleanValue();
        request.setTransparent(transparent);

        String bgcolor = getValue("BGCOLOR");

        if (bgcolor != null) {
            try {
                request.setBgColor(Color.decode(bgcolor));
            } catch (NumberFormatException nfe) {
                throw new WmsException("BGCOLOR " + bgcolor
                    + " incorrectly specified (0xRRGGBB format expected)");
            }
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

        if (isStylesRquired()) {
            List styleNames = parseStyles(layers);
            request.setStyles(styleNames);
        }

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
        List byFeatureTypes = readFlat(rawAtts, "|");
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

            if (minx > maxx) {
                throw new WmsException("illegal bbox, minX: " + minx + " is "
                    + "greater than maxX: " + maxx);
            }

            if (miny > maxy) {
                throw new WmsException("illegal bbox, minY: " + miny + " is "
                    + "greater than maxY: " + maxy);
            }
        } catch (NumberFormatException ex) {
            throw new WmsException(ex,
                "Illegal value for BBOX parameter: " + bboxParam,
                getClass().getName() + "::parseBbox()");
        }

        return bbox;
    }

    /**
     * Parses the list of style names requested for each requested layer.
     * 
     * <p>
     * A client _may_ request teh default Style using a null value (as in
     * "STYLES="). If  several layers are requested with a mixture of named
     * and default styles,  the STYLES parameter includes null values between
     * commas (as in  "STYLES=style1,,style2,,").  If all layers are to be
     * shown using the default style, either the  form "STYLES=" or
     * "STYLES=,,," is valid.
     * </p>
     *
     * @param layers the requested feature types
     *
     * @return a full <code>List</code> of the style names requested for the
     *         requiered layers with no null style names.
     *
     * @throws WmsException if some of the requested styles does not exist or
     *         its number if greater than zero and distinct of the number of
     *         requested layers
     */
    private List parseStyles(FeatureTypeInfo[] layers)
        throws WmsException {
        String rawStyles = getValue("STYLES");
        List styles = null;

        int numLayers = layers.length;

        if ("".equals(rawStyles)) {
            LOGGER.finer("Assigning default style to all the requested layers");
            styles = new ArrayList(layers.length);

            for (int i = 0; i < numLayers; i++)
                styles.add(layers[i].getDefaultStyle().getName());
        } else {
            styles = readFlat(rawStyles, INNER_DELIMETER);

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

            for (int i = 0; i < numLayers; i++) {
                st = (String) styles.get(i);

                if ((st != null) && !("".equals(st))) {
                    if (!configuredStyles.containsKey(st)) {
                        throw new WmsException(st
                            + " style not recognized by this server",
                            "StyleNotDefined");
                    }
                } else { //use the layer's default style
                    LOGGER.finer("applying default style to "
                        + layers[i].getName());

                    Style defStyle = layers[i].getDefaultStyle();

                    if (defStyle == null) {
                        String msg = "No default style has been defined for "
                            + layers[i].getName();
                        LOGGER.warning(msg);
                        throw new WmsException(msg,
                            getClass().getName() + "::parseStyles()");
                    }

                    styles.set(i, defStyle.getName());
                }
            }
        }

        return styles;
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
                layerName + ": no such layer on this server", "LayerNotDefined");
        }

        return featureTypes;
    }
}
