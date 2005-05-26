/* Copyright (c) 2005 NATO - Undersea Research Centre.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.servlets;

import javax.servlet.http.HttpServletRequest;

import org.vfny.geoserver.ExceptionHandler;
import org.vfny.geoserver.servlets.AbstractService;
import org.vfny.geoserver.util.Requests;
import org.vfny.geoserver.wcs.WcsExceptionHandler;


/**
 * Base servlet for all Web Coverage Server requests.
 * 
 * <p>
 * Subclasses should supply the handler, request and response mapping for the
 * service they implement.
 * </p>
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: WCService.java,v 0.1 Feb 15, 2005 12:18:00 PM $
 */
abstract public class WCService extends AbstractService {
    /**
     * a Web Coverage ServiceConfig exception handler
     *
     * @return an instance of WfsExceptionHandler
     */
    protected ExceptionHandler getExceptionHandler() {
        return WcsExceptionHandler.getInstance();
    }
    
    protected boolean isServiceEnabled(HttpServletRequest req){
    	return Requests.getWCS(req).isEnabled();
    }
}
