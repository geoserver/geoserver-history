/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.responses.CapabilitiesResponse;
import org.vfny.geoserver.responses.ResponseHandler;
import org.xml.sax.ContentHandler;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: Capabilities.java,v 1.2.2.5 2004/01/06 09:00:48 jive Exp $
 */
public class Capabilities extends CapabilitiesResponse {
    protected Service getGlobalService() {
        //return GeoServer.getInstance().getWMS();
    	// JG - that was a mistake right?
		if( request == null ){
			throw new IllegalStateException(
					"Call execute before get getGlobalService!"
			);
		}    	
    	return request.getGeoServer().getWFS();
    }

    protected ResponseHandler getResponseHandler(ContentHandler contentHandler) {
        return new WfsCapabilitiesResponseHandler(contentHandler);
    }    
}
