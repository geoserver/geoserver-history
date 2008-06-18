package org.geoserver.web.data;

import org.apache.wicket.markup.html.panel.Panel;
import org.geoserver.catalog.ResourceInfo;

public class ResourceConfigurationPanel extends Panel {
	private static final long serialVersionUID = 4881474189619124359L;

	private ResourceInfo myResourceInfo;
	
	public ResourceConfigurationPanel(String id, ResourceInfo info){
		super(id);
		myResourceInfo = info;
	}
}
