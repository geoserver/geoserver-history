/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.servlets;

import javax.servlet.http.HttpServletRequest;

import org.geoserver.wms.WMS;
import org.vfny.geoserver.servlets.AbstractService;


/**
 * Base servlet for all Web Map Server requests.
 *
 * <p>
 * Subclasses should supply the handler, request and response mapping for the
 * service they implement.
 * </p>
 *
 * @author Gabriel Rold?n
 * @version $Id$
 */
abstract public class WMService extends AbstractService {
    private WMS wms;

    /**
     * Constructor for WMS service.
     *
     * @param request The service request being made (GetCaps,GetFeature,...)
     * @param wms The WMS service reference.
     */
    public WMService(String request, WMS wms) {
        super("WMS", request, wms.getServiceInfo());
        this.wms = wms;
    }

    /**
     * @return The wms service ref.
     */
    public WMS getWMS() {
        return wms;
    }

    /**
     * Sets the wms service ref.
     * @param wms
     */
    public void setWMS(WMS wms) {
        this.wms = wms;
        setServiceRef(wms.getServiceInfo());
    }

    protected boolean isServiceEnabled(HttpServletRequest req) {
        return getWMS().isEnabled();
    }
}
