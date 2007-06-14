/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.styling.Style;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.servlets.WMService;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.IndexColorModel;
import java.util.Iterator;
import java.util.List;


/**
 * Represents a WMS GetMap request. as a extension to the WMS spec 1.1.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @author Simone Giannecchini
 * @version $Id$
 */
public class GetMapRequest extends WMSRequest {
    /** DOCUMENT ME! */
    static final Color DEFAULT_BG = Color.white;

    /** DOCUMENT ME! */
    public static final String SE_XML = "SE_XML";
    private static final String TRANSACTION_REQUEST_TYPE = "GetMap";

    /** set of mandatory request's parameters */
    private MandatoryParameters mandatoryParams = new MandatoryParameters();

    /** set of optionals request's parameters */
    private OptionalParameters optionalParams = new OptionalParameters();

    /**
     * Creates a GetMapRequest request.
     *
     * @param service The service handling the request.
     */
    public GetMapRequest(WMService service) {
        super(TRANSACTION_REQUEST_TYPE, service);
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
     * DJB: spec says SRS is *required*, so if they dont specify one, we should throw an error
     *      instead we use "NONE" - which is no-projection.
     *      Previous behavior was to the WSG84 lat/long (4326)
     *
     * @return request CRS, or <code>null</code> if not set.
     * TODO: make CRS manditory as for spec conformance
     */
    public CoordinateReferenceSystem getCrs() {
        return this.optionalParams.crs;
    }

    public String getSRS() {
        return this.optionalParams.srs;
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
    public MapLayerInfo[] getLayers() {
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
     * Gets a list of the the filters that will be applied to each layer before rendering
     *
     * @return -
     */
    public List getFilters() {
        return this.optionalParams.filters;
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
     * <a href="http://wiki.osgeo.org/index.php/WMS_Tiling_Client_Recommendation">WMS-C specification</a> tiling hint
     * @return
     */
    public boolean isTiled() {
        return this.optionalParams.tiled;
    }

    /**
     *
     * @return
     */
    public Point2D getTilesOrigin() {
        return this.optionalParams.tilesOrigin;
    }

    public int getBuffer() {
        return this.optionalParams.buffer;
    }

    public IndexColorModel getPalette() {
        return this.optionalParams.palette;
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
     * @return the KML/KMZ score value for image vs. vector response
     */
    public int getKMScore() {
        return this.optionalParams.KMScore;
    }

    /**
     * @return true: return full attribution for placemark <description>
     */
    public boolean getKMattr() {
        return this.optionalParams.KMattr;
    }

    /**
     * @return super overlay flag, <code>true</code> if super overlay requested.
     */
    public boolean getSuperOverlay() {
        return this.optionalParams.superOverlay;
    }

    /**
     * @return kml legend flag, <code>true</code> if legend is enabled.
     */
    public boolean getLegend() {
        return this.optionalParams.legend;
    }

    /**
     * @return The time request parameter.
     */
    public Integer getTime() {
        return this.optionalParams.time;
    }

    /**
     * @return The elevation request parameter.
     */
    public Integer getElevation() {
        return this.optionalParams.elevation;
    }
    
    /**
     * Returs the feature version optional parameter
     * @return
     */
    public String getFeatureVersion() {
        return this.optionalParams.featureVersion;
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
    public void setCrs(CoordinateReferenceSystem crs) {
        this.optionalParams.crs = crs;
    }

    /**
     * DOCUMENT ME!
     *
     * @param crs DOCUMENT ME!
     */
    public void setSRS(String srs) {
        this.optionalParams.srs = srs;
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
    public void setLayers(MapLayerInfo[] layers) {
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
     * Sets a list of filters, one for each layer
     *
     * @param styles List&lt;org.geotools.styling.Style&gt;
     */
    public void setFilters(List filters) {
        this.optionalParams.filters = filters;
    }

    /**
     * DOCUMENT ME!
     *
     * @param transparent DOCUMENT ME!
     */
    public void setTransparent(boolean transparent) {
        this.optionalParams.transparent = transparent;
    }

    public void setBuffer(int buffer) {
        this.optionalParams.buffer = buffer;
    }

    public void setPalette(IndexColorModel palette) {
        this.optionalParams.palette = palette;
    }

    public void setTiled(boolean tiled) {
        this.optionalParams.tiled = tiled;
    }

    public void setTilesOrigin(Point2D origin) {
        this.optionalParams.tilesOrigin = origin;
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
     * @param score the KML/KMZ score value for image vs. vector response, from 0 to 100
     */
    public void setKMScore(int score) {
        this.optionalParams.KMScore = score;
    }

    /**
     * @param on true: full attribution; false: no attribution
     */
    public void setKMattr(boolean on) {
        this.optionalParams.KMattr = on;
    }

    /**
     * Sets the super overlay parameter on the request.
     */
    public void setSuperOverlay(boolean superOverlay) {
        this.optionalParams.superOverlay = superOverlay;
    }

    /**
     * Sets the kml legend parameter of the request.
     */
    public void setLegend(boolean legend) {
        this.optionalParams.legend = legend;
    }

    /**
     * Sets the time request parameter.
     */
    public void setTime(Integer time) {
        this.optionalParams.time = time;
    }

    /**
     * Sets the elevation request parameter.
     */
    public void setElevation(Integer elevation) {
        this.optionalParams.elevation = elevation;
    }
    
    /**
     * Sets the feature version optional param
     * @param featureVersion
     */
    public void setFeatureVersion(String featureVersion) {
        this.optionalParams.featureVersion = featureVersion;
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
        MapLayerInfo[] layers;

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
        CoordinateReferenceSystem crs;

        /** EPSG code for the SRS */
        String srs;

        /** vendor extensions, allows to filter each layer with a user defined filter */
        List filters;

        /** DOCUMENT ME!  */
        String exceptions = SE_XML;

        /** DOCUMENT ME!  */
        boolean transparent = false;

        /**
         * Tiling hint, according to the
         * <a href="http://wiki.osgeo.org/index.php/WMS_Tiling_Client_Recommendation">WMS-C specification</a>
         */
        boolean tiled;

        /**
         * Temporary hack since finding a good tiling origin would require us to compute
         * the bbox on the fly
         * TODO: remove this once we cache the real bbox of vector layers
         */
        public Point2D tilesOrigin;

        /** the rendering buffer, in pixels **/
        int buffer;

        /** The palette used for rendering, if any */
        IndexColorModel palette;

        /** score value for KML/KMZ */
        int KMScore = 40;

        /** KML full/none attribution on returned placemark <description>. */
        boolean KMattr = true;

        /** KML super overlay vs normal ground overlay */
        boolean superOverlay = false;

        /** KML legend */
        boolean legend = false;

        /** time elevation parameter */
        Integer time;

        /** time elevation parameter */
        Integer elevation;
        
        /** feature version (for versioned requests) */
        String featureVersion;
    }

    /**
     * Standard override of toString()
     *
     * @return a String representation of this request.
     */
    public String toString() {
        StringBuffer returnString = new StringBuffer("\nGetMap Request");
        returnString.append("\n version: " + version);
        returnString.append("\n output format: " + mandatoryParams.format);
        returnString.append("\n width height: " + mandatoryParams.height + ","
            + mandatoryParams.width);
        returnString.append("\n bbox: " + mandatoryParams.bbox);
        returnString.append("\n layers: ");

        for (int i = 0; i < mandatoryParams.layers.length; i++) {
            returnString.append(mandatoryParams.layers[i].getName());

            if (i < (mandatoryParams.layers.length - 1)) {
                returnString.append(",");
            }
        }

        returnString.append("\n styles: ");

        for (Iterator it = mandatoryParams.styles.iterator(); it.hasNext();) {
            Style s = (Style) it.next();
            returnString.append(s.getName());

            if (it.hasNext()) {
                returnString.append(",");
            }
        }

        //returnString.append("\n inside: " + filter.toString());
        return returnString.toString();
    }

    
}
