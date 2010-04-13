/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.impl;

import org.geoserver.catalog.AttributeTypeInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.MetadataMap;
import org.opengis.feature.type.AttributeDescriptor;

public class AttributeTypeInfoImpl implements AttributeTypeInfo {

    protected String id;
    protected String name;
    protected int minOccurs;
    protected int maxOccurs;
    protected boolean nillable;
    protected transient AttributeDescriptor attribute;
    protected MetadataMap metadata = new MetadataMap();
    
    protected FeatureTypeInfo featureType;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
    
    public void setAttribute(AttributeDescriptor attribute) {
        this.attribute = attribute;
    }
    
    public MetadataMap getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataMap metadata) {
        this.metadata = metadata;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
