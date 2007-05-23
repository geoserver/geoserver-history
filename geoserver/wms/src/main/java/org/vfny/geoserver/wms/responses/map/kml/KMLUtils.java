/* Copyright (c) 2001, 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.vfny.geoserver.util.Requests;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Some convenience methods used by the kml transformers.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class KMLUtils {
    /**
     * Creates the parameters for a GetMap request from a map context + map layer.
     *
     * @param mapContext The map context.
     * @param mapLayer The Map layer.
     *
     * @return A map containing all the key value pairs for a GetMap request.
     */
    public static Map createGetMapRequest(WMSMapContext mapContext, MapLayer mapLayer) {
        return createGetMapRequest(mapContext, mapLayer, mapContext.getAreaOfInterest());
    }

    /**
     * Creates the parameters for a GetLegendGraphic request from a map context
     * + map layer.
     *
     * @param mapContext The map context.
     * @param mapLayer The map layer.
     *
     * @return A map containing all the key value pairs for a GetLegendGraphic request.
     */
    public static Map createGetLegendGraphicRequest(WMSMapContext mapContext, MapLayer mapLayer) {
        HashMap map = new HashMap();

        map.put("service", "wms");
        map.put("request", "GetLegendGraphic");
        map.put("version", "1.1.1");
        map.put("format", "image/png");
        map.put("layer", mapLayer.getTitle());
        map.put("style", mapLayer.getStyle().getName());
        map.put("height", "20");
        map.put("width", "20");

        return map;
    }

    /**
     * Creates the parameters for a GetMap request from a map context, map layer
     * and specified bounding box.
     *
     * @param mapContext The map context.
     * @param mapLayer The Map layer.
     * @param bbox The bounding box of the request.
     *
     * @return A map containing all the key value pairs for a GetMap request.
     */
    public static Map createGetMapRequest(WMSMapContext mapContext, MapLayer mapLayer, Envelope bbox) {
        HashMap map = new HashMap();

        map.put("service", "wms");
        map.put("request", "GetMap");
        map.put("version", "1.1.1");
        map.put("format", "image/png");
        map.put("layers", mapLayer.getTitle());
        map.put("styles", mapLayer.getStyle().getName());
        map.put("height", String.valueOf(mapContext.getMapHeight()));
        map.put("width", String.valueOf(mapContext.getMapWidth()));

        map.put("bbox", encode(bbox));

        map.put("srs", "EPSG:4326");
        map.put("transparent", "true");

        return map;
    }

    /**
     * Encodes an envelope to be used in a wms request.
     */
    public static String encode(Envelope box) {
        return new StringBuffer().append(box.getMinX()).append(",").append(box.getMinY()).append(",")
                                 .append(box.getMaxX()).append(",").append(box.getMaxY()).toString();
    }

    /**
     * Encodes the key value pairs of a GetMap request as a full url.
     *
     * @param mapContext The map context.
     * @param kvp The key value pairts of the request.
     *
     * @return The full request url.
     */
    public static String encode(WMSMapContext mapContext, Map kvp) {
        GetMapRequest request = mapContext.getRequest();

        String baseUrl = Requests.getBaseUrl(request.getHttpServletRequest(), request.getGeoServer());

        StringBuffer href = new StringBuffer(baseUrl);

        href.append("wms?");

        for (Iterator e = kvp.entrySet().iterator(); e.hasNext();) {
            Map.Entry entry = (Map.Entry) e.next();
            href.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        href.setLength(href.length() - 1);

        return href.toString();
    }

    /**
     * Creates sax attributes from an array of key value pairs.
     *
     * @param nameValuePairs Alternating key value pair array.
     *
     */
    public static Attributes attributes(String[] nameValuePairs) {
        AttributesImpl attributes = new AttributesImpl();

        for (int i = 0; i < nameValuePairs.length; i += 2) {
            String name = nameValuePairs[i];
            String value = nameValuePairs[i + 1];

            attributes.addAttribute("", name, name, "", value);
        }

        return attributes;
    }
}
