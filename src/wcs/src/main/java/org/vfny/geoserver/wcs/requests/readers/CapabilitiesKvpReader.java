/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests.readers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.geoserver.platform.ServiceException;
import org.geoserver.wcs.WCSInfo;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.util.requests.CapabilitiesRequest;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.wcs.requests.WCSCapabilitiesRequest;


/**
 * This utility reads in a GetCapabilities KVP request and turns it into an
 * appropriate internal CapabilitiesRequest object, upon request.
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id$
 */
public class CapabilitiesKvpReader extends KvpRequestReader {
    /**
     * Constructor with raw request string.  Calls parent.
     *
     * @param kvPairs The raw string of a capabilities kvp request.
     */
    public CapabilitiesKvpReader(Map kvPairs, WCSInfo wcs) {
        super(kvPairs, wcs);
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
        CapabilitiesRequest currentRequest = new WCSCapabilitiesRequest((WCSInfo) serviceConfig);
        currentRequest.setHttpServletRequest(request);
        currentRequest.setVersion(getValue("VERSION"));
        currentRequest.setUpdateSequence(getValue("UPDATESEQUENCE"));

        //service is set in the constructor
        //currentRequest.setService(getValue("SERVICE"));
        return currentRequest;
    }
}
