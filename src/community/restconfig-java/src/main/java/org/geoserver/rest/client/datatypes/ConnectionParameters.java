package org.geoserver.rest.client.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConnectionParameters {

	private Namespace namespace;

	private Url url;

	public ConnectionParameters() {

	}

	public ConnectionParameters(final Namespace namespace, final Url url) {

		this.namespace = namespace;
		this.url = url;
	}

	/**
	 * @return the namespace
	 */
	public Namespace getNamespace() {
		return this.namespace;
	}

	/**
	 * @return the url
	 */
	public Url getUrl() {
		return this.url;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(final Namespace namespace) {
		this.namespace = namespace;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(final Url url) {
		this.url = url;
	}
}
