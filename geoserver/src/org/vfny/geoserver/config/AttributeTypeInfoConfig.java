/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.geotools.feature.AttributeType;
import org.vfny.geoserver.global.dto.AttributeTypeInfoDTO;

/**
 * Allows editing of AttributeTypeInfo.
 * <p>
 * Represents most of a xs:element for an XMLSchema.
 * </p>
 * <p>
 * we have three types of information to store, Schema defined types, 
 * references and extentions on types. If the type represented is either 
 * a reference or a Schema defined type  then isRef should be true. 
 * </p>
 * <p>
 * Non-complex types are of the form:
 * </p>
 * <ul>
 * <li><code>{element name='test' type='xs:string'/}</code></li>
 * <li><code>{element name='test' type='gml:PointType'/}</code></li>
 * </ul>
 * <p> 
 * These cases have their type name stored in this.type
 * </p>
 * 
 * <p>
 * For complex types such as 
 * <code>
 * {element name='test'}
 *  {xs:complexContent}
 *    {xs:extension base="gml:AbstractFeatureType"}
 *      {xs:sequence}
 *         {xs:element name="id" type="xs:string" minOccurs="0"/}
 *         {xs:element ref="gml:pointProperty" minOccurs="0"/}
 *      {/xs:sequence}
 *    {/xs:extension}
 *  {/xs:complexContent}
 * {/element}
 * </code>
 * The type contains a similar XML fragment.
 * </p>
 * <p>
 * minOccurs, maxOccurs and nillable are all attributes for all cases. There is
 * more stuff in the XMLSchema spec but we don't care.
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: AttributeTypeInfoConfig.java,v 1.7 2004/01/13 23:46:53 jive Exp $
 */
public class AttributeTypeInfoConfig {
    
    /** Value of getType() used to indicate that fragement is in use */
    public static final String TYPE_FRAGMENT = "(xml fragment)";
    
    /** attribute name*/
    private String name;
    
    /** attribute min occurs*/
    private int minOccurs;
    
    /**
     * Maxmium number of occurances of this attribute in a feature.
     * <p>
     * For Features based on the Simple Feature Specification this
     * should be a value of 1. If the attribute is optional it should
     * still be 1, although often optional is represented by allowing the
     * Attribute to be <code>nillable</code>.
     * </p>
     * <p>
     * Common Min..Max Occurs values:
     * </p>
     * <ul>
     * <li>0..<b>1</b>: attribute is optional</li>
     * <li>1..<b>1</b>: attribute is required (usual for Simple Features)</li>
     * <li>0..<b>N</b>: attribute forms a list that may be empty</li>
     * </ul>
     * 
     * @see AttributeTypeInfoDTO.isNillable
     */
    private int maxOccurs;
    
    /**
     * Indicate if the attribute is allowed to be <code>null</code>.
     * <p>
     * Nillable is often used to indicate that an attribute is optional.
     * The use of minOccurs and maxOccurs may be a more correct way to
     * indicate optional attribtues.
     * </p>
     * @see AttributeTypeInfoDTO.minOccurs
     * @see AttributeTypeInfoDTO.maxOccurs
     */
    private boolean nillable;
    
    /**
     * Element type, a well-known gml or xs type or <code>TYPE_FRAGMENT</code>.
     * <p>
     * If getType is equals to TYPE_FRAGMENT please consult getFragment()
     * to examin the actual user's definition.
     * </p>
     * <p>
     * Other than that getType should be one of the constants defined by
     * GMLUtils.
     * </p>
     * @return type The element type, or <code>TYPE_FRAGMENT</code>
     */
    public String type;
    
    /**
     * XML Fragment used to define stuff.
     * <p>
     * This property is only used with getType() is equals to "(xml fragment)".
     * </p>
     * <p>
     * baseGMLTypes can only be used in your XML fragment.
     * </p>
     */
    private String fragment;

    /**
     * Set up AttributeTypeInfo based on attributeType.
     * <p>
     * Set up is determined by the AttributeTypeInfoDTO( AttributeType )
     * constructor. This allows all Schema generation to be acomplished
     * in the same palce.
     * </p>
     * @param attributeType GeoTools2 attributeType used for configuration
     */
    public AttributeTypeInfoConfig( AttributeType attributeType ){
        
    }
    
