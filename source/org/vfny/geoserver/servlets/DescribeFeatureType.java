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
 * Implements the WFS DescribeFeatureTypes inteface, which tells clients the schema for each feature type.
 *
 * This servlet returns descriptions of all feature types served by the FreeFS.
 * It does this by inspecting the table for the feature type every time it is
 * called.  This is not particularly efficient, but it does serve the purpose
 * of keeping in close sych with the database itself.  Future versions of this servlet
 * will likely store some of this data in the feature type directory.
 *
 * Note that this assumes that the possible schemas are only single tables,
 * with no foreign key relationships with other tables.
 *
 * IMPORTANT NOTE: This version assumes that all passwords, usernames, and database
 * for every table are the same.
 * 
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class DescribeFeatureType extends HttpServlet {

		/** standard logging instance for class */
		private static Category _log = Category.getInstance(DescribeFeatureType.class.getName());

		// THIS WILL LIKELY CHANGE SOON: NOT EXPLICITLY STATED IN SPEC
		/** set global MIME type */
		private static final String MIME_TYPE = "text/xml";

	 /**
		* Handles XML request objects and returns appropriate response.
		*
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/ 		
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

				// THIS IS CLUNKY; MUST BE A BETTER WAY TO DO THIS?
				/** create temporary response string */
				String tempResponse;

				// implements the main request/response logic
				try {
						DescribeFeatureTypeRequest wfsRequest = readXmlRequest( request.getReader() );
						DescribeFeatureTypeResponse wfsResponse = new DescribeFeatureTypeResponse( wfsRequest );
						tempResponse = wfsResponse.getXmlResponse();
				}

				// catches all errors; client should neve see a stack trace 
				catch (WfsException wfs) {
						tempResponse = wfs.getXmlResponse();
				}

				// set content type and return response, whatever it is 
				response.setContentType(MIME_TYPE);
				response.getWriter().write( tempResponse );

		}

	 /**
		* Handles KVP request objects and returns appropriate response.
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
						DescribeFeatureTypeRequest wfsRequest = readKvpRequest( request.getQueryString() );
						DescribeFeatureTypeResponse wfsResponse = new DescribeFeatureTypeResponse( wfsRequest );
						tempResponse = wfsResponse.getXmlResponse();
				}

				// catches all errors; client should neve see a stack trace 
				catch (WfsException wfs) {
						tempResponse = wfs.getXmlResponse();
				}

				// set content type and return response, whatever it is 
				response.setContentType(MIME_TYPE);
				response.getWriter().write( tempResponse );

		}


	 /**
		* Internal method to pull the feature type names from the XML request query.
		*
		* @param reader The raw servlet request reader.
		* @return The processed describe feature object.
		*/ 
		private DescribeFeatureTypeRequest readXmlRequest(BufferedReader reader)
				throws WfsException {

				/** instantiates an XML request reader and returns appropriate request object */
				DescribeFeatureTypeReaderXml currentXmlRequest = new DescribeFeatureTypeReaderXml( reader );

				return currentXmlRequest.getRequest();
		}

	 /**
		* Internal method to pull the feature type names from the KVP request query.
		*
		* @param request The raw servlet request string.
		* @return The processed describe feature object.
		*/ 
		private DescribeFeatureTypeRequest readKvpRequest(String request) 
				throws WfsException {

				/** instantiates an KVP request reader and returns appropriate request object */
				DescribeFeatureTypeReaderKvp currentKvpRequest = new DescribeFeatureTypeReaderKvp(request);

				return currentKvpRequest.getRequest();
		}



}
