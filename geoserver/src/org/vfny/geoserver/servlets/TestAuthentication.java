/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.servlets;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.responses.*;

/**
 * Handles all user authentication; test servlet, not currently used.
 *
 *@author Rob Hranac, TOPP
 *@version $VERSION$
 */
public class TestAuthentication extends HttpServlet {


    /** Standard logging instance for class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.responses");
    

     /**
      * Passes the Post method to the Get method, with no modifications.
      *
      * @param request The servlet request object.
      * @param response The servlet response object.
      */ 
    public void doPost(HttpServletRequest request,HttpServletResponse response)
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
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        Authenticator gatekeeper = new Authenticator();

        if( gatekeeper.isAllowed(request.getRemoteHost()) ) {
            response.getWriter().write( "PASSED" );
        } else {
            response.getWriter().write( "FAILED" );
        }
        
    }
    
}


