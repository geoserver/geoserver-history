/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.io.IOException;

import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;
import org.geotools.data.DataAccess;
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
 * only, or will throw Spring security exceptions
 * 
 * @author Andrea Aime - TOPP
 * 
 * @param <T>
 * @param <F>
 */
public class ReadOnlyFeatureSource<T extends FeatureType, F extends Feature>
        extends DecoratingFeatureSource<T, F> {

    WrapperPolicy policy;

    protected ReadOnlyFeatureSource(FeatureSource<T, F> delegate, WrapperPolicy policy) {
        super(delegate);
        this.policy = policy;
    }

    public DataAccess<T, F> getDataStore() {
        final DataAccess<T, F> store = delegate.getDataStore();
        if (store == null)
            return null;
        else 
            return (DataAccess) SecuredObjects.secure(store, policy);
    }

    public FeatureCollection<T, F> getFeatures() throws IOException {
        final FeatureCollection<T, F> fc = delegate.getFeatures();
        if (fc == null)
            return null;
        else
            return (FeatureCollection) SecuredObjects.secure(fc, policy);
    }

    public FeatureCollection<T, F> getFeatures(Filter filter)
            throws IOException {
        final FeatureCollection<T, F> fc = delegate.getFeatures(filter);
        if (fc == null)
            return null;
        else
            return (FeatureCollection) SecuredObjects.secure(fc, policy);
    }

    public FeatureCollection<T, F> getFeatures(Query query) throws IOException {
        final FeatureCollection<T, F> fc = delegate.getFeatures(query);
        if (fc == null)
            return null;
        else
            return (FeatureCollection) SecuredObjects.secure(fc, policy);
    }
}
