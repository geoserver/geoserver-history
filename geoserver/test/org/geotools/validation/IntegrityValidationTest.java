package org.geotools.validation;

import java.util.HashMap;

import org.geotools.data.*;
import org.geotools.data.memory.*;
import org.geotools.validation.attributes.UniqueFIDIntegrityValidation;
import org.geotools.validation.spatial.IsValidFeatureValidation;


/**
 * IntegrityValidationTest purpose.
 * <p>
 * Description of IntegrityValidationTest ...
 * <p>
 * 
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: cholmesny $ (last modification)
 * @version $Id: IntegrityValidationTest.java,v 1.2 2003/12/16 18:46:10 cholmesny Exp $
 */
public class IntegrityValidationTest extends DataTestCase {
    MemoryDataStore store;
    
    /**
     * FeatureValidationTest constructor.
     * <p>
     * Run test <code>testName</code>.
     * </p>
     * @param testName
     */
    public IntegrityValidationTest(String testName) {
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
    
    
	public void testUniqueFIDIntegrityValidation() throws Exception
   	{
   		// the visitor
		RoadValidationResults validationResults = new RoadValidationResults();
		
   		UniqueFIDIntegrityValidation validator = new UniqueFIDIntegrityValidation("isValidRoad", "Tests to see if a road is valid", IsValidFeatureValidation.ALL, "FID");
		validationResults.setValidation(validator);
		
		
   		HashMap layers = new HashMap();
   		layers.put("road", store.getFeatureSource("road"));
		layers.put("river", store.getFeatureSource("river"));
		
		assertTrue(validator.validate(layers, null, validationResults));	// validate will return true
   	}
   	
   	
   	

   	   	

}
