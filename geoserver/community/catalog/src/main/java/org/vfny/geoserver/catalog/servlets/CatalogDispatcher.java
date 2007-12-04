/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog.servlets;

import org.geoserver.ows.util.EncodingInfo;
import org.geoserver.ows.util.XmlCharsetDetector;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.catalog.CatalogException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.servlets.AbstractService;
import org.vfny.geoserver.servlets.Dispatcher;
import org.vfny.geoserver.util.requests.readers.DispatcherKvpReader;
import org.vfny.geoserver.util.requests.readers.DispatcherXmlReader;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


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
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public class CatalogDispatcher extends Dispatcher {
    /**
         * Comment for <code>serialVersionUID</code>
         */
    private static final long serialVersionUID = -8409244945998729743L;

    /** Class logger */
    private static Logger LOGGER = Logger.getLogger("org.vfny.geoserver.servlets.catalog");
    private static int sequence = 123;
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String ENCODING_HEADER_ARG = "Content-Type";
    private static final Pattern ENCODING_PATTERN = Pattern.compile(
            "encoding\\s*\\=\\s*\"([^\"]+)\"");

    /** Temporary file used to store the request */
    private File temp;

    /**
     * This figures out a dispatched post request.  It writes the request to a
     * temp file, and then reads it in twice from there.  I found no other way
     * to do this, but this solution doesn't seem to bad.  Obviously it is
     * better to use the servlets directly, without having to go through this
     * random file writing and reading.  If our xml handlers were written more
     * dynamically this probably wouldn't be necessary, but it is.  Hopefully
     * this should help GeoServer's interoperability with clients, since most
     * of them are kinda stupid, and can't handle different url locations for
     * the different requests.
     *
     * @param request The servlet request object.
     * @param response The servlet response object.
     *
     * @throws ServletException For any servlet problems.
     * @throws IOException For any io problems.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        int targetRequest = 0;

        try {
            InputStream is = new BufferedInputStream(request.getInputStream());

            // REVISIT: Should do more than sequence here
            // (In case we are running two GeoServers at once)
            // - Could we use response.getHandle() in the filename?
            // - ProcessID is traditional, I don't know how to find that in Java
            sequence++;
            temp = File.createTempFile("catalogdispatch" + sequence, "tmp");

            FileOutputStream fos = new FileOutputStream(temp);
            BufferedOutputStream out = new BufferedOutputStream(fos);

            int c;

            while (-1 != (c = is.read())) {
                out.write(c);
            }

            is.close();
            out.flush();
            out.close();

            //JD: GEOS-323, Adding char encoding support
            EncodingInfo encInfo = new EncodingInfo();

            BufferedReader disReader;
            BufferedReader requestReader;

            try {
                disReader = new BufferedReader(XmlCharsetDetector.getCharsetAwareReader(
                            new FileInputStream(temp), encInfo));

                requestReader = new BufferedReader(XmlCharsetDetector.createReader(
                            new FileInputStream(temp), encInfo));
            } catch (Exception e) {
                /*
                 * Any exception other than CatalogException will "hang up" the
                 * process - no client output, no log entries, only "Internal
                 * server error". So this is a little trick to make detector's
                 * exceptions "visible".
                 */
                throw new CatalogException(e);
            }

            //JD: GEOS-323, Adding char encoding support
            if (disReader != null) {
                DispatcherXmlReader requestTypeAnalyzer = new DispatcherXmlReader();

                try {
                    requestTypeAnalyzer.read(disReader, request);
                } catch (ServiceException e) {
                    throw new CatalogException(e);
                }

                //targetRequest = requestTypeAnalyzer.getRequestType();
            } else {
                targetRequest = UNKNOWN;
            }

            LOGGER.fine("post got request " + targetRequest);

            doResponse(requestReader, request, response, targetRequest);
        } catch (ServiceException catalog) {
            HttpSession session = request.getSession();
            ServletContext context = session.getServletContext();
            GeoServer geoServer = (GeoServer) context.getAttribute(GeoServer.WEB_CONTAINER_KEY);
            String tempResponse = ((CatalogException) catalog).getXmlResponse(geoServer
                    .isVerboseExceptions(), request, geoServer);

            response.setContentType(geoServer.getCharSet().toString());
            response.getWriter().write(tempResponse);
        }
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
        final String queryString = request.getQueryString();

        if (queryString != null) {
            Map kvPairs = KvpRequestReader.parseKvpSet(queryString);
            targetRequest = DispatcherKvpReader.getRequestType(kvPairs);
        } else {
            targetRequest = UNKNOWN;

            //throw exception
        }

        doResponse(null, request, response, targetRequest);
    }

    /**
     * Does the actual response, creates the appropriate servlet to handle the
     * detected request.  Note that the requestReader is a bit of a hack, if
     * it is null then it is from a get Request, if not then that means the
     * request is being stored as a file, and needs to be read with this
     * particular reader.
     *
     * <p>
     * This does have the downside of forcing us to have doGet and doPost
     * methods of AbstractService be public, perhaps there is a good pattern
     * for handling  this.  Or we could try to re-write Dispatcher to extend
     * AbstractService, but it may be tricky.
     * </p>
     *
     * @param requestReader The reader of a file that contains the request,
     *        null if from a get request.
     * @param request The http request that was made.
     * @param response The response to be returned.
     * @param req_type The request type.
     *
     * @throws ServletException for any problems.
     * @throws IOException If anything goes wrong reading or writing.
     */
    protected void doResponse(Reader requestReader, HttpServletRequest request,
        HttpServletResponse response, int req_type) throws ServletException, IOException {
        AbstractService dispatched;

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(new StringBuffer("req_type is ").append(req_type).toString());
        }
    }
}
