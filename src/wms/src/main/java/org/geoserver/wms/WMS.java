/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.MetadataMap;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.JAIInfo;
import org.geoserver.wms.WMSInfo.WMSInterpolation;
import org.geoserver.wms.WatermarkInfo.Position;
import org.geotools.styling.Style;
import org.geotools.util.Converters;
import org.geotools.util.Version;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.Name;


/**
 * A facade providing access to the WMS configuration details
 *  
 * @author Gabriel Roldan
 */
public class WMS {
    
    public static final String JPEG_COMPRESSION = "jpegCompression";
    
    public static final int JPEG_COMPRESSION_DEFAULT = 25;

    public static final String PNG_COMPRESSION = "pngCompression";
    
    public static final int PNG_COMPRESSION_DEFAULT = 25;

    static final Logger LOGGER = Logging.getLogger(WMS.class);

    public static final String WEB_CONTAINER_KEY = "WMS";

    /**
     * SVG renderers.
     */
    public static final String SVG_SIMPLE = "Simple";

    public static final String SVG_BATIK = "Batik";
    
    /**
     * KML reflector mode
     */
    public static String KML_REFLECTOR_MODE = "kmlReflectorMode";
    
    /**
     * KML reflector mode values
     */
    public static final String KML_REFLECTOR_MODE_REFRESH = "refresh";
    public static final String KML_REFLECTOR_MODE_SUPEROVERLAY = "superoverlay";
    public static final String KML_REFLECTOR_MODE_DOWNLOAD = "download";
    public static final String KML_REFLECTOR_MODE_DEFAULT = KML_REFLECTOR_MODE_REFRESH;

    /**
     * KML superoverlay sub-mode
     */
    public static final String KML_SUPEROVERLAY_MODE = "kmlSuperoverlayMode";
    public static final String KML_SUPEROVERLAY_MODE_AUTO = "auto";
    public static final String KML_SUPEROVERLAY_MODE_RASTER = "raster";
    public static final String KML_SUPEROVERLAY_MODE_OVERVIEW = "overview";
    public static final String KML_SUPEROVERLAY_MODE_HYBRID = "hybrid";
    public static final String KML_SUPEROVERLAY_MODE_DEFAULT = KML_SUPEROVERLAY_MODE_AUTO;
    
    public static final String KML_KMLATTR = "kmlAttr";
    public static final boolean KML_KMLATTR_DEFAULT = true;
    public static final String KML_KMLPLACEMARK = "kmlPlacemark";
    public static final boolean KML_KMLPLACEMARK_DEFAULT = false;

    public static final String KML_KMSCORE = "kmlKmscore";
    public static final int KML_KMSCORE_DEFAULT = 40;
    

    private final GeoServer geoserver;

    public WMS(GeoServer geoserver) {
        this.geoserver = geoserver;
    }

    private Catalog getCatalog() {
        return geoserver.getCatalog();
    }

    public WMSInfo getServiceInfo() {
        return geoserver.getService(WMSInfo.class);
    }

    public Style getStyleByName(String styleName) throws IOException {
        StyleInfo styleInfo = getCatalog().getStyleByName(styleName);
        return styleInfo == null ? null : styleInfo.getStyle();
    }

    public LayerInfo getLayerByName(String layerName) {
        return getCatalog().getLayerByName(layerName);
    }

    public LayerGroupInfo getLayerGroupByName(String layerGroupName) {
        return getCatalog().getLayerGroupByName(layerGroupName);
    }

    public boolean isEnabled() {
        WMSInfo serviceInfo = getServiceInfo();
        return serviceInfo.isEnabled();
    }

    public String getVersion() {
        WMSInfo serviceInfo = getServiceInfo();
        List<Version> versions = serviceInfo.getVersions();
        String version;
        if (versions.size() > 0) {
            version = versions.get(0).toString();
        } else {
            // shouldn't a version be set?
            version = "1.1.1";
        }
        return version;
    }

    public GeoServer getGeoServer() {
        return this.geoserver;
    }

    public WMSInterpolation getInterpolation() {
        return getServiceInfo().getInterpolation();
    }

    public Boolean getPNGNativeAcceleration() {
        JAIInfo jaiInfo = getJaiInfo();
        return Boolean.valueOf(jaiInfo.isPngAcceleration());
    }

    public Boolean getJPEGNativeAcceleration() {
        JAIInfo jaiInfo = getJaiInfo();
        return Boolean.valueOf(jaiInfo.isJpegAcceleration());
    }

    private JAIInfo getJaiInfo() {
        GeoServer geoServer = getGeoServer();
        GeoServerInfo global = geoServer.getGlobal();
        return global.getJAI();
    }

    public Charset getCharSet() {
        GeoServer geoServer2 = getGeoServer();
        GeoServerInfo global = geoServer2.getGlobal();
        String charset = global.getCharset();
        return Charset.forName(charset);
    }

    public String getProxyBaseUrl() {
        GeoServer geoServer = getGeoServer();
        GeoServerInfo global = geoServer.getGlobal();
        String proxyBaseUrl = global.getProxyBaseUrl();
        return proxyBaseUrl;
    }

    public long getUpdateSequence() {
        GeoServerInfo global = getGeoServer().getGlobal();
        return global.getUpdateSequence();
    }

    public int getWatermarkTransparency() {
        WatermarkInfo watermark = getServiceInfo().getWatermark();
        return watermark.getTransparency();
    }

    public int getWatermarkPosition() {
        WatermarkInfo watermark = getServiceInfo().getWatermark();
        Position position = watermark.getPosition();
        return position.getCode();
    }

    public boolean isGlobalWatermarking() {
        WatermarkInfo watermark = getServiceInfo().getWatermark();
        return watermark.isEnabled();
    }

