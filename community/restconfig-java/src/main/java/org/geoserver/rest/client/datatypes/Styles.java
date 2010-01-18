package org.geoserver.rest.client.datatypes;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Styles {

	protected String href;

	@XmlElement(name="style", type=Style.class)
	protected final List<Style> style;

	public Styles() {
		this.style = new LinkedList<Style>();
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return this.href;
	}

	/**
	 * @return the style
	 */
	public List<Style> getStyle() {
		return this.style;
	}

	/**
	 * @param href the href to set
	 */
	public void setHref(final String href) {
		this.href = href;
	}
}
