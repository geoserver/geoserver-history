package org.geoserver.security.model.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geoserver.security.model.LayerSecurityModel;


/**
 * Configuration
 * 
 * @author Francesco Izzi (geoSDI)
 */

public class ConfigurationSingleton implements Serializable{

	// field
	
	private String namespace;
	private String layer;
	private String access;
	private String role;
	private List layerSecurityModelList = new ArrayList();
	
	private static ConfigurationSingleton singleton = new ConfigurationSingleton();
	
	private ConfigurationSingleton(){}
	
	public static ConfigurationSingleton getInstance() {
        return singleton;
    }

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public static void setSingleton(ConfigurationSingleton aSingleton) {
        singleton = aSingleton;
    }

	public List getLayerSecurityModelList() {
		return layerSecurityModelList;
	}

	public void setLayerSecurityModelList(
			List layerSecurityModelList) {
		this.layerSecurityModelList = layerSecurityModelList;
	}
}
