package org.geoserver.web.data;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.geoserver.catalog.ResourceInfo;

public class ResourceConfigurationPanel extends Panel {
	private static final long serialVersionUID = 4881474189619124359L;

	public ResourceConfigurationPanel(String id, IModel model){
		super(id, model);
	}
	
	public ResourceInfo getResourceInfo(){
		return (ResourceInfo)getModelObject();
	}
	
}
