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
package org.geotools.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geotools.data.FeatureSource;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.validation.attributes.UniqueFIDIntegrityValidation;
import org.geotools.validation.spatial.IsValidFeatureValidation;

import com.vividsolutions.jts.geom.Envelope;

/**
 * ValidationProcessor purpose.
 * <p>
 * Description of ValidationProcessor ...
 * <p>
 * Capabilities:
 * <ul>
 * </li></li>
 * </ul>
 * Example Use:
 * <pre><code>
 * ValidationProcessor x = new ValidationProcessor(...);
 * </code></pre>
 * 
 * @author bowens, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: ValidationProcessor.java,v 1.1.2.1 2003/11/25 06:10:32 jive Exp $
 */
public class ValidationProcessor {

	HashMap featureLookup;		// a hashMap with featureTypes as keys that map to array lists 
								//    of feature validation tests
	HashMap integrityLookup;	// a hashMap with featureTypes as keys that map to array lists 
								//	of integrity validation tests
	HashMap plugIns;
	
	ArrayList modifiedFeatureTypes;	// a list of feature types that have been modified
									// These are no longer used for Integrity Validation tests
	
	public final static Object ANYTYPENAME = new Object();
	
	
	/**
	 * ValidationProcessor constructor.
	 * <p>
	 * Description
	 * </p>
	 * 
	 */
	public ValidationProcessor() {
		featureLookup = new HashMap();
		integrityLookup = new HashMap();
		plugIns = new HashMap();
	}
	
	
	/**
	 * ValidationProcessor constructor.
	 * <p>
	 * Description
	 * </p>
	 * @param testRun
	 */
	public ValidationProcessor(boolean testRun) {
		featureLookup = new HashMap();
		integrityLookup = new HashMap();
		plugIns = new HashMap();
		if (testRun)
			testInit();
	}

	private void testInit()
	{
		// create a feature validation tests
		IsValidFeatureValidation isValidFV = new IsValidFeatureValidation("isValidALL", "Tests to see if a geometry is valid", Validation.ALL);
		IsValidFeatureValidation isValidFV_roads = new IsValidFeatureValidation("isValidRoads", "Tests to see if a geometry is valid", new String[] {"road"});
		
		// add them to the featureLookup map
		addToFVLookup(isValidFV);
		addToFVLookup(isValidFV_roads);
		
		// create integrity validation tests
		UniqueFIDIntegrityValidation uniqueFID = new UniqueFIDIntegrityValidation("uniqueFID", "Checks if each feature has a unique ID", Validation.ALL, "FID");
		UniqueFIDIntegrityValidation uniqueFID_rivers = new UniqueFIDIntegrityValidation("uniqueFID_rivers", "Checks if each feature has a unique ID", new String[] {"river"}, "FID");
		UniqueFIDIntegrityValidation uniqueFID_road = new UniqueFIDIntegrityValidation("uniqueFID_road", "Checks if each feature has a unique ID", new String[] {"road"}, "FID");
		
		// add them to the integrityLookup map
		addToIVLookup(uniqueFID);
		addToIVLookup(uniqueFID_rivers);
		addToIVLookup(uniqueFID_road);
		
	}
	
	/**
	 * addToLookup purpose.
	 * <p>
	 * Description:
	 * Add the validation test to the map for every featureType that it validates
	 * </p>
	 * @param isValidFV
	 */
	private void addToFVLookup(FeatureValidation validation) 
	{
		String[] featureTypeList = validation.getTypeNames();

		if (featureTypeList == Validation.ALL)	// if null
		{
			ArrayList tests = (ArrayList)featureLookup.get(ANYTYPENAME);
			if (tests == null)
				tests = new ArrayList();
			tests.add(validation);
			featureLookup.put(ANYTYPENAME, tests);
		}
		else
		{
			for (int i=0; i<featureTypeList.length; i++)
			{
				ArrayList tests = (ArrayList)featureLookup.get(featureTypeList[i]);
				if (tests == null)
					tests = new ArrayList();
				tests.add(validation);
				featureLookup.put(featureTypeList[i], tests);
			}
		}
		
	}
	
	
	private void addToIVLookup(IntegrityValidation validation)
	{
		String[] integrityTypeList = validation.getTypeNames();

		if (integrityTypeList == Validation.ALL)	// if null
		{
			ArrayList tests = (ArrayList)integrityLookup.get(ANYTYPENAME);
			if (tests == null)
				tests = new ArrayList();
			tests.add(validation);
			integrityLookup.put(ANYTYPENAME, tests);
		}
		else
		{
			for (int i=0; i<integrityTypeList.length; i++)
			{
				ArrayList tests = (ArrayList)integrityLookup.get(integrityTypeList[i]);
				if (tests == null)
					tests = new ArrayList();
				tests.add(validation);
				integrityLookup.put(integrityTypeList[i], tests);
			}
		}
	}


