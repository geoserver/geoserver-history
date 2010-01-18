package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.DataStores;

@XmlRootElement
public class DataStoresJSONHolder {

	private DataStores dataStores;

	public DataStoresJSONHolder() {

	}

	public DataStoresJSONHolder(final DataStores dataStores) {
		this.dataStores = dataStores;
	}

	/**
	 * @return the dataStores
	 */
	public DataStores getDataStores() {
		return this.dataStores;
	}

	/**
	 * @param dataStore the dataStore to set
	 */
	public void setDataStores(final DataStores dataStores) {
		this.dataStores = dataStores;
	}
}
