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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.feature.Feature;
import org.geotools.validation.IntegrityValidation;
import org.geotools.validation.ValidationResults;

import com.vividsolutions.jts.geom.Envelope;

/**
 * UniqueFIDIntegrityValidation purpose.
 * <p>
 * This validation plugIn checks to see if every feature has a 
 * unique ID (column) specified by uniqueID. The FeatureTypes it checks against
 * are defined by typeNames[]. If a duplicate ID is detected, an error message 
 * returned via a ValidationResult used as a visitor in the validation() method.
 * <p>
 * Capabilities:
 * <ul>
 * </li></li>
 * </ul>
 * Example Use:
 * <pre><code>
 * UniqueFIDIntegrityValidation x = new UniqueFIDIntegrityValidation("uniqueFID_road", "Checks if each feature has a unique ID", new String[] {"road", "river"}, "FID");
 * x.validate();
 * </code></pre>
 * 
 * @author bowens, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: UniqueFIDIntegrityValidation.java,v 1.2.2.1 2003/12/30 23:00:46 dmzwiers Exp $
 */
public class UniqueFIDIntegrityValidation implements IntegrityValidation {


	private String name;			// name of the validation
	private String description;		// description of the validation
	private String[] typeNames;		// TypeNames that this validation tests
	private String uniqueID;		// the column name that this validation checks
									//   to see if they are all unique.
		
		
	/**
	 * UniqueFIDIntegrityValidation constructor.
	 * <p>
	 * An empty constructor placed here for Java Beans
	 * </p>
	 * 
	 */
	public UniqueFIDIntegrityValidation() {
	}

	/**
	 * UniqueFIDIntegrityValidation constructor.
	 * <p>
	 * Initializes allinformation needed to perform the validation.
	 * </p>
	 * @param name The name of this validation.
	 * @param description The description of this validation.
	 * @param typeNames The TypeNames that this validation is tested on.
	 * @param uniqueID The column name that this validation checks to see if it is unique. 
	 */
	public UniqueFIDIntegrityValidation(String name, String description, String[] typeNames, String uniqueID) {
		this.name = name;
		this.description = description;
		this.typeNames = typeNames;
		this.uniqueID = uniqueID;
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
	 * @return True if there were no errors. False if there were errors.
	 */
	public boolean validate(Map layers, Envelope envelope, ValidationResults results) throws Exception{
		
		HashMap FIDs = new HashMap();	// FIDs used for lookup to see if any match
		boolean result = true;
		Iterator it = layers.values().iterator();
		
		//TODO: get the needed layers from the database and use them instead
		
		while (it.hasNext())// for each layer
		{
			FeatureSource featureSource = (FeatureSource) it.next();
			FeatureReader reader = featureSource.getFeatures().reader();
			try {
				 
				while (reader.hasNext())	// for each feature
				{
					Feature feature = reader.next();
					String fid = feature.getID();
					if(FIDs.containsKey(fid))	// if a FID like this one already exists
					{
						results.error(feature, "FID already exists.");
						result = false;
					}
					else
						FIDs.put(fid, fid);
				}
			}
			finally {
				reader.close();		// this is an important line	
			}

		}
		
		return result;
	}

}
