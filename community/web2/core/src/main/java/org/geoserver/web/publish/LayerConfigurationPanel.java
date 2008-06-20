package org.geoserver.web.publish;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.geoserver.catalog.LayerInfo;

public class LayerConfigurationPanel extends Panel {
	private static final long serialVersionUID = 4881474189619124359L;

	public LayerConfigurationPanel(String id, IModel model){
		super(id, model);
	}
	
	public LayerInfo getLayerInfo(){
		return (LayerInfo)getModelObject();
	}
	
}
