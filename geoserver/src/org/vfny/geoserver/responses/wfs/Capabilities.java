/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import org.vfny.geoserver.config.*;
import org.vfny.geoserver.config.ServiceConfig;
import org.vfny.geoserver.responses.*;
import org.xml.sax.ContentHandler;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: Capabilities.java,v 1.1.2.2 2003/11/14 20:39:14 groldan Exp $
 */
public class Capabilities extends CapabilitiesResponse {
    protected ServiceConfig getServiceConfig() {
        return ServerConfig.getInstance().getWMSConfig();
    }

    protected ResponseHandler getResponseHandler(ContentHandler contentHandler) {
        return new WfsCapabilitiesResponseHandler(contentHandler);
    }
}
