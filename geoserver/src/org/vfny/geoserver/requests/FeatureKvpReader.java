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
 * This utility reads in a GetFeature KVP request and turns it into a 
 * GetFeature type request object.
 * 
 * <p>If you pass this utility a KVP request (everything after the '?' in the 
 * URI), it will translate this into a GetFeature type request object.  Note 
 * that you  must check for validity before passing the request.</p>
 * 
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $version$
 */
public class FeatureKvpReader 
     extends RequestKvpReader {
   
    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.requests");


    /** Constructor with raw request string.  Calls parent. */
    public FeatureKvpReader (String request) { super(request); }
    
    
    public FeatureRequest getRequest() throws WfsException {
	return getRequest(false);
    }

    /**
     * Returns GetFeature request object.
     * @return Feature request object.
     */
    public FeatureRequest getRequest(boolean withLock)
        throws WfsException {
	
	FeatureRequest currentRequest = new FeatureRequest();
	if( withLock || kvpPairs.containsKey("REQUEST")) {
	    String request = (String) kvpPairs.get("REQUEST");
	    if (withLock || request.equalsIgnoreCase("GETFEATUREWITHLOCK")) {
		withLock = true;
		currentRequest = new FeatureWithLockRequest();
	    }
            currentRequest.setRequest(request);
        }

        // set global request parameters
        LOGGER.finest("setting global request parameters");
        if( kvpPairs.containsKey("MAXFEATURES")) {
            currentRequest.setMaxFeatures((String)kvpPairs.get("MAXFEATURES"));
        }
        if( kvpPairs.containsKey("VERSION")) {
            currentRequest.setVersion((String) kvpPairs.get("VERSION"));
        }
        if( kvpPairs.containsKey("REQUEST")) {
            currentRequest.setRequest((String) kvpPairs.get("REQUEST"));
        }
        if( kvpPairs.containsKey("FEATUREVERSION")) {
            currentRequest.
                setFeatureVersion((String) kvpPairs.get("FEATUREVERSION"));
        }
        if( kvpPairs.containsKey("OUTPUTFORMAT")) {
            currentRequest.
		setOutputFormat((String) kvpPairs.get("OUTPUTFORMAT"));
        }
	if (withLock && kvpPairs.containsKey("EXPIRY")){
	    ((FeatureWithLockRequest)currentRequest).
		setExpiry(Integer.parseInt((String) kvpPairs.get("EXPIRY")));
	}
        // declare tokenizers for repeating elements
        LOGGER.finest("setting query request parameters");
        List typeList = readFlat((String)kvpPairs.get("TYPENAME"), 
                                 INNER_DELIMETER);
        List propertyList = readNested((String) kvpPairs.get("PROPERTYNAME"));
	String fidKvps = (String) kvpPairs.get("FEATUREID");
        List filterList = readFilters(fidKvps, 
                                      (String) kvpPairs.get("FILTER"),
                                      (String) kvpPairs.get("BBOX"));
       
        int propertySize = propertyList.size();
        int filterSize = filterList.size();

	if (typeList.size() == 0) {
	    typeList = getTypesFromFids(fidKvps);
	    if (typeList.size() == 0) {
		throw new WfsException("The typename element is mandatory if "
				       + "no FEATUREID is present");
	    }
	}
	
	int featureSize = typeList.size();
        
        // check for errors in the request
        if((propertySize != featureSize) && (propertySize > 1) || 
           ((filterSize != featureSize) && (filterSize > 1))) {
            throw new WfsException("Properties or filter sizes do not match"+  
                                   " feature types.  Property size: "+
                                   propertySize + " Filter size: "+ 
                                   filterSize + " Feature size: "+featureSize);
        }

        else {
            // loops through feature types, and creates queries based on them
            LOGGER.finest("setting query request parameters");
            for(int i = 0; i < featureSize; i++) {
                String featureType = (String) typeList.get(i);
                List properties;
                Filter filter;
                
                // permissive logic: lets one property list apply to all types
                LOGGER.finest("setting properties: " + i);
                if(propertySize == 0) {
                    properties = null;
                } else if(propertySize == 1){
                    properties = (List) propertyList.get(0);
                } else {
                    properties = (List) propertyList.get(i);
                }
                
                // permissive logic: lets one filter apply to all types
                LOGGER.finest("setting filters: " + i);
                if(filterSize == 0) {
                    filter = null;
                } else if(filterSize == 1){
                    filter = (Filter) filterList.get(0);
                } else {
                    filter = (Filter) filterList.get(i);
                }
                LOGGER.finest("query filter: " + filter);
                
                // add query
                currentRequest.addQuery(makeQuery(featureType, 
                                                  properties, filter));
            }       
            
            return currentRequest;
        }
    }

    /**
     * Returns a list of requested queries.
     * @return List of requested queries
     */
    private static Query makeQuery(String featureType, List propertyNames, 
                                   Filter filter)        
        throws WfsException {
        Query currentQuery = new Query();

        currentQuery.setTypeName(featureType);
        if(propertyNames != null) {
            for(int i=0; i < propertyNames.size(); i++) {
                currentQuery.addPropertyName((String) propertyNames.get(i));
            }
        }
        if(filter != null) {
            currentQuery.addFilter(filter);
        }
        return currentQuery;   
    }    
}
