/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;


/**
 * This utility reads in a GetCapabilities KVP request and turns it into an
 * appropriate internal CapabilitiesRequest object, upon request.
 *
 * @author Rob Hranac, TOPP
 * @version $Id: CapabilitiesKvpReader.java,v 1.2 2003/09/12 18:23:14 cholmesny Exp $
 */
public class CapabilitiesKvpReader extends RequestKvpReader {
    /**
     * Constructor with raw request string.  Calls parent.
     *
     * @param request The raw string of a capabilities kvp request.
     */
    public CapabilitiesKvpReader(String request) {
        super(request);
    }

    /**
     * Get Capabilities request.
     *
     * @return Capabilities request.
     */
    public CapabilitiesRequest getRequest() {
        CapabilitiesRequest currentRequest = new CapabilitiesRequest();
        currentRequest.setVersion(((String) kvpPairs.get("VERSION")));
        currentRequest.setService(((String) kvpPairs.get("SERVICE")));

        return currentRequest;
    }
}
