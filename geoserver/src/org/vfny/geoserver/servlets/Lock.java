/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.servlets;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vfny.geoserver.config.TypeInfo;
import org.vfny.geoserver.config.ConfigInfo;
import org.vfny.geoserver.requests.LockRequest;
import org.vfny.geoserver.requests.LockKvpReader;
import org.vfny.geoserver.requests.XmlRequestReader;
import org.vfny.geoserver.responses.LockResponse;
import org.vfny.geoserver.responses.WfsException;


/**
 * Implements the WFS Lock interface, which performs insert, update and
 * delete functions on the dataset.
 * This servlet accepts a Lock request and returns a LockResponse
 * xml element.
 *
 *@author Chris Holmes, TOPP
 *@version $Version$
 */
public class Lock
    extends HttpServlet {

    /** Class logger */
    private static Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.servlets");
    
    /** Specifies MIME type */
    private static final String MIME_TYPE = 
	ConfigInfo.getInstance().getMimeType();

    /**
     * Reads the XML request from the client, turns it into a generic request 
     * object, generates a generic response object, and writes to client.
     * @param request The servlet request object.
     * @param response The servlet response object.
     */ 
    public void doPost(HttpServletRequest request,HttpServletResponse response)
        throws ServletException, IOException {
        
        /** create temporary response string */
        String tempResponse = "";
	
        // implements the main request/response logic
	LockRequest wfsRequest = null;
	try {
            wfsRequest = XmlRequestReader.readLockRequest(request.getReader());
	    LOGGER.finer("sending request to Lock Response: " + wfsRequest);
            tempResponse = LockResponse.getXmlResponse(wfsRequest);        
        
        // catches all errors; client should not see a stack trace for a wfs
	    //error.
	    } catch (WfsException we) {
		LOGGER.info("Caught a WfsException...");
		//if (wfsRequest != null) {
		//    we.setHandle(wfsRequest.getHandle());
		//}
		tempResponse =  we.getXmlResponse();
	    }
	catch (Throwable e) {
	    WfsException wfse = new WfsException(e, "UNCAUGHT EXCEPTION",
						 null);
       	    tempResponse = wfse.getXmlResponse(true);
            LOGGER.info("Had an undefined error: " + e.getMessage());
	    e.printStackTrace();
        }
        
        response.setContentType(MIME_TYPE);
        response.getWriter().write( tempResponse );
        
    }
    
    
    /**
     * Handles all Get requests.
     * This method implements the main return logic for the class.
     * @param request The servlet request object.
     * @param response The servlet response object.
     */ 
    public void doGet(HttpServletRequest request,HttpServletResponse response) 
        throws ServletException, IOException {
        
        /** create temporary response string */
        String tempResponse;

	LOGGER.finer("incoming lock kvp request");

        
        // implements the main request/response logic
        try {
            LockKvpReader currentKvpRequest = 
                new LockKvpReader(request.getQueryString());
            LockRequest wfsRequest = 
                currentKvpRequest.getRequest();
            tempResponse = LockResponse.getXmlResponse(wfsRequest);
        }
        
        // catches all errors; client should never see a stack trace 
        catch (WfsException wfs) {
            tempResponse = wfs.getXmlResponse();
        }
	catch (Throwable e) {
	    WfsException wfse = new WfsException(e, "UNCAUGHT EXCEPTION",
						 null);
       	    tempResponse = wfse.getXmlResponse(true);
            LOGGER.info("Had an undefined error: " + e.getMessage());
	    e.printStackTrace();
        }
        
        // set content type and return response, whatever it is 
        response.setContentType(MIME_TYPE);
        response.getWriter().write( tempResponse );        
    }    
}
