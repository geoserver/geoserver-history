/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import junit.framework.TestCase;
import java.util.logging.Logger;


/**
 * Tests the get TypeInfo and the non-locking aspects of the TypeRepository.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: TypeSuite.java,v 1.1 2003/09/10 19:51:49 cholmesny Exp $
 */
public class TypeSuite extends TestCase {
    //Initializes the logger. Uncomment to see log messages.
    //static {
    //    org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", Level.FINEST);
    //}

    /** Class logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");

    /** Unit test data directory */
    private static final String CONFIG_DIR = System.getProperty("user.dir")
        + "/misc/unit/config/";

    /** Unit test data directory */
    private static final String TYPE_DIR = System.getProperty("user.dir")
        + "/misc/unit/featureTypes";
    private static final String TEST_URI = "http://beta.openplans.org/geoserver/ns01";
    private ConfigInfo config;
    private TypeRepository repo;
    private String prefix;

    /**
     * Constructor with super.
     *
     * @param testName the name of the test.
     */
    public TypeSuite(String testName) {
        super(testName);
    }

    /**
     * Handles test set up details.
     */
    public void setUp() {
        config = ConfigInfo.getInstance(CONFIG_DIR);
        config.setTypeDir(TYPE_DIR);
        repo = TypeRepository.getInstance();
        prefix = config.getDefaultNSPrefix();
    }

    public void testRepoBasics() throws Exception {
        LOGGER.fine(repo.toString());
        LOGGER.fine("has two types: " + (repo.typeCount() == 3));
        assertTrue(repo.typeCount() == 3);

        String prefix = config.getDefaultNSPrefix();
        LOGGER.fine("has roads: " + (repo.getType(prefix + ":roads") != null));
        assertTrue(repo.getType(prefix + ":roads") != null);
        LOGGER.fine("has rail: " + (repo.getType(prefix + ":rail") != null));
        assertTrue(repo.getType(prefix + ":rail") != null);
        LOGGER.fine("has ns01:rail: " + (repo.getType("ns01:rail") != null));
        assertTrue(repo.getType("ns01:rail") != null);
        LOGGER.fine("no cows: " + (repo.getType("cows") == null));
        assertTrue(repo.getType("cows") == null);
    }

    //TODO: validate against the wfs schemas.
    public void testXmlPrinting() throws Exception {
        TypeInfo rail = repo.getType(prefix + ":rail");
        String xml1 = rail.getCapabilitiesXml("1.0.0");
        LOGGER.fine("xml 1.0.0 is " + xml1);

        String xml15 = rail.getCapabilitiesXml("0.0.15");
        LOGGER.fine("xml 0.0.15 is " + xml15);
    }

    public void testTypeInfoGetters() throws Exception {
        TypeInfo nsRail = repo.getType("ns01:rail");
        LOGGER.fine("srs is " + nsRail.getSrs());
        assertEquals("-1", nsRail.getSrs());
        LOGGER.fine("abstract is " + nsRail.getAbstract());
        assertEquals("This is a simple rail coverage from the New York City"
            + " basemap.", nsRail.getAbstract());
        LOGGER.fine("bbox is " + nsRail.getBoundingBox());
        LOGGER.fine("namespace is " + nsRail.getXmlns());
        assertEquals(TEST_URI, nsRail.getXmlns());
        LOGGER.fine("full name is " + nsRail.getFullName());
        assertEquals("ns01:rail", nsRail.getFullName());
        LOGGER.fine("first mandatory att is " + nsRail.getMandatoryProps()[0]);
        assertEquals("name", nsRail.getMandatoryProps()[0]);
    }
}
