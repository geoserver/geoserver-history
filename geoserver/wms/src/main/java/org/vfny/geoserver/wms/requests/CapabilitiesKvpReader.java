/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import org.geoserver.platform.ServiceException;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.util.requests.CapabilitiesRequest;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.wms.servlets.WMService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;


/**
 * This utility reads in a GetCapabilities KVP request and turns it into an
 * appropriate internal CapabilitiesRequest object, upon request.
 *
 * @author Rob Hranac, TOPP
 * @author Gabriel Rold?n, Axios Engineering
 * @version $Id$
 */
public class CapabilitiesKvpReader extends KvpRequestReader {
    
    /**
     * Creates a Capabilities Kvp Reader.
     * 
     * @param kvPairs the kvp set.
     * @param service The wms service config.
     */
    public CapabilitiesKvpReader(Map kvPairs, WMS service) {
        super(kvPairs, service);
    }

    /**
     * Get Capabilities request.
     *
     * @return Capabilities request.
     *
     * @throws ServiceException DOCUMENT ME!
     */
    public Request getRequest(HttpServletRequest request)
        throws ServiceException {
        CapabilitiesRequest currentRequest = new WMSCapabilitiesRequest((WMS) serviceConfig);
        currentRequest.setHttpServletRequest(request);

        String reqVersion = WMS.getVersion();

        if (keyExists("VERSION")) {
            reqVersion = getValue("VERSION");
        } else if (keyExists("WMTVER")) {
            reqVersion = getValue("WMTVER");
        }

        currentRequest.setVersion(reqVersion);
        
        if (keyExists("UPDATESEQUENCE")) {
        	currentRequest.setUpdateSequence(getValue("UPDATESEQUENCE"));
        }

        return currentRequest;
    }
}
