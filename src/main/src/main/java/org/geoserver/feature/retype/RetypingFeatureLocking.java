/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.feature.retype;

import java.io.IOException;

import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLocking;
import org.geotools.data.Query;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * Renaming wrapper for a {@link FeatureLocking} instance, to be used along with {@link RetypingDataStore} 
 */
class RetypingFeatureLocking extends RetypingFeatureStore implements
        FeatureLocking<SimpleFeatureType, SimpleFeature> {

    public RetypingFeatureLocking(RetypingDataStore ds,
            FeatureLocking<SimpleFeatureType, SimpleFeature> wrapped, FeatureTypeMap typeMap) {
        super(ds, wrapped, typeMap);
    }
    
    FeatureLocking<SimpleFeatureType, SimpleFeature> featureLocking() {
        return (FeatureLocking<SimpleFeatureType, SimpleFeature>) wrapped;
    }

    public int lockFeatures() throws IOException {
        return featureLocking().lockFeatures();
    }

    public int lockFeatures(Query query) throws IOException {
        return featureLocking().lockFeatures(store.retypeQuery(query, typeMap));
    }

    public int lockFeatures(Filter filter) throws IOException {
        return featureLocking().lockFeatures(store.retypeFilter(filter, typeMap));
    }

    public void setFeatureLock(FeatureLock lock) {
        featureLocking().setFeatureLock(lock);
    }

    public void unLockFeatures() throws IOException {
        featureLocking().unLockFeatures();
    }

    public void unLockFeatures(Filter filter) throws IOException {
        featureLocking().unLockFeatures(store.retypeFilter(filter, typeMap));
    }

    public void unLockFeatures(Query query) throws IOException {
        featureLocking().unLockFeatures(store.retypeQuery(query, typeMap));
    }

}
