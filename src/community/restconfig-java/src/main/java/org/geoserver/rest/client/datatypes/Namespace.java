package org.geoserver.rest.client.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Namespace {

	private String href;

	private String string;

	private String namespace;

	public Namespace() {

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
	public String getString() {
		return this.string;
	}

	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return this.namespace;
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
	public void setString(final String name) {
		this.string = name;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(final String namespace) {
		this.namespace = namespace;
	}
}
