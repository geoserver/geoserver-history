/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets;

import org.vfny.geoserver.*;
import org.vfny.geoserver.config.ServerConfig;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.requests.readers.*;
import org.vfny.geoserver.responses.*;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
public abstract class AbstractService extends HttpServlet {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.servlets");
    protected static final ServerConfig config = ServerConfig.getInstance();

    /** Specifies mime type */
    protected static final String MIME_TYPE = config.getGlobalConfig()
                                                    .getMimeType();
    private static Map context;

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    protected void doGet(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        // implements the main request/response logic
        Request serviceRequest = null;

        try {
            String qString = request.getQueryString();
            Map requestParams = KvpRequestReader.parseKvpSet(qString);
            KvpRequestReader requestReader = getKvpReader(requestParams);
            serviceRequest = requestReader.getRequest();
        } catch (Throwable e) {
            sendError(response, e);
        }

        doService(request, response, serviceRequest);
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    protected void doPost(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        Request serviceRequest = null;

        // implements the main request/response logic
        try {
            XmlRequestReader requestReader = getXmlRequestReader();
            serviceRequest = requestReader.read(request.getReader());
        } catch (ServiceException se) {
            sendError(response, se);
        } catch (Throwable e) {
            sendError(response, e);
        }

        doService(request, response, serviceRequest);
    }

    /**
     * DOCUMENT ME!
     *
     * @param request
     * @param response
     * @param serviceRequest
     *
     * @task TODO: move gzip response encoding to a filter servlet
     */
    protected void doService(HttpServletRequest request,
        HttpServletResponse response, Request serviceRequest) {
        try {
            Response serviceResponse = getResponseHandler();

            serviceResponse.execute(serviceRequest);

            // set content type and return response, whatever it is
            String contentType = serviceResponse.getContentType();
            response.setContentType(contentType);

            OutputStream out = response.getOutputStream();

            out = new BufferedOutputStream(out, 2 * 1024 * 1024);

            /*
               boolean gzipIt = requestSupportsGzip(request);
               if (gzipIt)
               {
                   LOGGER.finer("GZIPPING RESPONSE...");
                   response.setHeader("content-encoding", "gzip");
                   out = new GZIPOutputStream(out, 2048);
               }
             */
            serviceResponse.writeTo(out);
        } catch (Throwable e) {
            sendError(response, e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected abstract Response getResponseHandler();

    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ServiceException if the set of key/value pairs of parameters
     *         defined by <code>params</code> can't be parsed to a valid
     *         <code>RequestKvpReader</code>
     */
    protected abstract KvpRequestReader getKvpReader(Map params);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected abstract XmlRequestReader getXmlRequestReader();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected abstract ExceptionHandler getExceptionHandler();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getMimeType() {
        return config.getGlobalConfig().getMimeType();
    }

    /**
     * DOCUMENT ME!
     *
     * @param response DOCUMENT ME!
     * @param content DOCUMENT ME!
     */
    protected void send(HttpServletResponse response, CharSequence content) {
        try {
            response.setContentType(getMimeType());
            response.getWriter().write(content.toString());
        } catch (IOException ex) { //stream closed by client, do nothing
            LOGGER.fine(ex.getMessage());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param response DOCUMENT ME!
     * @param t DOCUMENT ME!
     */
    protected void sendError(HttpServletResponse response, Throwable t) {
        LOGGER.info("Had an undefined error: " + t.getMessage());
        t.printStackTrace();

        ExceptionHandler exHandler = getExceptionHandler();
        ServiceException se = exHandler.newServiceException(t,
                "UNCAUGHT EXCEPTION", null);
        sendError(response, se);
    }

    /**
     * DOCUMENT ME!
     *
     * @param response DOCUMENT ME!
     * @param se DOCUMENT ME!
     */
    protected void sendError(HttpServletResponse response, ServiceException se) {
        send(response, se.getXmlResponse());
    }

    /**
     * DOCUMENT ME!
     *
     * @param response DOCUMENT ME!
     * @param result DOCUMENT ME!
     */
    protected void send(HttpServletResponse response, Response result) {
        OutputStream responseOut = null;

        try {
            responseOut = response.getOutputStream();
        } catch (IOException ex) { //stream closed, do nothing.
            LOGGER.info("apparently client has closed stream: "
                + ex.getMessage());
        }

        OutputStream out = new BufferedOutputStream(responseOut);
        response.setContentType(result.getContentType());

        try {
            result.writeTo(out);
        } catch (ServiceException ex) {
            sendError(response, ex);
        }
    }

    /**
     * Checks if the client requests supports gzipped responses by quering it's
     * 'accept-encoding' header.
     *
     * @param request the request to query the HTTP header from
     *
     * @return true if 'gzip' if one of the supported content encodings of
     *         <code>request</code>, false otherwise.
     */
    protected boolean requestSupportsGzip(HttpServletRequest request) {
        boolean supportsGzip = false;
        String header = request.getHeader("accept-encoding");

        if ((header != null) && (header.indexOf("gzip") > -1)) {
            supportsGzip = true;
        }

        return supportsGzip;
    }
}
