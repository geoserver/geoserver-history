/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import org.geotools.data.DefaultQuery;
import org.geotools.filter.Filter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;


/**
 * Provides an internal, generic representation of a query component to a
 * Feature request.  Note that Feature requests can contain multiple  query
 * components and that the 'version' inside the query component is  different
 * than the 'version' of the GetFeature request.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: Query.java,v 1.14 2004/01/31 00:27:25 jive Exp $
 */
public class Query {
    //back this by geotools query?  Have a get datasource query?

    /** Standard logging instance for the class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** The user-specified name for the query. */
    protected String handle = new String();

    // UNIMPLEMENTED - YOU CAN SET THIS BUT IT DOES NOTHING
    // NOTE THAT THIS IS FOR 'EVOLVING FEATURES' OR WHATEVER

    /**
     * The version of the feature to request - current implementation  ignores
     * entirely.
     */
    protected String version = new String();

    /** The feature type name requested. */
    protected String typeName = new String();

    /** The property names requested */
    protected List propertyNames = new ArrayList();

    /** The filter for the query */
    protected Filter filter = null;

    /** Flags whether or not all properties were requested */
    protected boolean allRequested = true;

    /**
     * Empty constructor.
     */
    public Query() {
    }

    /**
     * Gets the requested property names as a list.
     *
     * @return A list of the names of the requested properties.
     */
    public List getPropertyNames() {
        return propertyNames;
    }

    /**
     * Sets the feature type name for this query.
     *
     * @param typeName The featureType to query.
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Gets the feature type name for this query.
     *
     * @return The featureType to query.
     */
    public String getTypeName() {
        return this.typeName;
    }

    private String getName() {
        if (typeName.indexOf(":") == -1) {
            return typeName;
        } else {
            //HACK: Request kvp reader does the same thing.
            //put in common utility, and do more efficiently (indexOf?)
            String[] splitName = typeName.split("[:]");
            String name = typeName;

            if (splitName.length == 1) {
                name = splitName[0];
            } else {
                name = splitName[splitName.length - 1];
            }

            return name;
        }
    }

    /**
     * Adds a requested property name to the query.
     *
     * @param propertyName The name of a property to return.
     *
     * @task REVISIT: This regexp stuff is very inefficient.  Should compile
     *       the pattern and use it.
     */
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

    /**
     * Return boolean for all requested types.
     *
     * @return <tt>true</tt> if all properties are requested.
     */
    public boolean allRequested() {
        return this.allRequested;
    }

    /**
     * Sets the user-defined 'handle' for the query.
     *
     * @param handle The mnemonic handle to associate with this query.
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Gets the user-defined 'handle' for the query.
     *
     * @return The mnemonic handle associatee with this query.
     */
    public String getHandle() {
        return this.handle;
    }

    /**
     * Sets the 'version' of features to retrieve.  Not currently used.
     *
     * @param version The feature version to retrieve.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the 'version' of features to retrieve.
     *
     * @return The feature version to retrieve.
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Sets the filter for the query.
     *
     * @param filter The ogc filter to narrow the results.
     */
    public void addFilter(Filter filter) {
        this.filter = filter;
    }

    /**
     * Gets the filter for this query.
     *
     * @return The ogc filter to narrow the results.
     */
    public Filter getFilter() {
        return this.filter;
    }

    /**
     * Gets this query as a geotools Query object.
     *
     * @param maxFeatures The max number of Features to return.
     *
     * @return A geotools query object representing this Query with  the passed
     *         in maxFeatures.
     */
    public org.geotools.data.Query getDataSourceQuery(int maxFeatures) {
        String[] props = null;

        if ((propertyNames != null) && (propertyNames.size() > 0)) {
            props = (String[]) propertyNames.toArray(new String[0]);
        }

        DefaultQuery query = new DefaultQuery(getName(), this.filter,
                maxFeatures, props, this.handle);

        return query;
    }

