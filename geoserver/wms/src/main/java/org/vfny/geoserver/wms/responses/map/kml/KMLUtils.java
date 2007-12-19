/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import com.vividsolutions.jts.geom.Envelope;
import org.geoserver.wms.util.WMSRequests;
import org.geotools.map.MapLayer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;


/**
 * Some convenience methods used by the kml transformers.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * @deprecated use {@link WMSRequests}.
 */
public class KMLUtils {
    /**
     * Encodes the url of a GetMap request from a map context + map layer.
     * <p>
     * If the <tt>mapLayer</tt> argument is <code>null</code>, the request is
     * made including all layers in the <tt>mapContexT</tt>.
     * </p>
     * <p>
     * If the <tt>bbox</tt> argument is <code>null</code>. {@link WMSMapContext#getAreaOfInterest()}
     * is used for the bbox parameter.
     * </p>
     *
     * @param mapContext The map context.
     * @param mapLayer The Map layer, may be <code>null</code>.
     * @param bbox The bounding box of the request, may be <code>null</code>.
     * @param kvp Additional or overidding kvp parameters, may be <code>null</code>
     * @param tile Flag controlling wether the request should be made against tile cache
     *
     * @return The full url for a getMap request.
     * @deprecated use {@link WMSRequests#getGetMapUrl(WMSMapContext, MapLayer, Envelope, String[])}
     */
    public static String getMapUrl(WMSMapContext mapContext, MapLayer mapLayer, Envelope bbox,
        String[] kvp, boolean tile) {
        if (tile) {
            return WMSRequests.getTiledGetMapUrl(mapContext.getRequest(), mapLayer, bbox, kvp);
        }

        return WMSRequests.getGetMapUrl(mapContext.getRequest(), mapLayer, bbox, kvp);
    }

    /**
     * Encodes the url of a GetMap request from a map context + map layer.
     * <p>
     * If the <tt>mapLayer</tt> argument is <code>null</code>, the request is
     * made including all layers in the <tt>mapContexT</tt>.
     * </p>
     * @param mapContext The map context.
     * @param mapLayer The Map layer, may be <code>null</code>
     * @param kvp Additional or overidding kvp parameters, may be <code>null</code>
     * @param tile Flag controlling wether the request should be made against tile cache
     *
     * @return The full url for a getMap request.
     * @deprecated use {@link WMSRequests#getGetMapUrl(WMSMapContext, MapLayer, Envelope, String[])}
     */
    public static String getMapUrl(WMSMapContext mapContext, MapLayer mapLayer, boolean tile) {
        return getMapUrl(mapContext, mapLayer, mapContext.getAreaOfInterest(), null, tile);
    }

    /**
     * Encodes the url for a GetLegendGraphic request from a map context + map layer.
     *
     * @param mapContext The map context.
     * @param mapLayer The map layer.
     * @param kvp Additional or overidding kvp parameters, may be <code>null</code>
     *
     * @return A map containing all the key value pairs for a GetLegendGraphic request.
     * @deprecated use {@link WMSRequests#getGetLegendGraphicUrl(WMSMapContext, MapLayer, String[])
     */
    public static String getLegendGraphicUrl(WMSMapContext mapContext, MapLayer mapLayer,
        String[] kvp) {
        return WMSRequests.getGetLegendGraphicUrl(mapContext.getRequest(), mapLayer, kvp);
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
