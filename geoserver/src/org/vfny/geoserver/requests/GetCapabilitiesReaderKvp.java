/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is released under the Apache license, availible at the root GML4j directory.
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;


/**
 * This utility reads in a DescribeFeatureType KVP request and turns it into a list of requested Feature Types.
 * 
 * <p>If you pass this utility a KVP request (everything after the '?' in the URI),
 * it will translate this into a list of feature types.  Note that you must check for validity
 * before passing the request.</p>
 * 
 * @author Rob Hranac, Vision for New York
 * @version 0.9 beta, 12/01/01
 *
 */
public class GetCapabilitiesReaderKvp extends KvpRequestReader {


    /**
     * Constructor with raw request string.  Calls parent.
     *
     * @param getCapabilitiesRequest The raw request string from the client.
     */
    public GetCapabilitiesReaderKvp (String getCapabilitiesRequest) {
        super(getCapabilitiesRequest);        
    }
    
    
    /**
     * Returns a list of requested feature types..
     *
     */
    public GetCapabilitiesRequest getRequest () {
        GetCapabilitiesRequest currentRequest = new GetCapabilitiesRequest();
        currentRequest.setVersion( ((String) kvpPairs.get("VERSION")) );
        currentRequest.setService( ((String) kvpPairs.get("SERVICE")) );
        
        return currentRequest;
    }
    
    
}
