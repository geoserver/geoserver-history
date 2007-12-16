/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geoserver.wfs.WFS;
import org.vfny.geoserver.global.dto.ServiceDTO;
import org.vfny.geoserver.global.dto.WMSDTO;
import java.util.Collections;
import java.util.Map;


/**
 * WMS
 *
 * <p>
 * Represents the GeoServer information required to configure an  instance of
 * the WMS Server. This class holds the currently used  configuration and is
 * instantiated initially by the GeoServerPlugIn  at start-up, but may be
 * modified by the Configuration Interface  during runtime. Such modifications
 * come from the GeoServer Object  in the SessionContext.
 * </p>
 *
 * <p>
 * WMS wms = new WMS(dto); System.out.println(wms.getName() + wms.WMS_VERSION);
 * System.out.println(wms.getAbstract());
 * </p>
 *
 * @author Gabriel Rold???n
 * @version $Id$
 */
public class WMS extends Service {
    /** WMS version spec implemented */
    private static final String WMS_VERSION = "1.1.1";

    /** WMS spec specifies this fixed service name */
    private static final String FIXED_SERVICE_NAME = "OGC:WMS";

    /** list of WMS Exception Formats */
    private static final String[] EXCEPTION_FORMATS = {
            "application/vnd.ogc.se_xml", //DJB: these arent actually supported!!
                                          //"application/vnd.ogc.se_inimage",
                                          //  "application/vnd.ogc.se_blank"
        };
    public static final String WEB_CONTAINER_KEY = "WMS";

    /** svg Renderer to use **/
    private String svgRenderer;

    /** svg anitalias or not **/
    private boolean svgAntiAlias;
    
    /** global Watermarking **/
    private boolean globalWatermarking;

    /** global Watermarking URL **/
    private String globalWatermarkingURL;

    /** rendering interpolation or not **/
    private Map baseMapLayers;
    private Map baseMapStyles;
    private Map baseMapEnvelopes;
    private String allowInterpolation;
    private WFS wfs;

    /**
     * WMS constructor.
     *
     * <p>
     * Stores the data specified in the WMSDTO object in this WMS Object for
     * GeoServer to use.
     * </p>
     *
     * @param config The data intended for GeoServer to use.
     */
    public WMS(WMSDTO config) {
        super(config.getService());
        setId("wms");
        svgRenderer = config.getSvgRenderer();
        svgAntiAlias = config.getSvgAntiAlias();
        globalWatermarking = config.getGlobalWatermarking();
        globalWatermarkingURL = config.getGlobalWatermarkingURL();
        allowInterpolation = config.getAllowInterpolation();
        baseMapLayers = config.getBaseMapLayers();
        baseMapStyles = config.getBaseMapStyles();
        baseMapEnvelopes = config.getBaseMapEnvelopes();
    }

    /**
     * Creates the WMS service by getting the WMSDTO object from the
     * config and calling {@link #WMS(WMSDTO)}.
     *
     * @param config
     * @param data
     * @param geoServer
     * @throws ConfigurationException
     */
    public WMS(Config config, Data data, GeoServer geoServer, WFS wfs)
        throws ConfigurationException {
        this(config.getXMLReader().getWms());
        setData(data);
        setGeoServer(geoServer);
        this.wfs = wfs;
    }

    /**
     * WMS constructor.
     *
     * <p>
     * Package constructor intended for default use by GeoServer
     * </p>
     *
     * @see GeoServer#GeoServer()
     */
    WMS() {
        super(new ServiceDTO());
        setId("wms");
    }

    /**
     * Quick hack to fix geot-770, need a full class rewrite otherwise and
     * we are too near release to do that
     * @return
     */
    public WFS getWFS() {
        return wfs;
    }

    /**
     * load purpose.
     * <p>
     * loads a new instance of data into this object.
     * </p>
     * @param config
     */
    public void load(WMSDTO config) {
        super.load(config.getService());
        svgRenderer = config.getSvgRenderer();
        svgAntiAlias = config.getSvgAntiAlias();
        globalWatermarking = config.getGlobalWatermarking();
        globalWatermarkingURL = config.getGlobalWatermarkingURL();
        allowInterpolation = config.getAllowInterpolation();
        baseMapLayers = config.getBaseMapLayers();
        baseMapStyles = config.getBaseMapStyles();
        baseMapEnvelopes = config.getBaseMapEnvelopes();
    }

