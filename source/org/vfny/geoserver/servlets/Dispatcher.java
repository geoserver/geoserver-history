/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Routes requests made at the top-level URI to appropriate interface servlet.
 * 
 * THIS METHOD IS NOT USED IN ALPHA VERSION; WILL BE IMPLEMENTED IN LATER VERSIONS.
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
 * @author Vision for New York
 * @author Rob Hranac
 * @version 0.9 alpha, 11/01/01
 *
 */

public class Dispatcher extends HttpServlet {

		// Map request types into global variables
		private static String META_REQUEST = "GetMeta";
		private static String GET_CAPABILITIES_REQUEST = "GetCapabilities";
		private static String DESCRIBE_FEATURE_TYPE_REQUEST = "DescribeFeatureType";
		private static String GET_FEATURE_REQUEST = "GetFeature";

	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/ 
		public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				doGet(request, response);
		}

	 /**
		* Handles all Get requests.
		*
		* This method implements the main matching logic for the class.
		*
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/
		public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

				// Sets content type and several convenience variables for processing the request
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				String targetServlet = new String();
				String targetId = new String();

				// Examine the incoming request and create appropriate server objects
				//  to deal with each request
				if ( request.getQueryString() == null ) {

						// Special case: top-level URI requested, with no query data
						// Pass to meta-data HTML page
						// Mostly for web-indexes (contains appropriate search engine, dynamic meta-data),
						// those who are intersted in WFS but don't know how to talk to it, and lost web-surfers.

						out.write( request.getServletPath() );
						RequestDispatcher dispatcher = request.getRequestDispatcher("/metadata/default.jsp");
						getServletContext().getNamedDispatcher("jsp");
						dispatcher.forward(request, response);

						//RequestDispatcher dispatcher = getServletContext().getNamedDispatcher("jsp");
						//request.setAttribute( "servletPath", "freefs/metadata/default.jsp");
						//dispatcher.include(request, response);

				}
				else {
						// Reeturn search string
						targetId = identifyRequest( request.getQueryString() );
								
						// Pass to appropriate servlet
						if ( targetId.equals(GET_CAPABILITIES_REQUEST) ) {
								RequestDispatcher dispatcher = request.getRequestDispatcher(GET_CAPABILITIES_REQUEST);	
								dispatcher.forward(request, response);
						} else if ( targetId.equals(DESCRIBE_FEATURE_TYPE_REQUEST) ) {
								RequestDispatcher dispatcher = request.getRequestDispatcher(DESCRIBE_FEATURE_TYPE_REQUEST);	
								dispatcher.forward(request, response);
						} else if	 ( targetId.equals(GET_FEATURE_REQUEST) ) {
								RequestDispatcher dispatcher = request.getRequestDispatcher(GET_FEATURE_REQUEST);
								dispatcher.forward(request, response);
						}

						// Special case: request string contained text, but didn't match a request
						// For now, this passes to the general server meta-data, but this should be replaced with a FreeFS exception later
						else {
								RequestDispatcher dispatcher = request.getRequestDispatcher("metadata/default.jsp");
								dispatcher.forward(request, response);
						} 
				}
				
		}

	 /**
		* This method determines which Response  is most closely matches the Request..
		*
		* @param searchString The generic request string to search for a specific reqeust..
		* @return A string specifying the appropriate servlet or jsp. 
		*/ 
		private String identifyRequest(String searchString) {

				// Looping variable to traverse entire string
				int i = 0;

				// Check first to make sure that the search string contains data.  If not, route to default HTML meta-data.
				if ( searchString.length() == 0 )
						return META_REQUEST;
				else {

						// Loop through entire search string
						while ( i < searchString.length() ) {

								// If, during any given look, GetCapabilities string is found, return request string.
								if ( i <= searchString.length() - GET_CAPABILITIES_REQUEST.length() )
										if ( searchString.substring(i, i + GET_CAPABILITIES_REQUEST.length() ).equals(GET_CAPABILITIES_REQUEST) )
												return GET_CAPABILITIES_REQUEST;

								// If, during any given look, DescribeFeature Type string is found, return request string.
								if ( i <= searchString.length() - DESCRIBE_FEATURE_TYPE_REQUEST.length() )
										if ( searchString.substring(i, i + DESCRIBE_FEATURE_TYPE_REQUEST.length() ).equals(DESCRIBE_FEATURE_TYPE_REQUEST) )
												return DESCRIBE_FEATURE_TYPE_REQUEST;

								// If, during any given look, GetFeature string is found, return request string.
								if ( i <= searchString.length() - GET_FEATURE_REQUEST.length() )
										if ( searchString.substring(i, i + GET_FEATURE_REQUEST.length() ).equals(GET_FEATURE_REQUEST) )
												return GET_FEATURE_REQUEST;

								// Increment string position pointer
								i++;
						}

						// Return to main HTML meta-data, in case of no match.
						return META_REQUEST;
				}
		}
}
