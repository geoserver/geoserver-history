/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.responses;

import java.io.*;
import java.util.*;

import org.apache.log4j.Category;

import org.vfny.geoserver.db.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.config.*;

/**
 * Handles a Get Feature request and creates a Get Feature response GML string.
 *
 *@author Rob Hranac, Vision for New York
 *@version 0.9 alpha, 11/01/01
 *
 */
public class GetFeatureResponse {


		/** standard logging class */
		static Category _log = Category.getInstance(GetFeatureResponse.class.getName());

		/** main request information */
		private GetFeatureRequest request = new GetFeatureRequest();


	 /**
		* Constructor, which is required to take a request object.
		*
		* @param request The corresponding request object, which may contain multiple queries.
		*
		*/ 
		public GetFeatureResponse(GetFeatureRequest request) {

				this.request = request;
		}


	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*
		*/ 
		public String getXmlResponse () 
				throws WfsException {


				// a factory to generate appropriate response types, based on datastore
				GetFeatureFactory responseFactory = new GetFeatureFactory();

				// creates a generic type for datastores, waiting for factory-specific type
				GetFeatureTransaction datastore = null;

				// tracks current query, for looping through multiple queries
				Query currentQuery = null;

				// return string buffer, initialized with guess at average return content length
				// SOMEDAY, LOOK INTO OPTIMIZING ON EXPECTED CONTENT LENGTH
				StringBuffer getFeatureResponse = new StringBuffer(10000);

				// add GML preamble
				getFeatureResponse.append("<?xml version='1.0' encoding='UTF-8'?>\n<featureCollection xmlns:gml=\"http://www.opengis.net/gml\" scope=\"http://freefs.vfny.org:81/geoserver\">");


				// main handler and return string
				//  generate GML for heander for each table requested
				for( int i = 0; i < request.getQueryCount(); i++ ) {

						// get current query, extract feature type information, and use this information to connect to dabase
						currentQuery = request.getQuery(i);

						// new get feature
						currentQuery.setDatastoreConfiguration();

						// the basic return type for Postgis
						datastore = responseFactory.createDatastore( currentQuery.getDatastoreType() );

						// append bounding box preamble to response
						getFeatureResponse.append("\n <boundedBy>");
						getFeatureResponse.append("\n  <Box srsName=\"EPSG:32118\">");
						getFeatureResponse.append("\n   <coordinates>" + request.getBoundingBox().getCoordinates() + "</coordinates>");
						getFeatureResponse.append("\n  </Box>");
						getFeatureResponse.append("\n </boundedBy>");

						// extract features from database (as GML) and append to output string
						getFeatureResponse.append( datastore.getFeature(currentQuery, request.getMaxFeatures()) );

				}

				// add final GML
				getFeatureResponse.append( "\n</featureCollection>" );

				// return final string
				return getFeatureResponse.toString();
		}

}
