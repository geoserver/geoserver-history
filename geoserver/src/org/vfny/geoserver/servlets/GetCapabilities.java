/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
//import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.Logger;
//import org.apache.log4j.ConsoleAppender;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.responses.*;


/**
 * Implements the WFS GetCapabilities interface, which tells clients what the server can do.
 *
 * @author Rob Hranac, Vision for New York
 * @version 0.9 beta, 11/01/01
 *
 */
public class GetCapabilities extends HttpServlet {


    /** Standard logging instance for the class */
    //private static Logger _log = Logger.getLogger(GetCapabilities.class);
    
    /** Specifies mime type */
    private static final String MIME_TYPE = "text/xml";
    
    
    /**
     * Handles all XML POST requests for GetCapabilities.
     *
     * @param request The servlet request object.
     * @param response The servlet response object.
     */ 
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        //BasicConfigurator.configure(new ConsoleAppender());
        //this.getServletContext().log("you are an idiot??????????????????");
        //_log.info("************* configuration working");
        System.out.println("************* sys out working");

        // create temporary response string
        // THIS IS CLUNKY; MUST BE A BETTER WAY TO DO THIS?
        String tempResponse = new String();
        
        // implements the main request/response logic
        try {
            GetCapabilitiesRequest wfsRequest = readXmlRequest( request.getReader() );
            GetCapabilitiesResponse wfsResponse = new GetCapabilitiesResponse( wfsRequest );
            tempResponse = wfsResponse.getXmlResponse();
        }
        
        // catches all errors; client should neve see a stack trace 
        catch (WfsException wfs) {
            tempResponse = wfs.getXmlResponse();
        }
        
        // set content type and return response, whatever it is 
        response.setContentType(MIME_TYPE);
        response.getWriter().write( tempResponse );
    }
    
    
    /**
     * Handles all KVP GET request for GetCapabilities.
     *
     * @param request The servlet request object.
     * @param response The servlet response object.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        //this.getServletContext().log("you are an idiot??????????????????");
        //BasicConfigurator.configure();
        //BasicConfigurator.configure(new ConsoleAppender());
        //_log.info("************* configuration working");
        System.out.println("************* sys out working");

        // create temporary response string
        // THIS IS CLUNKY; MUST BE A BETTER WAY TO DO THIS?
        String tempResponse = new String();
        
        // implements the main request/response logic
        try {
            GetCapabilitiesRequest wfsRequest = readKvpRequest( request.getQueryString() );
            GetCapabilitiesResponse wfsResponse = new GetCapabilitiesResponse( wfsRequest );
            tempResponse = wfsResponse.getXmlResponse();
        }
        
        // catches all errors; client should neve see a stack trace 
        catch (WfsException wfs) {
            tempResponse = wfs.getXmlResponse();
        }
        
        // set content type and return response, whatever it is 
        response.setContentType(MIME_TYPE);
        response.getWriter().write( tempResponse );
        
    }
    
    
    /**
     * Internal method to pull the table names from the XML request query.
     *
     * @param request The servlet request object.
     */ 
    private GetCapabilitiesRequest readXmlRequest(BufferedReader reader)
        throws WfsException {
        
        // instantiates an XML request reader and returns appropriate request object
        GetCapabilitiesReaderXml currentXmlRequest = new GetCapabilitiesReaderXml( reader );
        return currentXmlRequest.getRequest();
    }

    
    /**
     * Internal method to pull the table names from the KVP request query.
     *
     * @param currentRequest The servlet request object.
     */ 
    private GetCapabilitiesRequest readKvpRequest(String request)
        throws WfsException {
        
        // instantiates a KVP request reader and returns appropriate request object
        GetCapabilitiesReaderKvp currentKvpRequest = new GetCapabilitiesReaderKvp(request);
        return currentKvpRequest.getRequest();
    }
    
    
}


