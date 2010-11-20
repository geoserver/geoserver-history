/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.util.Collection;
import java.util.Iterator;

import org.geoserver.catalog.Wrapper;
import org.geoserver.security.SecureCatalogImpl;
import org.geoserver.security.SecureCatalogImpl.Response;
import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.DecoratingFeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * Given a {@link FeatureCollection} makes sure no write operations can be
 * performed through it
 * 
 * @author Andrea Aime - TOPP
 * 
 * @param <T>
 * @param <F>
 */
public class ReadOnlyFeatureCollection<T extends FeatureType, F extends Feature> extends
        DecoratingFeatureCollection<T, F> {
    WrapperPolicy policy;

    ReadOnlyFeatureCollection(FeatureCollection<T, F> delegate, WrapperPolicy policy) {
        super(delegate);
        this.policy = policy;
    }

    public Iterator iterator() {
        return (Iterator) SecuredObjects.secure(delegate.iterator(), policy);
    }
    
    @Override
    public org.geotools.feature.FeatureIterator<F> features() {
        return (FeatureIterator) SecuredObjects.secure(delegate.features(), policy);
    }

    public FeatureCollection<T, F> sort(SortBy order) {
        final FeatureCollection<T, F> fc = delegate.sort(order);
        if(fc == null)
            return null;
        else
            return (FeatureCollection) SecuredObjects.secure(fc, policy);
    }

    public FeatureCollection<T, F> subCollection(Filter filter) {
        final FeatureCollection<T, F> fc = delegate.subCollection(filter);
        if(fc == null)
            return null;
        else
            return (FeatureCollection) SecuredObjects.secure(fc, policy);
    }
    
    @Override
    public void close(FeatureIterator<F> close) {
        if(close instanceof Wrapper && ((Wrapper) close).isWrapperFor(FeatureIterator.class))
            delegate.close(((Wrapper) close).unwrap(FeatureIterator.class));
        else
            delegate.close(close);
    }
    
    @Override
    public void close(Iterator<F> close) {
        if(close instanceof Wrapper && ((Wrapper) close).isWrapperFor(Iterator.class))
            delegate.close(((Wrapper) close).unwrap(Iterator.class));
        else
            delegate.close(close);
    }

    // ---------------------------------------------------------------------
    // Unsupported methods
    // ---------------------------------------------------------------------

    public boolean add(F o) {
        throw unsupportedOperation();
    }

    public boolean addAll(Collection c) {
        throw unsupportedOperation();
    }

    public void clear() {
        throw unsupportedOperation();
    }

    public boolean remove(Object o) {
        throw unsupportedOperation();
    }

    public boolean removeAll(Collection c) {
        throw unsupportedOperation();
    }

    public boolean retainAll(Collection c) {
        throw unsupportedOperation();
    }

    /**
     * Notifies the caller the requested operation is not supported, using a plain {@link UnsupportedOperationException}
     * in case we have to conceal the fact the data is actually writable, using an Spring security exception otherwise
     * to force an authentication from the user
     */
    RuntimeException unsupportedOperation() {
        String typeName = getID();
        if(policy.response == Response.CHALLENGE) {
            return SecureCatalogImpl.unauthorizedAccess(typeName);
        } else
            return new UnsupportedOperationException("Feature type " + typeName + " is read only");
    }
}
