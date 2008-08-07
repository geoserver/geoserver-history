package org.geoserver.catalog.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geoserver.catalog.AttributeTypeInfo;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.FeatureTypeInfo;
import org.opengis.feature.type.AttributeDescriptor;

public class AttributeTypeInfoImpl implements AttributeTypeInfo {

    String name;
    int minOccurs;
    int maxOccurs;
    boolean nillable;
    transient AttributeDescriptor attribute;
    HashMap metadata = new HashMap();
    
    FeatureTypeInfo featureType;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getMaxOccurs() {
        return maxOccurs;
    }
    
    public void setMaxOccurs(int maxOccurs) {
        this.maxOccurs = maxOccurs;
    }
    
    public int getMinOccurs() {
        return minOccurs;
    }

    public void setMinOccurs(int minOccurs) {
        this.minOccurs = minOccurs;
    }
    
    public boolean isNillable() {
        return nillable;
    }

    public void setNillable(boolean nillable) {
        this.nillable = nillable;
    }

    public FeatureTypeInfo getFeatureType() {
        return featureType;
    }
    
    public void setFeatureType(FeatureTypeInfo featureType) {
        this.featureType = featureType;
    }
    
    public AttributeDescriptor getAttribute() {
        return attribute;
    }
    
//    public void setAttribute(AttributeDescriptor attribute) {
//        this.attribute = attribute;
//    }
    
    public Map<String, Serializable> getMetadata() {
        return metadata;
    }
}
