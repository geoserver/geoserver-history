/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import org.vfny.geoserver.config.*;
import org.vfny.geoserver.responses.*;
import org.xml.sax.ContentHandler;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: WFSCapabilitiesResponse.java,v 1.1.2.2 2003/11/14 20:39:15 groldan Exp $
 */
public class WFSCapabilitiesResponse extends CapabilitiesResponse {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ServiceConfig getServiceConfig() {
        return ServerConfig.getInstance().getWFSConfig();
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
            ServerConfig.getInstance().getGlobalConfig().isVerbose());

        return cr;
    }
}
