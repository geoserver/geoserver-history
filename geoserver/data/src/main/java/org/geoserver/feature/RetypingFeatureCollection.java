/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.feature;

import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;

import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.collection.DelegateFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * FeatureCollection with "casts" features from on feature type to another.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class RetypingFeatureCollection extends DecoratingFeatureCollection {
   SimpleFeatureType target;

    public RetypingFeatureCollection(FeatureCollection delegate, SimpleFeatureType target) {
        super(delegate);
        this.target = target;
    }

    public SimpleFeatureType getSchema() {
        return target;
    }

    public Iterator iterator() {
        return new RetypingIterator(delegate.iterator(), target);
    }

    public void close(Iterator iterator) {
        RetypingIterator retyping = (RetypingIterator) iterator;
        delegate.close(retyping.delegate);
    }

    public FeatureIterator features() {
        return new DelegateFeatureIterator(this, iterator());
    }

    public void close(FeatureIterator iterator) {
        DelegateFeatureIterator delegate = (DelegateFeatureIterator) iterator;
        delegate.close();
    }

    static SimpleFeature retype(SimpleFeature source, SimpleFeatureType target)
        throws IllegalAttributeException {
        Object[] attributes = new Object[target.getAttributeCount()];

        for (int i = 0; i < target.getAttributeCount(); i++) {
            AttributeDescriptor attributeType = target.getAttribute(i);
            Object value = null;

            if (source.getFeatureType().getAttribute(attributeType.getName()) != null) {
                value = source.getAttribute(attributeType.getName());
            }

            attributes[i] = value;
        }

        return SimpleFeatureBuilder.build(target, attributes, source.getID());
    }

    public static class RetypingIterator implements Iterator {
        SimpleFeatureType target;
        Iterator delegate;

        public RetypingIterator(Iterator delegate, SimpleFeatureType target) {
            this.delegate = delegate;
            this.target = target;
        }

        public boolean hasNext() {
            return delegate.hasNext();
        }

        public Object next() {
            try {
                return RetypingFeatureCollection.retype((SimpleFeature) delegate.next(), target);
            } catch (IllegalAttributeException e) {
                throw new RuntimeException(e);
            }
        }

        public void remove() {
            delegate.remove();
        }
    }

    public static class RetypingFeatureReader implements FeatureReader {
        FeatureReader delegate;
        SimpleFeatureType target;

        public RetypingFeatureReader(FeatureReader delegate, SimpleFeatureType target) {
            this.delegate = delegate;
            this.target = target;
        }

        public void close() throws IOException {
            delegate.close();
            delegate = null;
            target = null;
        }

        public SimpleFeatureType getFeatureType() {
            return target;
        }

        public boolean hasNext() throws IOException {
            return delegate.hasNext();
        }

        public SimpleFeature next() throws IOException, IllegalAttributeException, NoSuchElementException {
            return RetypingFeatureCollection.retype(delegate.next(), target);
        }
    }
    
    public static class RetypingFeatureWriter implements FeatureWriter {
        FeatureWriter delegate;
        SimpleFeatureType target;
        private SimpleFeature current;
        private SimpleFeature retyped;

        public RetypingFeatureWriter(FeatureWriter delegate, SimpleFeatureType target) {
            this.delegate = delegate;
            this.target = target;
        }

        public void close() throws IOException {
            delegate.close();
            delegate = null;
            target = null;
        }

        public SimpleFeatureType getFeatureType() {
            return target;
        }

        public boolean hasNext() throws IOException {
            return delegate.hasNext();
        }

        public SimpleFeature next() throws IOException {
            try {
                current = delegate.next();
                retyped = RetypingFeatureCollection.retype(current, target);
                return retyped;
            } catch (IllegalAttributeException e) {
                throw (IOException) new IOException("Error occurred while retyping feature").initCause(e);
            }
        }

        public void remove() throws IOException {
            delegate.write();
        }

        public void write() throws IOException {
            try {
                for (int i = 0; i < target.getAttributeCount(); i++) {
                    AttributeDescriptor at = target.getAttribute(i);
                    Object value = retyped.getAttribute(i);
                    current.setAttribute(at.getLocalName(), value);
                }
                delegate.write();
            } catch(IllegalAttributeException e) {
                throw (IOException) new IOException("Error occurred while retyping feature").initCause(e);
            }
        }
    }
}
