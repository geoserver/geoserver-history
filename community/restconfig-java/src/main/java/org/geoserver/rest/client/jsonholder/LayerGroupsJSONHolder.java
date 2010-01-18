package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.LayerGroups;

@XmlRootElement
public class LayerGroupsJSONHolder {

	private LayerGroups layerGroups;

	public LayerGroupsJSONHolder() {

	}

	public LayerGroupsJSONHolder(final LayerGroups layerGroups) {
		this.layerGroups = layerGroups;
	}

	/**
	 * @return the layerGroups
	 */
	public LayerGroups getLayerGroups() {
		return this.layerGroups;
	}

	/**
	 * @param layerGroups the layerGroups to set
	 */
	public void setLayerGroups(final LayerGroups layerGroups) {
		this.layerGroups = layerGroups;
	}
}
