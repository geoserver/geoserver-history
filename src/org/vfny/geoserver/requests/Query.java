/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.requests;

import java.util.*;
import org.apache.log4j.Category;
import org.geotools.filter.Filter;
import org.vfny.geoserver.config.FeatureTypeBean;

/**
 * Provides an internal, generic representation of a query component to a GetFeature request.
 * Note that GetFeature requests can contain multiple query components and that the 'version'
 * inside the query component is different than the 'version' of the GetFeature request.
 *
 * @author Rob Hranac, TOPP
 * @version 0.9 beta, 11/01/01
 */
public class Query {

    /** Standard logging instance for the class */
    private Category _log = Category.getInstance(Query.class.getName());
    
    /** The user-specified name for the query. */
    protected String handle = new String();
    
    // UNIMPLEMENTED - YOU CAN SET THIS BUT IT DOES NOTHING
    // NOTE THAT THIS IS FOR 'EVOLVING FEATURES' OR WHATEVER
    /** The version of the feature to request - current implementation ignores entirely. */
    protected String version = new String();
    
    /** The feature type name requested. */
    protected String featureTypeName = new String();
    
    /** The property names requested */
    protected Vector propertyNames = new Vector();
    
    /** The filter for the query */
    protected Filter filter = null;
    
    /** Stores datbase configuration meta data for the query  */
    protected FeatureTypeBean featureType = null;
    
    /** Creates an associated of bounding box */
    protected BoundingBox boundingBox = new BoundingBox();
    
    
    /**
     * Empty constructor.
     *
     */ 
    public Query() {
    }
    
    
    /**
     * This method sets the configuration data for the query, based on the
     * feature type name.
     *
     */ 
    public void setDatastoreConfiguration() {
        this.featureType = new FeatureTypeBean(featureTypeName);
    }

    
    /**
     * Returns the configuration data for the query datastore.
     *
     */ 
    public FeatureTypeBean getDatastoreConfiguration() {
        return this.featureType;
    }
    
    
    /**
     * Gets the requested property names as a vector.
     *
     */ 
    public Vector getPropertyNames() {        
        return this.propertyNames;
    }
    
    
    /**
     * Sets the feature type name requested by the query.
     *
     * @param featureTypeName The feature type name of the query - can be only one per query.
     */ 
    public void setFeatureTypeName(String featureTypeName) {
        this.featureTypeName = featureTypeName;
    }
    
    
    /**
     * Gets the feature type name for this query.
     *
     */ 
    public String getFeatureTypeName() {
        return this.featureTypeName;
    }
    
    
    /**
     * Adds a requested property name to the query.
     *
     * @param propertyName The property name to add to the query.
     */ 
    public void addPropertyName(String propertyName) {
        this.propertyNames.add(propertyName);
    }
    
    
    /**
     * Sets the user-defined 'handle' for the query.
     *
     * @param handle The query handle, or client-specified name.
     */ 
    public void setHandle (String handle) {
        this.handle = handle;
    }
    

    /**
     * Gets the user-defined 'handle' for the query.
     *
     */ 
    public String getHandle() {
        return this.handle;
    }
    
    
    /**
     * Sets the 'version' of features to retrieve.
     *
     * @param version The specified feature version.
     */ 
    public void setVersion (String version) {
        this.version = version;
    }
    

    /**
     * Gets the 'version' of features to retrieve.
     *
     */ 
    public String getVersion() {
        return this.version;
    }
    
    
    /**
     * Sets the filter for the .
     *
     * @param filter The specified filter.
     */ 
    public void addFilter (org.geotools.filter.Filter filter) {
        if(this.filter == null) {
            this.filter = filter;
        }
        else {
            this.filter = this.filter.and(filter);
        }
    }
    
    
    /**
     * Passes the Post method to the Get method, with no modifications.
     *
     */ 
    public org.geotools.filter.Filter getFilter() {
        return this.filter;
    }
    
    
    /**
     * Passes the Post method to the Get method, with no modifications.
     *
     */ 
    public int getDatastoreType() {
        return 1;
    }
    
    /**
     * Returns the bounding box for this query.
     *
     */ 
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }
    
    
    /**
     * Sets the bounding box for this query.
     *
     * @param boundingBoxes Bounding box for this query.
     */ 
    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    
    /**
     * Returns the bounding box for this request.
     *
     */ 
    public FeatureTypeBean getMetadata () {
        return this.featureType;
    }
    

    /**
     * Returns a string representation for this query.
     *
     */ 
    public String toString() {
        StringBuffer returnString = new StringBuffer("\n  Query");
        returnString.append(" [" + handle + "]");
        returnString.append("\n   feature type:" + featureTypeName);
        returnString.append("\n   properties: ");
        
        Iterator iterator = propertyNames.listIterator();
        while( iterator.hasNext()) {
            returnString.append( iterator.next().toString() + "\n");
        }
        if(filter != null) {
            returnString.append("\n   inside: " + filter.toString());
        }

        return returnString.toString();
    }
    

    /**
     * Determines equality with another query.
     *
     */ 
    public boolean equals(Query query) {

        boolean isEqual = true;

        // check basic attributes for equality
        isEqual = query.getHandle().equals(handle) ?
            true && isEqual : false;
        isEqual = query.getFeatureTypeName().equals(featureTypeName) ?
            true && isEqual : false;
        isEqual = query.getVersion().equals(version) ?
            true && isEqual : false;

        // check filter for equality, handling null case
        if( query.getFilter() != null) {
            isEqual = query.getFilter().equals(filter) ?
                true && isEqual : false;
        }
        else {
            isEqual = (filter == null) ?
                true && isEqual : false;
        }

        // check property names for equality, handling case where property
        //  lists are of different lengths
        if( propertyNames.size() == query.getPropertyNames().size()) {
            Iterator i = propertyNames.listIterator();
            Iterator e = query.getPropertyNames().listIterator();
            while( i.hasNext()) {
                isEqual = i.next().equals(e.next()) ?
                    true && isEqual : false;
            }
        }
        else {
            isEqual = false;
        }

        return isEqual;
    }
    
}
