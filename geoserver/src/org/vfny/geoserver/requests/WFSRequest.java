/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

/**
 * Defines a general WFSConfig Request type
 *
 * @author Gabriel Roldán
 * @version $Id: WFSRequest.java,v 1.2.2.1 2003/12/31 23:36:44 dmzwiers Exp $
 */
abstract public class WFSRequest extends Request {
    public static final String WFS_SERVICE_TYPE = "WFSConfig";
    
    /** A WFSRequest configured with WFS_SERVICE_TYPE */
    public WFSRequest() {
        super( WFS_SERVICE_TYPE );
    }
    /** A WFSRequest configured with WFS_SERVICE_TYPE */    
    public WFSRequest( String requestType ){
        super( WFS_SERVICE_TYPE, requestType );
    }
}