	/**
	 * addPlugin adds 
	 * <p>
	 * Description ...
	 * </p>
	 * @param bean
	 * @param config configuration for the plugIn. Defined in plugin.xml
	 */
	public void addPlugIn( String bean, Map config)
	{
		//PlugIn plugin = new PlugIn( name, bean, text, config);
		//plugIns.put(name, plugin)
		
		Iterator it = config.values().iterator();
		
	}


	public void addPlugIn( String name, String bean, String text, Map config)
	{
		//PlugIn plugin = new PlugIn( name, bean, text, config);
		//plugIns.put(name, plugin)
	}
	
	
	public void runFeatureTests(FeatureType type, FeatureCollection collection, ValidationResults results) throws Exception
	{
		// check for any tests that are to be performed on ALL features
		ArrayList tests = (ArrayList) featureLookup.get(ANYTYPENAME);
		
		// check for any FeatureType specific tests
		ArrayList FT_tests = (ArrayList) featureLookup.get(type.getTypeName());

		// append featureType specific tests to the list of tests		
		if (FT_tests != null)
		{
			if (tests != null)
			{
				Iterator it = FT_tests.iterator();
				while (it.hasNext())
					tests.add((FeatureValidation)it.next());
			}
			else
				tests = FT_tests;
		}

		if (tests != null)	// if we found some tests to be performed on this FeatureType
		{
			// for each test that is to be performed on this featureType
			for (int i=0; i<tests.size(); i++)
			{
				FeatureValidation validator = (FeatureValidation) tests.get(i);
				results.setValidation(validator);
				Iterator it = collection.iterator();
				while (it.hasNext())// iterate through each feature and run the test on it
				{
					Feature feature = (Feature) it.next();
					validator.validate(feature, type, results);
				}
			}
		}		
		
	}
	
	
	/**
	 * runIntegrityTests purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param stores the Map of modified features (HashMaps of key=featureTypeName, value="featureStore"
	 * @param envelope
	 * @param results
	 * @throws Exception
	 */
	public void runIntegrityTests(HashMap stores, Envelope envelope, ValidationResults results) throws Exception
	{
		// stores is a HashMap of FeatureSources
		FeatureSource[] sources = new FeatureSource[stores.size()];
		Object[] array = stores.values().toArray();
		for (int i=0; i<stores.size(); i++)
		{
			if (array[i] instanceof FeatureSource)
				sources[i] = (FeatureSource) array[i];
			//else
			//	crap out and die
		} 
		// for each modified featureType
		for (int i=0; i<sources.length; i++)
		{
			// check for any tests that are to be performed on ALL features
			ArrayList tests = (ArrayList) integrityLookup.get(ANYTYPENAME);

			// check for any FeatureType specific tests
			ArrayList FT_tests = (ArrayList) integrityLookup.get(sources[i].getSchema().getTypeName());
			
			// append featureType specific integrity tests to the list of tests		
			if (FT_tests != null)
			{
				if (tests != null)
				{
					Iterator it = FT_tests.iterator();
					while (it.hasNext())
						tests.add((IntegrityValidation)it.next());
				}
				else
					tests = FT_tests;
			}
			 
			if (tests != null)	// if we found some tests to be performed on this FeatureType
			{
				// for each test that is to be performed on this featureType
				for (int j=0; j<tests.size(); j++)
				{
					IntegrityValidation validator = (IntegrityValidation) tests.get(j);
					results.setValidation(validator);
					validator.validate(stores, envelope, results);

				}
			}
			 
		}// end for each modified featureType
		
	}
}
