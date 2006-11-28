/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.data.feature;

import java.util.HashMap;
import java.util.Map;

import org.geotools.feature.AttributeType;

/**
 * AttributeTypeInfo represents AttributeTypeMetaData for GeoServer.
 * 
 * <p>
 * Holds information about AttributeType such as min/max occurs.
 * </p>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: AttributeTypeInfo.java,v 1.12 2004/06/26 19:51:24 jive Exp $
 */
public class AttributeTypeInfo {
	
	/** Real GeoTools2 AttributeType */
    private AttributeType type;
    private Map meta;

    public AttributeTypeInfo(AttributeType type) {
        this.type = type;
        
        meta = new HashMap();
    }
    
    /**
     * @return The underlying attribute type.
     *
     * @see org.geotools.data.AttributeTypeMetaData#getAttributeType()
     */
    public AttributeType getAttributeType() {
        return type;
    }

    /**
     * @return The name of the attribute type
     */
    public String getName(){
		return type.getName();
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
    
    /**
	 * Access maxOccurs property.
	 * 
	 * @return Returns the maxOccurs.
	 */
	public int getMaxOccurs() {
		return type.getMaxOccurs();
	}

	/**
	 * Access minOccurs property.
	 * 
	 * @return Returns the minOccurs.
	 */
	public int getMinOccurs() {
		return type.getMinOccurs();
	}

	/**
	 * Access nillable property.
	 * 
	 * @return Returns the nillable.
	 */
	public boolean isNillable() {
		return type.isNillable();
	}
}
