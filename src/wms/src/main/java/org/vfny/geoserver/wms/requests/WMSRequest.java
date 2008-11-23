/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.servlets.WMService;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Rold?n
 * @version $Id$
 */
public class WMSRequest extends Request {
    public static final String WMS_SERVICE_TYPE = "WMS";

    /**
     * Creates the new request, supplying the request name and the sevlet
     * handling the request.
     *
     * @param requestType name of hte request, (Example, GetCapabiliites)
     * @param wms The wms configuration object.
     * 
     */
    public WMSRequest(String request, WMS wms) {
        super(WMS_SERVICE_TYPE,request,wms);
    }

    /**
     * Convenience method for obtaining the global wms service instance.
     */
    public WMS getWMS() {
        return (WMS) getServiceConfig();
    }

    /**
     * Convenience method for obtaining the global geoserver instance.
     */
    public GeoServer getGeoServer() {
        GeoServer gs = getWMS().getGeoServer();

        return gs;
    }
    
    /**
     * Setter for the 'WMTVER' parameter, which is an alias for 'VERSION'.§ 
     * 
     */
    public void setWmtVer( String version ) {
        setVersion( version );
    }
}
