/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.DefaultExpression;
import org.geotools.filter.FidFilter;
import org.geotools.filter.Filter;
import org.geotools.filter.LiteralExpression;
import org.vfny.geoserver.requests.readers.KvpRequestReader;
import org.vfny.geoserver.requests.readers.XmlRequestReader;
import org.vfny.geoserver.requests.readers.wfs.GetFeatureKvpReader;
import org.vfny.geoserver.requests.readers.wfs.GetFeatureXmlReader;
import org.vfny.geoserver.requests.wfs.FeatureRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;


/**
 * This test aims to provide confidence on the complex_sco to trunk migration
 * process.
 * To run this test it is needed to have a geoserver instance running
 * on localhost:8080 and that such an instance has the sco:wq_plus featuretype
 * configured as for the test data
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id
 */
public class ComplexFeatureTest extends TestCase {

    /** Class logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /**
     * Constructor with super.
     *
     * @param testName The name of the test.
     */
    public ComplexFeatureTest(String testName) {
        super(testName);
    }
    

    /**
     * Expected response is:
     * <pre>
     * <code>
     * 
     * </code>
     * </pre>
     * @throws Exception
     */
    public void testGetFeature()throws Exception{
    	URL getFeatureURL = new URL("http://localhost:8080/geoserver/wfs/GetFeature?typeName=sco:wq_plus");
    	InputStream in;
		try {
			in = getFeatureURL.openStream();
		} catch (ConnectException e) {
			LOGGER.log(Level.SEVERE, "is geoserver running on localhost:8080?", e);
			throw e;
		}
    	
    	DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    	Document response = db.parse(in);
    	if("ServiceExceptionReport".equals(response.getDocumentElement().getNodeName()))
    		fail("Served thrown a WFS exception");
    	
    	assertEquals(1, response.getElementsByTagName("gml:featureMember").getLength());
    	
    	assertEquals(36, response.getElementsByTagName("sco:wq_plus").getLength());

    	Element first = (Element)response.getElementsByTagName("sco:wq_plus").item(0);
    	assertEquals(1, first.getElementsByTagName("sco:sitename").getLength());
    	assertEquals(1, first.getElementsByTagName("sco:anzlic_no").getLength());
    	assertEquals(1, first.getElementsByTagName("sco:project_no").getLength());
    	assertEquals(1, first.getElementsByTagName("gml:location").getLength());
    	assertEquals(30, first.getElementsByTagName("sco:measurement").getLength());
    }
}
