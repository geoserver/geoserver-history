/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import java.util.HashMap;
import java.util.Map;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.geoserver.data.test.MockData;
import org.geoserver.test.GeoServerTestSupport;


/**
 * Base support class for wfs tests.
 * <p>
 * Deriving from this test class provides the test case with preconfigured
 * geoserver and wfs objects.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class WFSTestSupport extends GeoServerTestSupport {
    /**
     * @return The global wfs instance from the application context.
     */
    protected WFS getWFS() {
        return (WFS) applicationContext.getBean("wfs");
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        // init xmlunit
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wfs", "http://www.opengis.net/wfs");
        namespaces.put("ows", "http://www.opengis.net/ows");
        namespaces.put(MockData.CITE_PREFIX, MockData.CITE_URI); 
        namespaces.put(MockData.CDF_PREFIX, MockData.CDF_URI);
        namespaces.put(MockData.CGF_PREFIX, MockData.CGF_URI);
        namespaces.put(MockData.SF_PREFIX, MockData.SF_URI);
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
    }
}
