/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms;

import org.vfny.geoserver.config.*;
import org.vfny.geoserver.responses.*;
import org.xml.sax.ContentHandler;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: WMSCapabilitiesResponse.java,v 1.2 2003/12/16 18:46:10 cholmesny Exp $
 */
public class WMSCapabilitiesResponse extends CapabilitiesResponse {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ServiceConfig getServiceConfig() {
        return ServerConfig.getInstance().getWMSConfig();
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
        cr.setPrettyPrint(true,
            ServerConfig.getInstance().getGlobalConfig().isVerbose());

        return cr;
    }
    /* (non-Javadoc)
     * @see org.vfny.geoserver.responses.Response#abort()
     */
    public void abort() {
        // nothing to undo    
    }

}
