/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.web.wicket.GeoServerDataProvider;

/**
 * Provides a filtered, sorted view over the catalog layers.
 * 
 * @author Andrea Aime - OpenGeo
 */
@SuppressWarnings("serial")
public class PreviewLayerProvider extends GeoServerDataProvider<PreviewLayer> {
    static final Property<PreviewLayer> TYPE = new BeanProperty<PreviewLayer>(
            "type", "type");

    static final Property<PreviewLayer> WORKSPACE = new Property<PreviewLayer>() {

        public Comparator<PreviewLayer> getComparator() {
            return new PropertyComparator<PreviewLayer>(this);
        }

        public IModel getModel(IModel itemModel) {
            PreviewLayer pl = (PreviewLayer) itemModel.getObject();
            if(pl.layerInfo != null) {
                return new PropertyModel(itemModel, "layerInfo.resource.store.workspace.name");
            } else {
                return new Model(null);
            }
        }

        public String getName() {
            return "workspace";
        }

        public Object getPropertyValue(PreviewLayer item) {
            if(item.layerInfo != null) {
                return item.layerInfo.getResource().getStore().getWorkspace().getName();
            } else {
                return null;
            }
        }
        
    };
            
    static final Property<PreviewLayer> NAME = new BeanProperty<PreviewLayer>(
            "name", "name");

    static final Property<PreviewLayer> COMMON = new PropertyPlaceholder<PreviewLayer>(
            "Common formats");

    static final Property<PreviewLayer> ALL = new PropertyPlaceholder<PreviewLayer>(
            "All formats");

    static final List<Property<PreviewLayer>> PROPERTIES = Arrays.asList(TYPE,
            WORKSPACE, NAME, COMMON, ALL);

    @Override
    protected List<PreviewLayer> getItems() {
        List<PreviewLayer> result = new ArrayList<PreviewLayer>();

        for (LayerInfo layer : getCatalog().getLayers()) {
            if (layer.isEnabled())
                result.add(new PreviewLayer(layer));
        }

        for (LayerGroupInfo group : getCatalog().getLayerGroups()) {
            boolean enabled = true;
            for (LayerInfo layer : group.getLayers()) {
                enabled &= layer.isEnabled();
            }
            if (enabled)
                result.add(new PreviewLayer(group));
        }

        return result;
    }

    @Override
    protected List<Property<PreviewLayer>> getProperties() {
        return PROPERTIES;
    }

    public IModel model(Object object) {
        return new PreviewLayerModel((PreviewLayer) object);
    }
    
}
