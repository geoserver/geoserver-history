/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.security;

import org.opengis.filter.Filter;

/**
 * Base class for all AccessLimits declared by a {@link ResourceAccessManager}
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class DataAccessLimits extends AccessLimits {
    /**
     * Used for vector reading, for raster if there is a read param taking an OGC filter, and in WMS
     * if the remote server supports CQL filters and on feature info requests. For workspaces it
     * will be just INCLUDE or EXCLUDE to allow or deny access to the workspace
     */
    Filter readFilter;

    /**
     * Builds a generic DataAccessLimits
     * 
     * @param readFilter
     *            This filter will be merged with the request read filters to limit the
     *            features/tiles that can be actually read
     */
    public DataAccessLimits(CatalogMode mode, Filter readFilter) {
        super(mode);
        this.readFilter = readFilter;
    }

    /**
     * This filter will be merged with the request read filters to limit the features/tiles that can
     * be actually read
     * 
     * @return
     */
    public Filter getReadFilter() {
        return readFilter;
    }

    /**
     * The catalog mode for this layer
     * 
     * @return
     */
    public CatalogMode getMode() {
        return mode;
    }

}