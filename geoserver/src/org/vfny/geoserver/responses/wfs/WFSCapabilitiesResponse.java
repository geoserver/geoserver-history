/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

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
 * @version $Id: WFSCapabilitiesResponse.java,v 1.2.2.4 2004/01/05 22:14:42 dmzwiers Exp $
 */
public class WFSCapabilitiesResponse extends CapabilitiesResponse {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Service getGlobalService() {
        return GeoServer.getInstance().getWFS();
    }

    /**
     * DOCUMENT ME!
     *
     * @param contentHandler DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ResponseHandler getResponseHandler(ContentHandler contentHandler) {
        CapabilitiesResponseHandler cr = new WfsCapabilitiesResponseHandler(contentHandler);
        cr.setPrettyPrint(true,
            GeoServer.getInstance().isVerbose());

        return cr;
    }
    /* (non-Javadoc)
     * @see org.vfny.geoserver.responses.Response#abort()
     */
    public void abort() {
        // nothing to undo
    }

}
