/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.vfny.geoserver.wms.requests.DescribeLayerRequest;
import org.vfny.geoserver.wms.requests.GetFeatureInfoRequest;
import org.vfny.geoserver.wms.requests.GetLegendGraphicRequest;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.requests.WMSCapabilitiesRequest;
import org.vfny.geoserver.wms.responses.DescribeLayerResponse;
import org.vfny.geoserver.wms.responses.GetFeatureInfoResponse;
import org.vfny.geoserver.wms.responses.GetLegendGraphicResponse;
import org.vfny.geoserver.wms.responses.GetMapResponse;
import org.vfny.geoserver.wms.responses.WMSCapabilitiesResponse;
import org.vfny.geoserver.wms.servlets.Capabilities;
import org.vfny.geoserver.wms.servlets.DescribeLayer;
import org.vfny.geoserver.wms.servlets.GetFeatureInfo;
import org.vfny.geoserver.wms.servlets.GetLegendGraphic;
import org.vfny.geoserver.wms.servlets.GetMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DefaultWebMapService implements WebMapService, ApplicationContextAware {
    /**
     * default for 'format' parameter.
     */
    public static String FORMAT = "image/png";

    /**
     * default for 'styles' parameter.
     */
    public static List STYLES = Collections.EMPTY_LIST;

    /**
     * default for 'height' parameter.
     */
    public static int HEIGHT = 256;

    /**
     * default for 'height' parameter.
     */
    public static int WIDTH = 256;

    /**
     * default for 'srs' parameter.
     */
    public static String SRS = "EPSG:4326";

    /**
     * default for 'transparent' parameter.
     */
    public static Boolean TRANSPARENT = Boolean.TRUE;

    /**
     * default for 'bbox' paramter
     */
    public static ReferencedEnvelope BBOX = new ReferencedEnvelope(new Envelope(-180, 180, -90, 90),
            DefaultGeographicCRS.WGS84);

    /**
     * Application context
     */
    ApplicationContext context;

    public void setApplicationContext(ApplicationContext context)
        throws BeansException {
        this.context = context;
    }

    public WMSCapabilitiesResponse getCapabilities(WMSCapabilitiesRequest request) {
        Capabilities capabilities = (Capabilities) context.getBean("wmsGetCapabilities");

        return (WMSCapabilitiesResponse) capabilities.getResponse();
    }

    public WMSCapabilitiesResponse capabilities(WMSCapabilitiesRequest request) {
        return getCapabilities(request);
    }

    public DescribeLayerResponse describeLayer(DescribeLayerRequest request) {
        DescribeLayer describeLayer = (DescribeLayer) context.getBean("wmsDescribeLayer");

        return (DescribeLayerResponse) describeLayer.getResponse();
    }

    public GetMapResponse getMap(GetMapRequest request) {
        GetMap getMap = (GetMap) context.getBean("wmsGetMap");

        return (GetMapResponse) getMap.getResponse();
    }

    public GetMapResponse map(GetMapRequest request) {
        return getMap(request);
    }

    public GetFeatureInfoResponse getFeatureInfo(GetFeatureInfoRequest request) {
        GetFeatureInfo getFeatureInfo = (GetFeatureInfo) context.getBean("wmsGetFeatureInfo");

        return (GetFeatureInfoResponse) getFeatureInfo.getResponse();
    }

    public GetLegendGraphicResponse getLegendGraphic(GetLegendGraphicRequest request) {
        GetLegendGraphic getLegendGraphic = (GetLegendGraphic) context.getBean(
                "wmsGetLegendGraphic");

        return (GetLegendGraphicResponse) getLegendGraphic.getResponse();
    }

    //refector operations
    public GetMapResponse reflect(GetMapRequest request) {
        return getMapReflect(request);
    }

    public GetMapResponse getMapReflect(GetMapRequest request) {
        GetMapRequest getMap = (GetMapRequest) request;

        //set the defaults
        if (getMap.getFormat() == null) {
            getMap.setFormat(FORMAT);
        }

        if (getMap.getHeight() < 1) {
            getMap.setHeight(HEIGHT);
        }

        if (getMap.getWidth() < 1) {
            getMap.setWidth(WIDTH);
        }

        if ((getMap.getStyles() == null) || getMap.getStyles().isEmpty()) {
            //set styles to be the defaults for the specified layers
            //TODO: should this be part of core WMS logic? is so lets throw this
            // into the GetMapKvpRequestReader
            if ((getMap.getLayers() != null) && (getMap.getLayers().length > 0)) {
                ArrayList styles = new ArrayList(getMap.getLayers().length);

                for (int i = 0; i < getMap.getLayers().length; i++) {
                    styles.add(getMap.getLayers()[i].getDefaultStyle());
                }

                getMap.setStyles(styles);
            } else {
                getMap.setStyles(STYLES);
            }
        }

        if (getMap.getSRS() == null) {
            getMap.setSRS(SRS);
        }

        if (getMap.getBbox() == null) {
            getMap.setBbox(BBOX);
        }

        return getMap(getMap);
    }
}
