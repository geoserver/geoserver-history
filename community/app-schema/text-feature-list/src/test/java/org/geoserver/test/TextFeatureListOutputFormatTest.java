/*
 * Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import java.io.IOException;
import java.io.InputStream;

import org.geoserver.wfs.WFSTestSupport;

/**
 * Test for {@link TextFeatureListOutputFormat}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public class TextFeatureListOutputFormatTest extends WFSTestSupport {

    /**
     * Test if a WFS request returns the text we expect.
     * 
     * @throws Exception
     */
    public void testTextFeatureListOutputFormat() throws Exception {
        String request = "wfs?request=GetFeature&typeName=cite:Buildings&outputFormat=text-feature-list";
        LOGGER.info("WFS request: " + request);
        String s = getAsString(request);
        LOGGER.info("WFS response: \n" + s);
        assertTrue(s
                .startsWith("type = http://www.opengis.net/cite:Buildings, id = Buildings.1107531701010\n"));
        assertTrue(s
                .contains("\ntype = http://www.opengis.net/cite:Buildings, id = Buildings.1107531701011\n"));
        assertTrue(s.endsWith("\ncount = 2\n"));
    }

    /**
     * Reads an {@link InputStream} into a {@link String}, converting line endings to "\n", not
     * discarding them as in super.string.
     */
    @Override
    protected String string(InputStream input) throws IOException {
        return TextUtil.readInputStreamAsString(input);
    }

}
