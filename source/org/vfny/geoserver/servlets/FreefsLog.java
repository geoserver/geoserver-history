/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets;

import org.apache.log4j.BasicConfigurator;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.io.IOException;

public class FreefsLog extends HttpServlet {
 
		public void init() {
				String prefix =  getServletContext().getRealPath("/");
				String file = getInitParameter("log4j-init-file");

				// if the log4j-init-file is not set, then no point in trying
				//if(file != null) {
				BasicConfigurator.configure();
						//PropertyConfigurator.configure( prefix + file );
						//}
		}
		
		public void doGet(HttpServletRequest req, HttpServletResponse res) {
				BasicConfigurator.configure();
		}

}
