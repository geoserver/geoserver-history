/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.util.*;
import java.util.logging.Logger;
import org.geotools.filter.Filter;
import org.geotools.feature.SchemaException;
import org.geotools.data.DefaultQuery;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.vfny.geoserver.responses.WfsException;

/**
 * Provides an internal, generic representation of a query component to a 
 * Feature request.  Note that Feature requests can contain multiple 
 * query components and that the 'version' inside the query component is 
 * different than the 'version' of the GetFeature request.
 *
 * @author Rob Hranac, TOPP
 * @version $version$
 */
public class Query {

    //back this by geotools query?  Have a get datasource query?

    /** Standard logging instance for the class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");
    
    /** The user-specified name for the query. */
    protected String handle = new String();
    
    // UNIMPLEMENTED - YOU CAN SET THIS BUT IT DOES NOTHING
    // NOTE THAT THIS IS FOR 'EVOLVING FEATURES' OR WHATEVER
    /** The version of the feature to request - current implementation 
        ignores entirely. */
    protected String version = new String();
    
    /** The feature type name requested. */
    protected String typeName = new String();
    
    /** The property names requested */
    protected List propertyNames = new ArrayList();
    
    /** The filter for the query */
    protected Filter filter = null;
    
     /** Flags whether or not all properties were requested */
    protected boolean allRequested = true;

    /** Empty constructor. */ 
    public Query() {}
    
    
    /** Gets the requested property names as a vector. */ 
    public List getPropertyNames() { return propertyNames; }
    
    /** Gets the feature type name for this query. */ 
    public void setTypeName(String typeName) { this.typeName = typeName; }
    
    /** Gets the feature type name for this query. */ 
    public String getTypeName() { return this.typeName; }
    
    /** Adds a requested property name to the query. */ 
    public void addPropertyName(String propertyName) { 
	 String[] splitName = propertyName.split("[.:/]");
	 String newPropName = propertyName;
	 if (splitName.length == 1) {
	     newPropName = splitName[0];
	 } else {
	     //REVISIT: move this code to geotools?
	     //REVISIT: not sure what to do if there are multiple
	     //delimiters.  
	     //REVISIT: should we examine the first value?  See
	     //if the namespace or typename matches up right?
	     //this is currently very permissive, just grabs
	     //the value of the end.
	     newPropName = splitName[splitName.length - 1];
	 }
	if (!propertyNames.contains(newPropName)) {
	    propertyNames.add(newPropName);
	}
	 this.allRequested = false;
    }

    /** Return boolean for all requested types. */
    public boolean allRequested() { return this.allRequested; }  

    /** Sets the user-defined 'handle' for the query. */ 
    public void setHandle (String handle) { this.handle = handle; }
    
    /** Gets the user-defined 'handle' for the query. */ 
    public String getHandle() { return this.handle; }
    
    
    /** Sets the 'version' of features to retrieve. */ 
    public void setVersion (String version) { this.version = version; }
    
    /** Gets the 'version' of features to retrieve. */ 
    public String getVersion() { return this.version; }
        
    /** Sets the filter for the query. */ 
    public void addFilter (Filter filter) { this.filter = filter; }    
    
    /** Passes the Post method to the Get method, with no modifications. */ 
    public Filter getFilter() { return this.filter; }
    
    
    /** Passes the Post method to the Get method, with no modifications. */ 
    public int getDatastoreType() { return 1; }
    
    public org.geotools.data.Query getDataSourceQuery(FeatureType schema, 
						      int maxFeatures) 
	throws WfsException{
	String[] props = null;
	List strippedNames = new ArrayList();
	if (propertyNames != null && propertyNames.size() > 0) {
	    //test this, is this done in geotools?
	    for(int i = 0; i < propertyNames.size(); i++) {
		String curPropName = propertyNames.get(i).toString();
		String[] splitName = curPropName.split("[.:/]");
		String newPropName = curPropName;
		if (splitName.length == 1) {
		    newPropName = splitName[0];
		} else {
		    //REVISIT: move this code to geotools?
		    //REVISIT: not sure what to do if there are multiple
		    //delimiters.  
		    //REVISIT: should we examine the first value?  See
		    //if the namespace or typename matches up right?
		    //this is currently very permissive, just grabs
		    //the value of the end.
		    newPropName = splitName[splitName.length - 1];
		}
		strippedNames.add(newPropName);
		}
	    //try {
	    //props = DefaultQuery.getValidProperties(schema, propertyNames);
	    //} catch (SchemaException e) {
	    //throw new WfsException(e, "problem with properties", handle);
	    //}
	    props = (String [])strippedNames.toArray(new String[0]);
	}
	DefaultQuery query = new DefaultQuery(null, this.filter, maxFeatures,
					props, this.handle);
	
	return query;
    }

    /*************************************************************************
     * OVERRIDES OF toString AND equals METHODS.                             *
     *************************************************************************/
    public String toString() {
        StringBuffer returnString = new StringBuffer("\n  Query");
        returnString.append(" [" + handle + "]");
        returnString.append("\n   feature type: " + typeName);
        if(filter != null) {
            returnString.append("\n   filter: " + filter.toString());
        }
        returnString.append("\n   [properties: ");
         if( this.allRequested()) {
            return returnString + " ALL ]";
        } else {
        Iterator iterator = propertyNames.listIterator();
        while( iterator.hasNext()) {
            returnString.append( iterator.next().toString());
            if(iterator.hasNext()) {
                returnString.append(", ");
            }
        }
	returnString.append("]");
        return returnString.toString();
	}    
    }
    
    public boolean equals(Query query) {

        boolean isEqual = true;

        // check basic attributes for equality
        isEqual = query.getHandle().equals(handle) ?
            true && isEqual : false;
        LOGGER.finest("checked handle: " + isEqual);
        isEqual = query.getTypeName().equals(typeName) ?
            true && isEqual : false;
        LOGGER.finest("checked feature type: " + isEqual + 
                      "; internal: " + query.getTypeName() +
                      "; external: " + typeName);
        isEqual = query.getVersion().equals(version) ?
            true && isEqual : false;
        LOGGER.finest("checked version: " + isEqual);

        // check filter for equality, handling null case
        if( query.getFilter() != null) {
            LOGGER.finest("checking filter: " + filter);
            isEqual = query.getFilter().equals(filter) ?
                true && isEqual : false;
            LOGGER.finest("checked filter: " + isEqual);
        } else {
            isEqual = (filter == null) ?
                true && isEqual : false;
        }

        // check property names for equality, handling case where property
        //  lists are of different lengths
        LOGGER.finest("checking properties, internal: " + propertyNames.size()+
                      "; external: " + query.getPropertyNames().size());
        if( propertyNames.size() == query.getPropertyNames().size()) {
            Iterator i = propertyNames.listIterator();
            Iterator e = query.getPropertyNames().listIterator();
            while( i.hasNext()) {
                isEqual = i.next().equals(e.next()) ?
                    true && isEqual : false;
            }
        } else {
            isEqual = false;
        }
        return isEqual;
    }
    
}
