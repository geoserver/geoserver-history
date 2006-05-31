/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms;

import java.awt.Color;

import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapLayer;
import org.vfny.geoserver.wms.requests.GetMapRequest;

/**
 * Extends DefaultMapContext to provide the whole set of request parameters a
 * WMS GetMap request can have.
 * 
 * <p>
 * In particular, adds holding for the following parameter values:
 * 
 * <ul>
 * <li> WIDTH </li>
 * <li> HEIGHT </li>
 * <li> BGCOLOR </li>
 * <li> TRANSPARENT </li>
 * </ul>
 * </p>
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class WMSMapContext extends DefaultMapContext {

	/**
	 * requested map image width in output units (pixels)
	 * 
	 * @uml.property name="mapWidth" multiplicity="(0 1)"
	 */
	private int mapWidth;

	/**
	 * requested map image height in output units (pixels)
	 * 
	 * @uml.property name="mapHeight" multiplicity="(0 1)"
	 */
	private int mapHeight;

	/**
	 * Requested BGCOLOR, defaults to white according to WMS spec
	 * 
	 * @uml.property name="bgColor" multiplicity="(0 1)"
	 */
	private Color bgColor = Color.white;

	/**
	 * true if background transparency is requested
	 * 
	 * @uml.property name="transparent" multiplicity="(0 1)"
	 */
	private boolean transparent;

	private GetMapRequest request;

	/**
	 * Default constructor
	 */
	public WMSMapContext() {
		super();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param layers
	 */
	public WMSMapContext(MapLayer[] layers) {
		super(layers);
	}

	/**
	 * @param req
	 */
	public WMSMapContext(GetMapRequest req) {
		super();
		this.request = req;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @uml.property name="bgColor"
	 */
	public Color getBgColor() {
		return this.bgColor;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param bgColor
	 *            DOCUMENT ME!
	 * 
	 * @uml.property name="bgColor"
	 */
	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @uml.property name="mapHeight"
	 */
	public int getMapHeight() {
		return this.mapHeight;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param mapHeight
	 *            DOCUMENT ME!
	 * 
	 * @uml.property name="mapHeight"
	 */
	public void setMapHeight(int mapHeight) {
		this.mapHeight = mapHeight;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @uml.property name="mapWidth"
	 */
	public int getMapWidth() {
		return this.mapWidth;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param mapWidth
	 *            DOCUMENT ME!
	 * 
	 * @uml.property name="mapWidth"
	 */
	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isTransparent() {
		return this.transparent;
	}

	/**
	 * Setting transparency for this wms context.
	 * 
	 * @param transparent
	 *            DOCUMENT ME!
	 * 
	 * @uml.property name="transparent"
	 */
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	public GetMapRequest getRequest() {
		return request;
	}

	public void setRequest(GetMapRequest request) {
		this.request = request;
	}
}
