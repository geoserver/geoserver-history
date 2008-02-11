/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.global.GeoServer;
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
     * @param service The servlet handling the WMS request.
     */
    public WMSRequest(String requestType, WMService service) {
        super(WMS_SERVICE_TYPE, requestType, service);
    }

    /**
     * Sets the wms service object.
     */
    public void setWMService(WMService wms) {
        setServiceRef(wms);
    }

    /**
     * Returns the wms service object..
     */
    public WMService getWMService() {
        return (WMService) getServiceRef();
    }

    /**
     * Convenience method for obtaining the global wms service instance.
     */
    public WMS getWMS() {
        return getWMService().getWMS();
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
