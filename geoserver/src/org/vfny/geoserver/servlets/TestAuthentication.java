/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;

import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.responses.*;


/**
 * Handles all user authentication; test servlet, not currently used.
 *
 * @author Rob Hranac, Vision for New York
 * @version 0.9 beta, 11/01/01
 *
 */
public class TestAuthentication extends HttpServlet {


		/** Standard logging instance for class */
		private Category _log = Category.getInstance(GetCapabilities.class.getName());
		

	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param request The servlet request object.
		* @param response The servlet response object.
		*/ 
		public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
				
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


