package org.geoserver.wms.web.publish;

import org.geoserver.web.publish.LayerConfigurationPanel;
import org.geoserver.web.util.MapModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.markup.html.form.TextField;

public class KMLLayerConfigPanel extends LayerConfigurationPanel {
    public KMLLayerConfigPanel(String id, IModel model){
        super(id, model);

        add(new TextField("kml.regionateAttribute", 
                    new MapModel(getLayerInfo().getResource().getMetadata(), "kml.regionateAttribute"))
           );
        add(new TextField("kml.regionateStrategy", 
                    new MapModel(getLayerInfo().getResource().getMetadata(), "kml.regionateStrategy"))
           );
        add(new TextField("kml.regionateFeatureLimit",
                    new MapModel(getLayerInfo().getResource().getMetadata(), "kml.regionateFeatureLimit"))
           );
    }
}