    /**
     * Implement toDTO.
     *
     * <p>
     * Package method used by GeoServer. This method may return references, and
     * does not clone, so extreme caution sould be used when traversing the
     * results.
     * </p>
     *
     * @return WMSDTO An instance of the data this class represents. Please see
     *         Caution Above.
     *
     * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
     * @see WMSDTO
     */
    public Object toDTO() {
        WMSDTO w = new WMSDTO();
        w.setService((ServiceDTO) super.toDTO());
        w.setSvgRenderer(svgRenderer);
        w.setSvgAntiAlias(svgAntiAlias);
        w.setGlobalWatermarking(globalWatermarking);
        w.setGlobalWatermarkingURL(globalWatermarkingURL);
        w.setAllowInterpolation(allowInterpolation);
        w.setBaseMapLayers(baseMapLayers);
        w.setBaseMapStyles(baseMapStyles);
        w.setBaseMapEnvelopes(baseMapEnvelopes);

        return w;
    }

    /**
     * getExceptionFormats purpose.
     *
     * <p>
     * Returns a static list of Exception Formats in as Strings
     * </p>
     *
     * @return String[] a static list of Exception Formats
     */
    public String[] getExceptionFormats() {
        return EXCEPTION_FORMATS;
    }

    /**
     * overrides getName() to return the fixed service name as specified by OGC
     * WMS 1.1 spec
     *
     * @return static service name.
     */
    public String getName() {
        return FIXED_SERVICE_NAME;
    }

    /**
     * Returns the version of this WMS Instance.
     *
     * @return static version name
     */
    public static String getVersion() {
        return WMS_VERSION;
    }

    /**
     * Informs the user that this WMS supports SLD.  We don't currently
     * handle sld, still needs to be rolled in from geotools, so this now
     * must be false.
     *
     *  //djb: we support it now
     *
     * @return false
     */
    public boolean supportsSLD() {
        return true; //djb: we support it now
    }

    /**
     * Informs the user that this WMS supports User Layers
     *
     * @return false
     */
    public boolean supportsUserLayer() {
        return true; //djb we support this partially - we support inlinefeatures.  Soon we'll support remote wfs
    }

    /**
     * Informs the user that this WMS supports User Styles
     *
     * @return false
     */
    public boolean supportsUserStyle() {
        return true; //djb: we support this now!
    }

    /**
     * Informs the user that this WMS supports Remote WFS.
     *
     * @return false
     */
    public boolean supportsRemoteWFS() {
        return false; //djb: hopefully this will change soon.
    }

    /**
     * @return the id of the SVG renderer being used by the wms.
     */
    public String getSvgRenderer() {
        return svgRenderer;
    }

    /**
     * Sets the id of the SVG renderer being used by the wms.
     */
    public void setSvgRenderer(String svgRenderer) {
        this.svgRenderer = svgRenderer;
    }

    /**
     * @return Flag indicating wether the svg renderer should anti-alias or not.
     */
    public boolean isSvgAntiAlias() {
        return svgAntiAlias;
    }

    /**
     * Sets the Flag indicating wether the svg renderer should anti-alias or not.
     */
    public void setSvgAntiAlias(boolean svgAntiAlias) {
        this.svgAntiAlias = svgAntiAlias;
    }

    /**
     * @return Flag indicating wether the renderer should interpolate or not.
     */
    public String getAllowInterpolation() {
        return allowInterpolation;
    }

    /**
     * Sets the Flag indicating wether the renderer should interpolate or not.
     */
    public void setAllowInterpolation(String allowInterpolation) {
        this.allowInterpolation = allowInterpolation;
    }

    public Map getBaseMapLayers() {
        return (baseMapLayers != null) ? baseMapLayers : Collections.EMPTY_MAP;
    }

    public void setBaseMapLayers(Map layers) {
        baseMapLayers = layers;
    }

    public Map getBaseMapStyles() {
        return (baseMapStyles != null) ? baseMapStyles : Collections.EMPTY_MAP;
    }

    public void setBaseMapStyles(Map styles) {
        baseMapStyles = styles;
    }

    public Map getBaseMapEnvelopes() {
        return (baseMapEnvelopes != null) ? baseMapEnvelopes
                                          : Collections.EMPTY_MAP;
    }

    public void getBaseMapEnvelopes(Map envelopes) {
        baseMapEnvelopes = envelopes;
    }

	public boolean isGlobalWatermarking() {
		return globalWatermarking;
	}

	public void setGlobalWatermarking(boolean globalWatermarking) {
		this.globalWatermarking = globalWatermarking;
	}

	public String getGlobalWatermarkingURL() {
		return globalWatermarkingURL;
	}

	public void setGlobalWatermarkingURL(String globalWatermarkingURL) {
		this.globalWatermarkingURL = globalWatermarkingURL;
	}
}
