package org.geoserver.rest.client.jsonholder;

import javax.xml.bind.annotation.XmlRootElement;

import org.geoserver.rest.client.datatypes.FeatureTypes;

@XmlRootElement
public class FeatureTypesJSONHolder {

	private FeatureTypes featureTypes;

	public FeatureTypesJSONHolder() {

	}

	public FeatureTypesJSONHolder(final FeatureTypes featureTypes) {
		this.featureTypes = featureTypes;
	}

	/**
	 * @return the featureTypes
	 */
	public FeatureTypes getFeatureTypes() {
		return this.featureTypes;
	}

	/**
	 * @param featureTypes the featureTypes to set
	 */
	public void setFeatureTypes(final FeatureTypes featureTypes) {
		this.featureTypes = featureTypes;
	}
}
