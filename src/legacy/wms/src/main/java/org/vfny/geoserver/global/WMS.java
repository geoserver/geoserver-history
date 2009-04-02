/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WatermarkInfo;
import org.geoserver.wms.WMSInfo.WMSInterpolation;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.global.dto.ServiceDTO;
import org.vfny.geoserver.global.dto.WMSDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * WMS
 * 
 * <p>
 * Represents the GeoServer information required to configure an instance of the
 * WMS Server. This class holds the currently used configuration and is
 * instantiated initially by the GeoServerPlugIn at start-up, but may be
 * modified by the Configuration Interface during runtime. Such modifications
 * come from the GeoServer Object in the SessionContext.
 * </p>
 * 
 * <p>
 * WMS wms = new WMS(dto); System.out.println(wms.getName() + wms.WMS_VERSION);
 * System.out.println(wms.getAbstract());
 * </p>
 * 
 * @author Gabriel Rold???n
 * @version $Id$
 * @deprecated use {@link WMSInfo}
 */
public class WMS extends Service {
    private static final Logger LOGGER = Logging.getLogger(WMS.class);
    
    /** WMS version spec implemented */
    private static final String WMS_VERSION = "1.1.1";

    /** WMS spec specifies this fixed service name */
    private static final String FIXED_SERVICE_NAME = "OGC:WMS";

    /** list of WMS Exception Formats */
    private static final String[] EXCEPTION_FORMATS = { "application/vnd.ogc.se_xml", // DJB:
    // these
    // arent
    // actually
    // supported!!
    // "application/vnd.ogc.se_inimage",
    // "application/vnd.ogc.se_blank"
    };

    public static final String WEB_CONTAINER_KEY = "WMS";

    //public static final int WATERMARK_UL = 0;
    public static final int WATERMARK_UL = WatermarkInfo.Position.TOP_LEFT.getCode();

    //public static final int WATERMARK_UC = 1;
    public static final int WATERMARK_UC = WatermarkInfo.Position.TOP_CENTER.getCode();
    
    public static final int WATERMARK_UR = WatermarkInfo.Position.TOP_RIGHT.getCode();
    //public static final int WATERMARK_UR = 2;

    public static final int WATERMARK_CL = WatermarkInfo.Position.MID_LEFT.getCode();
    //public static final int WATERMARK_CL = 3;
    
    public static final int WATERMARK_CC = WatermarkInfo.Position.MID_CENTER.getCode();
    //public static final int WATERMARK_CC = 4;
    
    public static final int WATERMARK_CR = WatermarkInfo.Position.MID_RIGHT.getCode();
    //public static final int WATERMARK_CR = 5;
    
    public static final int WATERMARK_LL = WatermarkInfo.Position.BOT_LEFT.getCode();
    //public static final int WATERMARK_LL = 6;
    
    public static final int WATERMARK_LC = WatermarkInfo.Position.BOT_CENTER.getCode();
    //public static final int WATERMARK_LC = 7;
    
    public static final int WATERMARK_LR = WatermarkInfo.Position.BOT_RIGHT.getCode();
    //public static final int WATERMARK_LR = 8;
    
    ///** svg Renderer to use * */
    //private String svgRenderer;
    //
    ///** svg anitalias or not * */
    //private boolean svgAntiAlias;
    //
    ///** global Watermarking * */
    //private boolean globalWatermarking;
    //
    ///** global Watermarking URL * */
    //private String globalWatermarkingURL;
    //
    ///** globlal Watermarking alpha * */
    //private int watermarkTransparency;
    //
    ///**
    // * Watermark position
    // * 
    // * <pre>
    // * O -- O -- O      0 -- 1 -- 2
    // * |    |    |      |    |    |
    // * O -- O -- O  ==  3 -- 4 -- 5
    // * |    |    |      |    |    |
    // * O -- O -- O      6 -- 7 -- 8
    // * </pre>
    // * 
    // */
    //private int watermarkPosition;
    //
    ///** rendering interpolation or not * */
    //private Map baseMapLayers;
    //
    //private Map baseMapStyles;
    //
    //private Map baseMapEnvelopes;
    //
    //private String allowInterpolation;
    //
    //private WFS wfs;
    //
    ///**
    // * Limited set of CRS codes displayed in the capabilities document
    // */
    //private Set capabilitiesCrsList = Collections.EMPTY_SET;

