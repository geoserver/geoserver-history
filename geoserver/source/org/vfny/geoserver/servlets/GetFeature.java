/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets;

import org.apache.log4j.Category;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.vfny.geoserver.servlets.utilities.*;

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
public class GetFeature extends HttpServlet {

		// specify mime type
		private static final String MIME_TYPE = "text/xml";

		// create standard logging instance for class
		private static Category _log = Category.getInstance(GetFeature.class.getName());

		// Establishes clean request object, which processes getFeature client request
		private static CleanRequest requestCleaner = new CleanRequest();

	 /**
		* Reads the XML request from the client, turns it into a generic request object, generates a generic response object, and writes to client.
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/ 
		public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

				// Reads the XML request, generates an associated response, and returns the response to the client 
				GetFeatureRequest wfsRequest = new XmlGetFeatureReader( request.getReader() );
				GetFeatureResponse wfsResponse = new GetFeatureResponse( wfsRequest );

				response.setContentType(MIME_TYPE);
				response.getWriter().write( wfsResponse.getXmlResponse() );

		}


	 /**
		* Handles all Get requests.
		* This method implements the main return logic for the class.
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/ 
		public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

				// Reads the KVP request, generates an associated response, and returns the response to the client 
				GetFeatureRequest wfsRequest = readKvpRequest( request.getQueryString() );
 				GetFeatureResponse wfsResponse = new GetFeatureResponse( wfsRequest );

				response.setContentType(MIME_TYPE);
				response.getWriter().write( wfsResponse.getXmlResponse() );
		}


	 /**
		* Internal method to pull the table names from the XML request query.
		* @param request The servlet request object.
		*/ 
		private GetFeatureRequest readXmlRequest(BufferedReader request) {

				// instantiates an XML request reader and returns appropriate request object
				XmlGetFeatureReader currentXmlRequest = new XmlGetFeatureReader( request );
				return currentXmlRequest;
		}


	 /**
		* Internal method to pull the table names from the KVP request query.
		* @param request The servlet request object.
		*/ 
		private GetFeatureRequest readKvpRequest(String request) {

				// instantiates an XML request reader and returns appropriate request object
				KvpGetFeatureReader currentKvpRequest = new KvpGetFeatureReader( request );
				return currentKvpRequest.getRequest();
		}

}
