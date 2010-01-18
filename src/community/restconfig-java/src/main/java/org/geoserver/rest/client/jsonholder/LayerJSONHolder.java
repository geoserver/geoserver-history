package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.Layer;

@XmlRootElement
public class LayerJSONHolder {

	private Layer layer;

	public LayerJSONHolder() {
	}

	public LayerJSONHolder(final Layer layer) {
		this.layer = layer;
	}

	public final Layer getLayer() {
		return this.layer;
	}

	public final void setLayer(final Layer layer) {
		this.layer = layer;
	}


}
