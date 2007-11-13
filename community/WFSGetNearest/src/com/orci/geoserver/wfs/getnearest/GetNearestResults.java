/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package com.orci.geoserver.wfs.getnearest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.FeatureResults;
import org.geotools.data.crs.ForceCoordinateSystemFeatureResults;
import org.geotools.feature.Feature;
import org.geotools.referencing.CRS;
import org.vfny.geoserver.global.FeatureTypeInfo;


/**
 * Parameter object wich represents the results of a GetFeature or
 * GetFeatureWithLock WFS request.<p>Holds the request, feature lock,
 * feature types info and feature results produced when a succesfull execution
 * of a GetFeature/GetFeatureWithLock ends</p>
 *
 * @author Gabriel Rold?n
 * @version $Id: GetFeatureResults.java,v 1.1 2004/03/10 23:39:06 groldan Exp $
 */
public class GetNearestResults {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.responses");

    /**
     * the GetFeature or GetFeatureWithLock request who's processing
     * has originated this results
     */
    private final GetNearestRequest request;

    /**
     * List of FeatureResults obtained from excecuting the queries from
     * the GetFeature request
     */
    private List features;

    /** List of FeatureTypeInfo objects for the queries featuretypes */
    private List typeInfo;

    /**
             * Creates a new GetFeatureResults object.
             *
             * @param request the GetFeature or GetFeatureWithLock request who's
             *        processing has originated the results this object will hold
             */
    public GetNearestResults(GetNearestRequest request) {
        this.request = request;
        features = new ArrayList(2);
        typeInfo = new ArrayList(2);
    }

    /**
     * returns the GetFeature or GetFeatureWithLock request who's
     * processing has originated this results
     *
     * @return the GetFeature or GetFeatureWithLock request who's processing
     *         has originated this results
     */
    public GetNearestRequest getRequest() {
        return request;
    }

    /**
     * returns the number of resultsets this object holds
     *
     * @return DOCUMENT ME!
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
     *         <code>index</code>
     *
     * @throws ArrayIndexOutOfBoundsException if index is not in the range from
     *         0(zero) to FeatureResults' count -1
     */
    public FeatureResults getFeatures(int index) throws ArrayIndexOutOfBoundsException {
        return (FeatureResults) features.get(index);
    }

    /**
     * returns the type info object of the queried featuretype at index
     * <code>index</code>
     *
     * @param index the position in the list of typeinfos to return
     *
     * @return the type info object of the queried featuretype at index
     *         <code>index</code>
     *
     * @throws ArrayIndexOutOfBoundsException if index is not in the range from
     *         0(zero) to FeatureTypeInfo's count -1
     */
    public FeatureTypeInfo getTypeInfo(int index) throws ArrayIndexOutOfBoundsException {
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
     * Adds the results and metadata of a queries FeatureType to the
     * list of GetFeature results this object holds
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

        if (!features.getSchema().getTypeName().equals(meta.getFeatureType().getTypeName())) {
            throw new IllegalArgumentException("The passed type info and results"
                + " do not seems to belong to the same type");
        }

        if ((meta.getNativeCRS() != null) && features.getSchema().getDefaultGeometry() != null
                && !CRS.equalsIgnoreMetadata(meta.getNativeCRS(), meta.getDeclaredCRS())) {
            try {
                features = new ForceCoordinateSystemFeatureResults(features, meta.getDeclaredCRS());
            } catch (Exception e) {
                LOGGER.severe("Could not map original CRS to external CRS, "
                    + "serving data in original CRS: " + e.getMessage());
                LOGGER.log(Level.FINE, "Detailed mapping error: ", e);
            }
        }

        this.typeInfo.add(meta);
        this.features.add(features);
    }
}
