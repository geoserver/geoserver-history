package org.geoserver.security.model.configuration;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.security.model.LayerSecurityModel;

public class ConfiguratorLayers implements IConfigurator {

	private List<LayerSecurityModel> layerSecurityModelList = new ArrayList<LayerSecurityModel>();
	
	public void configure(ConfigurationSingleton configuration) {
		configuration.setLayerSecurityModelList(layerSecurityModelList);
	}
	
	public void addLayerSecurityModel(LayerSecurityModel layer){
		this.layerSecurityModelList.add(layer);
	}
	
	public List<LayerSecurityModel> getLayerSecurityModelList() {
		return layerSecurityModelList;
	}

	public void setLayerSecurityModelList(
			List<LayerSecurityModel> layerSecurityModelList) {
		this.layerSecurityModelList = layerSecurityModelList;
	}

}
