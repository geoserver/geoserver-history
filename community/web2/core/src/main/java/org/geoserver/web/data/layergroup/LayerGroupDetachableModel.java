package org.geoserver.web.data.layergroup;

import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.web.GeoServerApplication;

public class LayerGroupDetachableModel extends LoadableDetachableModel {

    String id;
    
    public LayerGroupDetachableModel(LayerGroupInfo layerGroup) {
        this.id = layerGroup.getId();
    }
    
    @Override
    protected Object load() {
        return GeoServerApplication.get().getCatalog().getLayerGroup( id );
    }

}
