/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.servlets;

import org.geoserver.wms.WMS;


/**
 * GetMap handler that supports the old "request=map" requests (WMS 1.0)
 * @author aaime
 */
public class GetMapLegacy extends GetMap {
    public GetMapLegacy(WMS wms) {
        super("map", wms);
    }
}
