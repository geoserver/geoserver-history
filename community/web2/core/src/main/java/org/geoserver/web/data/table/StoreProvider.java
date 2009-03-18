/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.table;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.wicket.GeoServerDataProvider;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;
import org.geoserver.web.wicket.GeoServerDataProvider.PropertyPlaceholder;

public class StoreProvider extends GeoServerDataProvider {
    
    static final Property<StoreInfo> TYPE = new Property<StoreInfo>() {

        public IModel getModel(final IModel itemModel) {
            return new Model(itemModel) {
                
                @Override
                public Object getObject() {
                    StoreInfo si = (StoreInfo) itemModel.getObject();
                    return getPropertyValue(si);
                }
            };
        }

        public String getName() {
            return "type";
        }

        public Object getPropertyValue(StoreInfo item) {
            if (item instanceof DataStoreInfo)
                return "vector";
            else
                return "raster";
        }
        
        public Comparator<StoreInfo> getComparator() {
            return new PropertyComparator<StoreInfo>(this);
        }
        
    };

    static final Property<StoreInfo> WORKSPACE = new BeanProperty<StoreInfo>(
            "workspace", "workspace.name");

    static final Property<StoreInfo> NAME = new BeanProperty<StoreInfo>("name",
            "name");

    static final Property<StoreInfo> ENABLED = new BeanProperty<StoreInfo>(
            "enabled", "enabled");
    
    static final Property<StoreInfo> REMOVE = new PropertyPlaceholder<StoreInfo>("remove");

    static final List<Property<StoreInfo>> PROPERTIES = Arrays.asList(TYPE,
            WORKSPACE, NAME, ENABLED, REMOVE);

    WorkspaceInfo workspace;
    
    public StoreProvider() {
        this(null);
    }
    
    public StoreProvider(WorkspaceInfo workspace) {
        this.workspace = workspace;
    }
    
    @Override
    protected List<StoreInfo> getItems() {
        return workspace == null ? getCatalog().getStores(StoreInfo.class) 
            : getCatalog().getStoresByWorkspace( workspace, StoreInfo.class );
    }

    @Override
    protected List<Property<StoreInfo>> getProperties() {
        return PROPERTIES;
    }

    @Override
    protected Comparator<StoreInfo> getComparator(SortParam sort) {
        return super.getComparator(sort);
    }
    

    public IModel model(Object object) {
        return new StoreInfoDetachableModel((StoreInfo) object);
    }


    static class StoreInfoDetachableModel extends LoadableDetachableModel {
        String name;

        boolean selected;

        Class clazz;

        public StoreInfoDetachableModel(StoreInfo store) {
            super(store);
            this.name = store.getName();
            this.clazz = store.getClass();
        }

        @Override
        protected Object load() {
            return GeoServerApplication.get().getCatalog().getStoreByName(name, StoreInfo.class);
        }
    }
}
