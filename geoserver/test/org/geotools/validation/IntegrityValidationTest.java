package org.geotools.validation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.*;
import org.geotools.data.memory.*;
import org.geotools.feature.IllegalAttributeException;

import org.vfny.geoserver.requests.wfs.*;
import org.vfny.geoserver.config.*;

/**
 * IntegrityValidationTest purpose.
 * <p>
 * Description of IntegrityValidationTest ...
 * <p>
 * 
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: IntegrityValidationTest.java,v 1.1.2.2 2003/11/23 08:03:39 jive Exp $
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
   	
   	
   	
   	public void testUniqueFID2() throws Exception
   	{
        RoadValidationResults validationResults = new RoadValidationResults();
                
        UniqueFIDIntegrityValidation validator = new UniqueFIDIntegrityValidation("isValidRoad", "Tests to see if a road is valid", IsValidFeatureValidation.ALL, "FID");
        validationResults.setValidation(validator);
                
		roadFeatures[0] = roadFeatures[1];
        FeatureSource badDog = DataUtilities.source( roadFeatures );
        FeatureReader r = badDog.getFeatures().reader();
        while( r.hasNext() ){
            System.out.println( r.next() );
        }
        r.close();
        
        HashMap layers = new HashMap();
        layers.put("road", badDog );        
        
        assertTrue( !validator.validate(layers, null, validationResults) );
   		
   	}
   	   	

}
