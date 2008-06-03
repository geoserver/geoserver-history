/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security;

import org.acegisecurity.Authentication;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.WorkspaceInfo;

/**
 * Data access manager provides the {@link SecureCatalogImpl} with directives on
 * what the specified user can access.
 * TODO: decide if this is going to be an abstract class or an interface (i.e. how likely
 * is it that we'll need to add a new kind of object to be put under security control?)
 * @author Andrea Aime - TOPP
 *
 */
public abstract class DataAccessManager {

    /**
     * Returns true if user can access the workspace in the specified mode
     */
    public boolean canAccess(Authentication user, WorkspaceInfo workspace, AccessMode mode) {
        return true;
    }
    
    /**
     * Returns true if user can access the layer in the specified mode
     */
    public boolean canAccess(Authentication user, LayerInfo layer, AccessMode mode) {
        return true;
    }
    
    /**
     * Returns true if user can access the layer group in the specified mode
     */
    public boolean canAccess(Authentication user, LayerGroupInfo group, AccessMode mode) {
        return true;
    }
    
    /**
     * Returns true if user can access the resource in the specified mode
     */
    public boolean canAccess(Authentication user, ResourceInfo resource, AccessMode mode) {
        return true;
    }
}
