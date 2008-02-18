/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import com.vividsolutions.jts.geom.Envelope;
import org.geoserver.test.GeoServerTestSupport;
import org.geotools.data.DataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.servlets.GetMap;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.namespace.QName;


/**
 * Base support class for wms tests.
 * <p>
 * Deriving from this test class provides the test case with preconfigured
 * geoserver and wms objects.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class WMSTestSupport extends GeoServerTestSupport {
    /**
     * @return The global wms singleton from the application context.
     */
    protected WMS getWMS() {
        return (WMS) applicationContext.getBean("wms");
    }

    /**
     * Convenience method for subclasses to create a map layer from a layer name.
     * <p>
     * The map layer is created with the default style for the layer.
     * </p>
     * @param layerName The name of the layer.
     *
     * @return A new map layer.
     */
    protected MapLayer createMapLayer(QName layerName)
        throws IOException {
        //TODO: support coverages
        FeatureTypeInfo info = getCatalog().getFeatureTypeInfo(layerName);
        Style style = info.getDefaultStyle();

        DefaultMapLayer layer = new DefaultMapLayer(info.getFeatureSource(), style);
        layer.setTitle( info.getTypeName() );
        
        return layer;
    }

    /**
     * Calls through to {@link #createGetMapRequest(QName[])}.
     *
     */
    protected GetMapRequest createGetMapRequest(QName layerName) {
        return createGetMapRequest(new QName[] { layerName });
    }

    /**
     * Convenience method for subclasses to create a new GetMapRequest object.
     * <p>
     * The returned object has the following properties:
     *  <ul>
     *    <li>styles set to default styles for layers specified
     *    <li>bbox set to (-180,-90,180,180 )
     *    <li>crs set to epsg:4326
     *  </ul>
     *  Caller must set additional parameters of request as need be.
     * </p>
     *
     * @param The layer names of the request.
     *
     * @return A new GetMapRequest object.
     */
    protected GetMapRequest createGetMapRequest(QName[] layerNames) {
        GetMapRequest request = new GetMapRequest(new GetMap(getWMS()));
        request.setHttpServletRequest(createRequest("wms"));

        MapLayerInfo[] layers = new MapLayerInfo[layerNames.length];
        List styles = new ArrayList();

        for (int i = 0; i < layerNames.length; i++) {
            FeatureTypeInfo ftInfo = getCatalog().getFeatureTypeInfo(layerNames[i]);
            styles.add(ftInfo.getDefaultStyle());

            layers[i] = new MapLayerInfo(ftInfo);
        }

        request.setLayers(layers);
        request.setStyles(styles);
        request.setBbox(new Envelope(-180, -90, 180, 90));
        request.setCrs(DefaultGeographicCRS.WGS84);
        request.setSRS("EPSG:4326");
        request.setRawKvp(new HashMap());
        return request;
    }
    
    
    
}
