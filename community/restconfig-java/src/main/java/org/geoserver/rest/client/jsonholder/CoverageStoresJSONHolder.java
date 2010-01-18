package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.CoverageStores;

@XmlRootElement
public class CoverageStoresJSONHolder {

	private CoverageStores coverageStores;

	public CoverageStoresJSONHolder() {
		this.coverageStores = new CoverageStores();
	}

	public CoverageStoresJSONHolder(final CoverageStores coverageStores) {
		this.coverageStores = coverageStores;
	}

	public final CoverageStores getCoverageStores() {
		return this.coverageStores;
	}

	public final void setCoverageStores(final CoverageStores coverageStores) {
		this.coverageStores = coverageStores;
	}
}
