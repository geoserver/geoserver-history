package org.vfny.geoserver.wms.servlets;

import org.vfny.geoserver.global.WMS;

/**
 * GetMap handler that supports the old "request=map" requests (WMS 1.0) 
 * @author aaime
 */
public class GetMapLegacy extends GetMap {

    public GetMapLegacy(WMS wms) {
        super("map", wms);
    }

}
