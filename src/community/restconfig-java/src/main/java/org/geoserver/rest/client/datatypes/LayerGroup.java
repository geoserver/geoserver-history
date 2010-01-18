package org.geoserver.rest.client.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LayerGroup {

	protected NativeBoundingBox bounds;

	protected Layers layers;

	protected String name;

	protected String style;

	protected Styles styles;

	public LayerGroup() {

		this.layers = new Layers();
	}

	public NativeBoundingBox getBounds() {
		return this.bounds;
	}

	/**
	 * @return the layers
	 */
	public Layers getLayers() {
		return this.layers;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	public String getStyle() {
		return this.style;
	}

	public Styles getStyles() {
		return this.styles;
	}

	public void setBounds(final NativeBoundingBox bounds) {
		this.bounds = bounds;
	}

	/**
	 * @param layers the layers to set
	 */
	public void setLayers(final Layers layers) {
		this.layers = layers;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	public void setStyle(final String style) {
		this.style = style;
	}

	public void setStyles(final Styles styles) {
		this.styles = styles;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
