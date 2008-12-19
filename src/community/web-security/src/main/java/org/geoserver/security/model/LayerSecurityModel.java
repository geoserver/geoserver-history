package org.geoserver.security.model;

import java.io.Serializable;

public class LayerSecurityModel implements Serializable {

	private String namespace;
	private String layer;
	private String access;
	private String role;

	public LayerSecurityModel(String namespace, String layer, String access,
			String role) {
		this.namespace = namespace;
		this.layer = layer;
		this.access = access;
		this.role = role;
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

	@Override
	public String toString() {
		return getNamespace() + "." + getLayer() + "." + getAccess() + "="
				+ getRole();
	}

}
