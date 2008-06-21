package org.geoserver.wms.web.publish;

import org.geoserver.web.util.MapModel;
import org.geoserver.web.publish.LayerConfigurationPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.markup.html.form.CheckBox;

public class GeoSearchLayerConfigPanel extends LayerConfigurationPanel{
    public GeoSearchLayerConfigPanel(String id, IModel model){
        super(id, model);

        add(new CheckBox("geosearch.enable", new MapModel(getLayerInfo().getResource().getMetadata(), "indexingEnabled")));
    }
}
