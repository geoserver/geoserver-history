/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2003, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.geotools.validation.attributes;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.validation.FeatureValidation;
import org.geotools.validation.ValidationResults;

/**
 * NullZeroFeatureValidation purpose.
 * <p>
 * Description of NullZeroFeatureValidation ...
 * <p>
 * Capabilities:
 * <ul>
 * </li></li>
 * </ul>
 * Example Use:
 * <pre><code>
 * NullZeroFeatureValidation x = new NullZeroFeatureValidation(...);
 * </code></pre>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: NullZeroFeatureValidation.java,v 1.1.2.1 2003/12/16 00:37:23 jive Exp $
 */
public class NullZeroFeatureValidation implements FeatureValidation {

	private String path;
	private String name = "";
	private String description = "";
	private String[] typeNames;

	/**
	 * Implement validate.
	 * <p>
	 * Description ...
	 * </p>
	 * @see org.geotools.validation.FeatureValidation#validate(org.geotools.feature.Feature, org.geotools.feature.FeatureType, org.geotools.validation.ValidationResults)
	 * 
	 * @param feature
	 * @param type
	 * @param results
	 * @return
	 * @throws Exception
	 */
	public boolean validate(
		Feature feature,
		FeatureType type,
		ValidationResults results)
		throws Exception {
			Object ft = feature.getAttribute(path);
			if(ft==null){
				results.error(feature,path +" is Empty");
				return false;
			}
			if(ft instanceof Number){
				Number nb = (Number)ft;
				if(nb.intValue()==0){
					results.error(feature,path +" is Zero");
					return false;
				}
			}
		return true;
	}

	/**
	 * Implement setName.
	 * <p>
	 * Description ...
	 * </p>
	 * @see org.geotools.validation.Validation#setName(java.lang.String)
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Implement getName.
	 * <p>
	 * Description ...
	 * </p>
	 * @see org.geotools.validation.Validation#getName()
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Implement setDescription.
	 * <p>
	 * Description ...
	 * </p>
	 * @see org.geotools.validation.Validation#setDescription(java.lang.String)
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;

	}

	/**
	 * Implement getDescription.
	 * <p>
	 * Description ...
	 * </p>
	 * @see org.geotools.validation.Validation#getDescription()
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Implement getPriority.
	 * <p>
	 * Description ...
	 * </p>
	 * @see org.geotools.validation.Validation#getPriority()
	 * 
	 * @return
	 */
	public int getPriority() {
		return 0;
	}

	/**
	 * Implement setTypeNames.
	 * <p>
	 * Description ...
	 * </p>
	 * @see org.geotools.validation.Validation#setTypeNames(java.lang.String[])
	 * 
	 * @param names
	 */
	public void setTypeNames(String[] names) {
		typeNames = names;
	}

	/**
	 * Implement getTypeNames.
	 * <p>
	 * Description ...
	 * </p>
	 * @see org.geotools.validation.Validation#getTypeNames()
	 * 
	 * @return
	 */
	public String[] getTypeNames() {
		return typeNames;
	}

	/**
	 * getPath purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * setPath purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setPath(String string) {
		path = string;
	}

}
