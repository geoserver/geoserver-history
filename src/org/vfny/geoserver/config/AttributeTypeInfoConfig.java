/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.vfny.geoserver.global.dto.AttributeTypeInfoDTO;
/*
 * Represent configuration of an AttributeType.
 * <p>
 * Represents most of a xs:element for an XMLSchema.
 * </p>
 * <p>
 * We have three types of information to store:
 * <ol>
 * <li>Schema defined types: <code>isRef=true name="foo"</code>
 *   Example:<pre><code>
 *     {xs:element name="geom_test.geom"
 *                 type="gml:PolygonPropertyType"
 *                 nillable="false"
 *                 minOccurs="1"
 *                 maxOccurs="1"/}
 *   </code></pre>
 *   </li>
 * <li>References: <code>isRef=true name="gml:xyz"</code>
 *   Example:<pre><code>
 *     {xs:element type="gml:PolygonPropertyType"
 *                 nillable="false"
 *                 minOccurs="1"
 *                 maxOccurs="1"/}
 *   </code></pre>
 *   </li>
 * <li>Extensions:<br> isRef=false name="foo" type="XML fragment"
 *   Extension on an exsiting type.
 *   Example:<pre><code>
 *     {xs:element name="foo" 
 *                 nillable="false"
 *                 minOccurs="1"
 *                 maxOccurs="1"/}
 *       {xs:simpleType}
 * 	       {xs:restriction base="xs:string"}
 * 		     {xs:maxLength value="15"/}
 * 	       </xs:restriction}
 *       </xs:simpleType}
 *     {/xs:element}
 *   </code></pre>
 *   </li>  
 * </li>
 * </ol>
 * <p>
 * If the type represented is either a reference or a Schema defined type
 * then isRef should be true. 
 * </p> 
 * <p>
 * If isRef is true then we have one of two situations.
 * If the name is not specified then the type represents a "ref='gml:xyz'"
 * , otherwise it's of the form "name='foo'" and "type='bar'". 
 * </p>
 * <p>
 * The third case isRef is false, "name='foo'" and type contains an XML 
 * fragment defining the type.
 * </p>
 * <p>
 * minOccurs, maxOccurs and nillable are all attributes for all cases.
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: AttributeTypeInfoConfig.java,v 1.5 2004/01/13 21:12:20 jive Exp $
 */
public class AttributeTypeInfoConfig {
	/** attribute name*/
	private String name;
	/** attribute min occurs*/
	private int minOccurs;
	/** attribute max occurs*/
	private int maxOccurs;
	/** true when nillable */
	private boolean nillable;
	/** if is ref and a name is specified, then treat like a simple type (same thing ...) otherwise this is a complex type. */
	private String type;
	/** name and ref are mutualy exclusive. type overrides ref. */
	private boolean isRef;

	/**
	 * AttributeTypeInfoConfig constructor.
	 * <p>
	 * Default constructor, initializes with empty data.
	 * </p>
	 *
	 */
	public AttributeTypeInfoConfig(){
		name = type = "";
		minOccurs = 0;maxOccurs = 1;
		nillable = isRef = false;
	}
	
	/**
	 * AttributeTypeInfoConfig constructor.
	 * <p>
	 * Copies the data from the specified DTO to this one.
	 * </p>
	 * @param dto AttributeTypeInfoConfig The data source to copy from.
	 */
	public AttributeTypeInfoConfig(AttributeTypeInfoDTO dto){
		name = dto.getName(); 
		type = dto.getType();
		minOccurs = dto.getMinOccurs();
		maxOccurs = dto.getMaxOccurs();
		nillable = dto.isNillable();
		isRef = dto.isRef();
	}
	
	/**
	 * AttributeTypeInfoConfig constructor.
	 * <p>
	 * Copies the data from the specified DTO to this one.
	 * </p>
	 * @param dto AttributeTypeInfoConfig The data source to copy from.
	 */
	public AttributeTypeInfoConfig(org.geotools.feature.AttributeType dto){
		name = dto.getName();
		type = dto.getType().getName();
		minOccurs = 1; //TODO extract this correctly
		maxOccurs = 1; //TODO extract this correctly
		nillable = dto.isNillable();
		isRef = !dto.isNested();
	}

