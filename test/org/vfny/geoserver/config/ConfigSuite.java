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
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.Filter;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.FidFilter;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.DefaultExpression;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.LiteralExpression;
import org.geotools.resources.Geotools;
import org.vfny.geoserver.requests.FeatureRequest;
import org.vfny.geoserver.requests.Query;

/**
 * Tests the get feature request handling.
 *
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
public class ConfigSuite extends TestCase {

    /* Initializes the logger. */
    static {
        Geotools.init("Log4JFormatter", Level.FINE);
    }
    
    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.requests");

    /** Unit test data directory */
    private static final String CONFIG_DIR = 
        System.getProperty("user.dir") + "/misc/documents/configuration.xml";

    /** Unit test data directory */
    private static final String TYPE_DIR = 
        System.getProperty("user.dir") + "/misc/data/featureTypes";


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

}
