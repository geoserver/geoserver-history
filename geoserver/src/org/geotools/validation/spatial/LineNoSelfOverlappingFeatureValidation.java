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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;


/**
 * LineNoSelfOverlappingFeatureValidation
 * <p>
 * Tests to see if a LineString overlaps itself. It does this by breaking up
 * the LineString into two point segments then intersects them all. If a
 * segment has both of its points on another segment, then they overlap.
 * This is not true in all cases and this method has to be rewritten. If a
 * segment spans two segments, this method will say that they do not overlap
 * when clearly they do.
 * <p>
 * Capabilities:
 * <ul>
 * </li></li>
 * </ul>
 * Example Use:
 * <pre><code>
 * LineNoSelfOverlappingFeatureValidation x = new LineNoSelfOverlappingFeatureValidation("noSelfIntersectRoads", "Tests to see if a 
 * geometry intersects itself", new String[] {"road"});
 * </code></pre>
 * 
 * @author bowens, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 * @version $Id: LineNoSelfOverlappingFeatureValidation.java,v 1.1.2.2 2003/11/26 08:02:53 sploreg Exp $
 */
public class LineNoSelfOverlappingFeatureValidation implements FeatureValidation {
    /** The logger for the validation module. */
    private static final Logger LOGGER = Logger.getLogger(
            "org.geotools.validation");
            
	private String name;				// name of the validation
	private String description;			// description of the validation
	private String[] typeNames;			// the TypeNames that this validation
										//   is performed on.
	

	/**
	 * LineNoSelfOverlappingFeatureValidation constructor.
	 * <p>
	 * Description
	 * </p>
	 * 
	 */
	public LineNoSelfOverlappingFeatureValidation() {
	}
	
	/**
	 * LineNoSelfOverlappingFeatureValidation constructor.
	 * <p>
	 * Initializes allinformation needed to perform the validation.
	 * </p>
	 * @param name The name of the validation
	 * @param description The description of this validation.
	 * @param typeNames The TypeNames that this validation is tested on.
	 */
	public LineNoSelfOverlappingFeatureValidation(String name, String description, String[] typeNames) {
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
	 * Tests to see if a LineString overlaps itself. It does this by breaking up
	 * the LineString into two point segments then intersects them all. If a
	 * segment has both of its points on another segment, then they overlap.
	 * This is not true in all cases and this method has to be rewritten. If a
	 * segment spans two segments, this method will say that they do not overlap
	 * when clearly they do.
	 * 
	 * </p>
	 * @see org.geotools.validation.FeatureValidation#validate(org.geotools.feature.Feature, org.geotools.feature.FeatureType, org.geotools.validation.ValidationResults)
	 * 
 	 * @param feature The Feature to be validated
	 * @param type The FeatureType of the feature
	 * @param results The storage for error messages.
	 * @return True if the feature does not overlap itself.
	 */
	public boolean validate(
		Feature feature,
		FeatureType type,
		ValidationResults results){
		//BUG: refer to comments above.
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
			int numPoints = ls.getNumPoints();
			
			// break up the LineString into line segments
			LineString[] segments = new LineString[numPoints-1];
			for (int i=0; i<numPoints-1; i++)
			{
				Coordinate[] coords = new Coordinate[] {ls.getCoordinateN(i), 
														ls.getCoordinateN(i+1)};
				segments[i] = gf.createLineString(coords);
			}
			
			// overlap all of the line segments with each other
			for (int i=0; i<segments.length; i++)	// for each line segment
			{
				for (int j=0; j<segments.length; j++)	// test with every other line segment
				{
					if (i!=j && (i-1 != j) && (i+1 != j))	// if they aren't the same segment
					{
						// generate two points out of segment[i]
						Point p1 = gf.createPoint(segments[i].getCoordinateN(0));
						Point p2 = gf.createPoint(segments[i].getCoordinateN(0));
						if (p1.touches(segments[j]) && p2.touches(segments[j]))	// if they overlap
						{
							// log the error and return
							String message = "LineString overlapped itself.";
							results.error(feature, message );
							LOGGER.log( Level.FINEST, getName()+"("+feature.getID()+"):"+message );                
							return false;
						}
					}	
				}
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
