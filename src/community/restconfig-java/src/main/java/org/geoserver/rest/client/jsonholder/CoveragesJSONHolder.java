package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.Coverages;

@XmlRootElement
public class CoveragesJSONHolder {

	private Coverages coverages;

	public CoveragesJSONHolder() {

	}

	public CoveragesJSONHolder(final Coverages coverages) {
		this.coverages = coverages;
	}

	public final Coverages getCoverages() {
		return this.coverages;
	}

	public final void setCoverages(final Coverages coverages) {
		this.coverages = coverages;
	}


}
