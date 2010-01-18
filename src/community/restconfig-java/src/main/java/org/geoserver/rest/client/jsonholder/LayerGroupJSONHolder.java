package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.LayerGroup;
import org.geoserver.rest.client.datatypes.NativeBoundingBox;

@XmlRootElement
public class LayerGroupJSONHolder {

	private NativeBoundingBox bounds;

	private LayerGroup layerGroup;

	public LayerGroupJSONHolder() {

	}

	public LayerGroupJSONHolder(final LayerGroup layerGroup) {
		this.layerGroup = layerGroup;
	}

	public NativeBoundingBox getBounds() {
		return this.bounds;
	}

	/**
	 * @return the layerGroup
	 */
	public LayerGroup getLayerGroup() {
		return this.layerGroup;
	}

	public void setBounds(final NativeBoundingBox bounds) {
		this.bounds = bounds;
	}

	/**
	 * @param layerGroup the layerGroup to set
	 */
	public void setLayerGroup(final LayerGroup layerGroup) {
		this.layerGroup = layerGroup;
	}
}
