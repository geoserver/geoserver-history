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

		// create standard logging instance for class
		private static Category _log = Category.getInstance(DescribeFeatureType.class.getName());

		private static final String MIME_TYPE = "text/xml";

	 /**
		* Handles XML request objects and returns appropriate response.
		*
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/ 		
		public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

				// Reads the XML request, generates an associated response, and returns the response to the client 
				DescribeFeatureTypeRequest wfsRequest = readXmlRequest( request.getReader() );
				DescribeFeatureTypeResponse wfsResponse = new DescribeFeatureTypeResponse( wfsRequest );

				response.setContentType(MIME_TYPE);
				response.getWriter().write( wfsResponse.getXmlResponse() );

		}

	 /**
		* Handles KVP request objects and returns appropriate response.
		*
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/ 
		public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

				// Reads the KVP request, generates an associated response, and returns the response to the client 
				DescribeFeatureTypeRequest wfsRequest = readKvpRequest( request.getQueryString() );
				DescribeFeatureTypeResponse wfsResponse = new DescribeFeatureTypeResponse( wfsRequest );

				response.setContentType(MIME_TYPE);
				response.getWriter().write( wfsResponse.getXmlResponse() );

		}


	 /**
		* Internal method to pull the table names from the XML request query.
		*
		* @param request The servlet request object.
		*/ 
		private DescribeFeatureTypeRequest readXmlRequest(BufferedReader reader) throws IOException {

				// instantiates an XML request reader and returns appropriate request object
				XmlDescribeFeatureTypeReader currentXmlRequest = new XmlDescribeFeatureTypeReader( reader );
				return currentXmlRequest.getRequest();
		}

	 /**
		* Internal method to pull the table names from the KVP request query.
		*
		* @param currentRequest The servlet request object.
		*/ 
		private DescribeFeatureTypeRequest readKvpRequest(String request) {

				// instantiates a KVP request reader and returns appropriate request object
				KvpDescribeFeatureTypeReader currentKvpRequest = new KvpDescribeFeatureTypeReader(request);
				return currentKvpRequest.getRequest();
		}



}
