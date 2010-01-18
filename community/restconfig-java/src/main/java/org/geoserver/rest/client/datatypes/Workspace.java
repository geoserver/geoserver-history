package org.geoserver.rest.client.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Workspace {

	protected String coverageStores;

	protected String dataStores;

	protected String href;

	protected String name;

	public Workspace() {

	}

	public String getCoverageStores() {
		return this.coverageStores;
	}

	public String getDataStores() {
		return this.dataStores;
	}

	/**
	 * @return the link
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

	public void setCoverageStores(final String coverageStores) {
		this.coverageStores = coverageStores;
	}

	public void setDataStores(final String dataStores) {
		this.dataStores = dataStores;
	}

	/**
	 * @param link the link to set
	 */
	public void setHref(final String link) {
		this.href = link;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {



		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
