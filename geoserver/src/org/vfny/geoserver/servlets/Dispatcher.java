/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets;

import org.vfny.geoserver.*;
import org.vfny.geoserver.config.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.requests.readers.*;
import org.vfny.geoserver.servlets.wfs.*;
import java.io.*;
import java.util.Map;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * Routes requests made at the top-level URI to appropriate interface servlet.
 * Note that the logic of this method could be generously described as
 * 'loose.' It is not checking for request validity in any way (this is done
 * by the reqeust- specific servlets).  Rather, it is attempting to make a
 * reasonable guess as to what servlet to call, given that the client is
 * routing to the top level URI as opposed to the request-specific URI, as
 * specified in the GetCapabilities response. Thus, this is a convenience
 * method, which allows for some slight client laziness and helps explain to
 * lost souls/spiders what lives at the URL. Due to the string parsing, it is
 * much faster (and recommended) to use the URIs specified in the
 * GetCapabablities response.  Currently does not support post requests, but
 * most requests for this will likely come with get.
 *
 * @author Rob Hranac, Vision for New York
 * @author Chris Holmes, TOPP
 * @version $Id: Dispatcher.java,v 1.5.4.1 2003/11/04 23:29:19 cholmesny Exp $
 */
public class Dispatcher extends HttpServlet {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.servlets");
    private static final ServerConfig config = ServerConfig.getInstance();

    /** Specifies MIME type */
    private static final String MIME_TYPE = config.getGlobalConfig()
                                                  .getMimeType();

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

    /**
     * Passes the Post method to the Get method, with no modifications.
     *
     * @param request The servlet request object.
     * @param response The servlet response object.
     *
     * @throws ServletException For any servlet problems.
     * @throws IOException For any io problems.
     *
     * @task REVISIT: This is not working yet, as we can't seem to figure out
     *       how to read the reader twice.  It must be read once to see what
     *       the request type is,  and again to actually analyze it.  But we
     *       haven't yet found the way  to read it twice.  There should be
     *       some way to do this, but it doesn't seem that important, as users
     *       who use post should be able to figure out which servlet to send
     *       it to. I'm removing DispatcherReaderXml and DispatcherHandler
     *       from cvs, so that they don't get in the 1.0 release.  If anyone
     *       attempts to implement this there are deleted versions in cvs.
     *       Check the attic on the webcvs, or just do a checkout with the
     *       rel_0_98 tag.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        //BufferedReader tempReader = request.getReader();
        //String tempResponse = new String();
        int targetRequest = 0;

        LOGGER.finer("got to post request");

        //request.getReader().mark(10000);
        /*    try {
           if ( request.getReader() != null ) {
           DispatcherReaderXml requestTypeAnalyzer = new DispatcherReaderXml( request.getReader());
           targetRequest = requestTypeAnalyzer.getRequestType();
            } else {
             targetRequest = UNKNOWN;
             }
            } catch (WfsException wfs) {
                        targetRequest = ERROR;
                        tempResponse = wfs.getXmlResponse();
            }*/
        //request.getReader().reset();
        doResponse(false, request, response, targetRequest);
    }

    /**
     * Handles all Get requests.  This method implements the main matching
     * logic for the class.
     *
     * @param request The servlet request object.
     * @param response The servlet response object.
     *
     * @throws ServletException For any servlet problems.
     * @throws IOException For any io problems.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        int targetRequest = 0;

        // Examine the incoming request and create appropriate server objects
        //  to deal with each request
        //              try {
        if (request.getQueryString() != null) {
            Map kvPairs = KvpRequestReader.parseKvpSet(request.getQueryString());
            targetRequest = DispatcherKvpReader.getRequestType(kvPairs);
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

            break;

        case DESCRIBE_FEATURE_TYPE_REQUEST:
            dispatched = new Describe();

            break;

        case GET_FEATURE_REQUEST:
            dispatched = new Feature();

            break;

        case TRANSACTION_REQUEST:
            dispatched = new Transaction();

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
        if ((dispatched != null) && !isPost) {
            dispatched.service(request, response);
        } else {
            String message;

            if (isPost) {
                message = "Post requests are not supported with the dispatcher "
                    + "servlet.  Please try the request using the appropriate "
                    + "request servlet, such as GetCapabilities or GetFeature";
            } else {
                message = "No wfs kvp request recognized.  The REQUEST parameter"
                    + " must be one of GetFeature, GetFeatureWIthLock, "
                    + "DescribeFeatureType, LockFeature, or Transaction.";
            }

            WfsException wfse = new WfsException(message);
            String tempResponse = wfse.getXmlResponse(false);
            response.setContentType(MIME_TYPE);
            response.getWriter().write(tempResponse);
        }
    }
}