    /**
     * Set up AttributeTypeInfo based on Data Transfer Object.
     * 
     * @param dto AttributeTypeInfoDTO used for configuration
     */
    public AttributeTypeInfoConfig(AttributeTypeInfoDTO dto){
        name = dto.getName(); 
        if( dto.isRef() ){
            type = TYPE_FRAGMENT;
            fragment = dto.getType();
        }
        else {
            type = dto.getType();
            fragment = "";
        }
        minOccurs = dto.getMinOccurs();
        maxOccurs = dto.getMaxOccurs();
        nillable = dto.isNillable();        
    }

    /**
     * getMaxOccurs purpose.
     * <p>
     * The max number of occurences for this element.
     * </p>
     * @return max number of occurences
     */
    public int getMaxOccurs() {
        return maxOccurs;
    }

    /**
     * getMinOccurs purpose.
     * <p>
     * the min number of occurences for this element
     * </p>
     * @return min number of occurences
     */
    public int getMinOccurs() {
        return minOccurs;
    }

    /**
     * getName purpose.
     * <p>
     * returns the element name
     * </p>
     * @return the element name
     */
    public String getName() {
        return name;
    }

    /**
     * Indicate if the attribute is allowed to be <code>null</code>.
     * <p>
     * Nillable is often used to indicate that an attribute is optional.
     * The use of minOccurs and maxOccurs may be a more correct way to
     * indicate optional attribtues.
     * </p>
     * @see AttributeTypeInfoDTO.setMinOccurs
     * @see AttributeTypeInfoDTO.setMaxOccurs
     * @return <code>true </code> to indicate attribute is alloed to be <code>null</code>
     */
    public boolean isNillable() {
        return nillable;
    }

    /**
     * Element type, a well-known gml or xs type or <code>TYPE_FRAGMENT</code>.
     * <p>
     * If getType is equals to <code>TYPE_FRAGMENT</code> please consult
     * getFragment() to examine the actual user's definition.
     * </p>
     * <p>
     * Other than that getType should be one of the constants defined by
     * GMLUtils.
     * </p>
     * @return The element, or <code>TYPE_FRAGMENT</code>
     */
    public String getType() {
        return type;
    }

    /**
     * Maxmium number of occurances of this attribute in a feature.
     * <p>
     * For Features based on the Simple Feature Specification this
     * should be a value of 1. If the attribute is optional it should
     * still be 1, although often optional is represented by allowing the
     * Attribute to be <code>nillable</code>.
     * </p>
     * <p>
     * Common Min..Max Occurs values:
     * </p>
     * <ul>
     * <li>0..<b>1</b>: attribute is optional</li>
     * <li>1..<b>1</b>: attribute is required (usual for Simple Features)</li>
     * <li>0..<b>N</b>: attribute forms a list that may be empty</li>
     * </ul>
     * 
     * @see AttributeTypeInfoDTO.isNillable
     * @param max The maximum number of occurances
     */
    public void setMaxOccurs(int max) {
        maxOccurs = max;
    }

    /**
     * Minimum number of occrances of this attribute in a feature.
     * <p>
     * For Features based on the Simple Feture Specification this
     * should be a value of 1. If the attribute is optional is should
     * be 0, although often optional is represented by allowing the attribute
     * to be nillable.
     * </p>
     * Common Min..Max Occurs values:
     * </p>
     * <ul>
     * <li><b>0</b>..1: attribute is optional</li>
     * <li><b>1</b>..1: attribute is required (usual for Simple Features)</li>
     * <li><b>0</b>..N: attribute forms a list that may be empty</li>
     * </ul>
     * @see AttributeTypeInfoDTO.isNillable
     * @param min The minimum number of occurances
     */
    public void setMinOccurs(int min) {
        minOccurs = min;
    }
    
    /**
     * Indicate if the attribute is allowed to be <code>null</code>.
     * <p>
     * Nillable is often used to indicate that an attribute is optional.
     * The use of minOccurs and maxOccurs may be a more correct way to
     * indicate optional attribtues.
     * </p>
     * @see AttributeTypeInfoDTO.setMinOccurs
     * @see AttributeTypeInfoDTO.setMaxOccurs
     * @param nillable <code>true </code> to indicate attribute is alloed to be <code>null</code>
     */
    public void setNillable(boolean nillable) {
        this.nillable = nillable;
    }

    /**
     * Element type, a well-known gml or xs type or <code>TYPE_FRAGMENT</code>.
     * <p>
     * If getType is equals to <code>TYPE_FRAGMENT</code> please consult getFragment()
     * to examin the actual user's definition.
     * </p>
     * </p>
     * Other than that getType should be one of the constants defined by
     * GMLUtils.
     * </p>
     * @return type The element type, or <code>TYPE_FRAGMENT</code>
     */
    public void setType(String type) {
        this.type = type;
    }

}
