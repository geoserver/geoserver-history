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
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * Given a {@link SimpleFeatureSource} makes sure no write operations can be performed through it or
 * using a object that can be accessed thru it. Depending on the challenge policy, the object and
 * the related ones will simply act as read only, or will throw Spring security exceptions
 * 
 * @author Josh Vote, CSIRO Earth Science and Resource Engineering
 */
public class ReadOnlySimpleFeatureSource extends
        DecoratingSimpleFeatureSource {

    WrapperPolicy policy;

    protected ReadOnlySimpleFeatureSource(SimpleFeatureSource delegate, WrapperPolicy policy) {
        super(delegate);
        this.policy = policy;
    }

    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        final DataAccess<SimpleFeatureType, SimpleFeature> store = delegate.getDataStore();
        if (store == null)
            return null;
        else 
            return (DataAccess) SecuredObjects.secure(store, policy);
    }

    public SimpleFeatureCollection getFeatures() throws IOException {
        final SimpleFeatureCollection fc = delegate.getFeatures();
        if (fc == null)
            return null;
        else
            return (SimpleFeatureCollection) SecuredObjects.secure(fc, policy);
    }

    public SimpleFeatureCollection getFeatures(Filter filter)
            throws IOException {
        final SimpleFeatureCollection fc = delegate.getFeatures(filter);
        if (fc == null)
            return null;
        else
            return (SimpleFeatureCollection) SecuredObjects.secure(fc, policy);
    }

    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        final SimpleFeatureCollection fc = delegate.getFeatures(query);
        if (fc == null)
            return null;
        else
            return (SimpleFeatureCollection) SecuredObjects.secure(fc, policy);
    }
}
