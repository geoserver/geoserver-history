/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import java.awt.Color;
import java.util.List;

import org.vfny.geoserver.global.FeatureTypeInfo;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Represents a WMS GetMap request. as a extension to the WMS spec 1.1.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id: GetMapRequest.java,v 1.8 2004/03/14 16:00:54 groldan Exp $
 */
public class GetMapRequest extends WMSRequest {
    /** DOCUMENT ME! */
    static final Color DEFAULT_BG = Color.white;

    /** DOCUMENT ME! */
    public static final String SE_XML = "SE_XML";

    /** set of mandatory request's parameters */
    private MandatoryParameters mandatoryParams = new MandatoryParameters();

    /** set of optionals request's parameters */
    private OptionalParameters optionalParams = new OptionalParameters();

    /**
     * Creates a new GetMapRequest object.
     */
    public GetMapRequest() {
        super();
        setRequest("GetMap");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Envelope getBbox() {
        return this.mandatoryParams.bbox;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.awt.Color getBgColor() {
        return this.optionalParams.bgColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCrs() {
        return this.optionalParams.crs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getExceptions() {
        return this.optionalParams.exceptions;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFormat() {
        return this.mandatoryParams.format;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight() {
        return this.mandatoryParams.height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FeatureTypeInfo[] getLayers() {
        return this.mandatoryParams.layers;
    }

    /**
     * Gets a list of the names of the styles to be returned by the server.
     *
     * @return A list of Strings of the names of the styles.
     */
    public List getStyles() {
        return this.mandatoryParams.styles;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isTransparent() {
        return this.optionalParams.transparent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return this.mandatoryParams.width;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bbox DOCUMENT ME!
     */
    public void setBbox(Envelope bbox) {
        this.mandatoryParams.bbox = bbox;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bgColor DOCUMENT ME!
     */
    public void setBgColor(java.awt.Color bgColor) {
        this.optionalParams.bgColor = bgColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param crs DOCUMENT ME!
     */
    public void setCrs(String crs) {
        this.optionalParams.crs = crs;
    }

    /**
     * DOCUMENT ME!
     *
     * @param exceptions DOCUMENT ME!
     */
    public void setExceptions(String exceptions) {
        this.optionalParams.exceptions = exceptions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param format DOCUMENT ME!
     */
    public void setFormat(String format) {
        this.mandatoryParams.format = format;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(int height) {
        this.mandatoryParams.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param layers DOCUMENT ME!
     */
    public void setLayers(FeatureTypeInfo[] layers) {
        this.mandatoryParams.layers = layers;
    }

    /**
     * DOCUMENT ME!
     *
     * @param styles List&lt;org.geotools.styling.Style&gt;
     */
    public void setStyles(List styles) {
        this.mandatoryParams.styles = styles;
    }

    /**
     * DOCUMENT ME!
     *
     * @param transparent DOCUMENT ME!
     */
    public void setTransparent(boolean transparent) {
        this.optionalParams.transparent = transparent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(int width) {
        this.mandatoryParams.width = width;
    }

    /**
     * decodes a color of the form <code>#FFFFFF</code> into a
     * <code>java.awt.Color</code> object
     *
     * @param hexColor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static final Color decodeColor(String hexColor) {
        return Color.decode(hexColor);
    }

    /**
     * DOCUMENT ME!
     *
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private class MandatoryParameters {
        /** ordered list of requested layers */
        FeatureTypeInfo[] layers;

        /**
         * ordered list of requested layers' styles, in a one to one
         * relationship with <code>layers</code>
         */
        List styles;

        /** DOCUMENT ME!  */
        Envelope bbox;

        /** DOCUMENT ME!  */
        int width;

        /** DOCUMENT ME!  */
        int height;

        /** DOCUMENT ME!  */
        String format;
    }

    /**
     * DOCUMENT ME!
     *
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private class OptionalParameters {
        /**
         * the map's background color requested, or the default (white) if not
         * specified
         */
        Color bgColor = DEFAULT_BG;

        /** from SRS (1.1) or CRS (1.2) param */
        String crs;

        /** DOCUMENT ME!  */
        String exceptions = SE_XML;

        /** DOCUMENT ME!  */
        boolean transparent = false;
    }

}
