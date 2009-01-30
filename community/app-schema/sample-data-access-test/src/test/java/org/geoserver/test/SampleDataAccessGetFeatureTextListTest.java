/*
 * Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Test;

import org.geotools.data.SampleDataAccess;

/**
 * WFS GetFeature to test integration of {@link SampleDataAccess} with GeoServer. This version uses
 * the text-feature-list WFS output format so integration can be tested even before a working
 * encoder configuration is written.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public class SampleDataAccessGetFeatureTextListTest extends SampleDataAccessGeoServerTestSupport {

    /**
     * Read-only test so can use one-time setup.
     * 
     * @return
     */
    public static Test suite() {
        return new OneTimeTestSetup(new SampleDataAccessGetFeatureTextListTest());
    }

    /**
     * Test if GetFeature returns parsable XML.
     * 
     * @throws Exception
     */
    public void testGetFeatureTextList() throws Exception {
        String request = "wfs?request=GetFeature&typename=gsml:MappedFeature&outputformat=text-feature-list";
        LOGGER.info("WFS request: " + request);
        String s = getAsString(request);
        LOGGER.info("WFS response: \n" + s);
        assertTrue(s
                .startsWith("type = http://www.example.org/sample-data-access/GeoSciML-lite:MappedFeature, id = mf1\n"));
        assertTrue(s
                .contains("\ntype = http://www.example.org/sample-data-access/GeoSciML-lite:MappedFeature, id = mf2\n"));
        assertTrue(s.endsWith("\ncount = 2\n"));
    }

    /**
     * Reads an {@link InputStream} as a {@link String}, converting line endings to "\n", not
     * discarding them as in super.string.
     */
    @Override
    protected String string(InputStream input) throws IOException {
        return TextUtil.readInputStreamAsString(input);
    }

}
