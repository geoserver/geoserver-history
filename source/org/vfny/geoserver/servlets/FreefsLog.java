/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets;

import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.*;

import org.apache.log4j.BasicConfigurator;


/**
 * Initializes all logging functions.
 * 
 * @author Rob Hranac, Vision for New York
 * @version 0.9 beta, 11/01/01
 *
 */
public class FreefsLog extends HttpServlet {
 

	 /**
		* Initializes logging.
		*
		*/ 
		public void init() {
				String prefix =  getServletContext().getRealPath("/");
				String file = getInitParameter("log4j-init-file");

				// if the log4j-init-file is not set, then no point in trying
				//if(file != null) {
				BasicConfigurator.configure();
						//PropertyConfigurator.configure( prefix + file );
						//}
		}
		

	 /**
		* Initializes logging.
		*
		* @param req The servlet request object.
		* @param resp The servlet response object.
		*/ 
		public void doGet(HttpServletRequest req, HttpServletResponse res) {
				BasicConfigurator.configure();
		}

}
