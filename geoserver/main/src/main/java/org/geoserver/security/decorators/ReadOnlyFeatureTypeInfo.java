/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.io.IOException;

import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geotools.data.FeatureSource;
import org.geotools.factory.Hints;
import org.opengis.util.ProgressListener;

/**
 * Wraps a {@link FeatureTypeInfo} so that it will return a read only
 * FeatureSource
 * 
 * @author Andrea Aime - TOPP
 */
public class ReadOnlyFeatureTypeInfo extends DecoratingFeatureTypeInfo {

    public ReadOnlyFeatureTypeInfo(FeatureTypeInfo info) {
        super(info);
    }

    // -----------------------------------------------------------------------------
    // WRAPPED METHODS TO ENFORCE READ ONLY CONTRACT
    // -----------------------------------------------------------------------------

    public FeatureSource getFeatureSource(ProgressListener listener, Hints hints)
            throws IOException {
        final FeatureSource fs = delegate.getFeatureSource(listener, hints);
        if(fs == null)
            return null;
        else
            return new ReadOnlyFeatureSource(fs);
    }

    public DataStoreInfo getStore() {
        final DataStoreInfo store = delegate.getStore();
        if(store == null)
            return null;
        else
            return new ReadOnlyDataStoreInfo(store);
    }
}
