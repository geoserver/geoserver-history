package org.vfny.geoserver.requests.wms;


import com.vividsolutions.jts.geom.Envelope;

import org.geotools.filter.Filter;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.requests.WMSRequest;
import java.util.*;
import java.awt.Color;

/**
 * represents a WMS GetMap request.
 *
 * as a extension to the WMS spec 1.1, we provide the posibility to pass
 * xml encoded Filters in the <code>FILTERS</code>  parameters. This list
 * of filters attachs to the same constraints as the parameters
 * <code>LAYERS</code> and <code>STYLES</code>.
 * In a few words, if the FILTERS parameter is present, it's value must contain
 * as many comma separated filters as LAYERS requested, in a one to one order
 * relationship. If for a given layer the user do not wants to specify any
 * filter, it's position in the list of filters may be empty.
 *
 *
 * @author Gabriel Roldán
 * @version $Id: GetMapRequest.java,v 1.1.2.1 2003/11/14 20:39:14 groldan Exp $
 */
public class GetMapRequest extends WMSRequest
{
    private static final Color DEFAULT_BG = Color.white;

    private static final Filter[] NO_FILTERS = new Filter[0];

    /** DOCUMENT ME! */
    public static final String SE_XML = "SE_XML";

    /**ordered list of requested layers*/
    private FeatureTypeConfig[] layers;

    /**ordered list of requested layers' styles, in a one to one
     * relationship with <code>layers</code>*/
    private List styles;

    /** from SRS (1.1) or CRS (1.2) param */
    private String crs;
    private Envelope bbox;
    private int width;
    private int height;
    private String format;
    private boolean transparent = false;
    private Color bgColor = DEFAULT_BG;
    private String exceptions = SE_XML;

    /**
     * as a extension to the WMS spec 1.1, we provide the posibility to pass
     * xml encoded Filters in the <code>FILTERS</code>  parameters. This list
     * of filters attachs to the same constraints as the parameters
     * <code>LAYERS</code> and <code>STYLES</code>
     */
    private Filter[] filters = NO_FILTERS;

    /**
     * Creates a new GetMapRequest object.
     */
    public GetMapRequest()
    {
        super();
        setRequest("GetMap");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Envelope getBbox()
    {
        return bbox;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.awt.Color getBgColor()
    {
        return bgColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCrs()
    {
        return crs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getExceptions()
    {
        return exceptions;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFormat()
    {
        return format;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FeatureTypeConfig[] getLayers()
    {
        return layers;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List getStyles()
    {
        return styles;
    }

    public Filter[] getFilters()
    {
        return filters;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isTransparent()
    {
        return transparent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bbox DOCUMENT ME!
     */
    public void setBbox(Envelope bbox)
    {
        this.bbox = bbox;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bgColor DOCUMENT ME!
     */
    public void setBgColor(java.awt.Color bgColor)
    {
        this.bgColor = bgColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param crs DOCUMENT ME!
     */
    public void setCrs(String crs)
    {
        this.crs = crs;
    }

    /**
     * DOCUMENT ME!
     *
     * @param exceptions DOCUMENT ME!
     */
    public void setExceptions(String exceptions)
    {
        this.exceptions = exceptions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param format DOCUMENT ME!
     */
    public void setFormat(String format)
    {
        this.format = format;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(int height)
    {
        this.height = height;
    }

    public void setFilters(Filter[] filters)
    {
      this.filters = filters == null? NO_FILTERS : filters;
    }

    /**
     * DOCUMENT ME!
     *
     * @param layers DOCUMENT ME!
     */
    public void setLayers(FeatureTypeConfig[] layers)
    {
        this.layers = layers;
    }

    /**
     * DOCUMENT ME!
     *
     * @param styles DOCUMENT ME!
     */
    public void setStyles(List styles)
    {
        this.styles = styles;
    }

    /**
     * DOCUMENT ME!
     *
     * @param transparent DOCUMENT ME!
     */
    public void setTransparent(boolean transparent)
    {
        this.transparent = transparent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(int width)
    {
        this.width = width;
    }

    /**
     * decodes a color of the form <code>#FFFFFF</code> into
     * a <code>java.awt.Color</code> object
     *
     * @param hexColor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static final Color decodeColor(String hexColor)
    {
      return Color.decode(hexColor);
    }
}
