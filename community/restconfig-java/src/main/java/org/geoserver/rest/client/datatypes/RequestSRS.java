package org.geoserver.rest.client.datatypes;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RequestSRS {

	@XmlElement(name="string", type=String.class)
	private List<String> string;

	public RequestSRS() {
	}

	/**
	 * @return the string
	 */
	public List<String> getString() {
		return this.string;
	}

	/**
	 * @param string the string to set
	 */
	public void setString(final List<String> string) {
		this.string = string;
	}



}
