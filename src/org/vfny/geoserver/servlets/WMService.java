/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets;

import org.vfny.geoserver.ExceptionHandler;
import org.vfny.geoserver.WmsExceptionHandler;


/**
 * Base servlet for all Web Map Server requests.
 * 
 * <p>
 * Subclasses should supply the handler, request and response mapping for the
 * service they implement.
 * </p>
 *
 * @author Gabriel Roldán
 * @version $Id: WMService.java,v 1.4 2004/01/21 00:26:07 dmzwiers Exp $
 */
abstract public class WMService extends AbstractService {
    /**
     * returns a Web Map ServiceConfig exception handler
     *
     * @return WmsExceptionHandler
     */
    protected ExceptionHandler getExceptionHandler() {
        return WmsExceptionHandler.getInstance();
    }
}
