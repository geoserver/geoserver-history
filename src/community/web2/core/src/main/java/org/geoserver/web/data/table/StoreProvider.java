/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.web.GeoServerApplication;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.support.PropertyComparator;

public class StoreProvider extends SortableDataProvider {

    public static final String TYPE = "type";

    public static final String WORKSPACE = "workspace";

    public static final String WORKSPACE_PROPERTY = "workspace.name";

    public static final String NAME = "name";

    public static final String NAME_PROPERTY = "name";

    public static final String ENABLED = "enabled";

    public static final String ENABLED_PROPERTY = "enabled";

    String[] keywords;

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public Iterator iterator(int first, int count) {
        List<StoreInfo> stores = getFilteredStores();

        // global sorting
        Comparator comparator = getComparator();
        if (comparator != null)
            Collections.sort(stores, comparator);

        // paging
        int last = first + count;
        if (last > stores.size())
            last = stores.size();
        return stores.subList(first, last).iterator();
    }

    private List<StoreInfo> getFilteredStores() {
        // grab list
        Catalog catalog = GeoServerApplication.get().getCatalog();
        List<StoreInfo> stores = new ArrayList<StoreInfo>();
        stores.addAll(catalog.getDataStores());
        stores.addAll(catalog.getCoverageStores());

        // if needed, filter
        if (keywords != null && keywords.length > 0) {
            stores = filterByKeywords(stores);
        }
        return stores;
    }

    private List<StoreInfo> filterByKeywords(List<StoreInfo> stores) {
        StringBuilder sb = new StringBuilder();
        List<StoreInfo> result = new ArrayList<StoreInfo>();

        for (StoreInfo layerInfo : stores) {
            // build a single string representation of all the properties we filter on
            BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(layerInfo);
            sb.setLength(0); // reset the builder
            sb.append(layerInfo instanceof DataStoreInfo ? "vector" : "raster").append(" ");
            sb.append(wrapper.getPropertyValue(NAME_PROPERTY)).append(" ");
            sb.append(wrapper.getPropertyValue(WORKSPACE_PROPERTY)).append(" ");
            sb.append(wrapper.getPropertyValue(ENABLED_PROPERTY)).append(" ");
            String description = sb.toString();

            // brute force check for keywords
            for (String keyword : keywords) {
                if (description.indexOf(keyword) >= 0) {
                    result.add(layerInfo);
                    break;
                }
            }
        }

        return result;
    }

    Comparator getComparator() {
        SortParam sort = getSort();
        if (sort == null || sort.getProperty() == null)
            return null;

        if (TYPE.equals(sort.getProperty())) {
            return new StoreTypeComparator(sort.isAscending());
        } else if (WORKSPACE.equals(sort.getProperty())) {
            return new PropertyComparator(WORKSPACE_PROPERTY, true, sort.isAscending());
        } else if (NAME.equals(sort.getProperty())) {
            return new PropertyComparator(NAME_PROPERTY, true, sort.isAscending());
        } else if (ENABLED.equals(sort.getProperty())) {
            return new PropertyComparator(ENABLED_PROPERTY, true, sort.isAscending());
        } else {
            return null;
        }
    }

    public IModel model(Object object) {
        return new StoreInfoDetachableModel((StoreInfo) object);
    }

    public int size() {
        return getFilteredStores().size();
    }

    private Catalog getCatalog() {
        return GeoServerApplication.get().getCatalog();
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

    class StoreTypeComparator implements Comparator<StoreInfo> {
        int order;

        StoreTypeComparator(boolean ascending) {
            order = ascending ? 1 : -1;
        }

        public int compare(StoreInfo s1, StoreInfo s2) {
            String type1 = s1 instanceof DataStoreInfo ? "vector" : "raster";
            String type2 = s2 instanceof DataStoreInfo ? "vector" : "raster";
            return type1.compareToIgnoreCase(type2) * order;
        }

    }

}
