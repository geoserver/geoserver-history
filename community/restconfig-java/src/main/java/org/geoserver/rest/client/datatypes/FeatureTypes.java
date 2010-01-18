package org.geoserver.rest.client.datatypes;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FeatureTypes {

	@XmlElement(name="featureType", type=FeatureType.class)
	protected final List<FeatureType> featureType;

	protected String href;

	public FeatureTypes() {
		this.featureType = new LinkedList<FeatureType>();
	}

	/**
	 * @return the defaultStyle
	 */
	public List<FeatureType> getFeatureType() {
		return this.featureType;
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
