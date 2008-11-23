/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.opengis.feature.type.AttributeDescriptor;
import org.vfny.geoserver.global.dto.AttributeTypeInfoDTO;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * AttributeTypeInfo represents AttributeTypeMetaData for GeoServer.
 *
 * <p>
 * Holds information about AttributeDescriptor such as min/max occurs.
 * </p>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id$
 */
public class AttributeTypeInfo {
    //private String name;
    //private int minOccurs;
    //private int maxOccurs;
    //private boolean nillable;
    //private String typeName;
    //private boolean isComplex;
    //
    ///** Readl GeoTools2 AttributeDescriptor */
    //private AttributeDescriptor type;
    //private Map meta;
    //
    //public AttributeTypeInfo(AttributeTypeInfoDTO dto) {
    //    type = null;
    //    meta = new HashMap();
    //    name = dto.getName();
    //    minOccurs = dto.getMinOccurs();
    //    maxOccurs = dto.getMaxOccurs();
    //    nillable = dto.isNillable();
    //    isComplex = dto.isComplex();
    //    typeName = dto.getType();
    //}
    //
    //public AttributeTypeInfo(AttributeDescriptor type) {
    //    this.type = type;
    //    meta = new HashMap();
    //}

    org.geoserver.catalog.AttributeTypeInfo attributeType;
    
    public AttributeTypeInfo(org.geoserver.catalog.AttributeTypeInfo attributeType) {
        this.attributeType = attributeType;
    }

    public void load(AttributeTypeInfoDTO dto) {
        attributeType.setName( dto.getName() );
        attributeType.setMinOccurs( dto.getMinOccurs() );
        attributeType.setMaxOccurs( dto.getMaxOccurs() );
        attributeType.setNillable( dto.isNillable() );
    }
    
    public String getName() {
        return attributeType.getName();
        //return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     *
     * @see org.geotools.data.AttributeTypeMetaData#getAttributeType()
     */
    public AttributeDescriptor getAttributeType() {
        try {
            return attributeType.getAttribute();
        } 
        catch (IOException e) {
            throw new RuntimeException( e );
        }
        //return type;
    }

    /**
     * Allows AttributeDescriptor to be lazy about syncing with Schema
     *
     * @param type DOCUMENT ME!
     */
    public void sync(AttributeDescriptor type) {
        //attributeType.setAttribute(type);
        //this.type = type;
    }

    /**
     * Access AttributeName
     *
     * @return
     *
     * @see org.geotools.data.AttributeTypeMetaData#getAttributeName()
     */
    //public String getAttributeName() {
    //    String r = typeName;
    //
    //    if ((r == null) && (type != null)) {
    //        r = type.getLocalName();
    //    }
    //
    //    return r;
    //}

    /**
     * Element type, a well-known gml or xs type or <code>TYPE_FRAGMENT</code>.
     *
     * <p>
     * If getType is equals to <code>TYPE_FRAGMENT</code> please consult
     * getFragment() to examine the actual user's definition.
     * </p>
     *
     * <p>
     * Other than that getType should be one of the constants defined by
     * GMLUtils.
     * </p>
     *
     * @return The element, or <code>TYPE_FRAGMENT</code>
     */
    public String getType() {
        return "gml:AbstractFeatureType";
        //if (isComplex) {
        //    return "(xml fragment)";
        //} else {
        //    return typeName;
        //}
    }

    /**
     * XML Fragment used to define stuff.
     *
     * <p>
     * This property is only used with getType() is equals to "(xml fragment)".
     * </p>
     *
     * <p>
     * baseGMLTypes can only be used in your XML fragment.
     * </p>
     *
     * @param fragment The fragment to set.
     */
    //public String getFragment() {
    //    if (isComplex) {
    //        return typeName;
    //    } else {
    //        return null;
    //    }
    //}

    /**
     * Implement containsMetaData.
     *
     * @param key
     *
     * @return
     *
     * @see org.geotools.data.MetaData#containsMetaData(java.lang.String)
     */
    public boolean containsMetaData(String key) {
        return attributeType.getMetadata().get( key ) != null;
        //return meta.containsKey(key);
    }

    /**
     * Implement putMetaData.
     *
     * @param key
     * @param value
     *
     * @see org.geotools.data.MetaData#putMetaData(java.lang.String,
     *      java.lang.Object)
     */
    public void putMetaData(String key, Object value) {
        attributeType.getMetadata().put( key, (Serializable) value);
        //meta.put(key, value);
    }

    /**
     * Implement getMetaData.
     *
     * @param key
     *
     * @return
     *
     * @see org.geotools.data.MetaData#getMetaData(java.lang.String)
     */
    public Object getMetaData(String key) {
        return attributeType.getMetadata().get( key );
        //return meta.get(key);
    }

    Object toDTO() {
        AttributeTypeInfoDTO dto = new AttributeTypeInfoDTO();
        
        dto.setName( attributeType.getName() );
        dto.setMinOccurs( attributeType.getMinOccurs() );
        dto.setMaxOccurs( attributeType.getMaxOccurs() );
        dto.setNillable( attributeType.isNillable() );
        dto.setType( getType() );
        
        //dto.setComplex(isComplex);
        //dto.setMaxOccurs(maxOccurs);
        //dto.setMinOccurs(minOccurs);
        //dto.setName(name);
        //dto.setNillable(nillable);
        //dto.setType(typeName);

        return dto;
    }

    /**
     * Access maxOccurs property.
     *
     * @return Returns the maxOccurs.
     */
    public int getMaxOccurs() {
        return attributeType.getMaxOccurs();
        //return maxOccurs;
    }

    /**
     * Access minOccurs property.
     *
     * @return Returns the minOccurs.
     */
    public int getMinOccurs() {
        return attributeType.getMinOccurs();
        //return minOccurs;
    }

    /**
     * Access nillable property.
     *
     * @return Returns the nillable.
     */
    public boolean isNillable() {
        return attributeType.isNillable();
        //return nillable;
    }

    public String toString() {
        return "[AttributeTypeInfo backed by " + toDTO() + " with type " + attributeType.getName() + " and meta "
        + attributeType.getMetadata();
    }
}
