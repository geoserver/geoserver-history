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
package org.geotools.validation.spatial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.feature.Feature;
import org.geotools.validation.IntegrityValidation;
import org.geotools.validation.ValidationResults;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * LinesNotIntersectIntegrityValidation purpose.
 * <p>
 * This validation plugIn checks to see if any features intersect. If they do then
 * the validation failed.
 * <p>
 * Capabilities:
 * <ul>
 * </li></li>
 * </ul>
 * Example Use:
 * <pre><code>
 * LinesNotIntersectIntegrityValidation x = new LinesNotIntersectIntegrityValidation("uniqueFID_road", "Checks if each feature has a unique ID", new String[] {"road", "river"}, "FID");
 * x.validate();
 * </code></pre>
 * 
 * @author bowens, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 * @version $Id: LinesNotIntersectIntegrityValidation.java,v 1.1.2.2 2003/11/26 08:02:53 sploreg Exp $
 */
public class LinesNotIntersectIntegrityValidation implements IntegrityValidation {


	private String name;			// name of the validation
	private String description;		// description of the validation
	private String[] typeNames;		// TypeNames that this validation tests

		
		
	/**
	 * LinesNotIntersectIntegrityValidation constructor.
	 * <p>
	 * An empty constructor placed here for Java Beans
	 * </p>
	 * 
	 */
	public LinesNotIntersectIntegrityValidation() {
	}

	/**
	 * LinesNotIntersectIntegrityValidation constructor.
	 * <p>
	 * Initializes allinformation needed to perform the validation.
	 * </p>
	 * @param name The name of this validation.
	 * @param description The description of this validation.
	 * @param typeNames The TypeNames that this validation is tested on.
	 * @param uniqueID The column name that this validation checks to see if it is unique. 
	 */
	public LinesNotIntersectIntegrityValidation(String name, String description, String[] typeNames) {
		this.name = name;
		this.description = description;
		this.typeNames = typeNames;
	}

	/**
	 * Override setName.
	 * <p>
	 * Sets the name of this validation.
	 * </p>
	 * @see org.geotools.validation.Validation#setName(java.lang.String)
	 * 
	 * @param name The name of this validation.
	 * @return returns the name of this validation.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Override getName.
	 * <p>
	 * Returns the name of this particular validation.
	 * </p>
	 * @see org.geotools.validation.Validation#getName()
	 * 
	 * @return The name of this particular validation.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Override setDescription.
	 * <p>
	 * Sets the description of this validation.
	 * </p>
	 * @see org.geotools.validation.Validation#setDescription(java.lang.String)
	 * 
	 * @param description The description of the validation.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Override getDescription.
	 * <p>
	 * Returns the description of this validation as a string.
	 * </p>
	 * @see org.geotools.validation.Validation#getDescription()
	 * 
	 * @return The description of this validation.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Override getPriority.
	 * <p>
	 * Sets the priority level of this validation.
	 * </p>
	 * @see org.geotools.validation.Validation#getPriority()
	 * 
	 * @return A made up priority for this validation.
	 */
	public int getPriority() {
		return 10;
	}

	/**
	 * Override setTypeNames.
	 * <p>
	 * Sets the TypeNames of the FeatureTypes used in this particular validation.
	 * </p>
	 * @see org.geotools.validation.Validation#setTypeNames(java.lang.String[])
	 * 
	 * @param names The TypeNames of the FeatureTypes used in this particular validation.
	 */
	public void setTypeNames(String[] names) {
		this.typeNames = names;
	}

	/**
	 * Override getTypeNames.
	 * <p>
	 * Returns the TypeNames of the FeatureTypes used in this particular validation.
	 * </p>
	 * @see org.geotools.validation.Validation#getTypeNames()
	 * 
	 * @return An array of TypeNames
	 */
	public String[] getTypeNames() {
		return typeNames;
	}


	/**
	 * Override validate.
	 * <p>
	 * Description ...
	 * This is supposed to go off and grab the necesary features from the 
	 * database using the envelope with the typeNames. But it doesn't yet. 
	 * It just uses the ones passed in through parameter layers.
	 * </p>
	 * @see org.geotools.validation.IntegrityValidation#validate(java.util.Map, com.vividsolutions.jts.geom.Envelope, org.geotools.validation.ValidationResults)
	 * 
	 * @param layers a HashMap of key="TypeName" value="FeatureSource"
	 * @param envelope The bounding box of modified features
	 * @param results Storage for the error and warning messages
	 * @return True if no features intersect. If they do then the validation failed.
	 */
	public boolean validate(Map layers, Envelope envelope, ValidationResults results) throws Exception{
		
		ArrayList geoms = new ArrayList();	// FIDs used for lookup to see if any match
		boolean result = true;
		Iterator it = layers.values().iterator();
		
		//TODO: get the needed layers from the database and use them instead?
		
		while (it.hasNext())// for each layer
		{
			FeatureSource featureSource = (FeatureSource) it.next();
			FeatureReader reader = featureSource.getFeatures().reader();
			try {
				 
				while (reader.hasNext())	// for each feature
				{
					// check if it intersects any of the previous features
					Feature feature = reader.next();
					Geometry geom = feature.getDefaultGeometry();
					for (int i=0; i<geoms.size(); i++)	// for each existing geometry
					{
						// I don't trust this thing to work correctly
						if (geom.crosses((Geometry) geoms.get(i)))
						{
							results.error(feature, "Lines cross when they shouldn't.");
							result = false;
						}
					}
					geoms.add(geom);
				}
			}
			finally {
				reader.close();		// this is an important line	
			}

		}
		
		return result;
	}

}
