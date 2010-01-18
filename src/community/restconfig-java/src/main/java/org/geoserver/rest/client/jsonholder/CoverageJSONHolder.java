package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.Coverage;

@XmlRootElement
public class CoverageJSONHolder {

	private Coverage coverage;

	public CoverageJSONHolder() {
	}

	public CoverageJSONHolder(final Coverage coverage) {
		this.coverage = coverage;
	}

	public final Coverage getCoverage() {
		return this.coverage;
	}

	public final void setCoverage(final Coverage coverage) {
		this.coverage = coverage;
	}
}
