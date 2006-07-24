/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

/**
 * Represents a MetadataLink Attribute.
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id: MetadataLink.java,v 0.1 Feb 16, 2005 1:32:39 PM $
 */
public class MetaDataLink extends GlobalLayerSupertype {

	/**
	 * 
	 * @uml.property name="type" multiplicity="(0 1)"
	 */
	private String type;

	/**
	 * 
	 * @uml.property name="about" multiplicity="(0 1)"
	 */
	private String about;

	/**
	 * 
	 * @uml.property name="metadataType" multiplicity="(0 1)"
	 */
	private String metadataType;

	/**
	 * 
	 * @uml.property name="content" multiplicity="(0 1)"
	 */
	private String content;

	
	/* (non-Javadoc)
	 * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
	 */
	Object toDTO() {
		return null;
	}

	/**
	 * @return Returns the about.
	 * 
	 * @uml.property name="about"
	 */
	public String getAbout() {
		return about;
	}

	/**
	 * @param about The about to set.
	 * 
	 * @uml.property name="about"
	 */
	public void setAbout(String about) {
		this.about = about;
	}

	/**
	 * @return Returns the metadataType.
	 * 
	 * @uml.property name="metadataType"
	 */
	public String getMetadataType() {
		return metadataType;
	}

	/**
	 * @param metadataType The metadataType to set.
	 * 
	 * @uml.property name="metadataType"
	 */
	public void setMetadataType(String metadataType) {
		this.metadataType = metadataType;
	}

	/**
	 * @return Returns the type.
	 * 
	 * @uml.property name="type"
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 * 
	 * @uml.property name="type"
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns the content.
	 * 
	 * @uml.property name="content"
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content The content to set.
	 * 
	 * @uml.property name="content"
	 */
	public void setContent(String content) {
		this.content = content;
	}

}
