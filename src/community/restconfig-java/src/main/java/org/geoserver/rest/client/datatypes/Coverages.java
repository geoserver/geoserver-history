package org.geoserver.rest.client.datatypes;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Coverages {

	@XmlElement(name="coverage", type=Coverage.class)
	protected final List<Coverage> coverage;

	protected String href;

	public Coverages() {
		this.coverage = new LinkedList<Coverage>();
	}

	/**
	 * @return the coverage
	 */
	public List<Coverage> getCoverage() {
		return this.coverage;
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return this.href;
	}

	/**
	 * @param href the href to set
	 */
	public void setHref(final String href) {
		this.href = href;
	}
}
