/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geoserver.data.feature.FeatureTypeInfo;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureResults;


/**
 * Parameter object wich represents the results of a GetFeature or
 * GetFeatureWithLock WFS request.
 * <p>
 * Holds the request, feature lock, feature types info and feature results
 * produced when a succesfull execution of a GetFeature/GetFeatureWithLock ends
 * </p>
 *
 * @author Gabriel Rold?n
 * @version $Id$
 */
public class GetFeatureResults {
   
	/**
	 * Flag indicating wether locking is occuring
	 */
	private boolean isLocking;
	/**
	 * id of feature lock
	 */
	private String lockId;
    /**
     * List of FeatureResults obtained from excecuting the queries from the
     * GetFeature request
     */
    private List features;

    /** List of FeatureTypeInfo objects for the queries featuretypes */
    private List typeInfo;

    /** the feature producer being used to encode features */
    private FeatureProducer producer;
    
    /** the handle of the request */
    private String handle;
    
    /** the queries forming the request */
    private List queries;
    
    /**
     * Creates a new GetFeatureResults object.
     */
    public GetFeatureResults() {
        features = new ArrayList(2);
        typeInfo = new ArrayList(2);
        queries = new ArrayList();
    }

    public void setLocking(boolean isLocking) {
		this.isLocking = isLocking;
	}
    
    public boolean isLocking() {
		return isLocking;
	}
    
    public void setLockId(String lockId) {
		this.lockId = lockId;
	}
    
    public String getLockId() {
		return lockId;
	}
    
    /**
     * @return handle or application specific name given to the request.
     * 
     */
    public String getHandle() {
		return handle;
	}
    
    public void setHandle(String handle) {
		this.handle = handle;
	} 

    public void addQuery( Query query ) {
    		queries.add( query );
    }
    
    public void setQueries( List queries ) {
    		this.queries = queries;
    }
    
    public List getQueries() {
    		return queries;
    }
    
    
    /**
     * @return the number of resultsets this object holds.
     */
    public int getResultsetsCount() {
        return features.size();
    }

    /**
     * returns the results of the queried featuretype at index
     * <code>index</code>
     *
     * @param index the position in the list of FeatureResults to return
     *
     * @return the feature results object of the queried featuretype at index
     * <code>index</code>
     *
     * @throws ArrayIndexOutOfBoundsException if index is not in the range from
     * 0(zero) to FeatureResults' count -1
     */
    public FeatureResults getFeatures(int index)
        throws ArrayIndexOutOfBoundsException {
        return (FeatureResults) features.get(index);
    }

    /**
     * returns the type info object of the queried featuretype at index
     * <code>index</code>
     *
     * @param index the position in the list of typeinfos to return
     *
     * @return the type info object of the queried featuretype at index
     * <code>index</code>
     *
     * @throws ArrayIndexOutOfBoundsException if index is not in the range from
     * 0(zero) to FeatureTypeInfo's count -1
     */
    public FeatureTypeInfo getTypeInfo(int index)
        throws ArrayIndexOutOfBoundsException {
        return (FeatureTypeInfo) typeInfo.get(index);
    }

    /**
     * getter for the list of FeatureResults this object holds
     *
     * @return an unmodifiable <code>List</code> of the
     *         <code>FeatureResults</code> this object holds
     */
    public List getFeatures() {
        return Collections.unmodifiableList(features);
    }

    /**
     * getter for the list of FeatureResults this object holds
     *
     * @return an unmodifiable <code>List</code> of the
     *         <code>FeatureTypeInfo</code>'s this object holds
     */
    public List getTypesInfo() {
        return Collections.unmodifiableList(typeInfo);
    }

    /**
     * Adds the results and metadata of a queries FeatureType to the list of
     * GetFeature results this object holds
     *
     * @param meta the metadata object of the queried FeatureType
     * @param features the resulting features from the queries FeatureType
     *
     * @throws IOException if an error occurs comparing the schemas from
     *         <code>meta</code> and <code>features</code>
     * @throws NullPointerException if either of the arguments is
     *         <code>null</code>
     * @throws IllegalArgumentException if <code>meta</code> and
     *         <code>features</code> aren't from the same featuretype
     */
    public void addFeatures(FeatureTypeInfo meta, FeatureResults features)
        throws IOException {
        if ((meta == null) || (features == null)) {
            throw new NullPointerException(
                "Both the metadata and results of querying a featuretype must be passed");
        }

        if (!features.getSchema().getTypeName().equals(meta.featureType()
                                                               .getTypeName())) {
            throw new IllegalArgumentException(
                "The passed type info and results"
                + " do not seems to belong to the same type");
        }

        this.typeInfo.add(meta);
        this.features.add(features);
    }
}
