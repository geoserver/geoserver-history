/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geotools.validation;

import org.geotools.data.DataTestCase;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.validation.attributes.UniqueFIDIntegrityValidation;
import org.geotools.validation.spatial.IsValidGeometryFeatureValidation;
import java.util.HashMap;


/**
 * IntegrityValidationTest purpose.
 * 
 * <p>
 * Description of IntegrityValidationTest ...
 * </p>
 * 
 * <p></p>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: IntegrityValidationTest.java,v 1.5 2004/01/21 18:42:25 jive Exp $
 */
public class IntegrityValidationTest extends DataTestCase {
    MemoryDataStore store;

    /**
     * FeatureValidationTest constructor.
     * 
     * <p>
     * Run test <code>testName</code>.
     * </p>
     *
     * @param testName
     */
    public IntegrityValidationTest(String testName) {
        super(testName);
    }

    /**
     * Construct data store for use.
     *
     * @throws Exception
     *
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        store = new MemoryDataStore();
        store.addFeatures(roadFeatures);
        store.addFeatures(riverFeatures);
    }

    /**
     * Override tearDown.
     *
     * @throws Exception
     *
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        store = null;
        super.tearDown();
    }

    public void testUniqueFIDIntegrityValidation() throws Exception {
        // the visitor
        RoadValidationResults validationResults = new RoadValidationResults();

        UniqueFIDIntegrityValidation validator = new UniqueFIDIntegrityValidation("isValidRoad",
                "Tests to see if a road is valid",
                IsValidGeometryFeatureValidation.ALL, "FID");
        validationResults.setValidation(validator);

        HashMap layers = new HashMap();
        layers.put("road", store.getFeatureSource("road"));
        layers.put("river", store.getFeatureSource("river"));

        assertTrue(validator.validate(layers, null, validationResults)); // validate will return true
    }
}
