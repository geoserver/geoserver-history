/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.zserver;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Tests the GeoIndexer helper methods.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: GeoIndexerSuite.java,v 1.5 2004/01/12 21:01:29 dmzwiers Exp $
 */
public class GeoIndexerSuite extends TestCase {
    /* Initializes the logger. Uncomment to see log messages.*/

    //static {
    //    org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", Level.FINEST);
    //}

    /** Standard logging instance */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.zserver");
    private static final String BASE_DIR = System.getProperty("user.dir");

    /** Unit test data directory */
    private static final String DATA_DIRECTORY = BASE_DIR
        + "/misc/unit/zserver";
    private static final String INDEX_DIR = BASE_DIR + "/misc/documents/index";
    private static final String ATTRIBUTE_MAP = DATA_DIRECTORY + "/geo.map";
    private Properties testProps;

    /**
     * Constructor that calls super.
     *
     * @param testName name of the test.
     */
    public GeoIndexerSuite(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(GeoIndexerSuite.class);
        LOGGER.fine("Creating GeoIndexer suite.");

        return suite;
    }

    public void setUp() {
        testProps = new Properties();
        testProps.setProperty("database", INDEX_DIR);
        testProps.setProperty("datafolder", DATA_DIRECTORY);
        GeoProfile.setUseAttrMap(ATTRIBUTE_MAP);

        //testProps.setProperty("fieldmap", ATTRIBUTE_MAP);
    }

    public void testCreation() {
        File indexDir = new File(INDEX_DIR);
        deleteDir(indexDir);

        GeoIndexer indexer = new GeoIndexer(testProps);
        assertEquals(indexer.numDocs(), 0);

        int numIndexed = indexer.update();
        assertEquals(numIndexed, 2);
        assertEquals(indexer.numDocs(), 2);
    }

    //needs to be run with an already created index.
    public void testModifies() {
        GeoIndexer indexer = new GeoIndexer(testProps);
        int numIndexed = indexer.update();
        assertEquals(numIndexed, 0);
        assertEquals(indexer.numDocs(), 2);

        File test1 = new File(DATA_DIRECTORY + "/test1/metadata.xml");
        test1.setLastModified(new Date().getTime()); //set last modified to now
        numIndexed = indexer.update();

        LOGGER.fine("added " + numIndexed + " docs after modify");
        LOGGER.fine("num stored: " + indexer.numDocs());
        assertEquals(numIndexed, 1);
        assertEquals(indexer.numDocs(), 2); //should delete old doc.
    }

    public void testRemove() {
        File test = new File(DATA_DIRECTORY + "/test1/metadata.xml");
        File newName = new File(DATA_DIRECTORY + "/test1/renamed.txt");
        test.renameTo(newName);

        GeoIndexer indexer = new GeoIndexer(testProps);
        int numIndexed = indexer.update();
        LOGGER.fine("indexed " + numIndexed + " docs after remove");
        LOGGER.fine("num stored in index: " + indexer.numDocs());
        assertEquals(numIndexed, 0);
        assertEquals(indexer.numDocs(), 1);
        newName.renameTo(test);
        indexer = new GeoIndexer(testProps);
        numIndexed = indexer.update();
        LOGGER.fine("indexed " + numIndexed + " docs after moving back");
        LOGGER.fine("num stored in index: " + indexer.numDocs());
        assertEquals(numIndexed, 1);
        assertEquals(indexer.numDocs(), 2);
    }

    private void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();

            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }

            dir.delete();
        }
    }
}
