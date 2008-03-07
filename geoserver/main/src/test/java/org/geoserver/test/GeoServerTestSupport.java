/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.namespace.QName;

import org.geoserver.data.test.MockData;
import org.geoserver.data.test.TestData;
import org.vfny.geoserver.global.GeoserverDataDirectory;



/**
 * Base test class for GeoServer unit tests.
 * <p>
 * Deriving from this test class provides the test case with preconfigured
 * geoserver and catalog objects.
 * </p>
 * <p>
 * This test case provides a spring application context which loads the
 * application contexts from all modules on the classpath.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
public class GeoServerTestSupport extends GeoServerAbstractTestSupport {
    
    /**
     * mock GeoServer data directory
     */
    private MockData dataDirectory;
    
    @Override
    public MockData getTestData() throws Exception {
        if(dataDirectory == null) {
            // create the data directory
            dataDirectory = new MockData();
            populateDataDirectory(dataDirectory);
        } 
        return dataDirectory;
    }
    
    /**
     * Sets up a template in a feature type directory.
     * 
     * @param featureTypeName The name of the feature type.
     * @param template The name of the template.
     * @param body The content of the template.
     * 
     * @throws IOException
     */
    protected void setupTemplate(QName featureTypeName,String template,String body)
        throws IOException {
        
        dataDirectory.copyToFeatureTypeDirectory( new ByteArrayInputStream(body.getBytes()), featureTypeName, template );
    }

    
}
