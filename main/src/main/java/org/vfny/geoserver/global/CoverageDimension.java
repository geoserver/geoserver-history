/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geotools.util.NumberRange;

/**
 * Represents a CoverageDimension Attribute.
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id: CoverageDimension.java,v 0.1 Feb 16, 2005 1:32:39 PM $
 */
public class CoverageDimension extends GlobalLayerSupertype {

	/**
	 * 
	 */
	private String name;

	/**
	 * 
	 */
	private String description;

	/**
	 * 
	 */
	private Double[] nullValues;

	private NumberRange range;

	
	/* (non-Javadoc)
	 * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
	 */
	Object toDTO() {
		return null;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the nullValues.
	 */
	public Double[] getNullValues() {
		return nullValues;
	}

	/**
	 * @param nullValues The nullValues to set.
	 */
	public void setNullValues(Double[] nullValues) {
		this.nullValues = nullValues;
	}

	/**
	 * @param range
	 */
	public void setRange(NumberRange range) {
		this.range = range;
	}

	public NumberRange getRange() {
		return range;
	}
}