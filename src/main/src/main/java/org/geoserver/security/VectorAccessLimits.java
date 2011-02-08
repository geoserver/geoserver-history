/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security;

import java.util.ArrayList;
import java.util.List;

import org.geotools.data.Query;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.PropertyName;

/**
 * Describes the access limits on a vector layer
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class VectorAccessLimits extends DataAccessLimits {
    
    /**
     * The list of attributes the user is allowed to read (will be band names for raster data)
     */
    List<PropertyName> readAttributes;
    
    /**
     * The set of attributes the user is allowed to write on
     */
    List<PropertyName> writeAttributes;

    /**
     * Limits the features that can actually be written
     */
    Filter writeFilter;

    /**
     * Builds a new vector access limits
     * 
     * @param readAttributes
     *            The list of attributes that can be read
     * @param readFilter
     *            Only matching features will be returned to the user
     * @param writeAttributes
     *            The list of attributes that can be modified
     * @param writeFilter
     *            Only matching features will be allowed to be created/modified/deleted
     */
    public VectorAccessLimits(CatalogMode mode, List<PropertyName> readAttributes, Filter readFilter,
            List<PropertyName> writeAttributes, Filter writeFilter) {
        super(mode, readFilter);
        this.readAttributes = readAttributes;
        this.writeAttributes = writeAttributes;
        this.writeFilter = writeFilter;
    }

    /**
     * The list of attributes the user is allowed to read
     * 
     * @return
     */
    public List<PropertyName> getReadAttributes() {
        return readAttributes;
    }

    /**
     * The list of attributes the user is allowed to write
     * 
     * @return
     */
    public List<PropertyName> getWriteAttributes() {
        return writeAttributes;
    }

    /**
     * Identifies the features the user can write onto
     * 
     * @return
     */
    public Filter getWriteFilter() {
        return writeFilter;
    }
    
    /**
     * Returns a GeoTools query wrapping the read attributes and the read filter
     * @return
     */
    public Query getReadQuery() {
        return buildQuery(readAttributes, readFilter);
    }
    
    /**
     * Returns a GeoTools query wrapping the write attributes and the write filter
     * @return
     */
    public Query getWriteQuery() {
        return buildQuery(writeAttributes, writeFilter);
    }

    /**
     * Returns a GeoTools query build with the provided attributes and filters
     * @return
     */
    private Query buildQuery(List<PropertyName> attributes, Filter filter) {
        if(attributes == null && filter == null || filter == Filter.INCLUDE) {
            return Query.ALL;
        } else {
            Query q = new Query();
            q.setFilter(filter);
            // TODO: switch this to property names when possible
            q.setPropertyNames(flattenNames(attributes));
            return q;
        }
    }
    
    /**
     * Turns a list of {@link PropertyName} into a list of {@link String}
     * @param names
     * @return
     */
    List<String> flattenNames(List<PropertyName> names) {
        if(names == null) {
            return null;
        }
        
        List<String> result = new ArrayList<String>(names.size());
        for (PropertyName name : names) {
            result.add(name.getPropertyName());
        }
        
        return result;
    }

    @Override
    public String toString() {
        return "VectorAccessLimits [readAttributes=" + readAttributes + ", writeAttributes="
                + writeAttributes + ", writeFilter=" + writeFilter + ", readFilter=" + readFilter
                + ", mode=" + mode + "]";
    }

}