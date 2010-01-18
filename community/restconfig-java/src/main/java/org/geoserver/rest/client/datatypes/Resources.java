package org.geoserver.rest.client.datatypes;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Resources {

	protected String href;

	@XmlElement(name="resource", type=Resource.class)
	protected final List<Resource> resource;

	public Resources() {
		this.resource = new LinkedList<Resource>();
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return this.href;
	}

	/**
	 * @return the resource
	 */
	public List<Resource> getResource() {
		return this.resource;
	}

	/**
	 * @param href the href to set
	 */
	public void setHref(final String href) {
		this.href = href;
	}
}
