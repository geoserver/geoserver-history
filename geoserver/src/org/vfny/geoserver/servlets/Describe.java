/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets;

import org.vfny.geoserver.config.ConfigInfo;
import org.vfny.geoserver.requests.DescribeKvpReader;
import org.vfny.geoserver.requests.DescribeRequest;
import org.vfny.geoserver.requests.XmlRequestReader;
import org.vfny.geoserver.responses.DescribeResponse;
import org.vfny.geoserver.responses.WfsException;
import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Implements the WFS DescribeFeatureTypes inteface, which tells clients the
 * schema for each feature type. This servlet returns descriptions of all
 * feature types served by the server. Note that this assumes that the
 * possible schemas are only single tables, with no foreign key relationships
 * with other tables.
 *
 * @author Rob Hranac, TOPP
 * @version $Id: Describe.java,v 1.6 2003/09/15 23:07:16 cholmesny Exp $
 */
public class Describe extends HttpServlet {
    /** Standard logging instance for class */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** Stores global MIME type */
    private static final String MIME_TYPE = ConfigInfo.getInstance()
                                                      .getMimeType();

    /**
     * Handles XML request objects and returns appropriate response.
     *
     * @param request The servlet request object.
     * @param response The servlet response object.
     *
     * @throws ServletException For servlet problems.
     * @throws IOException For problems writing.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** Create temporary response string */
        String tempResponse;

        // implements the main request/response logic
        try {
            DescribeRequest wfsRequest = XmlRequestReader
                .readDescribeFeatureType(request.getReader());
            DescribeResponse wfsResponse = new DescribeResponse(wfsRequest);
            tempResponse = wfsResponse.getXmlResponse();
        } catch (WfsException wfs) {
            tempResponse = wfs.getXmlResponse();
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
     * Handles KVP request objects and returns appropriate response.
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
            DescribeKvpReader currentKvpRequest = new DescribeKvpReader(request
                    .getQueryString());
            DescribeRequest wfsRequest = currentKvpRequest.getRequest();
            DescribeResponse wfsResponse = new DescribeResponse(wfsRequest);
            tempResponse = wfsResponse.getXmlResponse();
        } catch (WfsException wfs) {
            tempResponse = wfs.getXmlResponse();
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
