package org.geoserver.web.data.layergroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.web.wicket.GeoServerDataProvider;

public class LayerGroupEntryProvider extends GeoServerDataProvider<LayerGroupEntry> {

    public static Property<LayerGroupEntry> LAYER = 
        new PropertyPlaceholder<LayerGroupEntry>( "layer" );

    public static Property<LayerGroupEntry> STYLE = 
        new PropertyPlaceholder<LayerGroupEntry>( "style" );
    
    public static Property<LayerGroupEntry> REMOVE = 
        new PropertyPlaceholder<LayerGroupEntry>( "remove" );

    static List PROPERTIES = Arrays.asList( LAYER, STYLE, REMOVE );
    
    String lgid; 
    
    public LayerGroupEntryProvider( LayerGroupInfo layerGroup ) {
        this.lgid = layerGroup.getId();
    }
    
    @Override
    protected List<LayerGroupEntry> getItems() {
        LayerGroupInfo layerGroup = getCatalog().getLayerGroup(lgid);
       
        ArrayList<LayerGroupEntry> entries = new ArrayList();
        for ( int i = 0; i < layerGroup.getLayers().size(); i++ ) {
            LayerInfo layer = layerGroup.getLayers().get( i );
            StyleInfo style = layerGroup.getStyles().get( i );
            entries.add( new LayerGroupEntry( layer, style, i ) );
        }
        return entries;
    }

    @Override
    protected List<Property<LayerGroupEntry>> getProperties() {
        return PROPERTIES;
    }

    public IModel model(Object object) {
        return ((LayerGroupEntry)object).toDetachableModel();
    }

  
}
