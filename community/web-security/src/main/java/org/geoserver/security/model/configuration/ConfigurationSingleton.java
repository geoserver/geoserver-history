package org.geoserver.security.model.configuration;


/**
 * Configuration
 * 
 * @author Francesco Izzi (geoSDI)
 */

public class ConfigurationSingleton {

	// field
	
	private String namespace;
	private String layer;
	private String access;
	private String role;
	
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
}
