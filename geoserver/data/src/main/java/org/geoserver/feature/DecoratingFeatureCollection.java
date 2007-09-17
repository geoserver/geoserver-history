/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.feature;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureList;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.visitor.FeatureVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.ProgressListener;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;


/**
 * Base class for a feature collection with decorates another feature collection.
 * <p>
 * Subclasses should override methods as needed to "decorate" .
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class DecoratingFeatureCollection implements FeatureCollection {
    protected FeatureCollection delegate;

    public DecoratingFeatureCollection(FeatureCollection delegate) {
        this.delegate = delegate;
    }

    public FeatureIterator features() {
        return delegate.features();
    }

    public Iterator iterator() {
        return delegate.iterator();
    }

    public void purge() {
        delegate.purge();
    }

    public void close(FeatureIterator iterator) {
        delegate.close(iterator);
    }

    public void close(Iterator iterator) {
        delegate.close(iterator);
    }

    public void addListener(CollectionListener listenter)
        throws NullPointerException {
        delegate.addListener(listenter);
    }

    public void removeListener(CollectionListener listener)
        throws NullPointerException {
        delegate.removeListener(listener);
    }

    public FeatureType getFeatureType() {
        return delegate.getFeatureType();
    }

    public FeatureType getSchema() {
        return delegate.getSchema();
    }

    public void accepts(FeatureVisitor visitor, ProgressListener listener)
        throws IOException {
        delegate.accepts(visitor, listener);
    }

    public FeatureCollection subCollection(Filter filter) {
        return delegate.subCollection(filter);
    }

    public FeatureList sort(SortBy sortBy) {
        return delegate.sort(sortBy);
    }

    public int size() {
        return delegate.size();
    }

    public void clear() {
        delegate.clear();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public Object[] toArray() {
        return delegate.toArray();
    }

    public boolean add(Object o) {
        return delegate.add(o);
    }

    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    public boolean remove(Object o) {
        return delegate.remove(o);
    }

    public boolean addAll(Collection c) {
        return delegate.addAll(c);
    }

    public boolean containsAll(Collection c) {
        return delegate.containsAll(c);
    }

    public boolean removeAll(Collection c) {
        return delegate.removeAll(c);
    }

    public boolean retainAll(Collection c) {
        return delegate.retainAll(c);
    }

    public Object[] toArray(Object[] a) {
        return delegate.toArray(a);
    }

    public ReferencedEnvelope getBounds() {
        return delegate.getBounds();
    }

    public String getID() {
        return delegate.getID();
    }

    public Object[] getAttributes(Object[] array) {
        return delegate.getAttributes(array);
    }

    public Object getAttribute(String name) {
        return delegate.getAttribute(name);
    }

    public Object getAttribute(int index) {
        return delegate.getAttribute(index);
    }

    public void setAttribute(int index, Object value)
        throws IllegalAttributeException, ArrayIndexOutOfBoundsException {
        delegate.setAttribute(index, value);
    }

    public int getNumberOfAttributes() {
        return delegate.getNumberOfAttributes();
    }

    public void setAttribute(String name, Object value)
        throws IllegalAttributeException {
        delegate.setAttribute(name, value);
    }

    public Geometry getDefaultGeometry() {
        return delegate.getDefaultGeometry();
    }

    public void setDefaultGeometry(Geometry geometry) throws IllegalAttributeException {
        delegate.setDefaultGeometry(geometry);
    }
    
}
