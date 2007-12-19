/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.feature.retype;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLocking;
import org.geotools.data.Query;
import org.opengis.filter.Filter;
import java.io.IOException;


/**
 * Renaming wrapper for a {@link FeatureLocking} instance, to be used along with {@link RetypingDataStore}
 */
class RetypingFeatureLocking extends RetypingFeatureStore implements FeatureLocking {
    public RetypingFeatureLocking(DataStore ds, FeatureLocking wrapped, FeatureTypeMap typeMap) {
        super(ds, wrapped, typeMap);
    }

    FeatureLocking featureLocking() {
        return (FeatureLocking) wrapped;
    }

    public int lockFeatures() throws IOException {
        return featureLocking().lockFeatures();
    }

    public int lockFeatures(Query query) throws IOException {
        return featureLocking().lockFeatures(retypeQuery(query));
    }

    public int lockFeatures(Filter filter) throws IOException {
        return featureLocking().lockFeatures(filter);
    }

    public void setFeatureLock(FeatureLock lock) {
        featureLocking().setFeatureLock(lock);
    }

    public void unLockFeatures() throws IOException {
        featureLocking().unLockFeatures();
    }

    public void unLockFeatures(Filter filter) throws IOException {
        featureLocking().unLockFeatures(filter);
    }

    public void unLockFeatures(Query query) throws IOException {
        featureLocking().unLockFeatures(retypeQuery(query));
    }
}
