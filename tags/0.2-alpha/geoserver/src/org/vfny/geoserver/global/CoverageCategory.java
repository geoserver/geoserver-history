/* Copyright (c) 2005 NATO - Undersea Research Centre.  All rights reserved.
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

	private String name;
	private String label;
	private NumberRange interval;
	
	/* (non-Javadoc)
	 * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
	 */
	Object toDTO() {
		return null;
	}

	/**
	 * @return Returns the interval.
	 */
	public NumberRange getInterval() {
		return interval;
	}
	/**
	 * @param interval The interval to set.
	 */
	public void setInterval(NumberRange interval) {
		this.interval = interval;
	}
	/**
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
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
}
