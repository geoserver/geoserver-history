package org.geotools.validation;

import java.util.HashMap;
import java.util.Map;

import org.geotools.data.*;
import org.geotools.data.memory.*;

import org.vfny.geoserver.requests.wfs.*;
import org.vfny.geoserver.config.*;

/**
 * FeatureValidationTest purpose.
 * <p>
 * Description of FeatureValidationTest ...
 * <p>
 * 
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: FeatureValidationTest.java,v 1.1.2.2 2003/11/23 03:19:02 jive Exp $
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

}
