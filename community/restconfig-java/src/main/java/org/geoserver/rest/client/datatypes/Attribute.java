package org.geoserver.rest.client.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Attribute {

	private int maxOccurs;

	private int minOccurs;

	private String name;

	private boolean nillable;

	public Attribute() {

	}

	/**
	 * @return the maxOccurs
	 */
	public int getMaxOccurs() {
		return this.maxOccurs;
	}

	/**
	 * @return the minOccurs
	 */
	public int getMinOccurs() {
		return this.minOccurs;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the nillable
	 */
	public boolean isNillable() {
		return this.nillable;
	}

	/**
	 * @param maxOccurs the maxOccurs to set
	 */
	public void setMaxOccurs(final int maxOccurs) {
		this.maxOccurs = maxOccurs;
	}

	/**
	 * @param minOccurs the minOccurs to set
	 */
	public void setMinOccurs(final int minOccurs) {
		this.minOccurs = minOccurs;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param nillable the nillable to set
	 */
	public void setNillable(final boolean nillable) {
		this.nillable = nillable;
	}
}
