/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms;

import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.responses.CapabilitiesResponse;
import org.vfny.geoserver.responses.CapabilitiesResponseHandler;
import org.vfny.geoserver.responses.ResponseHandler;
import org.xml.sax.ContentHandler;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: WMSCapabilitiesResponse.java,v 1.2.2.5 2004/01/06 09:00:48 jive Exp $
 */
public class WMSCapabilitiesResponse extends CapabilitiesResponse {
    /**
     * Retrieves the GeoServer's Global Web Map Service.
     *
     * @return Web Map Service
     */
    protected Service getGlobalService() {
        //return GeoServer.getInstance().getWMS();
    	return request.getGeoServer().getWMS();
    }

    /**
     * DOCUMENT ME!
     *
     * @param contentHandler DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ResponseHandler getResponseHandler(ContentHandler contentHandler) {
        CapabilitiesResponseHandler cr = new WmsCapabilitiesResponseHandler(contentHandler);
        cr.setPrettyPrint(true, request.getGeoServer().isVerbose() );

        return cr;
    }
}
