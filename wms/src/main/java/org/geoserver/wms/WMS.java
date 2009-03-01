package org.geoserver.wms;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Map;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.jai.JAIInfo;
import org.geoserver.wms.WatermarkInfo.Position;
import org.geotools.styling.Style;

/**
 * 
 * @author Gabriel Roldan
 * 
 */
public class WMS {

    public static final String WEB_CONTAINER_KEY = "WMS";

    /**
     * SVG renderers.
     */
    public static final String SVG_SIMPLE = "Simple";

    public static final String SVG_BATIK = "Batik";

    /**
     * Interpolation Types
     */
    public static final String INT_NEAREST = "Nearest";

    public static final String INT_BIlINEAR = "Bilinear";

    public static final String INT_BICUBIC = "Bicubic";

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
        return getCatalog().getStyleByName(styleName).getStyle();
    }

    public LayerInfo getLayerByName(String layerName) {
        return getCatalog().getLayerByName(layerName);
    }

    public LayerGroupInfo getLayerGroupByName(String layerGroupName) {
        return getCatalog().getLayerGroupByName(layerGroupName);
    }

    public boolean isEnabled() {
        return getServiceInfo().isEnabled();
    }

    public String getVersion() {
        return getServiceInfo().getVersions().get(0).toString();
    }

    public GeoServer getGeoServer() {
        return this.geoserver;
    }

    public String getInterpolation() {
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
        Map<String, Serializable> metadata = global.getMetadata();
        JAIInfo jaiInfo = (JAIInfo) metadata.get(JAIInfo.KEY);
        if (jaiInfo == null) {
            jaiInfo = new JAIInfo();
        }
        return jaiInfo;
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

    public int getUpdateSequence() {
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

}
