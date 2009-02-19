package org.geoserver.web.data.table;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.web.GeoServerApplication;

public class LayerProvider extends SortableDataProvider {
    
    public Iterator iterator(int first, int count) {
        Catalog catalog = GeoServerApplication.get().getCatalog();
        List<LayerInfo> layers = catalog.getLayers();
        int last = first + count;
        if(last > layers.size()) 
            last = layers.size();
        return layers.subList(first, last).iterator();
    }

    public IModel model(Object object) {
        return new LayerInfoDetachableModel((LayerInfo) object);
    }

    public int size() {
        return getCatalog().getLayers().size();
    }

    private Catalog getCatalog() {
        return GeoServerApplication.get().getCatalog();
    }
    
    static class LayerInfoDetachableModel extends LoadableDetachableModel {
        String name;
        boolean selected;
        
        public LayerInfoDetachableModel(LayerInfo layer) {
            super(layer);
            this.name = layer.getName();
        }
        
        
        @Override
        protected Object load() {
            return GeoServerApplication.get().getCatalog().getLayerByName(name);
        }
    }

}
