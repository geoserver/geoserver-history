/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.responses.*;

import org.geotools.filter.NullFilter;

/**
 * Implements the WFS GetCapabilities interface, which tells clients what the server can do.
 *
 * @author Rob Hranac, Vision for New York
 * @version 0.9 beta, 11/01/01
 *
 */
public class HelloWorld extends HttpServlet {


    /** Standard logging instance for the class */
    private static Logger _log = Logger.getLogger(HelloWorld.class);
    
    /** Specifies mime type */
    private static final String MIME_TYPE = "text/xml";
    
    
    /**
     * Reads the XML request from the client, turns it into a generic request object, generates a generic response object, and writes to client.
     *
     * @param request The servlet request object.
     * @param response The servlet response object.
     */ 
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {

        NullFilter cats = new NullFilter();
        // set content type and return response, whatever it is 
        response.setContentType(MIME_TYPE);
        response.getWriter().write( "<html>hello cows</html>" );
    }
    
    
    /**
     * Handles all KVP GET request for GetCapabilities.
     *
     * @param request The servlet request object.
     * @param response The servlet response object.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        
        // set content type and return response, whatever it is 
        response.setContentType(MIME_TYPE);
        response.getWriter().write( "<html>hello cows</html>" );
        
    }
    
            
}


