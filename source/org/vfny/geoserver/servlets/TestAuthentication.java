/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.vfny.geoserver.servlets.utilities.*;

/**
 * Implements the WFS GetCapabilities interface, which tells clients what the server can do.
 *
 * Note that this behavior is implemented using
 * the early access release of JAX-B from Sun.  I found this release to be
 * brilliant in concept, but imperfect in implementation.  In particular,
 * the validator appears to throw invalid exceptions on nested, repeated
 * subelements of valid, well-formed internal XML document representations.
 *
 * Therefore, the get response is assembled not as a monolithic document,
 * which would be much neater, but as a series of subdocuments.  Also, I 
 * have implemented some horrible hacks in the auto-generated code to
 * get it to work in places.  My advice: don't regenerate this code.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */

public class TestAuthentication extends HttpServlet {

		private Category _log = Category.getInstance(GetCapabilities.class.getName());
		
	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/ 
		public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				Authenticator gatekeeper = new Authenticator();

				if( gatekeeper.isAllowed(request.getRemoteHost()) ) {
						response.getWriter().write( "PASSED" );
				}
				else {
						response.getWriter().write( "FAILED" );
				}

		}

	 /**
		* Handles all Get requests.
		*
		* This method implements the main return XML logic for the class.
		*
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/
		public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				Authenticator gatekeeper = new Authenticator();

				if( gatekeeper.isAllowed(request.getRemoteHost()) ) {
						response.getWriter().write( "PASSED" );
				}
				else {
						response.getWriter().write( "FAILED" );
				}

		}

}


