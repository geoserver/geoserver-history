/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.wms;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import org.geotools.filter.Filter;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.requests.WMSRequest;

import com.vividsolutions.jts.geom.Envelope;


/**
 * represents a WMS GetMap request. as a extension to the WMS spec 1.1, we
 * provide the posibility to pass xml encoded Filters in the
 * <code>FILTERS</code>  parameters. This list of filters attachs to the same
 * constraints as the parameters <code>LAYERS</code> and <code>STYLES</code>.
 * In a few words, if the FILTERS parameter is present, it's value must
 * contain as many comma separated filters as LAYERS requested, in a one to
 * one order relationship. If for a given layer the user do not wants to
 * specify any filter, it's position in the list of filters may be empty.
 *
 * @author Gabriel Roldán
 * @version $Id: GetMapRequest.java,v 1.4 2004/01/12 21:01:32 dmzwiers Exp $
 */
public class GetMapRequest extends WMSRequest {
    /** DOCUMENT ME! */
    private static final Color DEFAULT_BG = Color.white;

    /** DOCUMENT ME! */
    private static final Filter[] NO_FILTERS = new Filter[0];

    /** DOCUMENT ME! */
    public static final String SE_XML = "SE_XML";

    /** set of mandatory request's parameters */
    private MandatoryParameters mandatorys = new MandatoryParameters();

    /** set of optionals request's parameters */
    private OptionalParameters optionals = new OptionalParameters();

    /** set of custom, non spec conformant, request's parameters */
    private CustomParameters customs = new CustomParameters();

    /**
     * Creates a new GetMapRequest object.
     */
    public GetMapRequest() {
        super();
        setRequest("GetMap");
    }

    /**
     * Gets the list of attributes to use in the request.  This is a custom
     * element, not part of the normal wms request.
     *
     * @return DOCUMENT ME!
     */
    public List getAttributes() {
        return customs.attributes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Envelope getBbox() {
        return mandatorys.bbox;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.awt.Color getBgColor() {
        return optionals.bgColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCrs() {
        return optionals.crs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getExceptions() {
        return optionals.exceptions;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFormat() {
        return mandatorys.format;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight() {
        return mandatorys.height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FeatureTypeInfo[] getLayers() {
        return mandatorys.layers;
    }

    /**
     * Gets a list of the names of the styles to be returned by the server.
     *
     * @return A list of Strings of the names of the styles.
     */
    public List getStyles() {
        return mandatorys.styles;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Filter[] getFilters() {
        return customs.filters;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isTransparent() {
        return optionals.transparent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return mandatorys.width;
    }

    public void setWriteSvgHeader(boolean writeSvgHeader) {
        customs.writeSvgHeader = writeSvgHeader;
    }

    public boolean getWriteSvgHeader() {
        return customs.writeSvgHeader;
    }

    /**
     * DOCUMENT ME!
     *
     * @param attributes DOCUMENT ME!
     */
    public void setAttributes(List attributes) {
        customs.attributes = attributes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bbox DOCUMENT ME!
     */
    public void setBbox(Envelope bbox) {
        mandatorys.bbox = bbox;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bgColor DOCUMENT ME!
     */
    public void setBgColor(java.awt.Color bgColor) {
        optionals.bgColor = bgColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param crs DOCUMENT ME!
     */
    public void setCrs(String crs) {
        optionals.crs = crs;
    }

    /**
     * DOCUMENT ME!
     *
     * @param exceptions DOCUMENT ME!
     */
    public void setExceptions(String exceptions) {
        optionals.exceptions = exceptions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param format DOCUMENT ME!
     */
    public void setFormat(String format) {
        mandatorys.format = format;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(int height) {
        mandatorys.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param filters DOCUMENT ME!
     */
    public void setFilters(Filter[] filters) {
        customs.filters = (filters == null) ? NO_FILTERS : filters;
    }

    /**
     * DOCUMENT ME!
     *
     * @param layers DOCUMENT ME!
     */
    public void setLayers(FeatureTypeInfo[] layers) {
        mandatorys.layers = layers;
    }

    /**
     * DOCUMENT ME!
     *
     * @param styles DOCUMENT ME!
     */
    public void setStyles(List styles) {
        mandatorys.styles = styles;
    }

    /**
     * DOCUMENT ME!
     *
     * @param transparent DOCUMENT ME!
     */
    public void setTransparent(boolean transparent) {
        optionals.transparent = transparent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(int width) {
        mandatorys.width = width;
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

    private class MandatoryParameters {
        /** ordered list of requested layers */
        FeatureTypeInfo[] layers;

        /**
         * ordered list of requested layers' styles, in a one to one
         * relationship with <code>layers</code>
         */
        List styles;
        Envelope bbox;
        int width;
        int height;
        String format;
    }

    private class OptionalParameters {
        /**
         * the map's background color requested, or the default (white) if not
         * specified
         */
        Color bgColor = DEFAULT_BG;

        /** from SRS (1.1) or CRS (1.2) param */
        String crs;
        String exceptions = SE_XML;
        boolean transparent = false;
    }

    /**
     *
     */
    private class CustomParameters {
        /** wether the xml header and SVG element must be printed */
        boolean writeSvgHeader = true;

        /**
         * as a extension to the WMS spec 1.1, we provide the posibility to
         * pass xml encoded Filters in the <code>FILTERS</code>  parameters.
         * This list of filters attachs to the same constraints as the
         * parameters <code>LAYERS</code> and <code>STYLES</code>
         */
        Filter[] filters = NO_FILTERS;

        /**
         * list of lists, with an entry for each layer requested, being each
         * entry a List of String's, who's elements are the attribute's type
         * names requested. By now, it is just used when generating SVG maps,
         * to send attribute info together with the XML elements that
         * represents the geometries in SVG, but can be used too for the
         * production of other non raster map formats.
         */
        List attributes = Collections.EMPTY_LIST;
    }
}
