package org.geoserver.rest.client.datatypes;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DefaultStyles {

	@XmlElement(name="defaultStyle", type=DefaultStyle.class)
	protected final List<DefaultStyle> defaultStyle;

	protected String href;

	public DefaultStyles() {
		this.defaultStyle = new LinkedList<DefaultStyle>();
	}

	/**
	 * @return the defaultStyle
	 */
	public List<DefaultStyle> getDefaultStyle() {
		return this.defaultStyle;
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
