/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Category;

import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.responses.*;

/**
 * Implements the WFS GetFeature interface, which responds to requests for GML.
 * This servlet accepts a getFeatures request and returns GML2.1 structured
 * XML docs.
 *
 *@author Rob Hranac, Vision for New York
 *@version 0.9 beta, 11/01/01
 *
 */
public class GetFeature extends HttpServlet {


		/** Standard logging instance for class */
		private static Category _log = Category.getInstance(GetFeature.class.getName());

		/** Specifies MIME type */
		private static final String MIME_TYPE = "text/xml";

		/** Establishes clean request object, which processes getFeature client request */
		private static CleanRequest requestCleaner = new CleanRequest();


	 /**
		* Reads the XML request from the client, turns it into a generic request object, generates a generic response object, and writes to client.
		*
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/ 
		public void doPost(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException, IOException {

				// THIS IS CLUNKY; MUST BE A BETTER WAY TO DO THIS?
				/** create temporary response string */
				String tempResponse = "";

				// implements the main request/response logic
				try {
						GetFeatureRequest wfsRequest = readXmlRequest( request.getReader() );
						GetFeatureResponse wfsResponse = new GetFeatureResponse( wfsRequest );
						tempResponse = wfsResponse.getXmlResponse();
				}

				// catches all errors; client should never see a stack trace 
				catch (WfsException wfs) {
						tempResponse = wfs.getXmlResponse();
						_log.info("Threw a wfs exception: " + wfs.getMessage());
						wfs.printStackTrace(response.getWriter());
						wfs.printStackTrace();
				}
				catch (Exception e) {
						tempResponse = e.getMessage();
						_log.info("Had an undefined error: " + e.getMessage());
						e.printStackTrace(response.getWriter());
						e.printStackTrace();
				}

				// set content type and return response, whatever it is 
				response.setContentType(MIME_TYPE);
				response.getWriter().write( tempResponse );

		}


	 /**
		* Handles all Get requests.
		* This method implements the main return logic for the class.
		*
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/ 
		public void doGet(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException, IOException {

				// THIS IS CLUNKY; MUST BE A BETTER WAY TO DO THIS?
				/** create temporary response string */
				String tempResponse;

				// implements the main request/response logic
				try {
						GetFeatureRequest wfsRequest = readKvpRequest( request.getQueryString() );
						GetFeatureResponse wfsResponse = new GetFeatureResponse( wfsRequest );
						tempResponse = wfsResponse.getXmlResponse();
				}

				// catches all errors; client should never see a stack trace 
				catch (WfsException wfs) {
						tempResponse = wfs.getXmlResponse();
				}

				// set content type and return response, whatever it is 
				response.setContentType(MIME_TYPE);
				response.getWriter().write( tempResponse );

		}


	 /**
		* Internal method to pull the table names from the XML request query.
		*
		* @param request The raw servlet buffered request reader.
		* @return The processed get feature request.
		*/ 
		private GetFeatureRequest readXmlRequest(BufferedReader request)
				throws WfsException {

				// instantiates an XML request reader and returns appropriate request object
				GetFeatureReaderXml currentXmlRequest = new GetFeatureReaderXml( request );

				return currentXmlRequest.getRequest();
		}


	 /**
		* Internal method to pull the table names from the KVP request query.
		*
		* @param request The raw servlet string request.
		* @return The processed get feature request.
		*/ 
		private GetFeatureRequest readKvpRequest(String request)
				throws WfsException {

				// instantiates a KVP request reader and returns appropriate request object
				GetFeatureReaderKvp currentKvpRequest = new GetFeatureReaderKvp( request );

				return currentKvpRequest.getRequest();
		}

}
