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
import org.geotools.filter.FilterFactory;
import org.geotools.filter.Filter;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.FidFilter;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.LiteralExpression;
import org.geotools.resources.Geotools;

/**
 * Tests the Update request handling.
 *
 * @version $VERSION$
  * @author Chris Holmes, TOPP
 */
public class UpdateSuite extends TestCase {

    /** Class logger */
    private static final Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.requests");

    /** Unit test data directory */
    private static final String DATA_DIRECTORY = 
        System.getProperty("user.dir") + "/misc/unit/requests";

    /** Holds mappings between HTTP and ASCII encodings */
    private static FilterFactory factory = FilterFactory.createFilterFactory();

    /* Initializes the logger. */
    static {
        Geotools.init("Log4JFormatter", Level.FINEST);
    }
    
    /** Constructor with super. */
    public UpdateSuite (String testName) { super(testName); }


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
	LOGGER.fine("base request: " + baseRequest);
	LOGGER.fine("read request: " + request);
	LOGGER.info("XML " + fileName +" test passed: " +  
		    baseRequest.equals(request));

        // Compare parsed request to base request
        if(match) { 
            return baseRequest.equals(request);
	} else { 
	    return !baseRequest.equals(request);
        } 
	}

    /** Handles test set up details. */
    public void setUp() {}

   public void testXml1() throws Exception { 
        // make base comparison objects        
        UpdateRequest update = new UpdateRequest();
	update.setTypeName("BUILTUPA_1M");
	update.setFilter(factory.createFidFilter("10131"));
	update.addProperty("POPULATION", "4070000");
	TransactionRequest baseRequest = new TransactionRequest();
        baseRequest.addSubRequest(update);
        // run test       
        assertTrue(runXmlTest( baseRequest, "update1", true));
    }

       public void testXml2() throws Exception { 
        // make base comparison objects        
        UpdateRequest update = new UpdateRequest();
	update.setTypeName("BUILTUPA_1M");
	FidFilter tempFilter = factory.createFidFilter("1031");
	tempFilter.addFid("34");
        tempFilter.addFid("24256");
	update.setFilter(tempFilter);
	update.addProperty("POPULATION_TYPE", "CITY");
	TransactionRequest baseRequest = new TransactionRequest();
        baseRequest.addSubRequest(update);
        // run test       
        assertTrue(runXmlTest( baseRequest, "update2", true));
    }

     public void testXml3() throws Exception { 
        // make base comparison objects        
        UpdateRequest update = new UpdateRequest();
	update.setTypeName("BUILTUPA_1M");
	FidFilter tempFilter = factory.createFidFilter("1031");
	tempFilter.addFid("34");
        tempFilter.addFid("24256");
	update.setFilter(tempFilter);
	update.addProperty("NAME", "somestring");
	UpdateRequest update2 = new UpdateRequest();
	update2.setTypeName("BUILTUPA_1M");
	CompareFilter compFilter = 
	    factory.createCompareFilter(AbstractFilter.COMPARE_GREATER_THAN);
	 AttributeExpression tempLeftExp = 
            factory.createAttributeExpression(null); 
        tempLeftExp.setAttributePath("TILE_ID");
	LiteralExpression tempRightExp = factory.
            createLiteralExpression(1000.0);
	compFilter.addLeftValue(tempLeftExp);
	compFilter.addRightValue(tempRightExp);
	update2.setFilter(compFilter);
	update2.addProperty("FAC_ID", "100");
	TransactionRequest baseRequest = new TransactionRequest();
        baseRequest.addSubRequest(update);
	 baseRequest.addSubRequest(update2);
        // run test       
        assertTrue(runXmlTest( baseRequest, "update3", true));
    }

    //causes class cast exception in fid filter.  Fixed in geotools cvs, if the
    //geotools jar that geoserver currently uses is not fixed then comment this
    //test out. ch
    /*   public void testXml4() throws Exception { 
        // make base comparison objects        
        UpdateRequest update = new UpdateRequest();
	update.setTypeName("TREESA_1M");
	FidFilter tempFilter = factory.createFidFilter("1010");
	update.setFilter(tempFilter);
	update.addProperty("TREETYPE", "CONIFEROUS");
	UpdateRequest update2 = new UpdateRequest();
	update2.setTypeName("OCEANSA_1M");
	CompareFilter compFilter = 
	    factory.createCompareFilter(AbstractFilter.COMPARE_GREATER_THAN);
	 AttributeExpression tempLeftExp = 
            factory.createAttributeExpression(null); 
        tempLeftExp.setAttributePath("DEPTH");
	LiteralExpression tempRightExp = factory.
            createLiteralExpression(2400.0);
	compFilter.addLeftValue(tempLeftExp);
	compFilter.addRightValue(tempRightExp);
	update2.setFilter(compFilter);
	update2.addProperty("DEPTH", "2400");
	TransactionRequest baseRequest = new TransactionRequest();
        baseRequest.addSubRequest(update2);
	 baseRequest.addSubRequest(update);
	 baseRequest.setVersion("1.0.0");
	 baseRequest.setService("WFS");
        // run test       
        assertTrue(runXmlTest( baseRequest, "update4", true));
	}*/

    
   public void testXml5() throws Exception { 
        // make base comparison objects        
        UpdateRequest update = new UpdateRequest();
	update.setTypeName("BUILTUPA_1M");
	update.setFilter(factory.createFidFilter("10131"));
	update.addProperty("POPULATION", "4070000");
	TransactionRequest baseRequest = new TransactionRequest();
        baseRequest.addSubRequest(update);
        // run test       
        assertTrue(runXmlTest( baseRequest, "update5", true));
    }

     public void testXml6() throws Exception { 
	UpdateRequest update2 = new UpdateRequest();
	update2.setTypeName("OCEANSA_1M");
	CompareFilter compFilter = 
	    factory.createCompareFilter(AbstractFilter.COMPARE_GREATER_THAN);
	 AttributeExpression tempLeftExp = 
            factory.createAttributeExpression(null); 
        tempLeftExp.setAttributePath("DEPTH");
	LiteralExpression tempRightExp = factory.
            createLiteralExpression(2400.0);
	compFilter.addLeftValue(tempLeftExp);
	compFilter.addRightValue(tempRightExp);
	update2.setFilter(compFilter);
	update2.addProperty("DEPTH", "2400");
	update2.addProperty("TREASURE", "Booty");
	update2.setHandle("update_booty");
	TransactionRequest baseRequest = new TransactionRequest();
        baseRequest.addSubRequest(update2);
	 baseRequest.setVersion("1.0.0");
	 baseRequest.setService("WFS");
	 baseRequest.setHandle("oceans"); 
	 // run test       
        assertTrue(runXmlTest( baseRequest, "update6", true));
	}
    

}
