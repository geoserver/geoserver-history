/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.logging.Logger;

import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.responses.WfsException;

/**
 * Routes requests made at the top-level URI to appropriate interface servlet.
 * 
 * Note that the logic of this method could be generously described as 'loose.'
 * It is not checking for request validity in any way (this is done by the reqeust-
 * specific servlets).  Rather, it is attempting to make a reasonable guess as to what
 * servlet to call, given that the client is routing to the top level URI as opposed
 * to the request-specific URI, as specified in the GetCapabilities response.
 * Thus, this is a convenience method, which allows for some slight client laziness
 * and helps explain to lost souls/spiders what lives at the URL.
 * Due to the string parsing, it is much faster (and recommended) to use the URIs
 * specified in the GetCapabablities response.
 *
 * @author Rob Hranac, Vision for New York
 * @version 0.9 beta, 11/01/01
 *
 */
public class Dispatcher extends HttpServlet {
    
    /** Class logger */
    private static Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.servlets");

        /** Stores MIME type */
    private static final String MIME_TYPE = "text/xml";
    
    /** Map metadata request type */
    public static String META_REQUEST = "GetMeta";

    /** Map get capabilities request type */
    public static final int GET_CAPABILITIES_REQUEST = 1;
    
    /** Map describe feature type request type */
    public static final int DESCRIBE_FEATURE_TYPE_REQUEST = 2;

    /** Map get feature  request type */
    public static final int GET_FEATURE_REQUEST = 3;
    
    /** Map get feature request type */
    public static final int TRANSACTION_REQUEST = 4;
    
    /** Map get feature with lock request type */
    public static final int GET_FEATURE_LOCK_REQUEST = 5;
    
    /** int representation of a lock request type */
    public static final int LOCK_REQUEST = 6;
    
    /** Map get feature  request type */
    public static final int UNKNOWN = -1;
    
    /** Map get feature  request type */
    public static final int ERROR = -2;
    
    /** Map get capabilities request type */
    public static final String GET_CAPABILITIES_INTERFACE = "GetCapabilities";
    
    /** Map describe feature type request type */
    public static final String DESCRIBE_FEATURE_TYPE_INTERFACE = "DescribeFeatureType";

    /** Map get feature  request type */
    public static final String GET_FEATURE_INTERFACE = "GetFeature";
    

    /**
     * Passes the Post method to the Get method, with no modifications.
     *
     * @param request The servlet request object.
     * @param response The servlet response object.
     */ 
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    
    //BufferedReader tempReader = request.getReader();
    String tempResponse = new String();
    int targetRequest = 0;
    
    LOGGER.finer("got to post request");
    
    //request.getReader().mark(10000);
    
    try {
        
        if ( request.getReader() != null ) {
        DispatcherReaderXml requestTypeAnalyzer = new DispatcherReaderXml( request.getReader() );
        targetRequest = requestTypeAnalyzer.getRequestType();
        }
        else {
        targetRequest = UNKNOWN;    
        }
        
    }
    
    catch (WfsException wfs) {
        targetRequest = ERROR;
        tempResponse = wfs.getXmlResponse();
    }
    
    request.getReader().reset();
    
    //forwardRequest( tempResponse, targetRequest, request, response );
    
    
    }
    

    /**
     * Handles all Get requests.
     * 
     * This method implements the main matching logic for the class.
     *
     * @param request The servlet request object.
     * @param response The servlet response object.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    
    String tempResponse = new String();
    int targetRequest = 0;
    
    // Examine the incoming request and create appropriate server objects
    //  to deal with each request
    //              try {
    
    if ( request.getQueryString() != null ) {
        
        DispatcherKvpReader requestTypeAnalyzer = new DispatcherKvpReader( request.getQueryString() );
        targetRequest = requestTypeAnalyzer.getRequestType();
    } else {
        
        targetRequest = UNKNOWN;    
        //throw exception
    }
    
    doResponse(false, request, response, targetRequest);
    
    }
    
    private void doResponse(boolean isPost, HttpServletRequest request, 
                HttpServletResponse response, int req_type)  
    throws ServletException, IOException {
    HttpServlet dispatched;
    LOGGER.finer("req_type is " + req_type);
    switch (req_type) {
    case GET_CAPABILITIES_REQUEST:
        dispatched = new Capabilities();
        //capServlet.doGet(request, response);
        break;
    case DESCRIBE_FEATURE_TYPE_REQUEST: 
        dispatched = new Describe();
        //descServlet.doGet(request, response);
        break;
    case GET_FEATURE_REQUEST: 
        dispatched = new Feature();
        //featServlet.doGet(request, response);
        break;
    case TRANSACTION_REQUEST:
        dispatched = new Transaction();
        //transServlet.doGet(request, response);
        break;
    case GET_FEATURE_LOCK_REQUEST:
        dispatched = new FeatureWithLock();
        break;
    case LOCK_REQUEST:
        dispatched = new Lock();
        break;
    default: 
        dispatched = null;
    }
    //TODO: catch the servlet exceptions from the other servlets.
    if (dispatched != null) {
        if (isPost) {
            
        } else {
            dispatched.service(request, response); 
        }
    } else {
        String message = "No wfs kvp request recognized.  Make sure" 
        + " the REQUEST kvp is a valid wfs request (only basic now supported)";
        
        WfsException wfse = new WfsException(message);
        String tempResponse = wfse.getXmlResponse(false);
        response.setContentType(MIME_TYPE);
        response.getWriter().write(tempResponse);
    }
  
    
    
    }
}


