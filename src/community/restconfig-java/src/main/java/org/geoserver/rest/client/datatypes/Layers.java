package org.geoserver.rest.client.datatypes;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Layers {

	@XmlElement(name="layer", type=Layer.class)
	protected final List<Layer> layer;

	public Layers() {
		this.layer = new LinkedList<Layer>();
	}

	/**
	 * @return the layer
	 */
	public List<Layer> getLayer() {
		return this.layer;
	}
}
