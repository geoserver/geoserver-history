/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

/**
 * AttributeTypeInfoDTO purpose.
 * <p>
 * Represents most of a xs:element for an XMLSchema.
 * 
 * we have three types of information to store, Schema defined types, 
 * references and extentions on types. If the type represented is either 
 * a reference or a Schema defined type  then isRef should be true. 
 * <p>
 * If isRef is true then we have one of two situations. If the name is 
 * not specified then the type represents a "<code>ref='gml:xyz'</code>", otherwise 
 * it's of the form "<code>name='foo'</code>" and "<code>type='bar'</code>".<br> 
 * The third case isRef is false, "<code>name='foo'</code>" and type contains an XML 
 * fragment defining the type.
 * </p>
 * <p>
 * minOccurs, maxOccurs and nillable are all attributes for all cases. There is
 * more stuff in the XMLSchema spec but we don't care.
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: AttributeTypeInfoDTO.java,v 1.2 2004/01/12 21:01:29 dmzwiers Exp $
 */
public class AttributeTypeInfoDTO  implements DataTransferObject {
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
	 * AttributeTypeInfoDTO constructor.
	 * <p>
	 * Default constructor, does nothing
	 * </p>
	 *
	 */
	public AttributeTypeInfoDTO(){}
	
	/**
	 * AttributeTypeInfoDTO constructor.
	 * <p>
	 * Copies the data from the specified DTO to this one.
	 * </p>
	 * @param dto AttributeTypeInfoDTO The data source to copy from.
	 */
	public AttributeTypeInfoDTO(AttributeTypeInfoDTO dto){
		name = dto.getName(); 
		type = dto.getType();
		minOccurs = dto.getMinOccurs();
		maxOccurs = dto.getMaxOccurs();
		nillable = dto.isNillable();
		isRef = dto.isRef();
	}

	/**
	 * Implement equals.
	 * <p>
	 * true when the data contained inside the objects is the same.
	 * </p>
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj an instance of AttributeTypeInfoDTO to compare
	 * @return true when they are the same, false otherwise
	 */
	public boolean equals(Object obj){
		boolean r = true;
		if(obj == null || !(obj instanceof AttributeTypeInfoDTO))
			return false;
		AttributeTypeInfoDTO dto = (AttributeTypeInfoDTO)obj;
		r = r && name == dto.getName(); 
		r = r && type == dto.getType();
		r = r && minOccurs == dto.getMinOccurs();
		r = r && maxOccurs == dto.getMaxOccurs();
		r = r && nillable == dto.isNillable();
		r = r && isRef == dto.isRef();
		return r;
	}
	
	/**
	 * Implement hashCode.
	 * <p>
	 * The hashcode for this object.
	 * </p>
	 * @see java.lang.Object#hashCode()
	 * 
	 * @return a hashcode value.
	 */
	public int hashCode(){
		return name.hashCode()*type.hashCode();
	}

	/**
	 * Implement clone.
	 * <p>
	 * An instance of AttributeTypeInfoDTO which is the same as this one.
	 * </p>
	 * @see java.lang.Object#clone()
	 * 
	 * @return Object a copy of this object.
	 */
	public Object clone(){
		AttributeTypeInfoDTO dto = new AttributeTypeInfoDTO();
		dto.setMaxOccurs(maxOccurs);
		dto.setMinOccurs(minOccurs);
		dto.setName(name);
		dto.setNillable(nillable);
		dto.setRef(isRef);
		dto.setType(type);
		return dto;
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
	 * setType purpose.
	 * <p>
	 * Stores the type for this element. This is an XML fragment when isRef() returns false.
	 * </p>
	 * @param stringthe type for this element. This is an XML fragment when isRef() returns false.
	 */
	public void setType(String string) {
		type = string;
	}

}
