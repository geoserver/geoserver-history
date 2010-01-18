package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.CoverageStore;

@XmlRootElement
public class CoverageStoreJSONHolder {

	private CoverageStore coverageStore;

	public CoverageStoreJSONHolder() {

	}

	public CoverageStoreJSONHolder(final CoverageStore coverageStore) {
		this.coverageStore = coverageStore;
	}

	public final CoverageStore getCoverageStore() {
		return this.coverageStore;
	}

	public final void setCoverageStore(final CoverageStore coverageStore) {
		this.coverageStore = coverageStore;
	}
}
