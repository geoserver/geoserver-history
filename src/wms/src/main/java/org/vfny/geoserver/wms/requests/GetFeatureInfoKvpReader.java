/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.MapLayerInfo;
import org.geoserver.wms.WMS;
import org.geoserver.wms.kvp.GetMapKvpRequestReader;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.wms.WmsException;

/**
 * Builds a GetFeatureInfo request object given by a set of CGI parameters supplied in the
 * constructor.
 * 
 * <p>
 * Request parameters:
 * </p>
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class GetFeatureInfoKvpReader extends WmsKvpRequestReader {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.vfny.geoserver.requests.readers.wms");

    /** the request wich will be built by getRequest method */
    private GetFeatureInfoRequest request;

    /** GetMap request reader used to parse the map context parameters needed. */
    private GetMapKvpRequestReader getMapReader;

    /**
     * Creates a new GetMapKvpReader object.
     * 
     * @param kvpPairs
     *            Key Values pairs of the request
     * @param wms
     *            The wms config object.
     */
    public GetFeatureInfoKvpReader(Map kvpPairs, WMS wms) {
        super(kvpPairs, wms);
        getMapReader = new GetMapKvpRequestReader(wms);
    }

    /**
     * Produces a <code>GetMapRequest</code> instance by parsing the GetMap mandatory, optional and
     * custom parameters.
     * 
     * @param httpRequest
     *            the servlet request who's application object holds the server configuration
     * 
     * @return a <code>GetMapRequest</code> completely setted up upon the parameters passed to this
     *         reader
     * 
     * @throws ServiceException
     *             DOCUMENT ME!
     * @throws WmsException
     *             DOCUMENT ME!
     */
    public Request getRequest(HttpServletRequest httpRequest) throws ServiceException {
        // WMService service = (WMService) getServiceRef();
        // WMS wms = service.getWMS();
        request = new GetFeatureInfoRequest(getWMS());
        request.setHttpServletRequest(httpRequest);

        String version = getRequestVersion();
        request.setVersion(version);

        //getMapReader.setFormatRequired(false);
        GetMapRequest getMapPart = new GetMapRequest(getWMS());
        try {
            getMapPart = getMapReader.read(getMapPart, super.kvpPairs, super.kvpPairs);
        } catch (Exception e) {
            throw new WmsException(e);
        }

        request.setGetMapRequest(getMapPart);

        // parse query layers
        request.setQueryLayers(parseLayers(getWMS()));
        // make sure they are a subset of layers
        List<MapLayerInfo> getMapLayers = new ArrayList(Arrays.asList(getMapPart.getLayers()));
        List<MapLayerInfo> queryLayers = new ArrayList<MapLayerInfo>(Arrays.asList(request.getQueryLayers()));
        queryLayers.removeAll(getMapLayers);
        if(queryLayers.size() > 0) {
            // we've already expanded base layers so let's avoid list the names, they are not
            // the original ones anymore
            throw new WmsException("QUERY_LAYERS contains layers not cited in LAYERS. " +
            		"It should be a proper subset of those instead");
        }
        

        String format = getValue("INFO_FORMAT");

        if (format == null) {
            // HACK: how we define the default info format?
            // Not sure I understand the question. If we don't like it here
            // then put it as the default in the FeatureInfoRequest. If
            // we want to allow users to be able to set it then we can put
            // it as a config parameter in the WMS service section. -ch
            format = "text/plain";
        } else {
            final List formats = org.vfny.geoserver.wms.responses.GetFeatureInfoResponse
                    .getFormats();
            if (!formats.contains(format)) {
                throw new WmsException("Invalid format '" + format + "', supported formats are "
                        + formats, "InvalidParameterValue", "info_format");
            }
        }

        request.setInfoFormat(format);

        request.setFeatureCount(1); // DJB: according to the WMS spec (7.3.3.7 FEATURE_COUNT) this
                                    // should be 1. also tested for by cite

        try {
            int maxFeatures = Integer.parseInt(getValue("FEATURE_COUNT"));
            request.setFeatureCount(maxFeatures);
        } catch (NumberFormatException ex) {
            // do nothing, FEATURE_COUNT is optional
        }

        try {
            int x = Integer.parseInt(getValue("X"));
            int y = Integer.parseInt(getValue("Y"));
            request.setXPixel(x);
            request.setYPixel(y);
        } catch (NumberFormatException ex) {
            throw new WmsException("X and Y incorrectly specified");
        }

        String exceptionsFormat = getValue("EXCEPTIONS");
        request.setExeptionFormat(exceptionsFormat);

        return request;
    }

    /**
     * Obtains the FeatureTypeInfo objects of the layers to query given by the
     * <code>QUERY_LAYERS</code>parameter.
     * 
     * @return the list of layers to query.
     * 
     * @throws WmsException
     *             if the parameter <code>QUERY_LAYERS</code> does not exists, has no layer names,
     *             or has at least an invalid layer name.
     */
    private MapLayerInfo[] parseLayers(WMS wms) throws WmsException {
        final List<String> queryLayers = readFlat(getValue("QUERY_LAYERS"), INNER_DELIMETER);

        // expand base layers, if there is any
        // if(wms.getBaseMapLayers() != null) {
        // for (int i = 0; i < layers.size(); i++) {
        // String layerGroup = (String) wms.getBaseMapLayers().get(layers.get(i));
        // if(layerGroup != null) {
        // List layerGroupExpanded = readFlat(layerGroup, INNER_DELIMETER);
        // layers.remove(i);
        // layers.addAll(i, layerGroupExpanded);
        // }
        // }
        // }

        if (queryLayers.size() == 0) {
            throw new WmsException("No QUERY_LAYERS has been requested, or no "
                    + "queriable layer in the request anyways", getClass().getName());
        }

        List<MapLayerInfo> layerInfos = new ArrayList<MapLayerInfo>();
        for (String layerName : queryLayers) {
            LayerInfo layerInfo = wms.getLayerByName(layerName);
            if (layerInfo == null) {
                LayerGroupInfo layerGroup = wms.getLayerGroupByName(layerName);
                if (layerGroup == null) {
                    throw new WmsException("Layer " + layerName + " does not exist",
                            "LayerNotDefined");
                }
                for (LayerInfo li : layerGroup.getLayers()) {
                    layerInfos.add(new MapLayerInfo(li));
                }
            } else {
                layerInfos.add(new MapLayerInfo(layerInfo));
            }
        }
        
        
        

        return layerInfos.toArray(new MapLayerInfo[layerInfos.size()]);
    }
}
