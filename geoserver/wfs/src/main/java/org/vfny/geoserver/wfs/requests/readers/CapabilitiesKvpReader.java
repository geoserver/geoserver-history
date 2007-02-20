/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.requests.readers;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.util.requests.CapabilitiesRequest;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.wfs.servlets.WFService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;


/**
 * This utility reads in a GetCapabilities KVP request and turns it into an
 * appropriate internal CapabilitiesRequest object, upon request.
 *
 * @author Rob Hranac, TOPP
 * @author Gabriel Rold?n
 * @version $Id: CapabilitiesKvpReader.java,v 1.7 2004/02/09 23:29:40 dmzwiers Exp $
 */
public class CapabilitiesKvpReader extends KvpRequestReader {
    /**
             * Creates a new reader from kvp pairs and service.
             *
             * @param kvpPairs The raw string of a capabilities kvp request.
             * @param service The
             */
    public CapabilitiesKvpReader(Map kvpPairs, WFService service) {
        super(kvpPairs, service);
    }

    /**
     * Get Capabilities request.
     *
     * @param request the servlet request to get the GeoServer object from
     *
     * @return Capabilities request.
     *
     * @throws ServiceException DOCUMENT ME!
     */
    public Request getRequest(HttpServletRequest request)
        throws ServiceException {
        CapabilitiesRequest currentRequest = new CapabilitiesRequest("WFS", service);
        currentRequest.setHttpServletRequest(request);
        currentRequest.setVersion(getValue("VERSION"));

        //service is set in the constructor
        //currentRequest.setService(getValue("SERVICE"));
        return currentRequest;
    }
}
