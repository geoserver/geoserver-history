package org.geoserver.web.data.workspace;

import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.web.GeoServerApplication;

public class NamespaceDetachableModel extends LoadableDetachableModel {

    String id;
    
    public NamespaceDetachableModel( NamespaceInfo ns ) {
        this.id = ns.getId();
    }
    
    @Override
    protected Object load() {
        return GeoServerApplication.get().getCatalog().getNamespace( id );
    }

}
