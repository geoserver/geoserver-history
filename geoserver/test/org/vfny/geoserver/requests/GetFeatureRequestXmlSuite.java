/*
 *    Geotools - OpenSource mapping toolkit
 *    (C) 2002, Centre for Computational Geography
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; 
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *    
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;
//import java.util.logging.*;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.BasicConfigurator;
import junit.framework.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.responses.*;

/**
 * Connects to a Postgis database and returns properly formatted GML.
 *
 * <p>This standard class must exist for every supported datastore.</p>
 *
 * @version $Id: GetFeatureRequestXmlSuite.java,v 1.1 2002/08/22 17:35:20 robhranac Exp $
 * @author Rob Hranac, TOPP
 */
public class GetFeatureRequestXmlSuite extends TestCase {

    /** Standard logging instance */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");

    /** The unit test data directory */
    private static final String DATA_DIRECTORY = 
        "/home/rob/wfs/geoserver/misc/testData/unit/requests";

    /** The query object */
    private Query query;

    /** The request object */
    private GetFeatureRequest request;

    /** The response object */
    private GetFeatureResponse response;


    /**
     * Initializes the database and request handler.
     *
     * @param response The query from the request object.
     * @param maxFeatures The query from the request object.
     */
    public GetFeatureRequestXmlSuite (String testName) {
        super(testName);
    }


    /**
     * Initializes the database and request handler.
     *
     * @param response The query from the request object.
     * @param maxFeatures The query from the request object.
     */
    private static BufferedReader readFile(String filename)
        throws Exception {
        File inputFile = new File(DATA_DIRECTORY + "/" + filename);
        Reader inputStream = new FileReader(inputFile);
        return new BufferedReader(inputStream);        
    }

    /**
     * Initializes the database and request handler.
     *
     * @param response The query from the request object.
     * @param maxFeatures The query from the request object.
     */
    private static BufferedReader readFile(String filename)
        throws Exception {
        File inputFile = new File(DATA_DIRECTORY + "/" + filename);
        Reader inputStream = new FileReader(inputFile);
        return new BufferedReader(inputStream);        
    }


    /*
    public static Test suite() {
        _log.info("starting suite...");
        TestSuite suite = new TestSuite(GetFeatureSuite.class);
        _log.info("made suite...");
        return suite;
    }
    */

    public void setUp() {
        BasicConfigurator.configure();

        //Handler handler = new StreamHandler(System.out, new SimpleFormatter());
        //LOGGER.addHandler(handler);
        //LOGGER.setLevel(Level.FINER);
        LOGGER.setLevel((Level)  Level.DEBUG);
    }


    public void testCustom1()
        throws Exception {

        Query baseQuery = new Query();
        baseQuery.setFeatureTypeName("rail");
        baseQuery.setHandle("test1");

        GetFeatureRequest baseRequest = new GetFeatureRequest();
        baseRequest.addQuery(baseQuery);
        baseRequest.setMaxFeatures(100);


        if(assertEquals, request, baseRequest)
        LOGGER.info("base request: " + baseRequest.toString());
        LOGGER.info("read request: " + request.toString());
        LOGGER.info("requests are equal: " + baseRequest.equals(request));
    }

}
