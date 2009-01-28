/*
 * Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import java.io.OutputStream;

import junit.framework.Test;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.geotools.data.SampleDataAccess;
import org.w3c.dom.Document;

/**
 * WFS GetFeature to test integration of {@link SampleDataAccess} with GeoServer.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public class SampleDataAccessGetFeatureTest extends SampleDataAccessGeoServerTestSupport {

    /**
     * Read-only test so can use one-time setup.
     * 
     * @return
     */
    public static Test suite() {
        return new OneTimeTestSetup(new SampleDataAccessGetFeatureTest());
    }

    /**
     * Test if GetFeature returns parsable XML.
     * 
     * @throws Exception
     */
    public void testGetFeature() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=gsml:MappedFeature" /* "&version=1.1.0&service=wfs" */ );
        prettyPrint(doc, System.out);
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement()
                .getNodeName());
    }
    
    public void prettyPrint(Document doc, OutputStream out) throws Exception {
        OutputFormat format = new OutputFormat(doc);
        format.setLineWidth(80);
        format.setIndenting(true);
        format.setIndent(4);
        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(doc);
    }   
    
}
