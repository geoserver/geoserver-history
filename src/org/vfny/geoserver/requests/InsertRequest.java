/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

//import java.io.*;
//import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.geotools.filter.Filter;
import org.geotools.feature.Feature;
import org.vfny.geoserver.responses.WfsException;

/**
 * 
 * @version $VERSION$
 * @author Rob Hranac, TOPP
 */
public class InsertRequest 
    extends SubTransactionRequest {

    
    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.requests");

    public static final short operationType = INSERT;

    public List features;

    private boolean releaseAll;
    
    /** Empty constructor. */
    public InsertRequest() { features = new ArrayList();}

    
    /** Gets whether all locked features should be released after
     this transaction, or only those that were affected. */ 
    public boolean getReleaseAll() { return releaseAll; }

    /** Sets  whether all locked features should be released after
     this transaction, or only those that were affected. */ 
    public void setReleaseAll(boolean releaseAll) { 
        this.releaseAll = releaseAll; }

    public void addFeature(Feature feature) {
	features.add(feature);
    }

    public void setFilter(Filter filter)
        throws WfsException {
        throw new WfsException("Attempted to add filter (" + filter + 
                               ") to update request: " + handle);
    }

    public short getOpType() { return operationType; }
   
    public String toString() {
	StringBuffer iRequest = new StringBuffer("Handle: " + handle);
	iRequest.append("\nReleaseAll: " + releaseAll + "\n");
	for (int i = 0; i < features.size(); i++) {
	    iRequest.append(features.get(i).toString() + "\n");
	}
	return iRequest.toString();
    } 

     public boolean equals(Object obj) {
	if (obj != null && obj.getClass() == this.getClass()){
	    InsertRequest testInsert = (InsertRequest)obj;
	    boolean isEqual = true;
	    if(this.handle != null) {
		isEqual = this.handle.equals(testInsert.handle);
	    } else {
		isEqual = (testInsert == null);
	    }
	    LOGGER.finest("handles are equal: " + isEqual);
	    isEqual = (this.releaseAll == testInsert.releaseAll) && isEqual;
	    LOGGER.finest("releaseAll equal: " + isEqual);
	    
	    if(this.features.size() == testInsert.features.size()){
		for(int i = 0; i < testInsert.features.size(); i++) {
		    isEqual = isEqual && this.features.contains
			(testInsert.features.get(i));
		}
	    } else {
		isEqual = false;
	    }
	    LOGGER.finest("features are equal " + isEqual);
	    return isEqual;
	} else {
	    return false;
	}
    }
    
}
