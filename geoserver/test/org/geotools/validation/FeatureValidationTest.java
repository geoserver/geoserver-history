package org.geotools.validation;

import java.util.HashMap;
import java.util.Map;

import org.geotools.data.*;
import org.geotools.data.memory.*;

import org.vfny.geoserver.requests.wfs.*;
import org.vfny.geoserver.config.*;
/**
 * Sample FeatureValidation Test.
 * 
 * @author Jody Garnett, Refractions Research, Inc.
 */
public class FeatureValidationTest extends DataTestCase {
    MemoryDataStore store;
    
    /**
     * Constructor for testName.
     * 
     * @param testName name of test (used in error reporting)
     */
    public FeatureValidationTest(String testName) {
        super(testName);
    }

    /**
     * Sets up a Simple DataStore for use.
     * 
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        store = new MemoryDataStore();
        store.addFeatures( roadFeatures );
        store.addFeatures( riverFeatures );     
    }
    /**
     * Clean up after DataStore.
     */
    protected void tearDown() throws Exception {
        store = null;
        super.tearDown();
    }

}
