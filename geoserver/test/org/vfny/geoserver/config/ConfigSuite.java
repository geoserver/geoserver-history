/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.geotools.resources.Geotools;
import org.geotools.resources.Log4JFormatter;
import org.vfny.geoserver.requests.FeatureRequest;
import org.vfny.geoserver.requests.Query;

/**
 * Tests the get feature request handling.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $VERSION$
 */
public class ConfigSuite extends TestCase {

    /* Initializes the logger. */
    static {
        Geotools.init("Log4JFormatter", Level.FINER);
	Log4JFormatter.init("org.vfny.geoserver", Level.FINER);
    }
    
    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.requests");

    /** Unit test data directory */
    private static final String CONFIG_DIR = 
        System.getProperty("user.dir") + "/misc/unit/config/";

    /** Unit test data directory */
    private static final String TYPE_DIR = 
        System.getProperty("user.dir") + "/misc/testData/featureTypes";


    private ConfigInfo config;
    private TypeRepository repo;

    /** Constructor with super. */
    public ConfigSuite (String testName) { super(testName); }


    /** Handles test set up details. */
    public void setUp() {
        config = ConfigInfo.getInstance(CONFIG_DIR);
        config.setTypeDir(TYPE_DIR);
        repo = TypeRepository.getInstance();
    }
    

    /*************************************************************************
     * XML TESTS                                                             *
     *************************************************************************
     * XML GetFeature parsing tests.  Each test reads from a specific XML    *
     * file and compares it to the base request defined in the test itself.  *
     * Tests are run via the static methods in this suite.  The tests        *
     * themselves are quite generic, so documentation is minimal.            *
     *************************************************************************/



    public void test1() throws Exception {        
        LOGGER.fine(repo.toString());
        LOGGER.fine("has two types: " + (repo.typeCount() == 2));
        assertTrue(repo.typeCount() == 2);
        LOGGER.fine("has roads: " + (repo.getType("roads") != null));
        assertTrue(repo.getType("roads") != null);
        LOGGER.fine("has rail: " + (repo.getType("rail") != null));
        assertTrue(repo.getType("rail") != null);
        LOGGER.fine("no cows: " + (repo.getType("cows") == null));
        assertTrue(repo.getType("cows") == null);
    }


    public void test2() throws Exception {        
        LOGGER.fine("locked: " + repo.isLocked("roads"));
        assertTrue(!repo.isLocked("roads"));
        String key = repo.lock("roads");
        LOGGER.fine("locked: " + repo.isLocked("roads"));
        assertTrue(repo.isLocked("roads"));
        boolean success = repo.unlock("roads", key);
        LOGGER.fine("locked: " + repo.isLocked("roads"));
        assertTrue(!repo.isLocked("roads"));
    }

    /* REVISIT: Can't seem to get JUnit to sleep correctly
       right now.
    public void testExpiry() throws Exception {
	LOGGER.fine("locked: " + repo.isLocked("roads"));
        assertTrue(!repo.isLocked("roads"));
        String key = repo.lock("roads", 1);
        LOGGER.fine("locked: " + repo.isLocked("roads"));
        assertTrue(repo.isLocked("roads"));
	//wait(61000);
        boolean success = repo.unlock("roads", key);
        LOGGER.fine("locked: " + repo.isLocked("roads"));
        assertTrue(!repo.isLocked("roads"));
	}*/

    public void testServiceConfig() throws Exception {
	ServiceConfig config = 
	    ServiceConfig.getInstance(CONFIG_DIR + "service-config1.xml");
	LOGGER.fine("config is " + config + ", matches " + testConfig(config));
    }

    public void testServiceConfigOld() throws Exception {
	ServiceConfig config = 
	    ServiceConfig.getInstance(CONFIG_DIR + "service-config2.xml");
	LOGGER.fine("config is " + config + ", matches " + testConfig(config));
    }

    public void testNoConfigFile() {
	try {
	    ServiceConfig config = 
	    ServiceConfig.getInstance(CONFIG_DIR + "config.xml");
	    //should throw error here.
	    fail();
	} catch (ConfigurationException e) {
	    LOGGER.fine("successfully caught config exception: " + e.getMessage());
	}
    }

    public void testBadConfigFile() {
	try {
	    ServiceConfig config = 
	    ServiceConfig.getInstance(TYPE_DIR + "/rail/info.xml");
	    //should throw error here.
	    fail();
	} catch (ConfigurationException e) {
	    LOGGER.fine("successfully caught config exception: " + e.getMessage());
	}
    }

     private boolean testConfig(ServiceConfig servConfig){
	 LOGGER.fine("title: " + servConfig.getTitle() + ", Fees: " + servConfig.getFees());
	 return (servConfig.getName().equals("FreeFS") &&
		 servConfig.getTitle().equals("TOPP GeoServer") &&
		 servConfig.getAbstract().equals("This is a test server.") &&
		 servConfig.getFees().equals("none") &&
		 servConfig.getKeywords().get(0).equals("WFS") &&
		 servConfig.getKeywords().get(1).equals("OGC") &&
		 servConfig.getOnlineResource().equals("http://beta.openplans.org/geoserver"));
        }

}
