package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.Layers;

@XmlRootElement
public class LayersJSONHolder {

	private Layers layers;

	public LayersJSONHolder() {
	}

	public LayersJSONHolder(final Layers layers) {
		this.layers = layers;
	}

	public final Layers getLayers() {
		return this.layers;
	}

	public final void setLayers(final Layers layers) {
		this.layers = layers;
	}
}
