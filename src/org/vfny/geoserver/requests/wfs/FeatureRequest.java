/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.wfs;

import org.vfny.geoserver.requests.Query;
import org.vfny.geoserver.requests.WFSRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;


/**
 * Implements the WFS GetFeature interface, which responds to requests for GML.
 * This servlet accepts a getFeatures request and returns GML2.0 structured
 * XML docs.  It is made up of the standard request params, plus one or  more
 * {@link Query} objects, plus a user-assigned handle.  There are also params
 * for feature versioning and alternate formats, but GeoServer does not yet
 * support those.
 *
 * @author Rob Hranac, TOPP
 * @version $Id: FeatureRequest.java,v 1.5 2004/01/21 00:26:07 dmzwiers Exp $
 */
public class FeatureRequest extends WFSRequest {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /**
     * The maximum returned if the user requests no limit of features at all,
     * but the other request parameters don't restrict to below 500.
     */

    //protected static final int SOFT_MAX_FEATURES = GeoServer.getInstance()
    //                                                           .getGlobalConfig()
    //                                                           .getMaxFeatures();

    /**
     * This is the maximum that is returned if the user specifically requests
     * more than the soft max.
     */

    //protected static final int HARD_MAX_FEATURES = SOFT_MAX_FEATURES + 1000;

    /** Creates a max features constraint for the entire request */
    protected int maxFeatures = Integer.MAX_VALUE; //SOFT_MAX_FEATURES;

    /** Specifies the output format */
    protected String outputFormat = "GML2";

    /** Specifices the user-defined name for the entire get feature request */
    protected String handle = null;

    /** Creates an object version type */
    protected String featureVersion = null;

    /** Creates a full list of queries */
    protected List queries = new ArrayList();

    /**
     * Empty constructor.
     */
    public FeatureRequest() {
        super();
        setRequest("GetFeature");
    }

    /**
     * Sets the entire set of queries for this GetFeature request.
     *
     * @param queries The Querys of this request.
     */
    public void setQueries(List queries) {
        this.queries = queries;
    }

    /**
     * Returns a specific query from this GetFeature request.
     *
     * @param query a Query to add to this request.
     */
    public void addQuery(Query query) {
        this.queries.add(query);
    }

    /**
     * Returns the entire set of queries for this GetFeature request.
     *
     * @return The List of Query objects for this request.
     */
    public List getQueries() {
        return this.queries;
    }

    /**
     * Returns the number of queries for this GetFeature request.
     *
     * @return The number of queries held by this request.
     */
    public int getQueryCount() {
        return this.queries.size();
    }

    /**
     * Returns a specific query from this GetFeature request.
     *
     * @param i The index of the query to retrieve.
     *
     * @return The query at position i.
     */
    public Query getQuery(int i) {
        return (Query) this.queries.get(i);
    }

    /**
     * Sets the output format for this GetFeature request.
     *
     * @param outputFormat only gml2 is currently supported.
     */
    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    /**
     * Gets the output format for this GetFeature request.
     *
     * @return "GML2"
     */
    public String getOutputFormat() {
        return this.outputFormat;
    }

    /**
     * Sets the user-defined name for this request.
     *
     * @param handle The string to be used in reporting errors.
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Returns the user-defined name for the entire GetFeature request.
     *
     * @return The string to use in error reporting with this request.
     */
    public String getHandle() {
        return this.handle;
    }

    /**
     * Returns the version for the entire GetFeature request. Not currently
     * used in GeoServer.
     *
     * @param version The version of the feature to retrieve.
     */
    public void setFeatureVersion(String version) {
        this.version = version;
    }

    /**
     * Returns the version for the entire GetFeature request. Not currently
     * used in GeoServer.
     *
     * @return The version of the feature to retrieve.
     */
    public String getFeatureVersion() {
        return this.version;
    }

    /**
     * Sets the maximum number of features this request should return.
     *
     * @param maxFeatures The maximum number of features to return.
     */
    public void setMaxFeatures(int maxFeatures) {
        if (maxFeatures > 0) {
            this.maxFeatures = maxFeatures;
        }

        //if (maxFeatures > HARD_MAX_FEATURES) {
        //  this.maxFeatures = HARD_MAX_FEATURES;
        //}
    }

    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     *
     * @param maxFeatures The maximum number of features to return.
     */
    public void setMaxFeatures(String maxFeatures) {
        if (maxFeatures != null) {
            Integer tempInt = new Integer(maxFeatures);
            setMaxFeatures(tempInt.intValue());
        }
    }

    /**
     * Returns the maximum number of features for this request.
     *
     * @return Maximum number of features this request will return.
     */
    public int getMaxFeatures() {
        return this.maxFeatures;
    }

    /**
     * Standard override of toString()
     *
     * @return a String representation of this request.
     */
    public String toString() {
        StringBuffer returnString = new StringBuffer("\nRequest");
        returnString.append(": " + handle);
        returnString.append("\n output format:" + outputFormat);
        returnString.append("\n max features:" + maxFeatures);
        returnString.append("\n version:" + version);
        returnString.append("\n queries: ");

        Iterator iterator = queries.listIterator();

        while (iterator.hasNext()) {
            returnString.append(iterator.next().toString() + " \n");
        }

        //returnString.append("\n inside: " + filter.toString());
        return returnString.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        super.equals(obj);

        if (!(obj instanceof FeatureRequest)) {
            return false;
        }

        FeatureRequest request = (FeatureRequest) obj;
        boolean isEqual = true;

        if ((this.version == null) && (request.getVersion() == null)) {
            isEqual = isEqual && true;
        } else if ((this.version == null) || (request.getVersion() == null)) {
            isEqual = false;
        } else if (request.getVersion().equals(version)) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }

        LOGGER.finest("checking version equality: " + isEqual);

        if ((this.handle == null) && (request.getHandle() == null)) {
            isEqual = isEqual && true;
        } else if ((this.handle == null) || (request.getHandle() == null)) {
            isEqual = false;
        } else if (request.getHandle().equals(handle)) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }

        LOGGER.finest("checking handle equality: " + isEqual);

        if ((this.outputFormat == null) && (request.getOutputFormat() == null)) {
            isEqual = isEqual && true;
        } else if ((this.outputFormat == null)
                || (request.getOutputFormat() == null)) {
            isEqual = false;
        } else if (request.getOutputFormat().equals(outputFormat)) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }

        LOGGER.finest("checking output format equality: " + isEqual);

        if (this.maxFeatures == request.getMaxFeatures()) {
            isEqual = isEqual && true;
        }

        LOGGER.finest("checking max features equality: " + isEqual);

        ListIterator internalIterator = queries.listIterator();
        ListIterator externalIterator = request.getQueries().listIterator();

        while (internalIterator.hasNext()) {
            if (!externalIterator.hasNext()) {
                isEqual = false;
                LOGGER.finest("query lists not same size");

                break;
            } else {
                if (((Query) internalIterator.next()).equals(
                            (Query) externalIterator.next())) {
                    isEqual = true && isEqual;
                    LOGGER.finest("query properties match: " + isEqual);
                } else {
                    isEqual = false;
                    LOGGER.finest("queries not equal");

                    break;
                }
            }
        }

        return isEqual;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        int result = super.hashCode();
        result = (23 * result) + ((queries == null) ? 0 : queries.hashCode());
        result = (23 * result) + ((handle == null) ? 0 : handle.hashCode());

        return result;
    }
}
