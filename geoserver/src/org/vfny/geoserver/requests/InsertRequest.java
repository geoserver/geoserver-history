/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.geotools.filter.Filter;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.vfny.geoserver.responses.WfsException;

/**
 * Represents a request for an insert operation.  Does some type checking
 * by making sure that all features added have the same schema names 
 * (which is also the type name).  
 * TODO: add increased typechecking, make sure schemas match one another.
 *
 * @version $VERSION$
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 */
public class InsertRequest 
    extends SubTransactionRequest {

    
    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.requests");

    /** Indicates that this is an insert request. */
    public static final short operationType = INSERT;

    /** The list of features to be inserted.*/
    public FeatureCollection features;

    /** flag to tell is all locked features should be released. */
    private boolean releaseAll;
    
    /** Specifies the table of features */
    protected String typeName = null;

    /** Empty constructor. */
    public InsertRequest() { 
	features = FeatureCollections.newCollection();
    }

    
    /** Gets whether all locked features should be released after
     this transaction, or only those that were affected. */ 
    public boolean getReleaseAll() { return releaseAll; }

    /** Sets  whether all locked features should be released after
     this transaction, or only those that were affected. */ 
    public void setReleaseAll(boolean releaseAll) { 
        this.releaseAll = releaseAll; }

    /** 
     * Adds a feature to this insert request.  Currently fairly permissive,
     * just checks that the typenames match.
     * The datasource will eventually complain, but it would be nice to
     * do some more type-checking, to make sure the schemas match.
     *
     * @param feature To be inserted into the database.
     * @throws WfsException if added typeName does not match the set typeNames.
     */
    public void addFeature(Feature feature) throws WfsException {
	if (typeName == null) {
	    features.add(feature);
	    typeName = feature.getFeatureType().getTypeName();
	} else {
	    String addTypeName = feature.getFeatureType().getTypeName();
	    if(typeName.equals(addTypeName)){
		features.add(feature);
	    } else {
		throw new WfsException("features do not match- added typeName: "
				       + addTypeName + ", set typeName: " +
				       typeName, handle);
	    }
	}

    }

    /** 
     * Convenience method to add an array of features.  See addFeature.
     *
     * @param features array of features to be inserted.
     * @throws WfsException if the typeNames don't match.
     */
    public void addFeatures(Feature[] features) throws WfsException {
	for (int i = 0; i < features.length; i++) {
	    addFeature(features[i]);
	}
    }
    
    /**
     * Returns the type name of the features held in this request.
     *
     * @return the typeName.
     */
    public String getTypeName(){
	return typeName;
    }

    /**
     * Sets the name.  This method should generally not be used, as
     * features that are added set their own name and throw exceptions
     * if they don't match the typename.  But this can be set before
     * adding features if you want to ensure that they all match this
     * name.
     * @param typeName the name of the schema of the added features.
    public void setTypeName(String typeName) {
	if (this.typeName == null || this.typeName.equals(typeName)) {
	    this.typeName = typeName;
	} else {
	    //throw exception?  do nothing?  We should not
	    //be setting a different type name if the typeName of
	    //the schemas is different.  To throw exception we must
	    //modify SubTransactionRequest.
	}
    }

    /**
     * Returns the features contained in this request.
     *
     * @return the array of features.
     */
    public FeatureCollection getFeatures(){
	return features;
    }

    /**
     * Filters can not be added to an insert request.
     *
     * @throws WfsException if called at all.
     */
    public void setFilter(Filter filter)
        throws WfsException {
        throw new WfsException("Attempted to add filter (" + filter + 
                               ") to update request: " + handle);
    }

    /** returns the insert short. */
    public short getOpType() { return operationType; }
   
    /**
     * gets the string representation of this request.
     */
    public String toString() {
	StringBuffer iRequest = new StringBuffer("Handle: " + handle);
	iRequest.append("\nReleaseAll: " + releaseAll);
	iRequest.append("\nTypeName: " + typeName + "\n");
	Iterator featIter = features.iterator();
	while (featIter.hasNext()){
	    iRequest.append(featIter.next().toString() + "\n");
	}
	//for (int i = 0; i < features.size(); i++) {
	//  iRequest.append(features.get(i).toString() + "\n");
	//}
	return iRequest.toString();
    } 

    /**
     * This method is currently broken, because geotools has not
     * yet implemented equals methods for features.
     */
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
		//TODO: iterator through each collection.  THis will be
		//better when datasources return FeatureDocument.
		//for(int i = 0; i < testInsert.features.size(); i++) {
		//  isEqual = isEqual && this.features.contains
		//(testInsert.features.get(i));
		//}
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
