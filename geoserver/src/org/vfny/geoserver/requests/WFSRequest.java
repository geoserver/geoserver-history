/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

/**
 * Defines a general WFS Request type
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
abstract public class WFSRequest extends Request {
    public WFSRequest() {
        super("WFS");
    }
}
