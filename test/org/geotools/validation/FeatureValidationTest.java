package org.geotools.validation;

import java.util.HashMap;
import java.util.Map;

import org.geotools.data.*;
import org.geotools.data.memory.*;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.validation.spatial.IsValidFeatureValidation;

import org.vfny.geoserver.requests.wfs.*;
import org.vfny.geoserver.config.*;

/**
 * FeatureValidationTest purpose.
 * <p>
 * Description of FeatureValidationTest ...
 * <p>
 * 
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: cholmesny $ (last modification)
 * @version $Id: FeatureValidationTest.java,v 1.2 2003/12/16 18:46:10 cholmesny Exp $
 */
public class FeatureValidationTest extends DataTestCase {
    MemoryDataStore store;
    
    /**
     * FeatureValidationTest constructor.
     * <p>
     * Run test <code>testName</code>.
     * </p>
     * @param testName
     */
    public FeatureValidationTest(String testName) {
        super(testName);
    }

    /**
     * Construct data store for use.
     * 
     * @see junit.framework.TestCase#setUp()
     * 
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        store = new MemoryDataStore();
        store.addFeatures( roadFeatures );
        store.addFeatures( riverFeatures );     
    }
    
    /**
     * Override tearDown.
     *
     * @see junit.framework.TestCase#tearDown()
     * 
     * @throws Exception
     */
    protected void tearDown() throws Exception {
        store = null;
        super.tearDown();
    }
    
	public void testIsValidFeatureValidation()
   	{
   		// the visitor
		RoadValidationResults validationResults = new RoadValidationResults();
		
   		IsValidFeatureValidation validator = new IsValidFeatureValidation("isValidRoad", "Tests to see if a road is valid", IsValidFeatureValidation.ALL);
   		validationResults.setValidation(validator);
		assertTrue(validator.validate(this.newRoad,this.roadType, validationResults));
		try {
			this.newRoad = this.roadType.create(new Object[] {
				new Integer(2), line(new int[] { 1, 2, 1, 2}), "r4"
			}, "road.rd4");
		} catch (IllegalAttributeException e) {}
		assertTrue(!validator.validate(this.newRoad,this.roadType, validationResults));	// validate will return false
   	}
   	
   	
   	
   	/**
	 * testABunchOfValidations purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 */
	public void testABunchOfValidations()
   	{
		// the visitor
		RoadNetworkValidationResults roadValidationResults = new RoadNetworkValidationResults();
		
		
		// various FeatureType tests
		IsValidFeatureValidation isValidValidator1 = new IsValidFeatureValidation("isValidRoad", 
																					"Tests to see if a road is valid", 
																					new String[] {"roads"});
		IsValidFeatureValidation isValidValidator2 = new IsValidFeatureValidation("isValidRail", 
																					"Tests to see if a railway is valid", 
																					new String[] {"roads", "rails"});
		IsValidFeatureValidation isValidValidator3 = new IsValidFeatureValidation("isValidRiver", 
																					"Tests to see if a river is valid", 
																					new String[] {"rivers"});
		IsValidFeatureValidation isValidValidator4 = new IsValidFeatureValidation("isValidAll", 
																					"Tests to see if all geometries are valid", 
																					IsValidFeatureValidation.ALL);
		
		// various Integrity tests
		//
		//
		//
		
		
		
		// tell the RoadValidator what tests to use
		roadValidationResults.setValidation(isValidValidator1);
		roadValidationResults.setValidation(isValidValidator2);
		roadValidationResults.setValidation(isValidValidator3);
		roadValidationResults.setValidation(isValidValidator4);
		
		
		
		// run each feature validation test on the featureTypes it tests
		String[] types1 = isValidValidator1.getTypeNames();
		for (int i=0; i<types1.length; i++)
		{
			//isValidValidator1.validate(featuresToTest(types[i]), featureType(types[i]), roadValidationResults);
		}
		String[] types2 = isValidValidator1.getTypeNames();
		for (int i=0; i<types2.length; i++)
		{
			//isValidValidator2.validate(featuresToTest(types[i]), featureType(types[i]), roadValidationResults);
		}
		String[] types3 = isValidValidator1.getTypeNames();
		for (int i=0; i<types3.length; i++)
		{
			//isValidValidator3.validate(featuresToTest(types[i]), featureType(types[i]), roadValidationResults);
		}
		String[] types4 = isValidValidator1.getTypeNames();
		for (int i=0; i<types4.length; i++)
		{
			//isValidValidator4.validate(featuresToTest(types[i]), featureType(types[i]), roadValidationResults);
		}
		
		
		
		// check the results of the roadValidator
		String[] roadFailures = roadValidationResults.getFailedMessages();
		String[] roadWarnings = roadValidationResults.getWarningMessages();
		boolean roadsPassed = true;
		if (roadFailures.length != 0)
		{
			roadsPassed = false;
			for (int i=0; i<roadFailures.length; i++)
			{
				System.out.println(roadFailures[i]);
			}
		}
		if (roadWarnings.length != 0)
		{
			for (int i=0; i<roadWarnings.length; i++)
			{
				System.out.println(roadWarnings[i]);
			}
		}
		
		assertTrue(roadsPassed);
		
   	}
   	

}
