package org.geoserver.rest.client.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Url {

	private String string;

	public Url() {

	}

	/**
	 * @return the url
	 */
	public String getString() {
		return this.string;
	}

	/**
	 * @param url the url to set
	 */
	public void setString(final String url) {
		this.string = url;
	}

}
