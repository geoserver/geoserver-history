/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

/**
 * Defines a general WFS Request type
 *
 * @author Gabriel Roldán
 * @version $Id: WFSRequest.java,v 1.1.2.2 2003/11/14 20:39:14 groldan Exp $
 */
abstract public class WFSRequest extends Request {
    public WFSRequest() {
        super("WFS");
    }
}