    public String getGlobalWatermarkingURL() {
        WatermarkInfo watermark = getServiceInfo().getWatermark();
        return watermark.getURL();
    }

    public FeatureTypeInfo getFeatureTypeInfo(final Name name) {
        Catalog catalog = getCatalog();
        FeatureTypeInfo resource = catalog.getResourceByName(name, FeatureTypeInfo.class);
        return resource;
    }

    public CoverageInfo getCoverageInfo(final Name name) {
        Catalog catalog = getCatalog();
        CoverageInfo resource = catalog.getResourceByName(name, CoverageInfo.class);
        return resource;
    }

    public List<LayerInfo> getLayers() {
        Catalog catalog = getCatalog();
        return catalog.getLayers();
    }

    public String getNamespaceByPrefix(final String prefix) {
        Catalog catalog = getCatalog();
        NamespaceInfo namespaceInfo = catalog.getNamespaceByPrefix(prefix);
        return namespaceInfo == null ? null : namespaceInfo.getURI();
    }

    public List<LayerGroupInfo> getLayerGroups() {
        Catalog catalog = getCatalog();
        List<LayerGroupInfo> layerGroups = catalog.getLayerGroups();
        return layerGroups;
    }

    /**
     * Informs the user that this WMS supports SLD. We don't currently handle sld, still needs to be
     * rolled in from geotools, so this now must be false.
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
     * <p>
     * We support this both remote wfs and inlineFeature
     * </p>
     * 
     * @return true
     */
    public boolean supportsUserLayer() {
        return true;
    }

    /**
     * Informs the user that this WMS supports User Styles
     * 
     * @return true
     */
    public boolean supportsUserStyle() {
        return true;
    }

    /**
     * Informs the user that this WMS supports Remote WFS.
     * 
     * @return true
     */
    public boolean supportsRemoteWFS() {
        return true;
    }

    public void setSvgRenderer(String svgRendererHint) {
        WMSInfo serviceInfo = getServiceInfo();
        serviceInfo.getMetadata().put("svgRenderer", svgRendererHint);
        getGeoServer().save(serviceInfo);
    }

    public String getSvgRenderer() {
        WMSInfo serviceInfo = getServiceInfo();
        String svgRendererHint = (String) serviceInfo.getMetadata().get("svgRenderer");
        return svgRendererHint;
    }

    public boolean isSvgAntiAlias() {
        WMSInfo serviceInfo = getServiceInfo();
        Boolean svgAntiAlias = Converters.convert(serviceInfo.getMetadata().get("svgAntiAlias"), Boolean.class);
        return svgAntiAlias == null ? true : svgAntiAlias.booleanValue();
    }
    
    public int getPngCompression() {
        WMSInfo serviceInfo = getServiceInfo();
        return getMetadataPercentage(serviceInfo.getMetadata(), PNG_COMPRESSION, PNG_COMPRESSION_DEFAULT);
    }
    
    public int getJpegCompression() {
        WMSInfo serviceInfo = getServiceInfo();
        return getMetadataPercentage(serviceInfo.getMetadata(), JPEG_COMPRESSION, JPEG_COMPRESSION_DEFAULT);
    }
    
    int getMetadataPercentage(MetadataMap metadata, String key, int defaultValue) {
        Integer parsedValue = Converters.convert(metadata.get(key), Integer.class);
        if(parsedValue == null)
            return defaultValue;
        int value = parsedValue.intValue();
        if(value < 0 || value > 100) {
            LOGGER.warning("Invalid percertage value for '" + key + "', it should be between 0 and 100");
            return defaultValue;
        }
        
        return value;
    }

    public int getNumDecimals() {
        GeoServerInfo global = getGeoServer().getGlobal();
        return global.getNumDecimals();
    }

    public String getNameSpacePrefix(final String nsUri) {
        Catalog catalog = getCatalog();
        NamespaceInfo ns = catalog.getNamespaceByURI(nsUri);
        return ns == null ? null : ns.getPrefix();
    }
    
    public int getMaxBuffer() {
        return getServiceInfo().getMaxBuffer();
    }
    
    public int getMaxRequestMemory() {
        return getServiceInfo().getMaxRequestMemory();
    }
    
    public int getMaxRenderingTime() {
        return getServiceInfo().getMaxRenderingTime();
    }
    
    public int getMaxRenderingErrors() {
        return getServiceInfo().getMaxRenderingErrors();
    }
    
    public String getKmlReflectorMode() {
        String value = (String) getServiceInfo().getMetadata().get(KML_REFLECTOR_MODE);
        return value != null ? value : KML_REFLECTOR_MODE_DEFAULT;
    }
    
    public String getKmlSuperoverlayMode() {
        String value = (String) getServiceInfo().getMetadata().get(KML_SUPEROVERLAY_MODE);
        return value != null ? value : KML_SUPEROVERLAY_MODE_DEFAULT;
    }
    
    public boolean getKmlKmAttr() {
        Boolean kmAttr = Converters.convert(getServiceInfo().getMetadata().get(KML_KMLATTR), Boolean.class);
        return kmAttr == null ? KML_KMLATTR_DEFAULT: kmAttr.booleanValue();
    }
    
    public boolean getKmlPlacemark() {
        Boolean kmAttr = Converters.convert(getServiceInfo().getMetadata().get(KML_KMLPLACEMARK), Boolean.class);
        return kmAttr == null ? KML_KMLPLACEMARK_DEFAULT: kmAttr.booleanValue();
    }
    
    public int getKmScore() {
        return getMetadataPercentage(getServiceInfo().getMetadata(), KML_KMSCORE, KML_KMSCORE_DEFAULT);
    }
}
