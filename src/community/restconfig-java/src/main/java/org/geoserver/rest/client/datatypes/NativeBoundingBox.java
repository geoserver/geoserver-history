package org.geoserver.rest.client.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NativeBoundingBox {

	protected String crs;

	protected double maxx;

	protected double maxy;

	protected double minx;

	protected double miny;

	public NativeBoundingBox() {

	}

	/**
	 * @return the crs
	 */
	public String getCrs() {
		return this.crs;
	}

	/**
	 * @return the maxX
	 */
	public double getMaxx() {
		return this.maxx;
	}

	/**
	 * @return the maxY
	 */
	public double getMaxy() {
		return this.maxy;
	}

	/**
	 * @return the minX
	 */
	public double getMinx() {
		return this.minx;
	}

	/**
	 * @return the minY
	 */
	public double getMiny() {
		return this.miny;
	}

	/**
	 * @param crs the crs to set
	 */
	public void setCrs(final String crs) {
		this.crs = crs;
	}

	/**
	 * @param maxX the maxX to set
	 */
	public void setMaxx(final double maxX) {
		this.maxx = maxX;
	}

	/**
	 * @param maxY the maxY to set
	 */
	public void setMaxy(final double maxY) {
		this.maxy = maxY;
	}

	/**
	 * @param minX the minX to set
	 */
	public void setMinx(final double minX) {
		this.minx = minX;
	}

	/**
	 * @param minY the minY to set
	 */
	public void setMiny(final double minY) {
		this.miny = minY;
	}
}
