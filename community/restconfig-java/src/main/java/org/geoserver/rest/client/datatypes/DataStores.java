package org.geoserver.rest.client.datatypes;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataStores {

	@XmlElement(name="dataStore", type=DataStore.class)
	protected final List<DataStore> dataStore;

	protected String href;

	public DataStores() {
		this.dataStore = new LinkedList<DataStore>();
	}

	/**
	 * @return the defaultStyle
	 */
	public List<DataStore> getDataStore() {
		return this.dataStore;
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
