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

import java.util.HashMap;

import org.geotools.data.DataTestCase;
import org.geotools.data.*;
import org.geotools.data.memory.*;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.IllegalAttributeException;

import junit.framework.TestCase;

/**
 * ValidationProcessorTest purpose.
 * <p>
 * Description of ValidationProcessorTest ...
 * <p>
 * Capabilities:
 * <ul>
 * </li></li>
 * </ul>
 * Example Use:
 * <pre><code>
 * ValidationProcessorTest x = new ValidationProcessorTest(...);
 * </code></pre>
 * 
 * @author bowens, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: ValidationProcessorTest.java,v 1.1.2.1 2003/11/25 18:15:22 jive Exp $
 */
public class ValidationProcessorTest extends DataTestCase {
	MemoryDataStore store;

	ValidationProcessor processor;
	RoadNetworkValidationResults validationResult;
	
	
	/**
	 * Constructor for ValidationProcessorTest.
	 * @param arg0
	 */
	public ValidationProcessorTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		store = new MemoryDataStore();
		store.addFeatures( roadFeatures );
		store.addFeatures( riverFeatures );
		processor = new ValidationProcessor();
		validationResult = new RoadNetworkValidationResults();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		store = null;
		super.tearDown();
	}

	public void testIsValidFeatureValidation() throws Exception
	{
		// the visitor
		RoadNetworkValidationResults validationResults = new RoadNetworkValidationResults();
		processor = new ValidationProcessor(true);
		
	// test the correct roads
		processor.runFeatureTests(this.roadType, DataUtilities.collection(this.roadFeatures), validationResults);
		assertTrue(validationResults.getFailedMessages().length == 0);
		
	// test the broken road
		// make an incorrect line
		try {
			this.newRoad = this.roadType.create(new Object[] {
				new Integer(2), line(new int[] { 1, 2, 1, 2}), "r4"
			}, "road.rd4");
		} catch (IllegalAttributeException e) {}
		Feature[] singleRoad = new Feature[1];
		singleRoad[0] = this.newRoad;
		processor.runFeatureTests(this.roadType, DataUtilities.collection(singleRoad), validationResults);
		assertTrue(validationResults.getFailedMessages().length > 0);


	// run integrity tests
		// make a map of FeatureSources
		HashMap map = new HashMap();
		String[] typeNames = this.store.getTypeNames();
		for (int i=0; i<typeNames.length; i++)
			map.put(typeNames[i], this.store.getFeatureSource(typeNames[i]));
		map.put("newThing", this.store.getFeatureSource(typeNames[0]));
			
		processor.runIntegrityTests(map, null, validationResults);
		assertTrue(validationResults.getFailedMessages().length > 0);
		/*
		String[] messages = validationResults.getFailedMessages();
		for (int i=0; i<validationResults.getFailedMessages().length; i++)
			System.out.println(messages[i]);
		*/	
			
			
	}
	
	

}
