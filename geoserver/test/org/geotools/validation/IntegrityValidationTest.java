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
package org.geotools.validation;

import org.geotools.data.*;
import org.geotools.data.memory.*;
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
 * @version $Id: IntegrityValidationTest.java,v 1.3 2003/12/16 23:11:23 jive Exp $
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
