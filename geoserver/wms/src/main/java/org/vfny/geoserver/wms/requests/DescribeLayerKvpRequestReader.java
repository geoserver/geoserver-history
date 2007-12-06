/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.servlets.WMService;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;


/**
 * Parses a DescribeLayer request, wich consists only of a list of
 * layer names, given by the <code>"LAYER"</code> parameter.
 *
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class DescribeLayerKvpRequestReader extends WmsKvpRequestReader {
    /** package's logger  */
    private static final Logger LOGGER = Logger.getLogger(DescribeLayerKvpRequestReader.class.getPackage()
                                                                                             .getName());

    /**
     * Constructs a new DescribeLayer request parser.
     * @param params
     * @param service The service that handles the request
     */
    public DescribeLayerKvpRequestReader(Map params, WMService service) {
        super(params, service);
    }

    /**
     * Does the request parsing and constructs a <code>DescribeLayerRequest</code>,
     * wich holds the requiered layers as <code>FeatureTypeInfo</code> references.
     *
     * @param request the original request.
     *
     * @return the parsed and validated <code>DescribeLayerRequest</code>
     *
     * @throws ServiceException see "throws WmsException"...
     * @throws WmsException if no layers has been requested, or
     * one of the requested layers does not exists on this
     * server instance.
     */
    public Request getRequest(HttpServletRequest request)
        throws ServiceException {
        DescribeLayerRequest req = new DescribeLayerRequest((WMService) service);
        req.setHttpServletRequest(request);

        String layersParam = getValue("LAYERS");

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(layersParam);
        }

        List layers = readFlat(layersParam, INNER_DELIMETER);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(layers.toString());
        }

        int layerCount = layers.size();

        if (layerCount == 0) {
            throw new WmsException("No LAYERS has been requested",
                getClass().getName());
        }

        Data catalog = req.getWMS().getData();

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(catalog.toString());
        }

        String layerName = null;
        MapLayerInfo layer = null;

        for (int i = 0; i < layerCount; i++) {
            layerName = (String) layers.get(i);

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(new StringBuffer("Looking for layer ").append(
                        layerName).toString());
            }

            try {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("looking featuretypeinfos");
                }

                FeatureTypeInfo ftype = catalog.getFeatureTypeInfo(layerName);
                layer = new MapLayerInfo();
                layer.setFeature(ftype);
                req.addLayer(layer);
            } catch (NoSuchElementException fex) {
                try {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("looking coverageinfos");
                    }

                    CoverageInfo cinfo = catalog.getCoverageInfo(layerName);
                    layer = new MapLayerInfo();
                    layer.setCoverage(cinfo);
                    req.addLayer(layer);

                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(new StringBuffer(layerName).append(" found")
                                                               .toString());
                    }
                } catch (NoSuchElementException cex) {
                    throw new WmsException(cex,
                        layerName + ": no such layer on this server",
                        "LayerNotDefined");
                }

                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(new StringBuffer(layerName).append(" found")
                                                           .toString());
                }
            }
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(new StringBuffer("parsed request ").append(req)
                                                           .toString());
        }

        return req;
    }
}
