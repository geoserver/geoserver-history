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
 * This utility reads in a Delete KVP request and turns it into an appropriate 
 * internal Delete type request object.
 * 
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $VERSION$
 */
public class DeleteKvpReader 
     extends RequestKvpReader {

    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.requests");


    /** Constructor with raw request string.  Calls parent. */
    public DeleteKvpReader (String request) { super(request); }
    
    
    /**
     * Returns Delete request object.
     * @return Delete request objects
     * @tasks REVISIT: What to do if typename is not specified?  filter
     * and bbox should fail, but the spec says that typename is optional
     * for featureid.  But it also has all examples with feature id's
     * having the typename appended, which is the case for oracle, but
     * not for all dbs.  So we need clarification on the spec. (ch)
     */
    public TransactionRequest getRequest()
        throws WfsException {

        TransactionRequest parentRequest = new TransactionRequest();
        boolean releaseAll = true;

        // set global request parameters
        LOGGER.finest("setting global request parameters");
        if( kvpPairs.containsKey("VERSION")) {
            parentRequest.setVersion((String) kvpPairs.get("VERSION"));
        }
        if( kvpPairs.containsKey("REQUEST")) {
            parentRequest.setRequest((String) kvpPairs.get("REQUEST"));
        }

        // declare tokenizers for repeating elements
        LOGGER.finest("setting query request parameters");
	//TODO: error checking here, make sure filter and bbox have a typename.
        List typeList = readFlat((String) kvpPairs.get("TYPENAME"), 
                                 INNER_DELIMETER);
        LOGGER.finest("type list size: " + typeList.size());
        LOGGER.finest("type list element: " + typeList.get(0));
        List filterList = readFilters((String) kvpPairs.get("FEATUREID"), 
                                      (String) kvpPairs.get("FILTER"),
                                      (String) kvpPairs.get("BBOX"));
        int featureSize = typeList.size();
        int filterSize = filterList.size();
        
        // prepare the release action boolean for all delete transactions
        if( kvpPairs.containsKey("RELEASEACTION")) {
            String lockAction = (String) kvpPairs.get("RELEASEACTION");
            if(lockAction == null) {
            } else if(lockAction.toUpperCase().equals("ALL")) {
                releaseAll = true;                
            } else if(lockAction.toUpperCase().equals("SOME")) {
                releaseAll = false;                
            } else {
                throw new WfsException("Illegal lock action: " + lockAction);
            }
        }
        
        // check for errors in the request
        if((filterSize != featureSize) && (filterSize > 0) || 
           ((filterSize > 0) && (featureSize == 0))) {
            throw new WfsException("Filter size does not match" +  
                                   " feature types.  Filter size: " + 
                                   filterSize + " Feature size: "+featureSize);
        } else if(filterSize == featureSize) {
            for(int i = 0, n = featureSize; i < n; i++) {
                DeleteRequest childRequest = new DeleteRequest();       
                childRequest.setTypeName((String) typeList.get(i));
                childRequest.setFilter((Filter) filterList.get(i));
                childRequest.setReleaseAll(releaseAll);
                parentRequest.addSubRequest(childRequest);
            }
        }
        return parentRequest;
    }
}
