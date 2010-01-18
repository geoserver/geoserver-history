package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.DataStore;

@XmlRootElement
public class DataStoreJSONHolder {

	private DataStore dataStore;

	public DataStoreJSONHolder() {

	}

	public DataStoreJSONHolder(final DataStore dataStore) {
		this.dataStore = dataStore;
	}

	/**
	 * @return the dataStores
	 */
	public DataStore getDataStore() {
		return this.dataStore;
	}

	/**
	 * @param dataStore the dataStore to set
	 */
	public void setDataStore(final DataStore dataStore) {
		this.dataStore = dataStore;
	}
}
