/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.responses.CapabilitiesResponse;
import org.vfny.geoserver.responses.ResponseHandler;
import org.xml.sax.ContentHandler;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: Capabilities.java,v 1.6 2004/02/09 23:11:36 dmzwiers Exp $
 */
public abstract class Capabilities extends CapabilitiesResponse {
    protected Service getGlobalService() {
        if (request == null) {
            throw new IllegalStateException(
                "Call execute before get getGlobalService!");
        }

        return request.getWFS();
    }

    protected ResponseHandler getResponseHandler(ContentHandler contentHandler) {
        return new WfsCapabilitiesResponseHandler(contentHandler, request);
    }
}
