/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets;

import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.*;
//import javax.servlet.*;
import org.geotools.resources.Geotools;
import org.vfny.geoserver.config.ConfigInfo;

//Logging system
import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * Initializes all logging functions.
 * 
 * @author Rob Hranac, Vision for New York
 * @author Chris Holmes, TOPP
 * @version 0.9 beta, 11/01/01
 *
 */
public class FreefsLog extends HttpServlet {
 
   
    /** Default name for feature type schemas */
    private static final String CONFIG_FILE =  "configuration.xml";
    /** Default name for feature type schemas */
    private static final String CONFIG_DIR =  "data/";

    /**
     * The logger for the filter module.
     */
    private static final Logger LOG = Logger.getLogger("org.vfny.geoserver.servlets");
   

    /**
     * Initializes logging and config.
     *
     */ 
    public void init() {
	Geotools.init("Log4JFormatter", Level.INFO);
	String root = this.getServletContext().getRealPath("/");
	String path = root + CONFIG_DIR;
	LOG.finest("init with path" + path);
	ConfigInfo cfgInfo = ConfigInfo.getInstance(path);

    }
    
    
    /**
     * Initializes logging.
     *
     * @param req The servlet request object.
     * @param resp The servlet response object.
     */ 
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        //BasicConfigurator.configure();
    }
    
}
