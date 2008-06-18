package org.geoserver.web.data;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;

public class BasicResourceConfig extends ResourceConfigurationPanel {

	public BasicResourceConfig(String id) {
		super(id);
		add(new Label("section", "Basic"));
		add(new TextField("title"));
		add(new TextField("abstract"));
//		add(new TextField("keywords"));
//		add(new TextField("SRS"));
//		add(new TextField("nativeBoundingBox"));
//		add(new TextField("boundingBox"));
	}

	private static final long serialVersionUID = 677955476932894110L;

}
