/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import org.vfny.geoserver.global.GeoServer;

/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: WMSRequest.java,v 1.6 2004/02/09 23:11:33 dmzwiers Exp $
 */
public class WMSRequest extends Request {
    public WMSRequest() {
        super("WMS");
    }

    public GeoServer getGeoServer(){
    	GeoServer gs = getWFS().getGeoServer();
    	return gs;
    }
}
