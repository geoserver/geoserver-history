/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.util.HashMap;
import java.util.Map;

import org.geotools.feature.AttributeType;
import org.geotools.feature.AttributeTypeFactory;
// import org.geotools.feature.SchemaException ;
import org.vfny.geoserver.global.dto.AttributeTypeInfoDTO;

import java.util.logging.Logger;

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
	
    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.global");
	
	private String name;
	private int minOccurs;
	private int maxOccurs;
	private boolean nillable;
	private String typeName;
	private boolean isComplex;
	private String xpath;
	private String dbJavaType;
	
    /** Readl GeoTools2 AttributeType */
    private AttributeType type;
    private Map meta;

    public AttributeTypeInfo(AttributeTypeInfoDTO dto) {
    	
    	// this should really throw a SchemaException, supporting the use case
    	// where a Geotools AttributeType is constructed from extended attributes
    	// in schema.xml supporting pass through SQL -pb
    	
        type = null;
    	meta = new HashMap();
        name = dto.getName();
        minOccurs = dto.getMinOccurs();
        maxOccurs = dto.getMaxOccurs();
        nillable = dto.isNillable();
        isComplex = dto.isComplex();
        typeName = dto.getType();
        xpath = dto.getXpath();
        dbJavaType = dto.getDbJavaType();
    	
        if (dbJavaType != null ) {
    		// make a GeoTools AttributeType so we can build a GeoTools 
    		// FeatureType schema in the geoserver FeatureTypeInfo; need to do this
    		// cause must be doing pass through SQL and GeoTools can't construct a 
    		// FeatureType in its usual way from table metadata
    		try {
	        	type = AttributeTypeFactory.newAttributeType(
	        			name, Class.forName(dbJavaType), nillable);
    			// LOGGER.fine("Created AttributeType for attribute " + name + " Class " + dbJavaType);	        	
    		} catch (ClassNotFoundException e) {
    			type = null;	// this is not really good enough
    			LOGGER.warning("Error creating geotools AttributeType - No such class " +
    	                dbJavaType + " for attribute " + name);
    		}
    	}
    }
    
    public AttributeTypeInfo(AttributeType type) {
        this.type = type;
        meta = new HashMap();
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return
     */    
    public String getName(){
    	return name;
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
    	String r = typeName;

        if (r==null && type != null) {
            r = type.getName();
        }

        return r;
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
        if( isComplex ){
            return "(xml fragment)";
        }
        else {
            return typeName;
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
        if( isComplex ){
            return typeName;
        }
        else {
            return null;
        }
    }

    /**
     * xpath element/attribute associated with database attribute in schema.xml,
     * eg used when doing bypass SQL and requiring GML2-AS (application schema) output
     *
     * @return the xpath fragment for this attribute
     */    
    public String getXpath(){
    	return xpath;
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
    
    Object toDTO(){
    	AttributeTypeInfoDTO dto = new AttributeTypeInfoDTO();
    	dto.setComplex(isComplex);
    	dto.setMaxOccurs(maxOccurs);
    	dto.setMinOccurs(minOccurs);
    	dto.setName(name);
    	dto.setNillable(nillable);
    	dto.setType(typeName);
    	dto.setXpath(xpath);
    	dto.setDbJavaType(dbJavaType);
		return dto;
    }
	/**
	 * Access maxOccurs property.
	 * 
	 * @return Returns the maxOccurs.
	 */
	public int getMaxOccurs() {
		return maxOccurs;
	}

	/**
	 * Access minOccurs property.
	 * 
	 * @return Returns the minOccurs.
	 */
	public int getMinOccurs() {
		return minOccurs;
	}

	/**
	 * Access nillable property.
	 * 
	 * @return Returns the nillable.
	 */
	public boolean isNillable() {
		return nillable;
	}
	
	public String toString() {
		return "[AttributeTypeInfo backed by " + toDTO() + " with type " + type + 
				" and meta " + meta;
	}

}
