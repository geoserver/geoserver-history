/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets;

import org.vfny.geoserver.ExceptionHandler;
import org.vfny.geoserver.WfsExceptionHandler;


/**
 * base servlet class for all WFS requests
 *
 * @author Gabriel Roldán
 * @version $Id: WFService.java,v 1.1.2.2 2003/11/14 20:39:15 groldan Exp $
 */
abstract public class WFService extends AbstractService {
    /**
     * a Web Feature Service exception handler
     *
     * @return an instance of WfsExceptionHandler
     */
    protected ExceptionHandler getExceptionHandler() {
        return WfsExceptionHandler.getInstance();
    }
}
