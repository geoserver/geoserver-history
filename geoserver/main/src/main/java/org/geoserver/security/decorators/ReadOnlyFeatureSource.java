/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.io.IOException;

import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;

/**
 * Given a {@link FeatureSource} makes sure no write operations can be performed
 * through it or using a object that can be accessed thru it. Depending on the
 * challenge policy, the object and the related ones will simply act as read
 * only, or will throw Acegi security exceptions
 * 
 * @author Andrea Aime - TOPP
 * 
 * @param <T>
 * @param <F>
 */
public class ReadOnlyFeatureSource<T extends FeatureType, F extends Feature>
        extends DecoratingFeatureSource<T, F> {

    boolean challenge;

    public ReadOnlyFeatureSource(FeatureSource<T, F> delegate, boolean challenge) {
        super(delegate);
        this.challenge = challenge;
    }

    public DataAccess<T, F> getDataStore() {
        final DataAccess<T, F> store = delegate.getDataStore();
        if (store == null)
            return null;
        else if (store instanceof DataStore)
            return (DataAccess) new ReadOnlyDataStore((DataStore) store,
                    challenge);
        else
            return new ReadOnlyDataAccess(store, challenge);
    }

    public FeatureCollection<T, F> getFeatures() throws IOException {
        final FeatureCollection<T, F> fc = delegate.getFeatures();
        if (fc == null)
            return null;
        else
            return new ReadOnlyFeatureCollection<T, F>(fc, challenge);
    }

    public FeatureCollection<T, F> getFeatures(Filter filter)
            throws IOException {
        final FeatureCollection<T, F> fc = delegate.getFeatures(filter);
        if (fc == null)
            return null;
        else
            return new ReadOnlyFeatureCollection<T, F>(fc, challenge);
    }

    public FeatureCollection<T, F> getFeatures(Query query) throws IOException {
        final FeatureCollection<T, F> fc = delegate.getFeatures(query);
        if (fc == null)
            return null;
        else
            return new ReadOnlyFeatureCollection<T, F>(fc, challenge);
    }
}
