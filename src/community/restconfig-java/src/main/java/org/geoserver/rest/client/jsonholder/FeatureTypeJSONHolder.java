package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.FeatureType;

@XmlRootElement
public class FeatureTypeJSONHolder {

	private FeatureType featureType;

	public FeatureTypeJSONHolder() {

	}

	public FeatureTypeJSONHolder(final FeatureType featureType) {
		this.featureType = featureType;
	}

	/**
	 * @return the featureType
	 */
	public FeatureType getFeatureType() {
		return this.featureType;
	}

	/**
	 * @param featureType the featureType to set
	 */
	public void setFeatureType(final FeatureType featureType) {
		this.featureType = featureType;
	}
}
