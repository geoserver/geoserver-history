/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.geotools.filter.Filter;
import org.geotools.filter.FidFilter;
import org.vfny.geoserver.responses.WfsException;

/**
 * This utility reads in a LockFeature KVP request and turns it into an
 * appropriate internal Lock type request object.
 * 
 * @author Rob Hranac, TOPP
 * @version beta, 12/01/01
 */
public class LockKvpReader 
     extends RequestKvpReader {
   
    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.requests");


    /** Constructor with raw request string.  Calls parent. */
    public LockKvpReader (String request) { super(request); }
    
    
    /**
     * Returns Lock feature request object.
     * @return Lock request objects
     */
    public LockRequest getRequest()
        throws WfsException {
        
        LockRequest currentRequest = new LockRequest();       

        // set global request parameters
        LOGGER.finest("setting global request parameters");
        if( kvpPairs.containsKey("VERSION")) {
            currentRequest.setVersion((String) kvpPairs.get("VERSION"));
        }
        if( kvpPairs.containsKey("REQUEST")) {
            currentRequest.setRequest((String) kvpPairs.get("REQUEST"));
        }
        if( kvpPairs.containsKey("EXPIRY")) {
            currentRequest.
                setExpiry(Integer.parseInt((String) kvpPairs.get("EXPIRY")));
        }
        if( kvpPairs.containsKey("LOCKACTION")) {
            String lockAction = (String) kvpPairs.get("LOCKACTION");
            if(lockAction == null) {
                currentRequest.setLockAll(true);
            } else if(lockAction.toUpperCase().equals("ALL")) {
                currentRequest.setLockAll(true);                
            } else if(lockAction.toUpperCase().equals("SOME")) {
                currentRequest.setLockAll(false);                
            } else {
                throw new WfsException("Illegal lock action: " + lockAction);
            }
        }

        // declare tokenizers for repeating elements
        LOGGER.finer("setting query request parameters");
        List typeList = readFlat((String)kvpPairs.get("TYPENAME"), 
                                 INNER_DELIMETER);
        LOGGER.finer("type list size: " + typeList.size());      
        List filterList = readFilters((String) kvpPairs.get("FEATUREID"), 
                                      (String) kvpPairs.get("FILTER"),
                                      (String) kvpPairs.get("BBOX"));
	if (typeList.size() == 0) {
	    typeList = getTypesFromFids((String) kvpPairs.get("FEATUREID"));
	    if (typeList.size() == 0) {
		throw new WfsException("The typename element is mandatory if "
				       + "no FEATUREID is present");
	    }
	}
        int featureSize = typeList.size();
        int filterSize = filterList.size();
        
        // check for errors in the request
        if((filterSize != featureSize) && (filterSize > 0) || 
           ((filterSize > 0) && (featureSize == 0))) {
            throw new WfsException("Filter size does not match" +  
                                   " feature types.  Filter size: " + 
                                   filterSize + " Feature size: "+featureSize);
        } else {
	    currentRequest.setLocks(typeList, filterList);
            return currentRequest;
        }
    }
}
