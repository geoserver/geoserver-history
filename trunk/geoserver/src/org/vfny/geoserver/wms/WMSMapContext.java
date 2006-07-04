/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms;

import org.geotools.map.GraphicEnhancedMapContext;
import org.geotools.map.MapLayer;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
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
 * @author Simone Giannecchini
 * @version $Id$
 */
public class WMSMapContext extends GraphicEnhancedMapContext {

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
	 * Default constructor
	 */
	public WMSMapContext(CoordinateReferenceSystem crs) {
		super(crs);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param layers
	 */
	public WMSMapContext(MapLayer[] layers, CoordinateReferenceSystem crs) {
		super(layers, crs);
	}

	/**
	 * @param req
	 */
	public WMSMapContext(GetMapRequest req, CoordinateReferenceSystem crs) {
		super(crs);
		this.request = req;
	}

	public GetMapRequest getRequest() {
		return request;
	}

	public void setRequest(GetMapRequest request) {
		this.request = request;
	}
}
