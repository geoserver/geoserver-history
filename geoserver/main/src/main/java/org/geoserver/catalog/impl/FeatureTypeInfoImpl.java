package org.geoserver.catalog.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geoserver.catalog.AttributeTypeInfo;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geotools.data.FeatureSource;
import org.geotools.factory.Hints;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.util.ProgressListener;

public class FeatureTypeInfoImpl extends ResourceInfoImpl implements
        FeatureTypeInfo {

    Filter filter;

    int maxFeatures;
    int numDecimals;
    
    public FeatureTypeInfoImpl(Catalog catalog) {
        super(catalog);
    }

    public FeatureTypeInfoImpl(Catalog catalog, String id) {
        super(catalog,id);
    }

    public DataStoreInfo getStore() {
        return (DataStoreInfo) super.getStore();
    }

    public List<AttributeTypeInfo> getAttributes() {
        return catalog.getResourcePool().getAttributeTypes(this);
    }
    
    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
    
    public int getMaxFeatures() {
        return maxFeatures;
    }
    
    public void setMaxFeatures(int maxFeatures) {
        this.maxFeatures = maxFeatures;
    }
    
    public int getNumDecimals() {
        return numDecimals;
    }
    
    public void setNumDecimals(int numDecimals) {
    }

    public SimpleFeatureType getFeatureType() throws IOException {
        return catalog.getResourcePool().getFeatureType( this );
    }
    
    public FeatureSource getFeatureSource(ProgressListener listener, Hints hints)
            throws IOException {
        return catalog.getResourcePool().getFeatureSource( this, hints );
    }

    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        //result = prime * result
                //+ ((attributes == null) ? 0 : attributes.hashCode());
        result = prime * result + ((filter == null) ? 0 : filter.hashCode());
        result = prime * result + maxFeatures;
        result = prime * result + numDecimals;
        return result;
    }

    public boolean equals(Object obj) {
        if ( !(obj instanceof FeatureTypeInfo ) ) {
            return false;
        }
        if ( !super.equals( obj ) ) {
            return false;
        }
        
        final FeatureTypeInfo other = (FeatureTypeInfo) obj;
        final List<AttributeTypeInfo> attributes = getAttributes();
        if (attributes == null) {
            if (other.getAttributes() != null)
                return false;
        } else if (!attributes.equals(other.getAttributes()))
            return false;
        if (filter == null) {
            if (other.getFilter() != null)
                return false;
        } else if (!filter.equals(other.getFilter()))
            return false;
        if (maxFeatures != other.getMaxFeatures())
            return false;
        if (numDecimals != other.getNumDecimals())
            return false;
        return true;
    }
    
    
}
