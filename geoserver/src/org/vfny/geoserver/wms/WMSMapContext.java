/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms;

import java.awt.Color;

import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapLayer;


/**
 * Extends DefaultMapContext to provide the whole set of request parameters a
 * WMS GetMap request can have.
 * 
 * <p>
 * In particular, adds holding for the following parameter values:
 * 
 * <ul>
 * <li>
 * WIDTH
 * </li>
 * <li>
 * HEIGHT
 * </li>
 * <li>
 * BGCOLOR
 * </li>
 * <li>
 * TRANSPARENT
 * </li>
 * </ul>
 * </p>
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class WMSMapContext extends DefaultMapContext {
    /** requested map image width in output units (pixels) */
    private int mapWidth;

    /** requested map image height in output units (pixels) */
    private int mapHeight;

    /** Requested BGCOLOR, defaults to white according to WMS spec */
    private Color bgColor = Color.white;

    /** true if background transparency is requested */
    private boolean transparent;

    /**
     *
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
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getBgColor() {
        return this.bgColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bgColor DOCUMENT ME!
     */
    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMapHeight() {
        return this.mapHeight;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapHeight DOCUMENT ME!
     */
    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMapWidth() {
        return this.mapWidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapWidth DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param transparent DOCUMENT ME!
     */
    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }
}
