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
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * Given a {@link SimpleFeatureCollection} makes sure no write operations can be performed through
 * it
 * 
 * @author Josh Vote, CSIRO Earth Science and Resource Engineering
 */
public class ReadOnlySimpleFeatureCollection extends
        DecoratingSimpleFeatureCollection {

    WrapperPolicy policy;
    
    ReadOnlySimpleFeatureCollection(SimpleFeatureCollection delegate, WrapperPolicy policy) {
        super(delegate);
        this.policy = policy;
    }

    public Iterator iterator() {
        return (Iterator) SecuredObjects.secure(delegate.iterator(), policy);
    }
    
    @Override
    public SimpleFeatureIterator features() {
        return (SimpleFeatureIterator) SecuredObjects.secure(delegate.features(), policy);
    }
    
    @Override
    public void close(FeatureIterator<SimpleFeature> close) {
        if(close instanceof Wrapper && ((Wrapper) close).isWrapperFor(FeatureIterator.class))
            delegate.close(((Wrapper) close).unwrap(FeatureIterator.class));
        else
            delegate.close(close);
    }
    
    @Override
    public void close(Iterator<SimpleFeature> close) {
        if(close instanceof Wrapper && ((Wrapper) close).isWrapperFor(Iterator.class))
            delegate.close(((Wrapper) close).unwrap(Iterator.class));
        else
            delegate.close(close);
    }

    public SimpleFeatureCollection sort(SortBy order) {
        final SimpleFeatureCollection fc = delegate.sort(order);
        if(fc == null)
            return null;
        else
            return (SimpleFeatureCollection) SecuredObjects.secure(fc, policy);
    }

    public SimpleFeatureCollection subCollection(Filter filter) {
        final SimpleFeatureCollection fc = delegate.subCollection(filter);
        if(fc == null)
            return null;
        else
            return (SimpleFeatureCollection) SecuredObjects.secure(fc, policy);
    }

    // ---------------------------------------------------------------------
    // Unsupported methods
    // ---------------------------------------------------------------------

    public boolean add(SimpleFeature o) {
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
            return new UnsupportedOperationException("SimpleFeature type " + typeName + " is read only");
    }
}
