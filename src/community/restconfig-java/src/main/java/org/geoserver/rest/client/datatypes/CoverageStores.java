package org.geoserver.rest.client.datatypes;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CoverageStores {

	@XmlElement(name="coverageStore", type=CoverageStore.class)
	protected final List<CoverageStore> coverageStore;

	protected String href;

	public CoverageStores() {
		this.coverageStore = new LinkedList<CoverageStore>();
	}

	/**
	 * @return the coverageStore
	 */
	public List<CoverageStore> getCoverageStore() {
		return this.coverageStore;
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
