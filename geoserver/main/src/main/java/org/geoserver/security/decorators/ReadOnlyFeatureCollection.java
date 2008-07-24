/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.util.Collection;
import java.util.Iterator;

import org.geoserver.security.SecureCatalogImpl;
import org.geotools.feature.FeatureCollection;
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
    boolean challenge;

    protected ReadOnlyFeatureCollection(FeatureCollection<T, F> delegate, boolean challenge) {
        super(delegate);
        this.challenge = challenge;
    }

    public Iterator iterator() {
        final Iterator<F> it = delegate.iterator();
        if (it == null)
            return null;
        else
            return new ReadOnlyIterator(it, challenge);
    }

    public FeatureCollection<T, F> sort(SortBy order) {
        final FeatureCollection<T, F> fc = delegate.sort(order);
        if(fc == null)
            return null;
        else
            return new ReadOnlyFeatureCollection<T, F>(fc, challenge);
    }

    public FeatureCollection<T, F> subCollection(Filter filter) {
        final FeatureCollection<T, F> fc = delegate.subCollection(filter);
        if(fc == null)
            return null;
        else
            return new ReadOnlyFeatureCollection<T, F>(fc, challenge);
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

    class ReadOnlyIterator implements Iterator {
        Iterator wrapped;
        boolean challenge;

        public ReadOnlyIterator(Iterator wrapped, boolean challenge) {
            this.wrapped = wrapped;
            this.challenge = challenge;
        }

        public boolean hasNext() {
            return wrapped.hasNext();
        }

        public Object next() {
            return wrapped.next();
        }

        public void remove() {
            throw unsupportedOperation();
        }

    }

    /**
     * Notifies the caller the requested operation is not supported, using a plain {@link UnsupportedOperationException}
     * in case we have to conceal the fact the data is actually writable, using an Acegi security exception otherwise
     * to force an authentication from the user
     */
    RuntimeException unsupportedOperation() {
        String typeName = getID(); 
        if(challenge) {
            return SecureCatalogImpl.unauthorizedAccess(typeName);
        } else
            return new UnsupportedOperationException("Feature type " + typeName + " is read only");
    }
}
