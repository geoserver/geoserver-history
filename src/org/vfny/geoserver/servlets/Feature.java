/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets;

import org.vfny.geoserver.config.ConfigInfo;
import org.vfny.geoserver.requests.FeatureKvpReader;
import org.vfny.geoserver.requests.FeatureRequest;
import org.vfny.geoserver.requests.XmlRequestReader;
import org.vfny.geoserver.responses.FeatureResponse;
import org.vfny.geoserver.responses.WfsException;
import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Implements the WFS GetFeature interface, which responds to requests for GML.
 * This servlet accepts a getFeatures request and returns GML2.1 structured
 * XML docs.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: Feature.java,v 1.9 2003/09/15 23:07:16 cholmesny Exp $
 */
public class Feature extends HttpServlet {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.servlets");
    private static final ConfigInfo config = ConfigInfo.getInstance();

    /** Specifies MIME type */
    private static final String MIME_TYPE = config.getMimeType();

    /**
     * Reads the XML request from the client, turns it into a generic request
     * object, generates a generic response object, and writes to client.
     *
     * @param request The servlet request object.
     * @param response The servlet response object.
     *
     * @throws ServletException For servlet problems.
     * @throws IOException For problems writing.
     *
     * @task TODO: implement these to run in threads so we can report memory
     *       errors in wfsExceptions, instead of nasty servlet exceptions.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** create temporary response string */
        String tempResponse = "";

        // implements the main request/response logic
        try {
            FeatureRequest wfsRequest = XmlRequestReader.readGetFeature(request
                    .getReader());
            tempResponse = FeatureResponse.getXmlResponse(wfsRequest);
        }
        // catches all errors; client should never see a stack trace 
         catch (WfsException wfs) {
            tempResponse = wfs.getXmlResponse(config.isPrintStack());
            LOGGER.info("Threw a wfs exception: " + wfs.getMessage());

            //if(response != null) wfs.printStackTrace(response.getWriter());
            wfs.printStackTrace();
        } catch (Throwable e) {
            WfsException wfse = new WfsException(e, "UNCAUGHT EXCEPTION", null);
            tempResponse = wfse.getXmlResponse(true);
            LOGGER.info("Had an undefined error: " + e.getMessage());
            e.printStackTrace();
        }

        // set content type and return response, whatever it is 
        response.setContentType(MIME_TYPE);
        response.getWriter().write(tempResponse);
    }

    /**
     * Handles all Get requests. This method implements the main return logic
     * for the class.
     *
     * @param request The servlet request object.
     * @param response The servlet response object.
     *
     * @throws ServletException For servlet problems.
     * @throws IOException For problems writing.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** create temporary response string */
        String tempResponse;

        // implements the main request/response logic
        try {
            FeatureKvpReader currentKvpRequest = new FeatureKvpReader(request
                    .getQueryString());
            FeatureRequest wfsRequest = currentKvpRequest.getRequest();
            tempResponse = FeatureResponse.getXmlResponse(wfsRequest);
        }
        // catches all errors; client should never see a stack trace 
         catch (WfsException wfs) {
            tempResponse = wfs.getXmlResponse(config.isPrintStack());
        } catch (Throwable e) {
            WfsException wfse = new WfsException(e, "UNCAUGHT EXCEPTION", null);
            tempResponse = wfse.getXmlResponse(true);
            LOGGER.info("Had an undefined error: " + e.getMessage());
            e.printStackTrace();
        }

        // set content type and return response, whatever it is 
        response.setContentType(MIME_TYPE);
        response.getWriter().write(tempResponse);
    }
}
