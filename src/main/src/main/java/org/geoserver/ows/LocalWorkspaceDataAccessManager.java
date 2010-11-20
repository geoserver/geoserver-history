/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows;

import org.springframework.security.Authentication;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.security.AccessMode;
import org.geoserver.security.DataAccessManagerWrapper;

/**
 * Restricts access to data based on the value of {@link LocalWorkspace} and {@link LocalLayer}. 
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class LocalWorkspaceDataAccessManager extends DataAccessManagerWrapper {

    @Override
    public boolean canAccess(Authentication user, LayerInfo layer, AccessMode mode) {
        if (super.canAccess(user, layer, mode)) {
            if (LocalLayer.get() != null && !LocalLayer.get().equals(layer)) {
                return false;
            }
            return this.canAccess(user, layer.getResource(), mode);
        }
        return false;
    }
    
    @Override
    public boolean canAccess(Authentication user, ResourceInfo resource, AccessMode mode) {
        if (super.canAccess(user, resource, mode)) {
            if (LocalLayer.get() != null) {
                for (LayerInfo l : resource.getCatalog().getLayers(resource) ) {
                    if (!l.equals(LocalLayer.get())) {
                        return false;
                    }
                }
            }
            return this.canAccess(user, resource.getStore().getWorkspace(), mode);
        }
        return false;
    }
    
    @Override
    public boolean canAccess(Authentication user, WorkspaceInfo workspace, AccessMode mode) {
        if (LocalWorkspace.get() != null && !LocalWorkspace.get().equals(workspace)) {
            return false;
        }
        return true;
    }
}
