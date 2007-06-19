/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.util;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.map.MapLayer;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.util.Requests;
import org.vfny.geoserver.wms.WMSMapContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;


/**
 * Utility class for creating wms requests.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * @see Requests
 */
public class WMSRequests {
    /**
     * Returns the base url for a wms request, something of the form:
     * <pre>&lt;protocol>://&lt;server>:&lt;port>/&lt;context>/wms</pre>
     *
     * @param request The request.
     * @param geoServer GeoServer configuration.
     *
     * @return The base for wms requests.
     */
    public static String getBaseUrl(HttpServletRequest request, GeoServer geoServer) {
        String baseUrl = Requests.getBaseUrl(request, geoServer);
        baseUrl = Requests.concatUrl(baseUrl, "wms");

        return baseUrl;
    }

    /**
     * Returns the base url for a wms request, something of the form:
     * <pre>&lt;protocol>://&lt;server>:&lt;port>/&lt;context>/wms</pre>
     *
     * @param map A wms map context.
     *
     * @return The base for wms requests.
     */
    public static String getBaseUrl(WMSMapContext map) {
        return getBaseUrl(map.getRequest().getHttpServletRequest(), map.getRequest().getGeoServer());
    }

    /**
     * Encodes the url of a GetMap request pointing to a tile cache if one
     * exists.
     * <p>
     * The tile cache location is determined from {@link GeoServer#getTileCache()}.
     * If the above method returns null this method falls back to the behaviour
     * of {@link #getGetMapUrl(WMSMapContext, MapLayer, Envelope, String[])}.
     * </p>
     * <p>
     * If the <tt>layer</tt> argument is <code>null</code>, the request is
     * made including all layers in the <tt>mapContexT</tt>.
     * </p>
     * <p>
     * If the <tt>bbox</tt> argument is <code>null</code>. {@link WMSMapContext#getAreaOfInterest()}
     * is used for the bbox parameter.
     * </p>
     *
     * @param map The map context.
     * @param layer The Map layer, may be <code>null</code>.
     * @param bbox The bounding box of the request, may be <code>null</code>.
     * @param kvp Additional or overidding kvp parameters, may be <code>null</code>
     *
     * @return The full url for a getMap request.
     */
    public static String getTiledGetMapUrl(WMSMapContext map, MapLayer layer, Envelope bbox,
        String[] kvp) {
        String baseUrl = Requests.getTileCacheBaseUrl(map.getRequest().getHttpServletRequest(),
                map.getRequest().getGeoServer());

        if (baseUrl == null) {
            return getGetMapUrl(map, layer, bbox, kvp);
        }

        return getGetMapUrl(baseUrl, map, layer, bbox, kvp);
    }

    /**
     * Encodes the url of a GetMap request.
     * <p>
     * If the <tt>layer</tt> argument is <code>null</code>, the request is
     * made including all layers in the <tt>mapContexT</tt>.
     * </p>
     * <p>
     * If the <tt>bbox</tt> argument is <code>null</code>. {@link WMSMapContext#getAreaOfInterest()}
     * is used for the bbox parameter.
     * </p>
     *
     * @param map The map context.
     * @param layer The Map layer, may be <code>null</code>.
     * @param bbox The bounding box of the request, may be <code>null</code>.
     * @param kvp Additional or overidding kvp parameters, may be <code>null</code>
     *
     * @return The full url for a getMap request.
     */
    public static String getGetMapUrl(WMSMapContext map, MapLayer layer, Envelope bbox, String[] kvp) {
        //base url
        String baseUrl = getBaseUrl(map);

        return getGetMapUrl(baseUrl, map, layer, bbox, kvp);
    }

    /**
     * Encodes the url of a GetLegendGraphic request.
     *
     * @param map The map context.
     * @param layer The Map layer, may not be <code>null</code>.
     * @param kvp Additional or overidding kvp parameters, may be <code>null</code>
     *
     * @return The full url for a getMap request.
     */
    public static String getGetLegendGraphicUrl(WMSMapContext map, MapLayer layer, String[] kvp) {
        //parameters
        HashMap params = new HashMap();

        params.put("service", "wms");
        params.put("request", "GetLegendGraphic");
        params.put("version", "1.1.1");
        params.put("format", "image/png");
        params.put("layer", layer.getTitle());
        params.put("style", layer.getStyle().getName());
        params.put("height", "20");
        params.put("width", "20");

        //overrides / additions
        for (int i = 0; (kvp != null) && (i < kvp.length); i += 2) {
            params.put(kvp[i], kvp[i + 1]);
        }

        return encode(getBaseUrl(map), params);
    }

    /**
     * Helper method for encoding GetMap request.
     *
     */
    static String getGetMapUrl(String baseUrl, WMSMapContext map, MapLayer layer, Envelope bbox,
        String[] kvp) {
        //parameters
        HashMap params = new HashMap();

        params.put("service", "wms");
        params.put("request", "GetMap");
        params.put("version", "1.1.1");
        params.put("format", "image/png");

        StringBuffer layers = new StringBuffer();
        StringBuffer styles = new StringBuffer();

        if (layer != null) {
            layers.append(layer.getTitle());
            styles.append(layer.getStyle().getName());
        } else {
            for (int i = 0; i < map.getLayerCount(); i++) {
                layer = (MapLayer) map.getLayer(i);
                layers.append(layer.getTitle()).append(",");
                styles.append(layer.getStyle().getName()).append(",");
            }

            layers.setLength(layers.length() - 1);
            styles.setLength(styles.length() - 1);
        }

        params.put("layers", layers.toString());
        params.put("styles", styles.toString());

        params.put("height", String.valueOf(map.getMapHeight()));
        params.put("width", String.valueOf(map.getMapWidth()));

        if (bbox == null) {
            bbox = map.getAreaOfInterest();
        }

        params.put("bbox", encode(bbox));

        params.put("srs", "EPSG:4326");
        params.put("transparent", "true");

        //overrides / additions
        for (int i = 0; (kvp != null) && (i < kvp.length); i += 2) {
            params.put(kvp[i], kvp[i + 1]);
        }

        return encode(baseUrl, params);
    }

    /**
     * Helper method to encode an envelope to be used in a wms request.
     */
    static String encode(Envelope box) {
        return new StringBuffer().append(box.getMinX()).append(",").append(box.getMinY()).append(",")
                                 .append(box.getMaxX()).append(",").append(box.getMaxY()).toString();
    }

    /**
     * Helper method for encoding a baseurl and some kvp params into a single
     * url.
     *
     * @param baseUrl The base url of the encoded request.
     * @param kvp The key value pairts of the request.
     *
     * @return The full request url.
     */
    static String encode(String baseUrl, Map kvp) {
        StringBuffer href = new StringBuffer(baseUrl);

        for (Iterator e = kvp.entrySet().iterator(); e.hasNext();) {
            Map.Entry entry = (Map.Entry) e.next();
            href.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        href.setLength(href.length() - 1);

        return href.toString();
    }
}
