package org.geoserver.feature;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.geotools.data.DataAccess;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.ResourceInfo;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.DecoratingFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * Feature Source wrapper which pages through the results based on an offset,
 * length pair.
 * <p>
 * Note that this is not an actual solution to do paging. For one thing it does 
 * not ensure that the underlying results are sorted, which really makes paging
 * invalid. This class should not be used in general. 
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class PagingFeatureSource implements FeatureSource {

    FeatureSource delegate;
    int offset, length;
    
    public PagingFeatureSource( FeatureSource delegate, int offset, int length ) {
        this.delegate = delegate;
        this.offset = offset;
        this.length = length;
    }

    public void addFeatureListener(FeatureListener listener) {
        delegate.addFeatureListener(listener);
    }
    
    public void removeFeatureListener(FeatureListener listener) {
        delegate.removeFeatureListener(listener);
    }

    public Name getName() {
        return delegate.getName();
    }

    public FeatureType getSchema() {
        return delegate.getSchema();
    }
    
    public ReferencedEnvelope getBounds() throws IOException {
        return delegate.getBounds();
    }

    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return delegate.getBounds(query);
    }

    public int getCount(Query query) throws IOException {
        //override get count to actual count
        FeatureCollection fc = getFeatures( query );
        return fc.size();
    }

    public DataAccess getDataStore() {
        return delegate.getDataStore();
    }

    public FeatureCollection getFeatures() throws IOException {
        return new PagingFeatureCollection( delegate.getFeatures() );
    }

    public FeatureCollection getFeatures(Filter filter) throws IOException {
        return new PagingFeatureCollection( delegate.getFeatures(filter) );
    }

    public FeatureCollection getFeatures(Query query) throws IOException {
        return new PagingFeatureCollection( delegate.getFeatures(query) );
    }

    public ResourceInfo getInfo() {
        return delegate.getInfo();
    }

    public Set getSupportedHints() {
        return delegate.getSupportedHints();
    }
    
    class PagingFeatureCollection extends DecoratingFeatureCollection<SimpleFeatureType, SimpleFeature> {

        protected PagingFeatureCollection(FeatureCollection<SimpleFeatureType, SimpleFeature> delegate) {
            super(delegate);
        }
        
        /**
         * Override size to ensure we take into account index + offset.
         */
        public int size() {
            Iterator i = iterator();
            try {
                int count = 0;
                while( i.hasNext() ) {
                    i.next();
                    count++;
                }
                
                return count;
            }
            finally {
                close( i );
            }
        }
        
        public Iterator iterator() {
            return new PagingIterator( delegate.iterator() );
        }
        
        public void close(Iterator<SimpleFeature> close) {
            delegate.close( ((PagingIterator)close).delegate );
        }
        
        public FeatureIterator<SimpleFeature> features() {
            return new PagingFeatureIterator( delegate.features() );
        }
       
        public void close(FeatureIterator<SimpleFeature> close) {
            delegate.close( ((PagingFeatureIterator)close).delegate );
        }
        
        class PagingIterator implements Iterator {

            Iterator delegate;
            int count = 0;
            
            public PagingIterator( Iterator delegate ) {
                this.delegate = delegate;
                
                //scroll to the offset
                for ( int i = 0; i < offset && delegate.hasNext(); i++ ) {
                    delegate.next();
                }
            }
            
            public boolean hasNext() {
                return count < length && delegate.hasNext();
            }

            public Object next() {
                if ( count > length ) {
                    return null;
                }
                
                count++;
                return delegate.next();
            }

            public void remove() {
            }
        }
     
        class PagingFeatureIterator implements FeatureIterator<SimpleFeature> {

            FeatureIterator<SimpleFeature> delegate;
            int count = 0;
            
            public PagingFeatureIterator( FeatureIterator<SimpleFeature> delegate ) {
                this.delegate = delegate;
                
                //scroll to the offset
                for ( int i = 0; i < offset && delegate.hasNext(); i++ ) {
                    delegate.next();
                }
            }
            
            public boolean hasNext() {
                return count <= length && delegate.hasNext();
            }

            public SimpleFeature next() throws NoSuchElementException {
                if ( count > length ) {
                    return null;
                }
                
                count++;
                return delegate.next();
            }
            
            public void close() {
                delegate.close();
                delegate = null;
            }
        }
    }
}
