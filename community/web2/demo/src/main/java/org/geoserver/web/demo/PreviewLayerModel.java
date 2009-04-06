/**
 * 
 */
package org.geoserver.web.demo;

import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.web.GeoServerApplication;

class PreviewLayerModel extends LoadableDetachableModel {
    PreviewLayer layer;
    String id;
    boolean group;
    
    public PreviewLayerModel(PreviewLayer pl) {
        super(pl);
        id = pl.layerInfo != null ? pl.layerInfo.getId() : pl.groupInfo.getId();
        group = pl.groupInfo != null;
    }

    @Override
    protected Object load() {
        if(group) {
            return new PreviewLayer(GeoServerApplication.get().getCatalog().getLayerGroup(id));
        } else {
            return new PreviewLayer(GeoServerApplication.get().getCatalog().getLayer(id));
        }
    }
}