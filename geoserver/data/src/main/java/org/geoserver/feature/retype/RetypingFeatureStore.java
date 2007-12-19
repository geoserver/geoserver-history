/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.feature.retype;

import org.geoserver.feature.RetypingFeatureCollection;
import org.geoserver.feature.RetypingFeatureCollection.RetypingFeatureReader;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureCollection;
import org.opengis.filter.Filter;
import java.io.IOException;
import java.util.Set;


/**
 * Renaming wrapper for a {@link FeatureStore} instance, to be used along with {@link RetypingDataStore}
 */
public class RetypingFeatureStore extends RetypingFeatureSource implements FeatureStore {
    public RetypingFeatureStore(DataStore ds, FeatureStore wrapped, FeatureTypeMap typeMap) {
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

    public void modifyFeatures(AttributeType type, Object value, Filter filter)
        throws IOException {
        featureStore().modifyFeatures(type, value, filter);
    }

    public void removeFeatures(Filter filter) throws IOException {
        featureStore().removeFeatures(filter);
    }

    public void modifyFeatures(AttributeType[] type, Object[] value, Filter filter)
        throws IOException {
        featureStore().modifyFeatures(type, value, filter);
    }

    public void setFeatures(FeatureReader reader) throws IOException {
        featureStore()
            .setFeatures(new RetypingFeatureReader(reader, typeMap.getOriginalFeatureType()));
    }

    public Set addFeatures(FeatureCollection collection)
        throws IOException {
        return featureStore()
                   .addFeatures(new RetypingFeatureCollection(collection,
                typeMap.originalFeatureType));
    }
}
