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
/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.wms;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.filter.Filter;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.requests.WMSRequest;
import java.awt.Color;
import java.util.Collections;
import java.util.List;


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
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id: GetMapRequest.java,v 1.8 2004/03/14 16:00:54 groldan Exp $
 */
public class GetMapRequest extends WMSRequest {
    /** DOCUMENT ME! */
    static final Color DEFAULT_BG = Color.white;

    /** DOCUMENT ME! */
    static final Filter[] NO_FILTERS = new Filter[0];

    /** DOCUMENT ME! */
    public static final String SE_XML = "SE_XML";

    /** set of mandatory request's parameters */
    private MandatoryParameters mandatoryParams = new MandatoryParameters();

    /** set of optionals request's parameters */
    private OptionalParameters optionalParams = new OptionalParameters();

    /** set of custom, non spec conformant, request's parameters */
    private CustomParameters customParams = new CustomParameters();

    /**
     * Creates a new GetMapRequest object.
     */
    public GetMapRequest() {
        super();
        setRequest("GetMap");
    }

    /**
     * Gets the list of attributes to return in output map formats that
     * supports returning feature attributes as well as styled geometries
     * (e.g. SVG).
     * 
     * <p>
     * This is a custom element, not part of the normal wms request.
     * </p>
     *
     * @return DOCUMENT ME!
     */
    public List getUserSuppliedAttributes() {
        return this.customParams.attributes;
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
    public Filter[] getUserSuppliedFilters() {
        return this.customParams.filters;
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
     * @param writeSvgHeader DOCUMENT ME!
     */
    public void setWriteSvgHeader(boolean writeSvgHeader) {
        this.customParams.writeSvgHeader = writeSvgHeader;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getWriteSvgHeader() {
        return this.customParams.writeSvgHeader;
    }

    /**
     * DOCUMENT ME!
     *
     * @param attributes DOCUMENT ME!
     */
    public void setAttributes(List attributes) {
        this.customParams.attributes = attributes;
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
     * @param filters DOCUMENT ME!
     */
    public void setFilters(Filter[] filters) {
        this.customParams.filters = (filters == null) ? NO_FILTERS : filters;
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
     * @param styles DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param collect DOCUMENT ME!
     */
    public void setCollectGeometries(boolean collect) {
        this.customParams.collectGeometries = collect;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gfactor DOCUMENT ME!
     */
    public void setGeneralizationFactor(double gfactor) {
        this.customParams.generalizationFactor = gfactor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCollectGeometries() {
        return this.customParams.collectGeometries;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getGeneralizationFactor() {
        return this.customParams.generalizationFactor;
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

    /**
     * holding of custom request parameters: FILTERS, SVGHEADER, ATTRIBUTES,
     * COLLECT, GENERALIZATIONFACTOR
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

        /** DOCUMENT ME!  */
        double generalizationFactor = -1;

        /** DOCUMENT ME!  */
        boolean collectGeometries = false;
    }
}
