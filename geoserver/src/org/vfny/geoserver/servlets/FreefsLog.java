/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets;

import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.http.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Category;


/**
 * Initializes all logging functions.
 * 
 * @author Rob Hranac, Vision for New York
 * @version 0.9 beta, 11/01/01
 *
 */
public class FreefsLog extends HttpServlet {
 
    /** Holds mappings between HTTP and ASCII encodings */
    private static Category _log = Category.getInstance(FreefsLog.class.getName());

    /**
     * Initializes logging.
     *
     */ 
    public void init() {
        BasicConfigurator.configure();
        _log.info("Logger (log4j) initialized correctly.");
        //this.getServletContext().log("you are an idiot");
        /*
        String prefix =  getServletContext().getRealPath("/");
        String file = getInitParameter("log4j-init-file");
        
        // if the log4j-init-file is not set, then no point in trying
        if(file != null) {
            PropertyConfigurator.configure(prefix + file);
        }
        */
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
