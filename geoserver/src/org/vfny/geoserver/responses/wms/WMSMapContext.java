/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vfny.geoserver.responses.wms;

import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapLayer;
import java.awt.Color;


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
    /** DOCUMENT ME!  */
    private int mapWidth;

    /** DOCUMENT ME!  */
    private int mapHeight;

    /** DOCUMENT ME!  */
    private Color bgColor;

    /** DOCUMENT ME!  */
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
