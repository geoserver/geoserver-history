/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.security;

/**
 * Base class for all AccessLimits declared by a {@link ResourceAccessManager}
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class AccessLimits {

    /**
     * Gets the catalog mode for this layer
     */
    CatalogMode mode;

    /**
     * Builds a generic AccessLimits
     */
    public AccessLimits(CatalogMode mode) {
        this.mode = mode;
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