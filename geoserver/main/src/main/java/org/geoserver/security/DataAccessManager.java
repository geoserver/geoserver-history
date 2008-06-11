/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security;

import org.acegisecurity.Authentication;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.WorkspaceInfo;

/**
 * Data access manager provides the {@link SecureCatalogImpl} with directives on
 * what the specified user can access.
 * @author Andrea Aime - TOPP
 *
 */
public interface DataAccessManager {

    /**
     * Returns true if user can access the workspace in the specified mode
     */
    public boolean canAccess(Authentication user, WorkspaceInfo workspace, AccessMode mode);
    
    /**
     * Returns true if user can access the layer in the specified mode
     */
    public boolean canAccess(Authentication user, LayerInfo layer, AccessMode mode);
    
    /**
     * Returns true if user can access the resource in the specified mode
     */
    public boolean canAccess(Authentication user, ResourceInfo resource, AccessMode mode);
}
