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
import org.vfny.geoserver.responses.WfsException;

/**
 * Routes requests made at the top-level URI to appropriate interface servlet.
 * 
 * Note that the logic of this method could be generously described as 'loose.'
 * It is not checking for request validity in any way (this is done by the reqeust-
 * specific servlets).  Rather, it is attempting to make a reasonable geuss as to what
 * servlet to call, given that the client is routing to the top level URI as opposed
 * to the request-specific URI, as specified in the GetCapabilities response.
 * Thus, this is a convenience method, which allows for some slight client laziness
 * and helps explain to lost souls/spiders what lives at the URL.
 * Due to the string parsing, it is much faster (and recommended) to use the URIs
 * specified in the GetCapabablities response.
 *
 * @author Rob Hranac, Vision for New York
 * @version 0.9 beta, 11/01/01
 *
 */
public class Dispatcher extends HttpServlet {


		/** Standard logging instance for class */
		private Category _log = Category.getInstance(Dispatcher.class.getName());

		/** Stores MIME type */
		private static final String MIME_TYPE = "text/xml";

		/** Map metadata request type */
		public static String META_REQUEST = "GetMeta";

		/** Map get capabilities request type */
		public static final int GET_CAPABILITIES_REQUEST = 1;

		/** Map describe feature type request type */
		public static final int DESCRIBE_FEATURE_TYPE_REQUEST = 2;

		/** Map get feature  request type */
		public static final int GET_FEATURE_REQUEST = 3;

		/** Map get feature  request type */
		public static final int UNKNOWN = -1;

		/** Map get feature  request type */
		public static final int ERROR = -2;

		/** Map get capabilities request type */
		public static final String GET_CAPABILITIES_INTERFACE = "GetCapabilities";

		/** Map describe feature type request type */
		public static final String DESCRIBE_FEATURE_TYPE_INTERFACE = "DescribeFeatureType";

		/** Map get feature  request type */
		public static final String GET_FEATURE_INTERFACE = "GetFeature";


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/ 
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

				//BufferedReader tempReader = request.getReader();
				String tempResponse = new String();
				int targetRequest = 0;

				_log.info("got to post request");

				//request.getReader().mark(10000);

				try {

						if ( request.getReader() != null ) {
								DispatcherReaderXml requestTypeAnalyzer = new DispatcherReaderXml( request.getReader() );
								targetRequest = requestTypeAnalyzer.getRequestType();
						}
						else {
								targetRequest = UNKNOWN;	
						}

				}

				catch (WfsException wfs) {
						targetRequest = ERROR;
						tempResponse = wfs.getXmlResponse();
				}

				request.getReader().reset();

				forwardRequest( tempResponse, targetRequest, request, response );


		}


	 /**
		* Handles all Get requests.
		*
		* This method implements the main matching logic for the class.
		*
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/
		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

				String tempResponse = new String();
				int targetRequest = 0;

				// Examine the incoming request and create appropriate server objects
				//  to deal with each request
				//				try {

						if ( request.getQueryString() != null ) {
								
								DispatcherReaderKvp requestTypeAnalyzer = new DispatcherReaderKvp( request.getQueryString() );
								targetRequest = requestTypeAnalyzer.getRequestType();
						}								
						else {
						
								targetRequest = UNKNOWN;	

								// Special case: request string contained text, but didn't match a request
								// For now, this passes to the general server meta-data, but this should be replaced with a FreeFS exception later
						}
						/*
				}
				
				catch (WfsException wfs) {
						targetRequest = ERROR;
						tempResponse = wfs.getXmlResponse();
				}
				*/

				_log.info("request is type: " + targetRequest);

				forwardRequest( tempResponse, targetRequest, request, response );

		}


	 /**
		* Handles all request forwarding.
		*
		* This method implements the main matching logic for this class.
		*
		* @param tempResponse Error response string.
		* @param targetRequest Internally noted target response for forward.
		* @param request Servlet request object.
		* @param response Servlet request object.
		*/
		private void forwardRequest(String tempResponse, int targetRequest, HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

				// Examine the incoming request and create appropriate server objects
				//  to deal with each request
				//  then pass to appropriate servlet
				if ( targetRequest == GET_CAPABILITIES_REQUEST ) {
						
						RequestDispatcher dispatcher = request.getRequestDispatcher( GET_CAPABILITIES_INTERFACE );	
						dispatcher.forward(request, response);

				// Pass to appropriate servlet
				} else if ( targetRequest == DESCRIBE_FEATURE_TYPE_REQUEST ) {
						
						RequestDispatcher dispatcher = request.getRequestDispatcher( DESCRIBE_FEATURE_TYPE_INTERFACE );	
						dispatcher.forward(request, response);
						
				} else if	( targetRequest == GET_FEATURE_REQUEST ) {

						_log.info("sending to get feature");
						
						RequestDispatcher dispatcher = request.getRequestDispatcher( GET_FEATURE_INTERFACE );
						dispatcher.forward(request, response);

				} else if	( targetRequest == ERROR ) {
						
						_log.info("is error type");
			 
						// set content type and return response, whatever it is 
						response.setContentType(MIME_TYPE);
						response.getWriter().write( tempResponse );

				}

				// Special case: top-level URI requested, with no query GET data and no POST data
				// Pass to meta-data HTML page
				// Mostly for web-indexes (contains appropriate search engine, dynamic meta-data),
				// those who are intersted in WFS but don't know how to talk to it, and lost web-surfers.
				else {

						response.getWriter().write( "unknown request" );

						/*
						RequestDispatcher dispatcher = request.getRequestDispatcher("metadata/default.jsp");
						getServletContext().getNamedDispatcher("jsp");
						dispatcher.forward(request, response);
						*/
				} 

		}


}
