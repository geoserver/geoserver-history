/* Copyright (c) 2005 NATO - Undersea Research Centre.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

/**
 * Represents a MetadataLink Attribute.
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: MetadataLink.java,v 0.1 Feb 16, 2005 1:32:39 PM $
 */
public class MetaDataLink extends GlobalLayerSupertype {

	private String type;
	private String about;
	private String metadataType;
	private String content;
	
	/* (non-Javadoc)
	 * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
	 */
	Object toDTO() {
		return null;
	}

	/**
	 * @return Returns the about.
	 */
	public String getAbout() {
		return about;
	}
	/**
	 * @param about The about to set.
	 */
	public void setAbout(String about) {
		this.about = about;
	}
	/**
	 * @return Returns the metadataType.
	 */
	public String getMetadataType() {
		return metadataType;
	}
	/**
	 * @param metadataType The metadataType to set.
	 */
	public void setMetadataType(String metadataType) {
		this.metadataType = metadataType;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
