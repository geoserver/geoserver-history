/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.feature;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.data.DataUtilities;
import org.geotools.data.store.DataFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;


/**
 * Wraps multiple feature collections into a single.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class CompositeFeatureCollection extends DataFeatureCollection {
    /**
     * wrapped collecitons
     */
    List collections;

    public CompositeFeatureCollection(List collections) {
        this.collections = collections;
    }

    protected Iterator openIterator() throws IOException {
        return new CompositeIterator();
    }

    public SimpleFeatureType getSchema() {
        return null;
    }

    public ReferencedEnvelope getBounds() {
        return ReferencedEnvelope.reference(DataUtilities.bounds(this));
    }

    public int getCount() throws IOException {
        int count = 0;
        Iterator i = iterator();

        try {
            while (i.hasNext()) {
                i.next();
                count++;
            }
        } finally {
            close(i);
        }

        return count;
    }

    class CompositeIterator implements Iterator {
        int index;
        Iterator iterator;

        public CompositeIterator() {
            index = 0;
        }

        public void remove() {
        }

        public boolean hasNext() {
            //is there a current iterator that has another element
            if ((iterator != null) && iterator.hasNext()) {
                return true;
            }

            //get the next iterator
            while (index < collections.size()) {
                //close current before we move to next
                if (iterator != null) {
                    ((FeatureCollection) collections.get(index - 1)).close(iterator);
                }

                //grap next
                iterator = ((FeatureCollection) collections.get(index++)).iterator();

                if (iterator.hasNext()) {
                    return true;
                }
            }

            //no more
            if (iterator != null) {
                //close the last iterator
                ((FeatureCollection) collections.get(collections.size() - 1)).close(iterator);
            }

            return false;
        }

        public Object next() {
            return iterator.next();
        }
    }
}
