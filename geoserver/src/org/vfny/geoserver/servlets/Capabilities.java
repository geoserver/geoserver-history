/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets;

import org.vfny.geoserver.config.ConfigInfo;
import org.vfny.geoserver.requests.CapabilitiesKvpReader;
import org.vfny.geoserver.requests.CapabilitiesRequest;
import org.vfny.geoserver.requests.XmlRequestReader;
import org.vfny.geoserver.responses.CapabilitiesResponse;
import org.vfny.geoserver.responses.WfsException;
import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Implements the WFS GetCapabilities interface, which tells clients what the
 * server can do.
 *
 * @author Rob Hranac, TOPP
 * @version $Id: Capabilities.java,v 1.5 2003/09/15 23:07:16 cholmesny Exp $
 */
public class Capabilities extends HttpServlet {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.servlets");

    /** Specifies mime type */
    private static final String MIME_TYPE = ConfigInfo.getInstance()
                                                      .getMimeType();

    /**
     * Handles all XML POST requests for GetCapabilities.
     *
     * @param request The servlet request object.
     * @param response The servlet response object.
     *
     * @throws ServletException For servlet problems.
     * @throws IOException For problems writing.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // create temporary response string
        String tempResponse = new String();

        // implements the main request/response logic
        try {
            CapabilitiesRequest wfsRequest = XmlRequestReader
                .readGetCapabilities(request.getReader());
            CapabilitiesResponse wfsResponse = new CapabilitiesResponse(wfsRequest);
            tempResponse = wfsResponse.getXmlResponse();
        }
        // catches all errors; client should never see a stack trace 
         catch (WfsException wfs) {
            tempResponse = wfs.getXmlResponse();
        } catch (Throwable e) {
            WfsException wfse = new WfsException(e, "UNCAUGHT EXCEPTION", null);
            tempResponse = wfse.getXmlResponse(true);
            LOGGER.info("Had an undefined error: " + e.getMessage());
            e.printStackTrace();
        }

        // set content type and return response, whatever it is 
        response.setContentType(MIME_TYPE);
        LOGGER.finest("response encoding is " + response.getCharacterEncoding());
        response.getWriter().write(tempResponse);
    }

    /**
     * Handles all KVP GET request for GetCapabilities.
     *
     * @param request The servlet request object.
     * @param response The servlet response object.
     *
     * @throws ServletException For servlet problems.
     * @throws IOException For problems writing.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // create temporary response string
        String tempResponse = new String();

        // implements the main request/response logic
        try {
            CapabilitiesKvpReader currentKvpRequest = new CapabilitiesKvpReader(request
                    .getQueryString());
            CapabilitiesRequest wfsRequest = currentKvpRequest.getRequest();
            CapabilitiesResponse wfsResponse = new CapabilitiesResponse(wfsRequest);
            tempResponse = wfsResponse.getXmlResponse();
        }
        // catches all errors; client should never see a stack trace 
         catch (WfsException wfs) {
            tempResponse = wfs.getXmlResponse();
        } catch (Throwable e) {
            WfsException wfse = new WfsException(e, "UNCAUGHT EXCEPTION", null);
            tempResponse = wfse.getXmlResponse(true);
            LOGGER.info("Had an undefined error: " + e.getMessage());
            e.printStackTrace();
        }

        LOGGER.finer("response is " + tempResponse);

        // set content type and return response, whatever it is 
        response.setContentType(MIME_TYPE);
        LOGGER.finer("response encoding is " + response.getCharacterEncoding());
        response.getWriter().write(tempResponse);
    }
}
