package org.geoserver.rest.client.datatypes;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Keywords {

	@XmlElement(name="string", type=String.class)
	private final List<String> string;

	public Keywords() {
		this.string = new LinkedList<String>();
	}

	/**
	 * @return the string
	 */
	public List<String> getString() {
		return this.string;
	}
}
