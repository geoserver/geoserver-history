package com.orci.geoserver.wfs.getnearest;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureType;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.collection.AbstractFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * A feature collection wrapping a base collection, returning features that do 
 * conform to the specified type (which has have a subset of the attributes in the
 * original schema), and that do use the wrapped features to compute their bounds (so that
 * the Feature bounds can be computed even if the visible attributes do not include geometries) 
 * @author Andrea Aime - TOPP
 *
 */
class FeatureBoundsFeatureCollection extends AbstractFeatureCollection {
    FeatureCollection wrapped;

    /**
     * Builds a new BoundsFeatureCollection
     * @param wrapped the wrapped feature collection
     * @param targetSchema the target schema
     */
    public FeatureBoundsFeatureCollection(FeatureCollection wrapped, FeatureType targetSchema) {
        super(targetSchema);
        this.wrapped = wrapped;
    }

    protected void closeIterator(Iterator close) {
        ((BoundsIterator) close).close();
    }

    protected Iterator openIterator() {
        return  new BoundsIterator(wrapped.features(), getSchema());
    }

    public int size() {
        return wrapped.size();
    }

    /**
     * 
     * @author Andrea Aime - TOPP
     *
     */
    private static class BoundsIterator implements Iterator {
        FeatureIterator wrapped;
        FeatureType targetSchema;

        public BoundsIterator(FeatureIterator wrapped, FeatureType targetSchema) {
            this.wrapped = wrapped;
            this.targetSchema = targetSchema;
        }

        public void close() {
            wrapped.close();
        }

        public boolean hasNext() {
            return wrapped.hasNext();
        }

        public Object next() throws NoSuchElementException {
            Feature base = wrapped.next();
            return new BoundedFeature(base, targetSchema);
        }

        public void remove() {
            throw new UnsupportedOperationException("Removal is not supported");
        }
    }

    /**
     * Wraps a Feature shaving off all attributes not included in the original type, but 
     * delegates bounds computation to the original feature.
     * @author Andrea Aime - TOPP
     *
     */
    private static class BoundedFeature implements Feature {
        private Feature wrapped;

        private FeatureType type;
        
        public BoundedFeature(Feature wrapped, FeatureType type) {
            this.wrapped = wrapped;
            this.type = type;
        }

        public Object getAttribute(int index) {
            return wrapped.getAttribute(type.getAttributeType(index).getName());
        }

        public Object getAttribute(String path) {
            if (type.getAttributeType(path) == null)
                return null;
            return wrapped.getAttribute(path);
        }

        public Object[] getAttributes(Object[] attributes) {
            Object[] retval = attributes != null ? attributes : new Object[type.getAttributeCount()];
            for (int i = 0; i < retval.length; i++) {
                retval[i] = wrapped.getAttribute(type.getAttributeType(i).getName());
            }
            return retval;
        }

        public Envelope getBounds() {
            // we may not have the default geometry around in the reduced feature type,
            // so let's output a referenced envelope if possible
            if(wrapped.getFeatureType().getDefaultGeometry() != null) {
                CoordinateReferenceSystem crs = wrapped.getFeatureType().getDefaultGeometry().getCoordinateSystem();
                if(crs != null) {
                    return new ReferencedEnvelope(wrapped.getBounds(), crs);
                }
            }
            return  wrapped.getBounds();
        }

        public Geometry getDefaultGeometry() {
            GeometryAttributeType defaultGeometry = type.getDefaultGeometry();
            if(defaultGeometry == null)
                return null;
            return (Geometry) wrapped.getAttribute(defaultGeometry.getName());
        }

        public FeatureType getFeatureType() {
            return type;
        }

        public String getID() {
            return wrapped.getID();
        }

        public int getNumberOfAttributes() {
            return type.getAttributeCount();
        }

        public void setAttribute(int position, Object val) throws IllegalAttributeException,
                ArrayIndexOutOfBoundsException {
            throw new UnsupportedOperationException("This feature wrapper is read only");
        }

        public void setAttribute(String path, Object attribute) throws IllegalAttributeException {
            throw new UnsupportedOperationException("This feature wrapper is read only");
        }

        public void setDefaultGeometry(Geometry geometry) throws IllegalAttributeException {
            throw new UnsupportedOperationException("This feature wrapper is read only");
        }

        public FeatureCollection getParent() {
            return wrapped.getParent();
        }

        public void setParent(FeatureCollection collection) {
            wrapped.setParent(collection);
        }

    }
}
