/* This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets.utilities;

import org.apache.log4j.Category;
import java.io.*;
import java.util.*;
import org.vfny.geoserver.db.jdbc.*;

/**
 * Implements the WFS GetFeature interface, which responds to requests for GML.
 * This servlet accepts a getFeatures request and returns GML2.0 structured
 * XML docs.
 *
 *@author Vision for New York
 *@author Rob Hranac
 *@version 0.9 alpha, 11/01/01
 *
 */
public class GetFeatureResponse {

		static Category _log = Category.getInstance(GetFeatureResponse.class.getName());

		// main request handler
		private GetFeatureRequest request = new GetFeatureRequest();
		private List queries = Collections.synchronizedList(new ArrayList());
		private List boundingBoxes = Collections.synchronizedList(new ArrayList());

	 /**
		* Empty constructor.
		*/ 
		public GetFeatureResponse(GetFeatureRequest request) {
				this.request = request;
				this.queries = request.getQueries(); 
				this.boundingBoxes = request.getBoundingBoxes();
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*
		* @param outputFormat The XML WFS GetFeature request.
		*/ 
		public String getXmlResponse () {

				_log.info("bounding box: " + (String) this.boundingBoxes.get(0) );

				//_log.info("am sending response");
				// return string
				String getFeatureResponse = "<?xml version='1.0' encoding='UTF-8'?>";
				getFeatureResponse = getFeatureResponse + "\n<featureCollection scope=\"http://freefs.vfny.org:81/freefs\">";

				// main handler and return string
				// generate GML for heander plus each table requested
				PostgisGetFeature postgresFeatureExtractor = new PostgisGetFeature("jdbc:postgresql","org.postgresql.Driver","Postgres");
				for( int i = 0; i < this.queries.size(); i++ ) {
						getFeatureResponse = getFeatureResponse + "\n <boundedBy>";
						getFeatureResponse = getFeatureResponse + "\n  <Box srsName=\"EPSG:32118\">";
						getFeatureResponse = getFeatureResponse + "\n   <coordinates>" + (String) this.boundingBoxes.get(i) + "</coordinates>";
						getFeatureResponse = getFeatureResponse + "\n  </Box>";
						getFeatureResponse = getFeatureResponse + "\n </boundedBy>";
						getFeatureResponse = getFeatureResponse + postgresFeatureExtractor.getFeature( ((SQLStatement) this.queries.get(i)) );
				}
				getFeatureResponse = getFeatureResponse + "\n</featureCollection>";

				return getFeatureResponse;
		}

}
