/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security;

import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.MultiPolygon;

/**
 * Describes access limits on a cascaded WMS layer
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class WMSAccessLimits extends DataAccessLimits {
    /**
     * ROI on the returned images
     */
    MultiPolygon rasterFilter;

    /**
     * Whether to allow feature info cascading or not
     */
    boolean allowFeatureInfo;

    /**
     * Builds a WMS limits
     * 
     * @param filter
     *            Used as a CQL filter on servers supporting it and on cascaded feature info
     *            requests, and also to slice away feature info results
     * @param rasterFilter
     *            Used as a ROI on the returned data
     */
    public WMSAccessLimits(CatalogMode mode, Filter filter, MultiPolygon rasterFilter,
            boolean allowFeatureInfo) {
        super(mode, filter);
        this.rasterFilter = rasterFilter;
        this.allowFeatureInfo = allowFeatureInfo;
    }

    /**
     * Acts as a ROI on the returned images
     * 
     * @return
     */
    public MultiPolygon getRasterFilter() {
        return rasterFilter;
    }

    /**
     * Wheter to allow GetFeatureInfo cascading or not
     * 
     * @return
     */
    public boolean isAllowFeatureInfo() {
        return allowFeatureInfo;
    }

    @Override
    public String toString() {
        return "WMSAccessLimits [allowFeatureInfo=" + allowFeatureInfo + ", rasterFilter="
                + rasterFilter + ", readFilter=" + readFilter + ", mode=" + mode + "]";
    }
    
}