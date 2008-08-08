/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wfs.web.publish;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.web.publish.LayerConfigurationPanel;

@SuppressWarnings("serial")
public class WFSLayerConfig extends LayerConfigurationPanel {

    public WFSLayerConfig(String id, IModel model){
        super(id, model);

        add(new TextField("maxFeatures", new PropertyModel(model, "resource.maxFeatures")));
    }
}
