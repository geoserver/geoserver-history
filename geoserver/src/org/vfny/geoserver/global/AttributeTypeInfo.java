/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.util.HashMap;
import java.util.Map;

import org.geotools.data.AttributeTypeMetaData;
import org.geotools.feature.AttributeType;
import org.vfny.geoserver.global.dto.AttributeTypeInfoDTO;

/**
 * AttributeTypeInfo represents AttributeTypeMetaData for GeoServer.
 * <p>
 * Holds information about AttributeType such as min/max occurs.
 * </p>
 * 
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: AttributeTypeInfo.java,v 1.2 2004/01/12 21:01:27 dmzwiers Exp $
 */
public class AttributeTypeInfo implements AttributeTypeMetaData {
    
    /** Following Davids lead with use of DTO delegate */
    private AttributeTypeInfoDTO delegate;
    
    /** Readl GeoTools2 AttributeType */
    private AttributeType type;
    
    private Map meta;
    
    public AttributeTypeInfo( AttributeTypeInfoDTO dto ){
        delegate = dto;
        type = null;
        meta = new HashMap();
    }
    public AttributeTypeInfo( AttributeType type ){
        delegate = null;
        this.type = type;
        meta = new HashMap();
    }
    /**
     * @see org.geotools.data.AttributeTypeMetaData#getAttributeType()
     * 
     * @return
     */
    public AttributeType getAttributeType() {
        return type;
    }
    
    /** Allows AttributeType to be lazy about syncing with Schema */
    public void sync( AttributeType type ){
        this.type = type;        
    }
    
    /**
     * Access AttributeName
     * @see org.geotools.data.AttributeTypeMetaData#getAttributeName()
     * 
     * @return
     */
    public String getAttributeName() {
        if( delegate != null ) delegate.getName();
        if( type != null ) type.getName();
        return null;
    }

    /**
     * Implement containsMetaData.
     * 
     * @see org.geotools.data.MetaData#containsMetaData(java.lang.String)
     * 
     * @param key
     * @return
     */
    public boolean containsMetaData(String key) {
        return meta.containsKey( key );
    }

    /**
     * Implement putMetaData.
     * 
     * @see org.geotools.data.MetaData#putMetaData(java.lang.String, java.lang.Object)
     * 
     * @param key
     * @param value
     */
    public void putMetaData(String key, Object value) {
        meta.put( key, value );
    }

    /**
     * Implement getMetaData.
     * 
     * @see org.geotools.data.MetaData#getMetaData(java.lang.String)
     * 
     * @param key
     * @return
     */
    public Object getMetaData(String key) {
        return meta.get( key );
    }

}
