/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.zserver;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.geotools.resources.Geotools;
import java.io.FileNotFoundException;

/**
 * Tests the GeoProfile helper methods.
 *
 * @author Chris Holmes, TOPP
 * @version $VERSION$
 */

public class GeoProfileSuite extends TestCase {

    /* Initializes the logger. */
    static {
        Geotools.init("Log4JFormatter", Level.FINEST);
    }

    /** Standard logging instance */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.zserver");


        /** Unit test data directory */
    private static final String DATA_DIRECTORY = 
        System.getProperty("user.dir") + "/misc/testData/unit/zserver";
    
    private static final String ATTRIBUTE_MAP = DATA_DIRECTORY + "/geo.map";

    /**
     * Initializes the database and request handler.
     */
    public GeoProfileSuite (String testName) {
        super(testName);
    }


    
    public static Test suite() {
        TestSuite suite = new TestSuite(GeoProfileSuite.class);
	LOGGER.info("Creating GeoProfile suite.");
        return suite;
    }
    

    public void setUp() {
	
    }

    public void testNumbers() {
	assertTrue(GeoProfile.
		   isFGDCnum("//metadata/idinfo/spdom/bounding/eastbc"));
	assertTrue(!GeoProfile.
		   isFGDCnum("//metadata/idinfo/spdom/bounding/eastbcd"));
	assertTrue(!GeoProfile.isFGDCnum("//test/westbc/bla"));
	assertTrue(GeoProfile.isFGDCnum("//test/westbc"));
	assertTrue(GeoProfile.isFGDCnum("//test/extent"));
	assertTrue(GeoProfile.isFGDCnum("//test/northbc"));
	assertTrue(GeoProfile.isFGDCnum("//test/southbc"));
	assertTrue(GeoProfile.isFGDCnum("//test/latprjo"));
	assertTrue(GeoProfile.isFGDCnum("//test/srcscale"));
	assertTrue(GeoProfile.isFGDCnum("//test/cloud"));
	assertTrue(GeoProfile.isFGDCnum("//test/numstop"));
	assertTrue(GeoProfile.isFGDCnum("//test/rdommax"));
	assertTrue(GeoProfile.isFGDCnum("//test/feast"));
	
    }

    public void testDateRange() {
	assertTrue(GeoProfile.isDateRange(GeoProfile.BEFORE));
	assertTrue(GeoProfile.isDateRange(GeoProfile.BEFORE_OR_DURING));
	assertTrue(GeoProfile.isDateRange(GeoProfile.DURING));
	assertTrue(GeoProfile.isDateRange(GeoProfile.DURING_OR_AFTER));
	assertTrue(GeoProfile.isDateRange(GeoProfile.AFTER));
	assertTrue(!GeoProfile.isDateRange(GeoProfile.EQUALS));
	assertTrue(!GeoProfile.isDateRange(3));
    }

    public void testExtentComputation() {
	Double result = GeoProfile.computeExtent("5", "-5", "10", "-10");
	assertTrue(result.doubleValue() == 200);
	//revisit: doubles don't work well for this computation, as
	//things get all fuzzy with decimals.  It's accurate enough
	//to use, but you can't really compare the results to 
	//a unit test.
	//result = GeoProfile.computeExtent("0.32", "3.3", "2.21", "-5.6");
	//LOGGER.info("result is " + result);
	result = GeoProfile.computeExtent("34", "-3d6", "12", "10");
	assertNull(result);
	LOGGER.info("result is " + result);
	result = GeoProfile.computeExtent("34", null, "12", "10");
	assertNull(result);
    }

    public void testAttributeUseMap(){
	GeoProfile.setUseAttrMap(ATTRIBUTE_MAP);
	Properties attrMap = GeoProfile.getUseAttrMap();
	assertEquals("//metadata/idinfo/citation/citeinfo/origin", 
		     attrMap.get("1005"));
    }
}
