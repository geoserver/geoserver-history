/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import org.geotools.validation.ValidationProcessor;
import org.vfny.geoserver.global.GeoServer;

/**
 * Defines a general WFS Request type
 *
 * @author Gabriel Roldán
 * @version $Id: WFSRequest.java,v 1.7 2004/02/09 23:11:33 dmzwiers Exp $
 */
abstract public class WFSRequest extends Request {
    public static final String WFS_SERVICE_TYPE = "WFS";

    /**
     * A WFSRequest configured with WFS_SERVICE_TYPE
     */
    public WFSRequest() {
        super(WFS_SERVICE_TYPE);
    }

    /**
     * A WFSRequest configured with WFS_SERVICE_TYPE
     *
     * @param requestType DOCUMENT ME!
     */
    public WFSRequest(String requestType) {
        super(WFS_SERVICE_TYPE, requestType);
    }

    public GeoServer getGeoServer(){
    	GeoServer gs = getWFS().getGeoServer();
    	return gs;
    }

    
    public ValidationProcessor getValidationProcessor(){
    	ValidationProcessor vp = getWFS().getValidation();
    	return vp;
    }
}
