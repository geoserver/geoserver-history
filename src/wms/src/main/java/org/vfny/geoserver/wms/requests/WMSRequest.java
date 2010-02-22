/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import org.geoserver.wms.WMS;
import org.vfny.geoserver.Request;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Rold?n
 * @version $Id$
 */
public class WMSRequest extends Request {
    public static final String WMS_SERVICE_TYPE = "WMS";
    protected WMS wms;

    /**
     * Creates the new request, supplying the request name and the sevlet
     * handling the request.
     *
     * @param requestType name of hte request, (Example, GetCapabiliites)
     * @param wms The wms configuration object.
     * 
     */
    public WMSRequest(String request, WMS wms) {
        super(WMS_SERVICE_TYPE,request,wms.getServiceInfo());
        this.wms = wms;
    }

    /**
     * Convenience method for obtaining the global wms service instance.
     */
    public WMS getWMS() {
        return wms;
    }

    /**
     * Convenience method for obtaining the global geoserver instance.
     */
//    public org.geoserver.config.GeoServer getGeoServer() {
//        return getWMS().getGeoServer();
//    }
    
    /**
     * Setter for the 'WMTVER' parameter, which is an alias for 'VERSION'.� 
     * 
     */
    public void setWmtVer( String version ) {
        setVersion( version );
    }
}
