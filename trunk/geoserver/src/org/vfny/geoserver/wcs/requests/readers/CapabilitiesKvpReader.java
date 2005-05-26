/* Copyright (c) 2005 NATO - Undersea Research Centre.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests.readers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.util.requests.CapabilitiesRequest;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;

/**
 * This utility reads in a GetCapabilities KVP request and turns it into an
 * appropriate internal CapabilitiesRequest object, upon request.
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: CapabilitiesKvpReader.java,v 0.1 Feb 15, 2005 12:34:52 PM $
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
     * @param request the servlet request to get the GeoServer object from
     *
     * @return Capabilities request.
     *
     * @throws ServiceException DOCUMENT ME!
     */
    public Request getRequest(HttpServletRequest request) throws ServiceException {
        CapabilitiesRequest currentRequest = new CapabilitiesRequest("WCS");
        currentRequest.setHttpServletRequest(request);
        currentRequest.setVersion(getValue("VERSION"));

        //service is set in the constructor
        //currentRequest.setService(getValue("SERVICE"));
        return currentRequest;
    }
}
