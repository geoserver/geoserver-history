package org.geoserver.web.data.table;

import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.web.GeoServerApplication;

public class LayerDetachableModel extends LoadableDetachableModel {
    String name;

    public LayerDetachableModel(LayerInfo layer) {
        super(layer);
        this.name = layer.getName();
    }

    @Override
    protected Object load() {
        return GeoServerApplication.get().getCatalog().getLayerByName(name);
    }
}
