/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;

/**
 * This utility reads in a GetCapabilities KVP request and turns it into an 
 * appropriate internal CapabilitiesRequest object, upon request.
 *
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
public class CapabilitiesKvpReader 
    extends RequestKvpReader {

    /** Constructor with raw request string.  Calls parent. */
    public CapabilitiesKvpReader (String request) { super(request); }
    
    /**
     * Get Capabilities request.
     * @returns Capabilities request.
     */
    public CapabilitiesRequest getRequest () {
        CapabilitiesRequest currentRequest = new CapabilitiesRequest();
        currentRequest.setVersion(((String) kvpPairs.get("VERSION")));
        currentRequest.setService(((String) kvpPairs.get("SERVICE")));        
        return currentRequest;
    }    
}
