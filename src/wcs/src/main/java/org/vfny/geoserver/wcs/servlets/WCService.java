/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.servlets;

import org.vfny.geoserver.global.WCS;
import org.vfny.geoserver.servlets.AbstractService;

import javax.servlet.http.HttpServletRequest;


/**
 * Base servlet for all Web Coverage Server requests.
 *
 * <p>
 * Subclasses should supply the handler, request and response mapping for the
 * service they implement.
 * </p>
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id$
 */
abstract public class WCService extends AbstractService {
    /**
         * Constructor for WCS service.
         *
         * @param request The service request being made (GetCaps,GetCoverage,...)
         * @param wcs The WCS service reference.
         */
    public WCService(String request, WCS wcs) {
        super("WCS", request, wcs);
    }

    /**
     * @return The wcs service ref.
     */
    public WCS getWCS() {
        return (WCS) getServiceRef();
    }

    /**
     * Sets the wcs service ref.
     * @param wcs
     */
    public void setWCS(WCS wcs) {
        setServiceRef(wcs);
    }

    protected boolean isServiceEnabled(HttpServletRequest req) {
        return getWCS().isEnabled();
    }
}
