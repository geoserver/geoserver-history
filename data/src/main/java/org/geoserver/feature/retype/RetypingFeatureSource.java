/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.feature.retype;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.geoserver.feature.RetypingFeatureCollection;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Renaming wrapper for a {@link FeatureSource} instance, to be used along with {@link RetypingDataStore} 
 */
public class RetypingFeatureSource implements FeatureSource {

    FeatureSource wrapped;

    FeatureTypeMap typeMap;

    DataStore ds;
    
    Map listeners = new HashMap();

    public RetypingFeatureSource(DataStore ds, FeatureSource wrapped, FeatureTypeMap typeMap) {
        this.ds = ds;
        this.wrapped = wrapped;
        this.typeMap = typeMap;
    }

    public void addFeatureListener(FeatureListener listener) {
        FeatureListener wrapper = new WrappingFeatureListener(this, listener);
        listeners.put(listener, wrapper);
        wrapped.addFeatureListener(wrapper);
    }

    public void removeFeatureListener(FeatureListener listener) {
        FeatureListener wrapper = (FeatureListener) listeners.get(listener);
        if(wrapper != null) {
            wrapped.removeFeatureListener(listener);
            listeners.remove(listener);
        }
    }

    public Envelope getBounds() throws IOException {
        // not fully correct if we use this to shave attributes too, but this is
        // not in the scope now
        return wrapped.getBounds();
    }

    public Envelope getBounds(Query query) throws IOException {
        // not fully correct if we use this to shave attributes too, but this is
        // not in the scope now
        return wrapped.getBounds(retypeQuery(query));
    }

    public int getCount(Query query) throws IOException {
        return wrapped.getCount(retypeQuery(query));
    }

    public DataStore getDataStore() {
        return ds;
    }

    public FeatureCollection getFeatures() throws IOException {
        return getFeatures(Query.ALL);
    }

    public FeatureCollection getFeatures(Query query) throws IOException {
        return new RetypingFeatureCollection(wrapped.getFeatures(retypeQuery(query)), typeMap
                .getFeatureType());
    }

    public FeatureCollection getFeatures(Filter filter) throws IOException {
        return getFeatures(new DefaultQuery(typeMap.getName(), filter));
    }

    public FeatureType getSchema() {
        return typeMap.getFeatureType();
    }

    public Set getSupportedHints() {
        return wrapped.getSupportedHints();
    }

    Query retypeQuery(Query q) {
        DefaultQuery modified = new DefaultQuery(q);
        modified.setTypeName(typeMap.getOriginalName());
        return modified;
    }

}