    ///**
    // * WMS constructor.
    // * 
    // * <p>
    // * Stores the data specified in the WMSDTO object in this WMS Object for
    // * GeoServer to use.
    // * </p>
    // * 
    // * @param config
    // *            The data intended for GeoServer to use.
    // */
    //public WMS(WMSDTO config) {
    //    super(config.getService());
    //    setId("wms");
    //    svgRenderer = config.getSvgRenderer();
    //    svgAntiAlias = config.getSvgAntiAlias();
    //    globalWatermarking = config.getGlobalWatermarking();
    //    globalWatermarkingURL = config.getGlobalWatermarkingURL();
    //    watermarkTransparency = config.getWatermarkTransparency();
    //    watermarkPosition = config.getWatermarkPosition();
    //    allowInterpolation = config.getAllowInterpolation();
    //    baseMapLayers = config.getBaseMapLayers();
    //    baseMapStyles = config.getBaseMapStyles();
    //    baseMapEnvelopes = config.getBaseMapEnvelopes();
    //    capabilitiesCrsList = config.getCapabilitiesCrs();
    //}

    ///**
    // * Creates the WMS service by getting the WMSDTO object from the config and
    // * calling {@link #WMS(WMSDTO)}.
    // * 
    // * @param config
    // * @param data
    // * @param geoServer
    // * @throws ConfigurationException
    // */
    //public WMS(Config config, Data data, GeoServer geoServer, WFS wfs)
    //        throws ConfigurationException {
    //    this(config.getWms());
    //    setData(data);
    //    setGeoServer(geoServer);
    //    this.wfs = wfs;
    //}

    WMSInfo wms;
    
    public WMS( org.geoserver.config.GeoServer gs ) {
        super( gs.getService(WMSInfo.class), gs );
        init();
        //wms.setName( FIXED_SERVICE_NAME );
    }
    
    public WMSInfo getInfo() {
        return wms;
    }
    
    public void init() {
        wms = gs.getService(WMSInfo.class);
        service = wms;
    }
    /**
     * Quick hack to fix geot-770, need a full class rewrite otherwise and we
     * are too near release to do that
     * 
     * @return
     */
    public WFS getWFS() {
        return new WFS( gs );
        //return wfs;
    }

    /**
     * load purpose.
     * <p>
     * loads a new instance of data into this object.
     * </p>
     * 
     * @param config
     */
    public void load(WMSDTO config) {
        super.load(config.getService());
        
        setSvgRenderer( config.getSvgRenderer() );
        setSvgAntiAlias( config.getSvgAntiAlias() );
        setGlobalWatermarking(config.getGlobalWatermarking());
        setGlobalWatermarkingURL(config.getGlobalWatermarkingURL());
        setWatermarkTransparency(config.getWatermarkTransparency());
        setWatermarkPosition(config.getWatermarkPosition());
        setAllowInterpolation(WMSInterpolation.valueOf(config.getAllowInterpolation()));
        setBaseMapLayers(config.getBaseMapLayers());
        setBaseMapStyles(config.getBaseMapStyles());
        setBaseMapEnvelopes(config.getBaseMapEnvelopes());
        setCapabilitiesCrsList(config.getCapabilitiesCrs());
        
        //svgRenderer = config.getSvgRenderer();
        //svgAntiAlias = config.getSvgAntiAlias();
        //globalWatermarking = config.getGlobalWatermarking();
        //globalWatermarkingURL = config.getGlobalWatermarkingURL();
        //watermarkTransparency = config.getWatermarkTransparency();
        //watermarkPosition = config.getWatermarkPosition();
        //allowInterpolation = config.getAllowInterpolation();
        //baseMapLayers = config.getBaseMapLayers();
        //baseMapStyles = config.getBaseMapStyles();
        //baseMapEnvelopes = config.getBaseMapEnvelopes();
        //capabilitiesCrsList = config.getCapabilitiesCrs();
    }