	/**
	 * isRef purpose.
	 * <p>
	 * Returns is this is a reference element type or a document defined type.
	 * </p>
	 * @return true when either a ref or XMLSchema type.
	 */
	public boolean isRef() {
		return isRef;
	}

	/**
	 * The max number of occurences for this element.
     * 
	 * @return max number of occurences
	 */
	public int getMaxOccurs() {
		return maxOccurs;
	}

	/**
	 * The min number of occurences for this element
	 * 
	 * @return min number of occurences
	 */
	public int getMinOccurs() {
		return minOccurs;
	}

	/**
	 * Attribute name property.
	 * <p>
	 * May be used directly to lookup AttribtueType in FeatureType as an
     * XPATH expression:
	 * </p>
     * <pre><code>
     * AttributeType type = featureType.getAttribtue( attributeTypeConfig.getName() );
     * </code></pre>
     * <p>
     * May also be used to identify the element name in a XMLSchema document
     * genrated by describe type (or our XML based persistence). 
     * </p>
	 * @return Attribute the element name
	 */
	public String getName() {
		return name;
	}

	/**
	 * isNillable purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public boolean isNillable() {
		return nillable;
	}

	/**
	 * getType purpose.
	 * <p>
	 * returns the element type. This is an XML fragment if isRef() returns false.
	 * </p>
	 * @return the element type. This is an XML fragment if isRef() returns false.
	 */
	public String getType() {
		return type;
	}

	/**
	 * setRef purpose.
	 * <p>
	 * Sets whether this is a reference type element or not
	 * </p>
	 * @param b true when this is a reference type element.
	 */
	public void setRef(boolean b) {
		isRef = b;
	}

	/**
	 * setMaxOccurs purpose.
	 * <p>
	 * Stores the max occurs for the element
	 * </p>
	 * @param i the max occurs for the element
	 */
	public void setMaxOccurs(int i) {
		maxOccurs = i;
	}

	/**
	 * setMinOccurs purpose.
	 * <p>
	 * Stores the min occurs for the element
	 * </p>
	 * @param i the min occurs for the element
	 */
	public void setMinOccurs(int i) {
		minOccurs = i;
	}

	/**
	 * setName purpose.
	 * <p>
	 * Stores the name for the element
	 * </p>
	 * @param string the name for the element
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * setNillable purpose.
	 * <p>
	 * Stores if this element is nillable
	 * </p>
	 * @param b true when this element is nillable
	 */
	public void setNillable(boolean b) {
		nillable = b;
	}

	/**
	 * Set the type property
	 * <p>
	 * Stores the type for this element.
     * This is an XML fragment when isComplex() returns false.
	 * </p>
	 * @param type of attribute (as a XML fragment when isComplex() returns false)
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Update AttributeTypeInfo based on a new Data Transfer Object.
	 * <p>
	 * Loads the specified data into this class.
	 * </p>
	 * @param dto AttributeTypeInfoDTO the data source.
	 */
	public void update(AttributeTypeInfoDTO dto){
		isRef = dto.isRef();
		maxOccurs = dto.getMaxOccurs();
		minOccurs = dto.getMinOccurs();
		name = dto.getName();
		nillable = dto.isNillable();
		type = dto.getName();
	}

    /**
     * Package up Configuration as a Data Transfer Object
     * <p>
     * The resulting DTO object can be used to write a backup to disk
     * using global.dto, or used to configure the live GeoSever application
     * using global.
     * </p>
     */    
	public AttributeTypeInfoDTO toDto(){
		AttributeTypeInfoDTO dto = new AttributeTypeInfoDTO();
		dto.setMaxOccurs(maxOccurs);
		dto.setMinOccurs(minOccurs);
		dto.setName(name);
		dto.setNillable(nillable);
		dto.setRef(isRef);
		dto.setType(type);
		return dto;
	}
}
