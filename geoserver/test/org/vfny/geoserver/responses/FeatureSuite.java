/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import junit.framework.TestCase;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.DefaultExpression;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.LiteralExpression;
import org.vfny.geoserver.config.ConfigInfo;
import org.vfny.geoserver.requests.FeatureRequest;
import org.vfny.geoserver.requests.Query;
import java.util.logging.Logger;


/**
 * Tests the get feature response handling.  This class needs a good
 * amount of work, but the cite test engine ends up testing things
 * extensively, so it's not a very high priority.  For now it also
 * relies on keeping the sample road feature in the misc/data/featureTypes
 * directory, so we don't have to maintain multiple copies of that file.
 * Ideally for response unit testing we should have a shapefile and
 * a shp2pgsql script of that shapefile that will create the same
 * features.  Perhaps parameter of which test to use, which would 
 * use different featureType dirs, or just different info files (like
 * info-pg.xml and info-shp.xml instead of info.xml, this would probably
 * take some tweaking though). This would be nice to extend to all
 * potential datasources, a sort of sanity check to make sure that a new
 * pluggable datasource actually does everything needed by geoserver.
 * <p>We also need a way to assert the returned test data, perhaps just
 * using dom, or maybe the gt2 gml datasource.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: FeatureSuite.java,v 1.4 2003/09/16 16:13:20 cholmesny Exp $
 */
public class FeatureSuite extends TestCase {
    //Initializes the logger. Uncomment to see log messages.
    //static {
    //org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", 
    //java.util.logging.Level.FINE);
    //}

    /** Class logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");

    /** Unit test data directory */
    private static final String CONFIG_DIR = System.getProperty("user.dir")
        + "/misc/unit/config/";

    /** Unit test data directory */
    private static final String TYPE_DIR = System.getProperty("user.dir")
        + "/misc/data/featureTypes";

    /** Holds mappings between HTTP and ASCII encodings */
    private static FilterFactory factory = FilterFactory.createFilterFactory();
    private ConfigInfo config;

    /**
     * Constructor with super.
     *
     * @param testName The name of the test.
     */
    public FeatureSuite(String testName) {
        super(testName);
    }

    /**
     * Handles test set up details.
     */
    public void setUp() {
        config = ConfigInfo.getInstance(CONFIG_DIR);
        config.setTypeDir(TYPE_DIR);
    }

    public void test1() throws Exception {
        // make base comparison objects
        Query query = new Query();
        query.setTypeName("road");

        AttributeExpression tempLeftExp = factory.createAttributeExpression(null);
        tempLeftExp.setAttributePath("nlanes");

        LiteralExpression tempRightExp = factory.createLiteralExpression(DefaultExpression.LITERAL_INTEGER);
        tempRightExp.setLiteral(new Integer(8));

        CompareFilter tempFilter = factory.createCompareFilter(AbstractFilter.COMPARE_EQUALS);
        tempFilter.addRightValue(tempRightExp);
        tempFilter.addLeftValue(tempLeftExp);
        query.addFilter(tempFilter);

        FeatureRequest request = new FeatureRequest();
        request.addQuery(query);

        String gml = FeatureResponse.getXmlResponse(request);
        LOGGER.fine(gml);

        //TODO: assert true on the strings returned.  Maybe use 
        //the gt2 gml datasource?  Turn into DOM (gml4J?) and
        //test what should be in there?  Or perhaps just rely
        //on cite test engine for the response tests.
    }
}
