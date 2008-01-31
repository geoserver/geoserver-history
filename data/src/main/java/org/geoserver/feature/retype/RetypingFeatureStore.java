/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.feature.retype;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.geoserver.feature.RetypingFeatureCollection;
import org.geoserver.feature.RetypingFeatureCollection.RetypingFeatureReader;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;

/**
 * Renaming wrapper for a {@link FeatureStore} instance, to be used along with {@link RetypingDataStore} 
 */
public class RetypingFeatureStore extends RetypingFeatureSource implements FeatureStore {

    public RetypingFeatureStore(RetypingDataStore ds, FeatureStore wrapped, FeatureTypeMap typeMap) {
        super(ds, wrapped, typeMap);
    }

    protected FeatureStore featureStore() {
        return (FeatureStore) wrapped;
    }

    public Transaction getTransaction() {
        return featureStore().getTransaction();
    }

    public void setTransaction(Transaction transaction) {
        featureStore().setTransaction(transaction);
    }

    public void modifyFeatures(AttributeDescriptor type, Object value, Filter filter) throws IOException {
        featureStore().modifyFeatures(type, value, store.retypeFilter(filter, typeMap));
    }

    public void removeFeatures(Filter filter) throws IOException {
        featureStore().removeFeatures(store.retypeFilter(filter, typeMap));
    }

    public void modifyFeatures(AttributeDescriptor[] type, Object[] value, Filter filter)
            throws IOException {
        featureStore().modifyFeatures(type, value, store.retypeFilter(filter, typeMap));
    }

    public void setFeatures(FeatureReader reader) throws IOException {
        featureStore().setFeatures(
                new RetypingFeatureReader(reader, typeMap.getOriginalFeatureType()));
    }

    public Set addFeatures(FeatureCollection collection) throws IOException {
        Set<String> ids = featureStore().addFeatures(
                new RetypingFeatureCollection(collection, typeMap.getOriginalFeatureType()));
        Set<String> retyped = new HashSet<String>();
        for (String id : ids) {
            retyped.add(RetypingFeatureCollection.reTypeId(id, typeMap.getOriginalFeatureType(), typeMap.getFeatureType()));
        }
        return retyped;
    }

}