    /**
     * Get this query as a geotools Query.
     * 
     * <p>
     * if maxFeatures is a not positive value DefaultQuery.DEFAULT_MAX will be
     * used.
     * </p>
     * 
     * <p>
     * The method name is changed to toDataStoreQuery since this is a one way
     * conversion.
     * </p>
     *
     * @param maxFeatures number of features, or 0 for DefaultQuery.DEFAULT_MAX
     *
     * @return A Query for use with the FeatureSource interface
     */
    public org.geotools.data.Query toDataQuery(int maxFeatures) {
        if (maxFeatures <= 0) {
            maxFeatures = DefaultQuery.DEFAULT_MAX;
        }

        String[] props = null;

        if ((propertyNames != null) && (propertyNames.size() > 0)) {
            props = (String[]) propertyNames.toArray(new String[0]);
        }

        if (filter == null) {
            filter = Filter.NONE;
        }

        DefaultQuery query = new DefaultQuery(getName(), this.filter,
                maxFeatures, props, this.handle);

        return query;
    }

    /**
     * Override of toString.
     *
     * @return String representation of this query.
     */
    public String toString() {
        StringBuffer returnString = new StringBuffer("\n  Query");
        returnString.append(" [" + handle + "]");
        returnString.append("\n   feature type: " + typeName);

        if (filter != null) {
            returnString.append("\n   filter: " + filter.toString());
        }

        returnString.append("\n   [properties: ");

        if (this.allRequested()) {
            return returnString + " ALL ]";
        } else {
            Iterator iterator = propertyNames.listIterator();

            while (iterator.hasNext()) {
                returnString.append(iterator.next().toString());

                if (iterator.hasNext()) {
                    returnString.append(", ");
                }
            }

            returnString.append("]");

            return returnString.toString();
        }
    }

    /**
     * Override of equals.
     *
     * @param obj the object to compare against.
     *
     * @return <tt>true</tt> if obj is equal to this Query.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Query)) {
            return false;
        }

        Query query = (Query) obj;
        boolean isEqual = true;

        // check basic attributes for equality
        isEqual = query.getHandle().equals(handle) ? (true && isEqual) : false;
        LOGGER.finest("checked handle: " + isEqual);
        isEqual = query.getTypeName().equals(typeName) ? (true && isEqual) : false;
        LOGGER.finest("checked feature type: " + isEqual + "; internal: "
            + query.getTypeName() + "; external: " + typeName);
        isEqual = query.getVersion().equals(version) ? (true && isEqual) : false;
        LOGGER.finest("checked version: " + isEqual);

        // check filter for equality, handling null case
        if (query.getFilter() != null) {
            LOGGER.finest("checking filter: " + filter);
            isEqual = query.getFilter().equals(filter) ? (true && isEqual) : false;
            LOGGER.finest("checked filter: " + isEqual);
        } else {
            isEqual = (filter == null) ? (true && isEqual) : false;
        }

        // check property names for equality, handling case where property
        //  lists are of different lengths
        LOGGER.finest("checking properties, internal: " + propertyNames.size()
            + "; external: " + query.getPropertyNames().size());

        if (propertyNames.size() == query.getPropertyNames().size()) {
            Iterator i = propertyNames.listIterator();
            Iterator e = query.getPropertyNames().listIterator();

            while (i.hasNext()) {
                isEqual = i.next().equals(e.next()) ? (true && isEqual) : false;
            }
        } else {
            isEqual = false;
        }

        return isEqual;
    }

    /**
     * Override of hashCode.
     *
     * @return an int to hash this Query with.
     */
    public int hashCode() {
        int result = 17;
        result = (23 * result) + ((typeName == null) ? 0 : typeName.hashCode());
        result = (23 * result)
            + ((propertyNames == null) ? 0 : propertyNames.hashCode());

        //Filter in geotools doesn't have hashCode implemented.
        return result;
    }
}
