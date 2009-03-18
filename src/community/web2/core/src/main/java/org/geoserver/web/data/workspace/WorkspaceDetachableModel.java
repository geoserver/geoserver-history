package org.geoserver.web.data.workspace;

import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerApplication;

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
