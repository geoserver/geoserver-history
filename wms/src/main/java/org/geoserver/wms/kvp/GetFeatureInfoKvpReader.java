/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.kvp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geoserver.ows.KvpRequestReader;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.MapLayerInfo;
import org.geoserver.wms.WMS;
import org.geoserver.wms.request.GetFeatureInfoRequest;
import org.geoserver.wms.request.GetMapRequest;

/**
 * Builds a GetFeatureInfo request object given by a set of CGI parameters supplied in the
 * constructor.
 * 
 * <p>
 * Request parameters:
 * </p>
 * 
 * @author Gabriel Roldan
 * @version $Id$
 */
public class GetFeatureInfoKvpReader extends KvpRequestReader {

    /** GetMap request reader used to parse the map context parameters needed. */
    private GetMapKvpRequestReader getMapReader;

    private WMS wms;

    public GetFeatureInfoKvpReader(WMS wms) {
        super(GetFeatureInfoRequest.class);
        getMapReader = new GetMapKvpRequestReader(wms);
        this.wms = wms;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Object read(Object req, Map kvp, Map rawKvp) throws Exception {
        GetFeatureInfoRequest request = (GetFeatureInfoRequest) super.read(req, kvp, rawKvp);
        request.setRawKvp(rawKvp);

        request.setQueryLayers(new MapLayerInfoKvpParser("QUERY_LAYERS", wms).parse((String)rawKvp.get("QUERY_LAYERS")));
        
        if (request.getQueryLayers() == null || request.getQueryLayers().size() == 0) {
            throw new ServiceException("No QUERY_LAYERS has been requested, or no "
                    + "queriable layer in the request anyways");
        }

        GetMapRequest getMapPart = new GetMapRequest();
        try {
            getMapPart = getMapReader.read(getMapPart, kvp, rawKvp);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            throw new ServiceException(e);
        }

        request.setGetMapRequest(getMapPart);

        // make sure they are a subset of layers
        List<MapLayerInfo> getMapLayers = getMapPart.getLayers();
        List<MapLayerInfo> queryLayers = new ArrayList<MapLayerInfo>(request.getQueryLayers());
        queryLayers.removeAll(getMapLayers);
        if (queryLayers.size() > 0) {
            // we've already expanded base layers so let's avoid list the names, they are not
            // the original ones anymore
            throw new ServiceException("QUERY_LAYERS contains layers not cited in LAYERS. "
                    + "It should be a proper subset of those instead");
        }

        String format = (String) (kvp.containsKey("INFO_FORMAT") ? kvp.get("INFO_FORMAT") : null);

        if (format == null) {
            format = "text/plain";
        } else {
            List<String> infoFormats = wms.getAvailableFeatureInfoFormats();
            if (!infoFormats.contains(format)) {
                throw new ServiceException("Invalid format '" + format
                        + "', supported formats are " + infoFormats, "InvalidParameterValue",
                        "info_format");
            }
        }

        request.setInfoFormat(format);

        request.setFeatureCount(1); // DJB: according to the WMS spec (7.3.3.7 FEATURE_COUNT) this
                                    // should be 1. also tested for by cite

        try {
            int maxFeatures = Integer.parseInt(String.valueOf(kvp.get("FEATURE_COUNT")));
            request.setFeatureCount(maxFeatures);
        } catch (NumberFormatException ex) {
            // do nothing, FEATURE_COUNT is optional
        }

        try {
            int x = Integer.parseInt(String.valueOf(kvp.get("X")));
            int y = Integer.parseInt(String.valueOf(kvp.get("Y")));
            request.setXPixel(x);
            request.setYPixel(y);
        } catch (NumberFormatException ex) {
            throw new ServiceException("X and Y incorrectly specified");
        }

        return request;
    }
}
