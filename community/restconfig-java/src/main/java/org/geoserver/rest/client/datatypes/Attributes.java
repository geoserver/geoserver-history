package org.geoserver.rest.client.datatypes;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Attributes {

	@XmlElement(name="attribute", type=Attribute.class)
	private final List<Attribute> attribute;

	public Attributes() {
		this.attribute = new LinkedList<Attribute>();
	}

	/**
	 * @return the attribute
	 */
	public List<Attribute> getAttribute() {
		return this.attribute;
	}
}
