/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.servlets;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.wms.requests.GetMapReflectKvpReader;
import java.util.Map;


/**
 * A 'reflector' that enables one to leave off mandatory params by passing in some
 * defaults.  This just extends GetMap and overrides the kvp request reader to
 * return a more friendly one.
 *
 * @author Chris Holmes
 * @version $Id$
 */
public class Reflect extends GetMap {
    /**
     * Creates a new reflect object.
     *
     */
    public Reflect(WMS wms) {
        super("Reflect", wms);
    }

    protected Reflect(String id, WMS wms) {
        super(id, wms);
    }

    //No defaults for Post requests, as it makes less sense.

    /**
     * Gets the more relaxed GetMapReflectKvpReader
     *
     * @param params the key value pairs of the request
     *
     * @return A reader to parse the key value pairs.
     */
    protected KvpRequestReader getKvpReader(Map params) {
        Map layers = this.getWMS().getBaseMapLayers();
        Map styles = this.getWMS().getBaseMapStyles();

        GetMapReflectKvpReader kvp = new GetMapReflectKvpReader(params, this);

        // filter layers and styles if the user specified "layers=basemap"
        // This must happen after the kvp reader has been initially called
        if ((layers != null) && !layers.equals("")) {
            kvp.filterBaseMap(layers, styles);
        }

        return kvp;
    }
}