    ///**
    // * WMS constructor.
    // * 
    // * <p>
    // * Package constructor intended for default use by GeoServer
    // * </p>
    // * 
    // * @see GeoServer#GeoServer()
    // */
    //WMS() {
    //    super(new ServiceDTO());
    //    setId("wms");
    //}

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
    public WMSDTO toDTO() {
        WMSDTO w = new WMSDTO();
        w.setService((ServiceDTO) super.toDTO());
        
        w.setSvgRenderer(getSvgRenderer());
        w.setSvgAntiAlias(isSvgAntiAlias());
        w.setGlobalWatermarking(isGlobalWatermarking());
        w.setGlobalWatermarkingURL(getGlobalWatermarkingURL());
        w.setWatermarkTransparency(getWatermarkTransparency());
        w.setWatermarkPosition(getWatermarkPosition());
        w.setAllowInterpolation(getAllowInterpolation().name());
        w.setBaseMapLayers(getBaseMapLayers());
        w.setBaseMapStyles(getBaseMapStyles());
        w.setBaseMapEnvelopes(getBaseMapEnvelopes());
        w.setCapabilitiesCrs(getCapabilitiesCrsList());
        
        //w.setSvgRenderer(svgRenderer);
        //w.setSvgAntiAlias(svgAntiAlias);
        //w.setGlobalWatermarking(globalWatermarking);
        //w.setGlobalWatermarkingURL(globalWatermarkingURL);
        //w.setWatermarkTransparency(watermarkTransparency);
        //w.setWatermarkPosition(watermarkPosition);
        //w.setAllowInterpolation(allowInterpolation);
        //w.setBaseMapLayers(baseMapLayers);
        //w.setBaseMapStyles(baseMapStyles);
        //w.setBaseMapEnvelopes(baseMapEnvelopes);
        //w.setCapabilitiesCrs(capabilitiesCrsList);

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

    ///**
    // * overrides getName() to return the fixed service name as specified by OGC
    // * WMS 1.1 spec
    // * 
    // * @return static service name.
    // */
    //public String getName() {
    //    return FIXED_SERVICE_NAME;
    //}

    /**
     * Returns the version of this WMS Instance.
     * 
     * @return static version name
     */
    public static String getVersion() {
        return WMS_VERSION;
    }

    /**
     * Informs the user that this WMS supports SLD. We don't currently handle
     * sld, still needs to be rolled in from geotools, so this now must be
     * false.
     * 
     * //djb: we support it now
     * 
     * @return false
     */
    public boolean supportsSLD() {
        return true; // djb: we support it now
    }

    /**
     * Informs the user that this WMS supports User Layers
     * 
     * @return false
     */
    public boolean supportsUserLayer() {
        return true; // djb we support this partially - we support
        // inlinefeatures. Soon we'll support remote wfs
    }

    /**
     * Informs the user that this WMS supports User Styles
     * 
     * @return false
     */
    public boolean supportsUserStyle() {
        return true; // djb: we support this now!
    }

    /**
     * Informs the user that this WMS supports Remote WFS.
     * 
     * @return false
     */
    public boolean supportsRemoteWFS() {
        return true;
    }

    /**
     * @return the id of the SVG renderer being used by the wms.
     */
    public String getSvgRenderer() {
        return (String) wms.getMetadata().get( "svgRenderer" );
        //return svgRenderer;
    }

    /**
     * Sets the id of the SVG renderer being used by the wms.
     */
    public void setSvgRenderer(String svgRenderer) {
        wms.getMetadata().put( "svgRenderer", svgRenderer );
        //this.svgRenderer = svgRenderer;
    }

    /**
     * @return Flag indicating wether the svg renderer should anti-alias or not.
     */
    public boolean isSvgAntiAlias() {
        Boolean svgAntiAlias = (Boolean) wms.getMetadata().get( "svgAntiAlias");
        return svgAntiAlias != null ? svgAntiAlias : true; 
        //return svgAntiAlias;
    }

    /**
     * Sets the Flag indicating wether the svg renderer should anti-alias or
     * not.
     */
    public void setSvgAntiAlias(boolean svgAntiAlias) {
        wms.getMetadata().put( "svgAntiAlias", svgAntiAlias );
        //this.svgAntiAlias = svgAntiAlias;
    }

    /**
     * @return Flag indicating wether the renderer should interpolate or not.
     */
    public WMSInterpolation getAllowInterpolation() {
        return wms.getInterpolation();
    }

    /**
     * Sets the Flag indicating wether the renderer should interpolate or not.
     */
    public void setAllowInterpolation(WMSInterpolation allowInterpolation) {
        wms.setInterpolation(allowInterpolation);
        //this.allowInterpolation = allowInterpolation;
    }

    public Map getBaseMapLayers() {
        HashMap baseLayers = new HashMap();
        for ( LayerGroupInfo map : gs.getCatalog().getLayerGroups() ) {
            StringBuffer layers = new StringBuffer();
            for ( LayerInfo l : map.getLayers() ) {
                layers.append( l.getName() ).append( "," );
            }
            layers.setLength( layers.length() - 1 );
            baseLayers.put( map.getName(), layers.toString() );
        }
        return baseLayers;
        //return baseMapLayers != null ? baseMapLayers : Collections.EMPTY_MAP;
    }

    public void setBaseMapLayers(Map layers) {
        HashMap baseLayers = new HashMap();
        for ( LayerGroupInfo map : gs.getCatalog().getLayerGroups() ) {
            gs.getCatalog().remove( map );
        }
        for ( Iterator e = layers.entrySet().iterator(); e.hasNext(); ) {
            Map.Entry entry = (Map.Entry) e.next();
            String name = (String) entry.getKey();
            String[] layerNames = ((String) entry.getValue()).split(",");
            
            LayerGroupInfo baseMap = gs.getCatalog().getFactory().createLayerGroup();
            baseMap.setName( name );
            for ( String layerName : layerNames ) {
                ResourceInfo resource = null;
                
                if ( layerName.contains( ":" ) ) {
                    resource = gs.getCatalog().getResourceByName( layerName.split(":")[0], layerName.split(":")[1], ResourceInfo.class );
                } else {
                    resource = gs.getCatalog().getResourceByName( layerName, ResourceInfo.class );
                }
                
                LayerInfo layer = null;
                if ( resource != null ) {
                    layer = gs.getCatalog().getLayers( resource ).get( 0 );
                }
                
                if(layer == null) {
                    LOGGER.log(Level.SEVERE, "No such layer: " + layerName + ", ignoring it in the base map " + name);
                    continue;
                }
                
                baseMap.getLayers().add( layer );
            }
            gs.getCatalog().add( baseMap );
        }
        
    }

    public Map getBaseMapStyles() {
        HashMap baseMapStyles = new HashMap();
        for ( LayerGroupInfo map : gs.getCatalog().getLayerGroups() ) {
            String rawStyleList = (String) map.getMetadata().get( "rawStyleList");
            if ( rawStyleList != null ) {
                baseMapStyles.put( map.getName(), rawStyleList );
            }
            else {
                //generate it from the actual style list
                StringBuffer styles = new StringBuffer();
                for ( StyleInfo s : map.getStyles() ) {
                    styles.append( s.getName() ).append( "," );
                }
                styles.setLength( styles.length() - 1 );
                baseMapStyles.put( map.getName(), styles.toString() );
            }
        }
        return baseMapStyles;
        //return baseMapStyles != null ? baseMapStyles : Collections.EMPTY_MAP;
    }

    public void setBaseMapStyles(Map styles) {
        for ( Iterator e = styles.entrySet().iterator(); e.hasNext(); ) {
            Map.Entry entry = (Map.Entry) e.next();
            String name = (String) entry.getKey();
            String styleNamesRaw = ((String)entry.getValue()).trim();
            List<String> styleNames = null;
            if ( "".equals( styleNamesRaw ) ) {
                styleNames = new ArrayList();
            }
            else {
                styleNames = new ArrayList( Arrays.asList( styleNamesRaw.split(",") ) );    
            }
            
            LayerGroupInfo map = gs.getCatalog().getLayerGroupByName( name );
            
            //save the original raw style list
            map.getMetadata().put( "rawStyleList", styleNamesRaw );
            
            //if there were less styles specified than base layers, pad with 
            // defaults
            for ( int i = styleNames.size(); i < map.getLayers().size(); i++ ) {
                styleNames.add( "" );
            }
            
            map.getStyles().clear();
            for ( int i = 0; i < styleNames.size(); i++ ) {
                String styleName = styleNames.get( i );
                StyleInfo style = null;
                if ( "".equals( styleName ) ) {
                    //use the default style for the layer
                    style = map.getLayers().get(i).getDefaultStyle();
                }
                else {
                    style = gs.getCatalog().getStyleByName( styleName );
                }
                
                if ( style == null ) {
                    throw new RuntimeException( "No such style: " + styleName );
                }
                map.getStyles().add( style );
            }
            
            gs.getCatalog().save( map );
        }
        //baseMapStyles = styles;
    }

    public Map getBaseMapEnvelopes() {
        HashMap baseMapEnvelopes = new HashMap();
        for ( LayerGroupInfo map : gs.getCatalog().getLayerGroups() ) {
            baseMapEnvelopes.put( map.getName(), new GeneralEnvelope( map.getBounds() ) );
        }
        return baseMapEnvelopes;
        //return baseMapEnvelopes != null ? baseMapEnvelopes : Collections.EMPTY_MAP;
    }

    public void getBaseMapEnvelopes(Map envelopes) {
        setBaseMapEnvelopes(envelopes);
        //baseMapEnvelopes = envelopes;
    }
    
    public void setBaseMapEnvelopes(Map envelopes) {
        for ( Iterator e = envelopes.entrySet().iterator(); e.hasNext(); ) {
            Map.Entry entry = (Map.Entry) e.next();
            String name = (String) entry.getKey();
            GeneralEnvelope env = (GeneralEnvelope) entry.getValue();
            
            LayerGroupInfo map = gs.getCatalog().getLayerGroupByName( name );
            map.setBounds( new ReferencedEnvelope( env ) );
            gs.getCatalog().save( map );
        }
        //baseMapEnvelopes = envelopes;
    }

    public boolean isGlobalWatermarking() {
        return wms.getWatermark().isEnabled();
        //return globalWatermarking;
    }

    public void setGlobalWatermarking(boolean globalWatermarking) {
        wms.getWatermark().setEnabled(globalWatermarking);
        //this.globalWatermarking = globalWatermarking;
    }

    public String getGlobalWatermarkingURL() {
        return wms.getWatermark().getURL();
        //return globalWatermarkingURL;
    }

    public void setGlobalWatermarkingURL(String globalWatermarkingURL) {
        wms.getWatermark().setURL( globalWatermarkingURL );
        //this.globalWatermarkingURL = globalWatermarkingURL;
    }

    public int getWatermarkTransparency() {
        return wms.getWatermark().getTransparency();
        //return watermarkTransparency;
    }

    public void setWatermarkTransparency(int watermarkTransparency) {
        wms.getWatermark().setTransparency(watermarkTransparency);
        //this.watermarkTransparency = watermarkTransparency;
    }

    public int getWatermarkPosition() {
        return wms.getWatermark().getPosition().getCode();
        //return watermarkPosition;
    }

    public void setWatermarkPosition(int watermarkPosition) {
        WatermarkInfo.Position p = WatermarkInfo.Position.get( watermarkPosition );
        wms.getWatermark().setPosition(p);
        //this.watermarkPosition = watermarkPosition;
    }

    public Set getCapabilitiesCrsList() {
        return new TreeSet(wms.getSRS());
        //return new TreeSet(capabilitiesCrsList);
    }

    public void setCapabilitiesCrsList(Set epsgCodes) {
        wms.getSRS().clear();
        if ( epsgCodes != null ) {
            wms.getSRS().addAll( epsgCodes );    
        }
        
        //this.capabilitiesCrsList = epsgCodes == null ? Collections.EMPTY_SET : new TreeSet(
        //        epsgCodes);
    }
}
