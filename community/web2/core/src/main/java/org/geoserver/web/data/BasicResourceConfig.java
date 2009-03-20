/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.web.wicket.KeywordsEditor;

/**
 * A generic configuration panel for all basic ResourceInfo properties
 */
@SuppressWarnings("serial")
public class BasicResourceConfig extends ResourceConfigurationPanel {
	
	public BasicResourceConfig(String id, IModel model) {
		super(id, model);

		add(new TextField("title"));
		add(new TextArea("abstract"));
		add(new KeywordsEditor("keywords", new PropertyModel(model, "keywords")));
        add(new MetadataLinkEditor("metadataLinks", model));
		add(new TextField("SRS"));
		add(new TextField("nativeBoundingBox"));
        add(new Label("boundingBox"));
        add(new TextField("latLonBoundingBox"));
	}
}
