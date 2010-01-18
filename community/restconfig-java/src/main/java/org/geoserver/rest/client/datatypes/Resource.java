package org.geoserver.rest.client.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Resource {

	protected String _class;

	protected String href;

	protected String name;

	public Resource() {
	}

	public String get_class() {
		return this._class;
	}

	/**
	 * @return the href
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

	public void set_class(final String _class) {
		this._class = _class;
	}

	/**
	 * @param href the href to set
	 */
	public void setHref(final String href) {
		this.href = href;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}
}
