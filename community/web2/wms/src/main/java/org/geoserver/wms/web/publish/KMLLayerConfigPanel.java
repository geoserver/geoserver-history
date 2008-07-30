/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.web.publish;

import org.geoserver.web.publish.LayerConfigurationPanel;
import org.geoserver.web.util.MapModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.markup.html.form.TextField;

public class KMLLayerConfigPanel extends LayerConfigurationPanel {
    public KMLLayerConfigPanel(String id, IModel model){
        super(id, model);

        add(new TextField("kml.regionateAttribute", 
                    new MapModel(new PropertyModel(model, "resource.metadata"), "kml.regionateAttribute"))
           );
        add(new TextField("kml.regionateStrategy", 
                    new MapModel(new PropertyModel(model, "resource.metadata"), "kml.regionateStrategy"))
           );
        add(new TextField("kml.regionateFeatureLimit",
                    new MapModel(new PropertyModel(model, "resource.metadata"), "kml.regionateFeatureLimit"))
           );
    }
}
