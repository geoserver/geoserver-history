/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers.wms;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.filter.Filter;
import org.vfny.geoserver.*;
import org.vfny.geoserver.config.*;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.readers.*;
import org.vfny.geoserver.requests.wms.GetMapRequest;
import java.util.*;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: GetMapKvpReader.java,v 1.1.2.1 2003/11/14 20:39:14 groldan Exp $
 */
public class GetMapKvpReader extends WmsKvpRequestReader {
    /**
     * Creates a new GetMapKvpReader object.
     *
     * @param kvpPairs DOCUMENT ME!
     */
    public GetMapKvpReader(Map kvpPairs) {
        super(kvpPairs);
    }

    /**
     * DOCUMENT ME!
     *
     * @return a <code>GetMapRequest</code> completely setted up upon the
     *         parameters passed to this reader
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws WmsException if any request restriction is not satisfied as
     *         stated in WMS spec v1.1.1
     */
    public Request getRequest() throws ServiceException {
        GetMapRequest request = new GetMapRequest();
        String version = getRequestVersion();
        request.setVersion(version);

        FeatureTypeConfig[] layers = parseLayers();
        request.setLayers(layers);

        List styles = parseStyles(layers.length);
        request.setStyles(styles);

        String crs = getValue("SRS");
        request.setCrs(crs);

        Envelope bbox = parseBbox();
        request.setBbox(bbox);

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

        Filter[] filters = parseFilters(layers.length);
        request.setFilters(filters);

        return request;
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

        Map configuredStyles = config.getCatalog().getStyles();
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

    private Filter[] parseFilters(int numLayers) throws ServiceException {
        List filtersList = Collections.EMPTY_LIST;
        String rawFilters = getValue("FILTERS");

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
    private FeatureTypeConfig[] parseLayers() throws WmsException {
        List layers = layers = readFlat(getValue("LAYERS"), INNER_DELIMETER);
        int layerCount = layers.size();

        if (layerCount == 0) {
            throw new WmsException("No LAYERS has been requested",
                getClass().getName());
        }

        FeatureTypeConfig[] featureTypes = new FeatureTypeConfig[layerCount];
        CatalogConfig catalog = config.getCatalog();
        String layerName = null;
        FeatureTypeConfig ftype = null;

        try {
            for (int i = 0; i < layerCount; i++) {
                layerName = (String) layers.get(i);
                ftype = catalog.getFeatureType(layerName);
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
