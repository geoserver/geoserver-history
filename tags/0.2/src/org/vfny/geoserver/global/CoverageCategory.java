/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geotools.util.NumberRange;

/**
 * Represents a CoverageCategory Attribute.
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: CoverageCategory.java,v 0.1 Feb 16, 2005 1:32:39 PM $
 */
public class CoverageCategory extends GlobalLayerSupertype {

	/**
	 * 
	 * @uml.property name="name" multiplicity="(0 1)"
	 */
	private String name;

	/**
	 * 
	 * @uml.property name="label" multiplicity="(0 1)"
	 */
	private String label;

	/**
	 * 
	 * @uml.property name="interval"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private NumberRange interval;

	
	/* (non-Javadoc)
	 * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
	 */
	Object toDTO() {
		return null;
	}

	/**
	 * @return Returns the interval.
	 * 
	 * @uml.property name="interval"
	 */
	public NumberRange getInterval() {
		return interval;
	}

	/**
	 * @param interval The interval to set.
	 * 
	 * @uml.property name="interval"
	 */
	public void setInterval(NumberRange interval) {
		this.interval = interval;
	}

	/**
	 * @return Returns the label.
	 * 
	 * @uml.property name="label"
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label The label to set.
	 * 
	 * @uml.property name="label"
	 */
	public void setLabel(String label) {
		this.label = label;
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

}
