/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.publish;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.geoserver.web.util.MapModel;

public class HTTPLayerConfig extends LayerConfigurationPanel {
    public HTTPLayerConfig(String id, IModel model){
        super(id, model);
        add(new CheckBox("cachingEnabled", new MapModel(new PropertyModel(model, "resource.metadata"), "cachingEnabled")));
        add(new TextField("cacheAgeMax", new MapModel(new PropertyModel(model, "resource.metadata"), "cacheAgeMax")));
    }
}

