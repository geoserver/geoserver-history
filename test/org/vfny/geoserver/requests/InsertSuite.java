/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

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
import org.geotools.resources.Geotools;
import org.geotools.feature.FeatureType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureFactory;
import org.geotools.feature.FeatureTypeFlat;
import org.geotools.feature.AttributeTypeDefault;
import org.geotools.feature.AttributeType;
import org.geotools.feature.SchemaException;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
//import com.vividsolutions.jts.geom.LinearRing;
//import com.vividsolutions.jts.geom.LinearRing;

/**
 * Tests the Update request handling.
 *
 * @version $VERSION$
  * @author Chris Holmes, TOPP
 */
public class InsertSuite extends TestCase {

    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.requests");

    /** Unit test data directory */
    private static final String DATA_DIRECTORY = 
        System.getProperty("user.dir") + "/misc/unit/requests";

    private FeatureType schema;



    private FeatureFactory featureFactory;

    /* Initializes the logger. */
    static {
        Geotools.init("Log4JFormatter", Level.FINEST);
    }
    
    /** Constructor with super. */
    public InsertSuite (String testName) { super(testName); }


        /** Handles test set up details. */
    public void setUp() {
	AttributeType[] atts = { 
	    new AttributeTypeDefault("fid", Integer.class),
	    new AttributeTypeDefault("gid", Integer.class),
	    new AttributeTypeDefault("geom", Polygon.class),
	    new AttributeTypeDefault("name", String.class)};
	try {
	schema = (new FeatureTypeFlat(atts)).setTypeName("geom_test");
	} catch (SchemaException e) {
	    LOGGER.finer("problem with creating schema");
	}
	featureFactory = new FeatureFactory(schema);
	
    }


    /************************ ************************************************
     * STATIC METHODS FOR TEST RUNNING                                       *
     *************************************************************************/
    /**
     * Handles actual XML test running details.
     * @param baseRequest Base request, for comparison.
     * @param fileName File name to parse.
     * @param match Whether or not base request and parse request should match.
     * @throws Exception If there is any problem running the test.
     */
    private static boolean runXmlTest(TransactionRequest baseRequest,
				       String fileName, 
                                       boolean match)
	throws Exception {
	// Read the file and parse it
	File inputFile = new File(DATA_DIRECTORY + "/" + fileName + ".xml");
	Reader inputStream = new FileReader(inputFile);
        TransactionRequest request = XmlRequestReader.
            readTransaction(new BufferedReader(inputStream));
	LOGGER.info("base request: " + baseRequest);
	LOGGER.info("read request: " + request);
	LOGGER.info("XML " + fileName +" test passed: " +  
		    baseRequest.equals(request));

        // Compare parsed request to base request
        if(match) { 
	    //return baseRequest.equals(request);
	    return baseRequest.toString().equals(request.toString());
	} else { 
	    return !baseRequest.equals(request);
        } 
	}



   public void testXml1() throws Exception { 
        // make base comparison objects        
       InsertRequest insert = new InsertRequest();
       

	Coordinate[] points = { new Coordinate(15, 15),
				new Coordinate(15, 25),
				new Coordinate(25, 25),
				new Coordinate(25, 15),
				 new Coordinate(15, 15) };
	PrecisionModel precModel = new PrecisionModel();
	int srid = 2035;
	LinearRing shell = new LinearRing(points, precModel, srid);
	Polygon the_geom = new Polygon(shell, precModel, srid);


	Integer gid = new Integer(44);
	Integer featureId = gid;
	String name = "insert polygon";
	Object[] attributes = { featureId, gid, the_geom, name };
	//try{
	    
	Feature feature = featureFactory.create(attributes, 
						String.valueOf(featureId));
	insert.setHandle("insert 1");
	insert.addFeature(feature);
       TransactionRequest baseRequest = new TransactionRequest();
       baseRequest.addSubRequest(insert);
       	baseRequest.setHandle("my insert");
        // run test       
        assertTrue(runXmlTest( baseRequest, "insert1", true));
    }

    

}
