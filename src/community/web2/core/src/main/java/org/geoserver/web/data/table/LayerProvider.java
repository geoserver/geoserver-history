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
import org.geoserver.catalog.LayerInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.wicket.GSDataProvider;

/**
 * Provides a filtered, sorted view over the catalog layers.
 * @author Andrea Aime - OpenGeo
 */
public class LayerProvider extends GSDataProvider<LayerInfo> {
    static final Property<LayerInfo> TYPE = new BeanProperty<LayerInfo>("type",
            "type");

    static final Property<LayerInfo> WORKSPACE = new BeanProperty<LayerInfo>(
            "workspace", "resource.store.workspace.name");

    static final Property<LayerInfo> STORE = new BeanProperty<LayerInfo>(
            "store", "resource.store.name");

    static final Property<LayerInfo> NAME = new BeanProperty<LayerInfo>("name",
            "name");

    static final Property<LayerInfo> ENABLED = new BeanProperty<LayerInfo>(
            "enabled", "enabled");

    static final Property<LayerInfo> SRS = new BeanProperty<LayerInfo>("SRS",
            "resource.SRS") {

        /**
         * We roll a custom comparator that treats the numeric part of the
         * code as a number
         */
        public java.util.Comparator<LayerInfo> getComparator() {
            return new Comparator<LayerInfo>() {

                public int compare(LayerInfo o1, LayerInfo o2) {
                    // split out authority and code
                    String[] srs1 = o1.getResource().getSRS().split(":");
                    String[] srs2 = o2.getResource().getSRS().split(":");

                    // use sign to control sort order
                    if (srs1[0].equalsIgnoreCase(srs2[0]) && srs1.length > 1
                            && srs2.length > 1) {
                        try {
                            // in case of same authority, compare numbers
                            return new Integer(srs1[1]).compareTo(new Integer(
                                    srs2[1]));
                        } catch(NumberFormatException e) {
                            // a handful of codes are not numeric,
                            // handle the general case as well
                            return srs1[1].compareTo(srs2[1]);
                        }
                    } else {
                        // compare authorities
                        return srs1[0].compareToIgnoreCase(srs2[0]);
                    }
                }

            };

        }
    };
    
    static final Property<LayerInfo> REMOVE = new PropertyPlaceholder<LayerInfo>(
            "remove");

    static final List<Property<LayerInfo>> PROPERTIES = Arrays.asList(TYPE,
            WORKSPACE, STORE, NAME, ENABLED, SRS, REMOVE);

    @Override
    protected List<LayerInfo> getItems() {
        return getCatalog().getLayers();
    }

    @Override
    protected List<Property<LayerInfo>> getProperties() {
        return PROPERTIES;
    }

    public IModel model(Object object) {
        return new LayerInfoDetachableModel((LayerInfo) object);
    }

    @Override
    protected Comparator<LayerInfo> getComparator(SortParam sort) {
        return super.getComparator(sort);
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
