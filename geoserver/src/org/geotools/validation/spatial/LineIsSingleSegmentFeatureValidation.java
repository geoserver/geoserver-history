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
package org.geotools.validation.spatial;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.validation.FeatureValidation;
import org.geotools.validation.ValidationResults;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;


/**
 * LineIsSingleSegmentFeatureValidation purpose.
 * <p>
 * Tests to see if a LineString is made of only one segment, meaning it only has
 * two points. If the LineString has more than two points, the test fails.
 * <p>
 * Capabilities:
 * <ul>
 * </li></li>
 * </ul>
 * Example Use:
 * <pre><code>
 * LineIsSingleSegmentFeatureValidation x = new LineIsSingleSegmentFeatureValidation("noSelfIntersectRoads", "Tests to see if a 
 * geometry intersects itself", new String[] {"road"});
 * </code></pre>
 * 
 * @author bowens, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: LineIsSingleSegmentFeatureValidation.java,v 1.3 2004/01/12 21:01:31 dmzwiers Exp $
 */
public class LineIsSingleSegmentFeatureValidation implements FeatureValidation {
    /** The logger for the validation module. */
    private static final Logger LOGGER = Logger.getLogger(
            "org.geotools.validation");
            
	private String name;				// name of the validation
	private String description;			// description of the validation
	private String[] typeNames;			// the TypeNames that this validation
										//   is performed on.
	

	/**
	 * LineIsSingleSegmentFeatureValidation constructor.
	 * <p>
	 * Description
	 * </p>
	 * 
	 */
	public LineIsSingleSegmentFeatureValidation() {
	}
	
	/**
	 * LineIsSingleSegmentFeatureValidation constructor.
	 * <p>
	 * Initializes allinformation needed to perform the validation.
	 * </p>
	 * @param name The name of the validation
	 * @param description The description of this validation.
	 * @param typeNames The TypeNames that this validation is tested on.
	 */
	public LineIsSingleSegmentFeatureValidation(String name, String description, String[] typeNames) {
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
	 * Tests to see if a LineString is made of only one segment, meaning it only has
	 * two points. If the LineString has more than two points, the test fails.
	 * </p>
	 * @see org.geotools.validation.FeatureValidation#validate(org.geotools.feature.Feature, org.geotools.feature.FeatureTypeInfo, org.geotools.validation.ValidationResults)
	 * 
 	 * @param feature The Feature to be validated
	 * @param type The FeatureTypeInfo of the feature
	 * @param results The storage for error messages.
	 * @return True if the feature is simple (one segment).
	 */
	public boolean validate(
		Feature feature,
		FeatureType type,
		ValidationResults results){
		
		LOGGER.setLevel(Level.ALL);   
		
		Geometry geom =  feature.getDefaultGeometry();
        if( geom == null )
        {
			results.error(feature, "Geometry is null - cannot validate.");
			return false;
        }
        
    	if (geom instanceof LineString)
    	{
    		if (geom.getNumPoints() < 2)
    		{
				results.error(feature, "LineString contains too few points - cannot validate.");
    			return false;
    		}
    		
    		GeometryFactory gf = new GeometryFactory();
    		
			// get the LineString out of the Geometry
			LineString ls = (LineString)geom;
			if (ls.getNumPoints() > 2)
			{
				// log the error and return
				String message = "LineString is not a simple segment (more than 1 segment exists).";
				results.error(feature, message );
				LOGGER.log( Level.FINEST, getName()+"("+feature.getID()+"):"+message );                
				return false;
			}
	
    	}
    	else
    	{
			results.error(feature, "Geometry not a LineString - cannot validate.");
			return false;
    	}
            

        LOGGER.log( Level.FINEST, getName()+"("+feature.getID()+") passed" );
       
		return true;
	}
	

}
