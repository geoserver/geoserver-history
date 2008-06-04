package org.geoserver.security.decorators;

import java.util.Collection;
import java.util.Iterator;

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

    private static final String RO = "This is a read only feature collection";

    protected ReadOnlyFeatureCollection(FeatureCollection<T, F> delegate) {
        super(delegate);
    }

    public Iterator iterator() {
        return new ReadOnlyIterator(delegate.iterator());
    }

    public FeatureCollection<T, F> sort(SortBy order) {
        return new ReadOnlyFeatureCollection<T, F>(delegate.sort(order));
    }

    public FeatureCollection<T, F> subCollection(Filter filter) {
        return new ReadOnlyFeatureCollection<T, F>(delegate.subCollection(filter));
    }

    // ---------------------------------------------------------------------
    // Unsupported methods
    // ---------------------------------------------------------------------

    public boolean add(F o) {
        throw new UnsupportedOperationException(RO);
    }

    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException(RO);
    }

    public void clear() {
        throw new UnsupportedOperationException(RO);
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException(RO);
    }

    public boolean removeAll(Collection c) {
        throw new UnsupportedOperationException(RO);
    }

    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException(RO);
    }

    class ReadOnlyIterator implements Iterator {
        Iterator wrapped;

        public ReadOnlyIterator(Iterator wrapped) {
            this.wrapped = wrapped;
        }

        public boolean hasNext() {
            return wrapped.hasNext();
        }

        public Object next() {
            return wrapped.next();
        }

        public void remove() {
            throw new UnsupportedOperationException("Write access is not allowed");
        }

    }

}
