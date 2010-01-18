package org.geoserver.rest.client.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Layer {

	protected DefaultStyle defaultStyle;

	protected Boolean enabled;

	protected String href;

	protected String name;

	protected Resource resource;

	protected Styles styles;

	protected String type;

	public Layer() {
	}

	/**
	 * @return the defaultStyle
	 */
	public DefaultStyle getDefaultStyle() {
		return this.defaultStyle;
	}

	/**
	 * @return the path
	 */
	public String getHref() {
		return this.href;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the resource
	 */
	public Resource getResource() {
		return this.resource;
	}

	/**
	 * @return the styles
	 */
	public Styles getStyles() {
		return this.styles;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @return the enabled
	 */
	public Boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * @param defaultStyle the defaultStyle to set
	 */
	public void setDefaultStyle(final DefaultStyle defaultStyle) {
		this.defaultStyle = defaultStyle;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(final Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param path the path to set
	 */
	public void setHref(final String path) {
		this.href = path;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(final Resource resource) {
		this.resource = resource;
	}

	/**
	 * @param styles the styles to set
	 */
	public void setStyles(final Styles styles) {
		this.styles = styles;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
