/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers.wms;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.requests.CapabilitiesRequest;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.readers.KvpRequestReader;

/**
 * This utility reads in a GetCapabilities KVP request and turns it into an
 * appropriate internal CapabilitiesRequest object, upon request.
 *
 * @author Rob Hranac, TOPP
 * @author Gabriel Rold?n, Axios Engineering
 * @version $Id: CapabilitiesKvpReader.java,v 1.7 2004/02/09 23:29:47 dmzwiers Exp $
 */
public class CapabilitiesKvpReader extends KvpRequestReader {
    /**
     * Constructor with raw request string.  Calls parent.
     *
     * @param kvPairs The raw string of a capabilities kvp request.
     */
    public CapabilitiesKvpReader(Map kvPairs) {
        super(kvPairs);
    }

    /**
     * Get Capabilities request.
     *
     * @return Capabilities request.
     *
     * @throws ServiceException DOCUMENT ME!
     */
    public Request getRequest(HttpServletRequest request) throws ServiceException {
        CapabilitiesRequest currentRequest = new CapabilitiesRequest("WMS");
        currentRequest.setHttpServletRequest(request);
        String reqVersion = WMS.getVersion();

        if (keyExists("VERSION")) {
            reqVersion = getValue("VERSION");
        } else if (keyExists("WMTVER")) {
            reqVersion = getValue("WMTVER");
        }

        currentRequest.setVersion(reqVersion);

        return currentRequest;
    }
}
