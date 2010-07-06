/*
 * Copyright (c) 2001 - 2010 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import junit.framework.Test;

import org.w3c.dom.Document;

/**
 * This is to test spatial (bbox) queries for complex features
 * 
 * @author Derrick Wong, Curtin University of Technology
 */

public class BBoxFilterTest extends AbstractAppSchemaWfsTestSupport {
    private final String WFS_GET_FEATURE = "wfs?request=GetFeature&typename=ex:geomContainer";

    private final String WFS_GET_FEATURE_LOG = "WFS GetFeature&typename=ex:geomContainerresponse:\n";

    private final String LONGLAT = "&BBOX=130,-29,134,-24";

    private final String LATLONG = "&BBOX=-29,130,-24,134";

    /**
     * Read-only test so can use one-time setup.
     * 
     */
    public static Test suite() {
        return new OneTimeTestSetup(new BBoxFilterTest());
    }

    protected NamespaceTestData buildTestData() {
        return new BBoxMockData();
    }

    /**
     * The following performs a WFS request and obtains all features specified in
     * BBoxTestPropertyfile.properties
     */
    public void testQuery() {
        Document doc = getAsDOM(WFS_GET_FEATURE);
        LOGGER.info(WFS_GET_FEATURE_LOG + prettyString(doc));
        assertXpathEvaluatesTo("3", "/wfs:FeatureCollection/@numberOfFeatures", doc);
        assertXpathCount(3, "//ex:geomContainer", doc);

    }

    /**
     * The following performs a WFS request specifying a BBOX parameter of axis ordering longitude
     * latitude. 
     */
    public void testQueryBboxLongLat() {
        Document doc = getAsDOM(WFS_GET_FEATURE + LONGLAT);
        LOGGER.info(WFS_GET_FEATURE_LOG + LONGLAT + prettyString(doc));
        assertXpathEvaluatesTo("0", "/wfs:FeatureCollection/@numberOfFeatures", doc);
        assertXpathCount(0, "//ex:geomContainer", doc);
    }

    /**
     * The following performs a WFS request specifying a BBOX parameter of axis ordering latitude
     * longitude. This test should return features if the axis ordering behaves similar to queries
     * to Simple features.
     */
    public void testQueryBboxLatLong() {
        Document doc = getAsDOM(WFS_GET_FEATURE + LATLONG);
        LOGGER.info(WFS_GET_FEATURE_LOG + LATLONG + prettyString(doc));
        assertXpathEvaluatesTo("2", "/wfs:FeatureCollection/@numberOfFeatures", doc);
        assertXpathCount(2, "//ex:geomContainer", doc);
    }

}
