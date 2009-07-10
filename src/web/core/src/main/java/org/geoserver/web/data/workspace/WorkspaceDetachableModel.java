/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.workspace;

import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerApplication;

/**
 * Detachable model for a specific workspace 
 */
@SuppressWarnings("serial")
public class WorkspaceDetachableModel extends LoadableDetachableModel {

    String id;
    
    public WorkspaceDetachableModel( WorkspaceInfo workspace ) {
        this.id = workspace.getId();
    }
    
    @Override
    protected Object load() {
        return GeoServerApplication.get().getCatalog().getWorkspace( id );
    }

}
