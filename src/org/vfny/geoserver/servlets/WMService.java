/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets;

import org.vfny.geoserver.ExceptionHandler;
import org.vfny.geoserver.WmsExceptionHandler;


/**
 * base servlet class for all WFS requests
 *
 * @author Gabriel Roldán
 * @version $Id: WMService.java,v 1.2 2003/12/16 18:46:10 cholmesny Exp $
 */
abstract public class WMService extends AbstractService {
    /**
     * returns a Web Map Service exception handler
     *
     * @return WmsExceptionHandler
     */
    protected ExceptionHandler getExceptionHandler() {
        return WmsExceptionHandler.getInstance();
    }
}
