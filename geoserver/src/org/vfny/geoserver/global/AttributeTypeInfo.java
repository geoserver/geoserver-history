/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geotools.data.AttributeTypeMetaData;
import org.geotools.feature.AttributeType;
import org.vfny.geoserver.global.dto.AttributeTypeInfoDTO;
import java.util.HashMap;
import java.util.Map;


/**
 * AttributeTypeInfo represents AttributeTypeMetaData for GeoServer.
 * 
 * <p>
 * Holds information about AttributeType such as min/max occurs.
 * </p>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: AttributeTypeInfo.java,v 1.6 2004/02/07 00:52:30 jive Exp $
 */
public class AttributeTypeInfo implements AttributeTypeMetaData {
    /** Following Davids lead with use of DTO delegate */
    private AttributeTypeInfoDTO delegate;

    /** Readl GeoTools2 AttributeType */
    private AttributeType type;
    private Map meta;

    public AttributeTypeInfo(AttributeTypeInfoDTO dto) {
        delegate = dto;
        type = null;
        meta = new HashMap();
    }

    public AttributeTypeInfo(AttributeType type) {
        delegate = null;
        this.type = type;
        meta = new HashMap();
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     *
     * @see org.geotools.data.AttributeTypeMetaData#getAttributeType()
     */
    public AttributeType getAttributeType() {
        return type;
    }

    /**
     * Allows AttributeType to be lazy about syncing with Schema
     *
     * @param type DOCUMENT ME!
     */
    public void sync(AttributeType type) {
        this.type = type;
    }

    /**
     * Access AttributeName
     *
     * @return
     *
     * @see org.geotools.data.AttributeTypeMetaData#getAttributeName()
     */
    public String getAttributeName() {
        if (delegate != null) {
            delegate.getName();
        }

        if (type != null) {
            type.getName();
        }

        return null;
    }

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
    public String getType(){
        if( delegate.isComplex() ){
            return "(xml fragment)";
        }
        else {
            return delegate.getType();
        }        
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
    public String getFragment(){
        if( delegate.isComplex() ){
            return delegate.getType();
        }
        else {
            return null;
        }
    }
    
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
        return meta.containsKey(key);
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
        meta.put(key, value);
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
        return meta.get(key);
    }
}
