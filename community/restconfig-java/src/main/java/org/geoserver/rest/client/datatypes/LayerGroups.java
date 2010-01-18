package org.geoserver.rest.client.datatypes;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LayerGroups {

	@XmlElement(name="layerGroup", type=LayerGroup.class)
	private final List<LayerGroup> layerGroup;

	public LayerGroups() {
		this.layerGroup = new LinkedList<LayerGroup>();
	}

	/**
	 * @return the layerGroup
	 */
	public List<LayerGroup> getLayerGroup() {
		return this.layerGroup;
	}
}
