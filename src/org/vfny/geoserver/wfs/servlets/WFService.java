/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets;

import javax.servlet.http.HttpServletRequest;

import org.vfny.geoserver.ExceptionHandler;
import org.vfny.geoserver.WfsExceptionHandler;
import org.vfny.geoserver.requests.Requests;


/**
 * Base servlet for all Web Feature Server requests.
 * 
 * <p>
 * Subclasses should supply the handler, request and response mapping for the
 * service they implement.
 * </p>
 *
 * @author Gabriel Roldán
 * @version $Id: WFService.java,v 1.6 2004/02/17 22:42:32 dmzwiers Exp $
 */
abstract public class WFService extends AbstractService {
    /**
     * a Web Feature ServiceConfig exception handler
     *
     * @return an instance of WfsExceptionHandler
     */
    protected ExceptionHandler getExceptionHandler() {
        return WfsExceptionHandler.getInstance();
    }
    
    protected boolean isServiceEnabled(HttpServletRequest req){
    	return Requests.getWFS(req).isEnabled();
    }
}
