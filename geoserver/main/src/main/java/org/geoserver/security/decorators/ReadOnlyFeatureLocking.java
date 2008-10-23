/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.io.IOException;

import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLocking;
import org.geotools.data.Query;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;

/**
 * See {@link ReadOnlyFeatureStore} for an explanation of why this class exists
 * 
 * @author Andrea Aime
 * 
 * @param <T>
 * @param <F>
 */
public class ReadOnlyFeatureLocking<T extends FeatureType, F extends Feature> extends
        ReadOnlyFeatureStore<T, F> implements FeatureLocking<T, F> {

    protected ReadOnlyFeatureLocking(FeatureLocking delegate, WrapperPolicy policy) {
        super(delegate, policy);
    }

    public int lockFeatures() throws IOException {
        throw unsupportedOperation();
    }

    public int lockFeatures(Query query) throws IOException {
        throw unsupportedOperation();
    }

    public int lockFeatures(Filter filter) throws IOException {
        throw unsupportedOperation();
    }

    public void setFeatureLock(FeatureLock lock) {
        throw unsupportedOperation();
    }

    public void unLockFeatures() throws IOException {
        throw unsupportedOperation();
    }

    public void unLockFeatures(Filter filter) throws IOException {
        throw unsupportedOperation();
    }

    public void unLockFeatures(Query query) throws IOException {
        throw unsupportedOperation();
    }

}
