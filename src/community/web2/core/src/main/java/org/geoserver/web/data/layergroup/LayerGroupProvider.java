package org.geoserver.web.data.layergroup;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.web.wicket.GeoServerDataProvider;

public class LayerGroupProvider extends GeoServerDataProvider<LayerGroupInfo> {

    public static Property<LayerGroupInfo> NAME = 
        new BeanProperty<LayerGroupInfo>( "name", "name" );

    public static Property<LayerGroupInfo> REMOVE = 
        new PropertyPlaceholder<LayerGroupInfo>( "remove" );

    static List PROPERTIES = Arrays.asList( NAME, REMOVE );
    
    @Override
    protected List<LayerGroupInfo> getItems() {
        return getCatalog().getLayerGroups();
    }

    @Override
    protected List<Property<LayerGroupInfo>> getProperties() {
        return PROPERTIES;
    }

    public IModel model(Object object) {
        return new LayerGroupDetachableModel( (LayerGroupInfo) object );
    }

}
