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
 *@version $0.9 beta, 22/03/02$
 */
public class GetFeatureResponse {


		/** Standard logging class */
		private static Category _log = Category.getInstance(GetFeatureResponse.class.getName());

		/** Encapsulates all request information */
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
		* Parses the GetFeature reqeust and returns a contentHandler.
		*
		* @return XML response to send to client
		*/ 
		public String getXmlResponse () 
				throws WfsException {

				// tracks current query, for looping through multiple queries
				Query currentQuery = null;

				// a factory to generate appropriate response types, based on datastore
				GetFeatureFactory responseFactory = new GetFeatureFactory( request.getMaxFeatures() );

				// creates a generic type for datastores, waiting for factory-specific type
				GetFeatureTransaction datastore = null;

				// main handler and return string
				//  generate GML for heander for each table requested
				for( int i = 0; i < request.getQueryCount(); i++ ) {

						// get current query, extract feature type information, and use this information to connect to dabase
						currentQuery = request.getQuery(i);

						// new get feature
						currentQuery.setDatastoreConfiguration();

						// the basic return type for Postgis
						datastore = responseFactory.createDatastore( currentQuery.getDatastoreType() );

						// extract features from database (as GML) and append to output string
						datastore.getFeature(currentQuery);

				}

				// return final string
				return datastore.getFinalResponse();
		}

}
