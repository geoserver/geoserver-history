
package org.geoserver.web.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.LayerInfo.Type;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.wms.DefaultWebMapService;
import org.geoserver.wms.MapLayerInfo;
import org.geoserver.wms.WMS;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.wms.requests.GetMapRequest;

import com.vividsolutions.jts.geom.Envelope;

public class PreviewLayer {
    static final Logger LOGGER = Logging.getLogger(PreviewLayer.class);
    enum PreviewLayerType {Raster, Vector, Remote, Group};
    LayerInfo layerInfo;
    LayerGroupInfo groupInfo;
    transient GetMapRequest request;
    
    
    public PreviewLayer(LayerInfo layerInfo) {
        this.layerInfo = layerInfo;
    }
    
    public PreviewLayer(LayerGroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }
    
    public String getName() {
        if(layerInfo != null) {
            return layerInfo.getName();
        } else {
            return groupInfo.getName();
        }
    }
    
    public PreviewLayer.PreviewLayerType getType() {
        if(layerInfo != null) {
            if(layerInfo.getType() == Type.RASTER)
                return PreviewLayerType.Raster;
            else if(layerInfo.getType() == Type.VECTOR)
                return PreviewLayerType.Vector;
            else 
                return PreviewLayerType.Remote;
        } else {
            return PreviewLayerType.Group;
        }
    }
    
    /**
     * Builds a fake GetMap request
     * 
     * @param prefixedName
     * @return
     */
    GetMapRequest getRequest() {
        if(request == null) {
            GeoServerApplication app = GeoServerApplication.get();
            request = new GetMapRequest(new WMS(app.getGeoServer()));
            Catalog catalog = app.getCatalog();
            List<MapLayerInfo> layers = expandLayers(catalog);
            request.setLayers(layers.toArray(new MapLayerInfo[layers.size()]));
            request.setFormat("application/openlayers");
            try {
                DefaultWebMapService.autoSetBoundsAndSize(request);
            } catch(Exception e) {
                LOGGER.log(Level.INFO, "Could not set figure out automatically a good preview link for " + getName(), e);
            }
        }
        return request;
    }

    /**
     * Expands the specified name into a list of layer info names
     * 
     * @param name
     * @param catalog
     * @return
     */
    private List<MapLayerInfo> expandLayers(Catalog catalog) {
        List<MapLayerInfo> layers = new ArrayList<MapLayerInfo>();
        
        if ( layerInfo != null ) {
            layers.add( new MapLayerInfo( layerInfo ) );
        }  else {
            for ( LayerInfo l : groupInfo.getLayers() ) {
                layers.add( new MapLayerInfo( l ) );
            }
        }
        return layers;
    }

    
    /**
     * Given a request and a target format, builds the WMS request
     * 
     * @param request
     * @param string
     * @return
     */
    protected String getWmsLink() {
        GetMapRequest request = getRequest();
        final Envelope bbox = request.getBbox();
        if (bbox == null)
            return null;
        
        return "../wms?service=WMS&version=1.1.0&request=GetMap" //
                + "&layers=" + getName() //
                + "&styles=" //
                + "&bbox=" + bbox.getMinX() + "," + bbox.getMinY() //
                + "," + bbox.getMaxX() + "," + bbox.getMaxY() //
                + "&width=" + request.getWidth() //
                + "&height=" + request.getHeight() + "&srs=" + request.getSRS();
    }
}