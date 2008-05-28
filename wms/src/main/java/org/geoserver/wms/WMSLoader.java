package org.geoserver.wms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogFactory;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.util.LegacyServicesReader;
import org.geoserver.config.util.ServiceLoader;
import org.geoserver.wms.WatermarkInfo.Position;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;

public class WMSLoader extends ServiceLoader {

    static Logger LOGGER = Logging.getLogger( "org.geoserver.wms" );
    
    public ServiceInfo load(LegacyServicesReader reader, GeoServer geoServer)
            throws Exception {
        WMSInfo wms = new WMSInfoImpl();
        
        Map<String,Object> props = reader.wms();
        load( wms, props, geoServer );
        
        WatermarkInfo wm = new WatermarkInfoImpl();
        wm.setEnabled( (Boolean) props.get( "globalWatermarking" ) );
        wm.setURL( (String) props.get("globalWatermarkingURL" ) );
        wm.setTransparency( (Integer) props.get("globalWatermarkingTransparency") );
        wm.setPosition( Position.get( (Integer) props.get( "globalWatermarkingPosition" ) ) );
        wms.setWatermark( wm );
            
        wms.setInterpolation( (String) props.get( "allowInterpolation" ) );
        wms.getMetadata().put( "svgRenderer", (Serializable) props.get( "svgRenderer") );
        wms.getMetadata().put( "svgAntiAlias",(Serializable) props.get( "svgAntiAlias") );
        
        //base maps
        Catalog catalog = geoServer.getCatalog();
        CatalogFactory factory = catalog.getFactory();
        
        List<Map> baseMaps = (List<Map>) props.get( "BaseMapGroups");
        if ( baseMaps != null ) {
         O:  for ( Map baseMap : baseMaps ) {
                LayerGroupInfo bm = factory.createLayerGroup();
                bm.setName( (String) baseMap.get( "baseMapTitle" ) );
                
                //process base map layers
                List<String> layerNames = (List) baseMap.get( "baseMapLayers");
                for ( String layerName : layerNames ) {
                    ResourceInfo resource = null;
                    if ( layerName.contains( ":" ) ) {
                        String[] qname = layerName.split( ":" );
                        resource = catalog.getResourceByName( qname[0],qname[1], ResourceInfo.class );
                    }
                    else {
                        resource = catalog.getResourceByName( layerName, ResourceInfo.class );
                    }
                   
                    if ( resource == null ) {
                        LOGGER.warning("Ignoring layer group '" + bm.getName() + 
                            "', resource '"+ layerName + "' does not exist" );
                        continue O;
                    }
                
                    List<LayerInfo> layers = catalog.getLayers(resource);
                    if ( layers.isEmpty() ) {
                        LOGGER.warning( "Ignoring layer group '" + bm.getName() + 
                            "', no layer found for resource '" + layerName + "'");
                        continue O;
                    }
                    
                    bm.getLayers().add( layers.get( 0 ) );
                }
                
                //process base map styles
                List<String> styleNames = (List) baseMap.get( "baseMapStyles" );
                if ( styleNames.isEmpty() ) {
                    //use defaults
                    for ( LayerInfo l : bm.getLayers() ) {
                        bm.getStyles().add( l.getDefaultStyle() );
                    }
                }
                else {
                    for ( String styleName : styleNames ) {
                        StyleInfo style = catalog.getStyleByName( styleName );
                        if ( style == null ) {
                            LOGGER.warning( "Ignoring layer group '" + bm.getName() + 
                                    "', style '" + styleName + "' does not exist.");
                            continue O;
                        }
                        bm.getStyles().add( style );
                    }    
                }
                
                
                //base map enveloper
                ReferencedEnvelope e = (ReferencedEnvelope) baseMap.get( "baseMapEnvelope");
                if ( e == null ) {
                    e = new ReferencedEnvelope();
                    e.setToNull();
                }
                bm.setBounds( e );
                
                LOGGER.info( "Processed layer group '" + bm.getName() + "'" );
                catalog.add( bm );
            }
        }
        
        return wms;
    }

}
