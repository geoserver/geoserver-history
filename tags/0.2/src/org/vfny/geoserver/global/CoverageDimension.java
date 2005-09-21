/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

/**
 * Represents a CoverageDimension Attribute.
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: CoverageDimension.java,v 0.1 Feb 16, 2005 1:32:39 PM $
 */
public class CoverageDimension extends GlobalLayerSupertype {

	/**
	 * 
	 * @uml.property name="name" multiplicity="(0 1)"
	 */
	private String name;

	/**
	 * 
	 * @uml.property name="description" multiplicity="(0 1)"
	 */
	private String description;

	/**
	 * 
	 * @uml.property name="nullValues" multiplicity="(0 1)"
	 */
	private Double[] nullValues;

	/**
	 * 
	 * @uml.property name="categories"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 */
	private CoverageCategory[] categories;

	
	/* (non-Javadoc)
	 * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
	 */
	Object toDTO() {
		return null;
	}

	/**
	 * @return Returns the categories.
	 * 
	 * @uml.property name="categories"
	 */
	public CoverageCategory[] getCategories() {
		return categories;
	}

	/**
	 * @param categories The categories to set.
	 * 
	 * @uml.property name="categories"
	 */
	public void setCategories(CoverageCategory[] categories) {
		this.categories = categories;
	}

	/**
	 * @return Returns the description.
	 * 
	 * @uml.property name="description"
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 * 
	 * @uml.property name="description"
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the name.
	 * 
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 * 
	 * @uml.property name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the nullValues.
	 * 
	 * @uml.property name="nullValues"
	 */
	public Double[] getNullValues() {
		return nullValues;
	}

	/**
	 * @param nullValues The nullValues to set.
	 * 
	 * @uml.property name="nullValues"
	 */
	public void setNullValues(Double[] nullValues) {
		this.nullValues = nullValues;
	}

}
