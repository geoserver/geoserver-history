/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;

/**
 * This utility reads in a DescribeFeatureType KVP request and turns it into an
 * appropriate internal DescribeRequest object.
 *
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
public class DescribeKvpReader 
    extends RequestKvpReader {

     /** Constructor with raw request string.  Calls parent. */
    public DescribeKvpReader (String request) { super(request); }

    /**
     * Returns a list of requested feature types..
     * @returns Describe request object.
     */
    public DescribeRequest getRequest () {
        DescribeRequest currentRequest = new DescribeRequest();        
        currentRequest.setVersion(((String) kvpPairs.get("VERSION")));
        currentRequest.setRequest(((String) kvpPairs.get("REQUEST")));
        currentRequest.setOutputFormat(((String) kvpPairs.get("OUTPUTFORMAT")));
	currentRequest.setFeatureTypes
	    (readFlat((String) kvpPairs.get("TYPENAME"), INNER_DELIMETER));     
        return currentRequest;
    }    
}
