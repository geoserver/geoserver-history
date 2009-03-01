package org.geoserver.wms;

import java.io.IOException;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.config.GeoServer;
import org.geotools.styling.Style;

/**
 * 
 * @author Gabriel Roldan
 *
 */
public class WMS {

    public static final String WEB_CONTAINER_KEY = "WMS";
    
    private final GeoServer geoserver;

    public WMS(GeoServer geoserver){
        this.geoserver = geoserver;
    }
    
    private Catalog getCatalog(){
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
}
