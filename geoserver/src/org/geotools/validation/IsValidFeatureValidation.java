/*
*    Geotools2 - OpenSource mapping toolkit
*    http://geotools.org
*    (C) 2002, Geotools Project Managment Committee (PMC)
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
package org.geotools.validation;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import com.vividsolutions.jts.geom.Geometry;


/**
 * @author bowens
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class IsValidFeatureValidation implements FeatureValidation {

	private String name;
	private String description;
	private String[] typeNames;
	
	
	/**
	 * Empty constructor needed for Java Beans.
	 * 
	 */
	public IsValidFeatureValidation() {
	}
	
	public IsValidFeatureValidation(String name, String description, String[] typeNames) {
		this.name = name;
		this.description = description;
		this.typeNames = typeNames;
	}

	/* (non-Javadoc)
	 * @see org.geotools.validation.Validation#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.geotools.validation.Validation#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.geotools.validation.Validation#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see org.geotools.validation.Validation#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see org.geotools.validation.Validation#getPriority()
	 */
	public int getPriority() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.geotools.validation.Validation#setTypeNames(java.lang.String[])
	 */
	public void setTypeNames(String[] names) {
		this.typeNames = names;
	}

	/* (non-Javadoc)
	 * @see org.geotools.validation.Validation#getTypeNames()
	 */
	public String[] getTypeNames() {
		return typeNames;
	}
	

	/**
	 * F and I call me
	 * <p>
	 * Description ...     Three dots walked into a bar
	 * </p>
	 * @see org.geotools.validation.FeatureValidation#validate(org.geotools.feature.Feature, org.geotools.feature.FeatureType, org.geotools.validation.ValidationResults)
	 * 
	 * @param feature
	 * @param type
	 * @param results
	 * @return
	 */
	public boolean validate(
		Feature feature,
		FeatureType type,
		ValidationResults results) {
		
		Geometry geom =  feature.getDefaultGeometry();
		if (!type.getDefaultGeometry().isNillable()) {
			if (geom == null) {
				results.error(feature, "Geometry was null but is not nillable.");
				return false;
			}
		}
		else
			if (geom == null)
				return true;
		
		if (!geom.isValid()) {
			results.error(feature, "Not a valid geometry. isValid() failed");
			return false;
		}
			
		return true;
	}
	

}
