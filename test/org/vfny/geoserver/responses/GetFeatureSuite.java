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

package org.vfny.geoserver.responses;

import java.util.*;
import junit.framework.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.responses.*;

/**
 * Connects to a Postgis database and returns properly formatted GML.
 *
 * <p>This standard class must exist for every supported datastore.</p>
 *
 * @version $Id: GetFeatureSuite.java,v 1.1 2002/08/22 17:35:20 robhranac Exp $
 * @author Rob Hranac, Vision for New York
 */
public class GetFeatureSuite extends TestCase {

    /** Standard logging instance */
    private static Logger _log = Logger.getLogger(GetFeatureSuite.class);

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
    public GetFeatureSuite (String testName) {
        super(testName);
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
        _log.getRootLogger().setLevel( (Level) Level.DEBUG);
    }


    public void testBasic()
        throws Exception {
        query = new Query();
        query.setFeatureTypeName("rail");

        request = new GetFeatureRequest();
        request.addQuery(query);

        response = new GetFeatureResponse(request);
        String cows = response.getXmlResponse();        
        _log.info("finished");
        //_log.info(response.getXmlResponse());        
    }

}
