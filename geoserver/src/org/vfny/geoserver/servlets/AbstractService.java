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
 * Represents a service that all others extend from.  Subclasses should provide
 * response and exception handlers as appropriate.
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: AbstractService.java,v 1.1.2.9 2003/11/16 19:29:39 groldan Exp $
 *
 * @task TODO: I changed this so it automatically buffers responses, so  as to
 *       better handle errors, not serving up nasty servlet errors if
 *       something goes wrong during the transforms.  This obviously slows
 *       things down and is not scalable.  First we should make it user
 *       configurable.   This will allow users to say if they want things
 *       absolutely complaint (returning errors other than the proper xml
 *       ServiceExceptions is required by the spec, but if we hit an error
 *       while writing to out we've already started writing.  Though perhaps
 *       there's some way to access what is actually written?  I mean the
 *       servlet container does it...), or if they want more performance at
 *       the expense of being absolutely compliant.  This ideally should
 *       happen very rarely, as the execute method should take care of most of
 *       the errors that may arise, but it's still going to happen, since for
 *       GetFeature we definitely do a good amount of working during the
 *       transform.
 */
public abstract class AbstractService extends HttpServlet {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.servlets");

    /** DOCUMENT ME! */
    protected static final ServerConfig config = ServerConfig.getInstance();

    /** Specifies mime type */
    protected static final String MIME_TYPE = config.getGlobalConfig()
                                                    .getMimeType();
    private static Map context;

    /**
     * GR: if SPEED, FILE and BUFFER are static instances, so their methods
     * should be synchronized, ending in a not multiuser server, so I made
     * saftyMode dynamically instantiated in init() and the stratagy choosed
     * at server config level in web.xml. If I'm wrong, just tell me. If this
     * is correct, may be it will be better to allow for user customized
     * ServiceStratagy implementations to be parametrized by a servlet context
     * param
     */
    public static final Map serviceStratagys = new HashMap();

    static {
        serviceStratagys.put("SPEED", SpeedStratagy.class);
        serviceStratagys.put("FILE", FileStratagy.class);
        serviceStratagys.put("BUFFER", BufferStratagy.class);
    }

    /** Controls the Safty Mode used when using execute/writeTo. */
    private static Class saftyMode;

    /**
     * loads the "serviceStratagy" servlet context parameter and checks it if
     * reffers to a valid ServiceStratagy (by now, one of SPEED, FILE or
     * BUFFER); if no, just sets the stratagy to BUFFER as default
     *
     * @param config the servlet environment
     *
     * @throws ServletException if the configured stratagy class is not a
     *         derivate of ServiceStratagy or it is thrown by the parent class
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        LOGGER.info("Looking for configured service responses' stratagy");

        ServletContext context = config.getServletContext();
        String stgyKey = context.getInitParameter("serviceStratagy");
        Class stgyClass = BufferStratagy.class;

        if (stgyKey == null) {
            LOGGER.info("No service stratagy configured, defaulting to BUFFER");
        } else {
            LOGGER.info("Looking for configured service stratagy " + stgyKey);

            Class configurefStgyClass = (Class) serviceStratagys.get(stgyKey);

            if (configurefStgyClass == null) {
                LOGGER.info("No service stratagy named " + stgyKey
                    + "found, defaulting to BUFFER. Please check your config");
            }else{
              stgyClass = configurefStgyClass;
            }
        }

        LOGGER.fine("verifying configured stratagy");

        if (!(ServiceStratagy.class.isAssignableFrom(stgyClass))) {
            throw new ServletException("the configured service stratagy "
                + stgyClass + " is not a ServiceStratagy derivate");
        }

        LOGGER.info("Using service stratagy " + stgyClass);
        this.saftyMode = stgyClass;
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
     * @throws ServletException if the stratagy can't be instantiated
     *
     * @task TODO: move gzip response encoding to a filter servlet
     */
    protected void doService(HttpServletRequest request,
        HttpServletResponse response, Request serviceRequest)
        throws ServletException {
        ServiceStratagy stratagy = null;
        LOGGER.finest("getting stratagy instance");

        try {
            stratagy = (ServiceStratagy) saftyMode.newInstance();
        } catch (Exception ex) {
            throw new ServletException(stratagy
                + " is not a valid ServiceStratagy", ex);
        }

        LOGGER.finer("stratagy is: " + stratagy);

        Response serviceResponse = getResponseHandler();
        LOGGER.finer("response handler is: " + serviceResponse);

        try {
            // execute request
            LOGGER.fine("executing request");
            serviceResponse.execute(serviceRequest);
        } catch (ServiceException serviceException) {
            LOGGER.warning("service exception while executing request: "
                + serviceException.getMessage());
            serviceResponse.abort();
            sendError(response, serviceException);
            return;
        }catch(Throwable t){
          //we can safelly send errors here, since we have not touched response yet
          sendError(response, t);
        }

        String mimeType = serviceResponse.getContentType();
        response.setContentType(mimeType);
        LOGGER.fine("execution succeed, mime type is: " + mimeType);

        OutputStream stratagyOuput = null;

        //obtain the stratagy output stream
        try {
            LOGGER.finest("getting stratagy output");
            stratagyOuput = stratagy.getDestination(response);
            LOGGER.finer("stratagy output is: "
                + stratagyOuput.getClass().getName());
        } catch (IOException ex) {
            if (ex instanceof java.net.SocketException) {
                LOGGER.fine(
                    "it seems that the user has closed the request stream: "
                    + ex.getMessage());
            } else {
                sendError(response, ex);
            }
        }

        try {
            // gather response
            serviceResponse.writeTo(stratagyOuput);
            stratagyOuput.flush();
            stratagy.flush();
        } catch(java.net.SocketException sockEx){
          //it's ok, user bored waiting a response or socket error
        }catch (Exception ex) {
            //ups! this is the worst error case, since we have already setted
            //the response's mime type and are not sure about the state of
            //the response object. Anyway, try to send an error
            sendError(response, ex);
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

        //TODO: put the stack trace in the logger.
        t.printStackTrace();

        String pre = "UNCAUGHT EXCEPTION";
        ExceptionHandler exHandler = getExceptionHandler();
        ServiceException se = exHandler.newServiceException(t, pre, null);

        //sendError(response, se);
        send(response, se.getXmlResponse(true));
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
        } catch (IOException ioe) {
            //user just closed the socket stream, do nothing
            LOGGER.fine("connection closed by user: " + ioe.getMessage());
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

    /**
     * Interface used for ServiceMode stratagy objects.
     *
     * <p>
     * While this interface resembles the Enum idiom in that only three
     * instances are available SPEED, BUFFER and FILE, we are using this class
     * to plug-in the implementation for our doService request in the manner
     * of the Stratagy pattern.
     * </p>
     *
     * @author Jody Garnett, Refractions Research
     */
    static public interface ServiceStratagy {
        public OutputStream getDestination(HttpServletResponse response)
            throws IOException;

        public void flush() throws IOException;
    }
}


/**
 * Fast and Dangeroud service stratagy.
 *
 * <p>
 * Will fail when a ServiceException is encountered on writeTo, and will not
 * tell the user about it! This is the worst case scenario, you are trading
 * speed for danger by using this ServiceStratagy.
 * </p>
 *
 * @author jgarnett
 */
class SpeedStratagy implements AbstractService.ServiceStratagy {
    private OutputStream out = null;

    /**
     * DOCUMENT ME!
     *
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public OutputStream getDestination(HttpServletResponse response)
        throws IOException {
        out = response.getOutputStream();
        out = new BufferedOutputStream(out);

        return out;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void flush() throws IOException {
        if (out != null) {
            out.flush();
        }
    }
}


/**
 * A safe Service stratagy that buffers output until writeTo completes.
 *
 * <p>
 * This stratagy wastes memory, for saftey. It represents a middle ground
 * between SpeedStratagy and FileStratagy
 * </p>
 *
 * @author jgarnett To change the template for this generated type comment go
 *         to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
class BufferStratagy implements AbstractService.ServiceStratagy {
    /** DOCUMENT ME! */
    ByteArrayOutputStream buffer = null;
    private HttpServletResponse response;

    /**
     * DOCUMENT ME!
     *
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public OutputStream getDestination(HttpServletResponse response)
        throws IOException {
        this.response = response;
        buffer = new ByteArrayOutputStream(1024 * 1024);

        return buffer;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void flush() throws IOException {
        if ((buffer == null) || (response == null)) {
            return;
        }

        OutputStream out = response.getOutputStream();
        BufferedOutputStream buffOut = new BufferedOutputStream(out, 1024 * 1024);
        buffer.writeTo(buffOut);
        buffOut.flush();
        buffOut.close();
    }
}


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1.2.9 $
 */
class FileStratagy implements AbstractService.ServiceStratagy {
    private static int BUFF_SIZE = 4096;
    static int sequence = 0;
    private HttpServletResponse response;
    private File temp = null;

    /**
     * DOCUMENT ME!
     *
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public OutputStream getDestination(HttpServletResponse response)
        throws IOException {
        sequence++;
        temp = File.createTempFile("wfs" + sequence, "tmp");

        FileOutputStream safe = new FileOutputStream(temp);

        return safe;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void flush() throws IOException {
        if ((temp == null) || (response == null) || !temp.exists()) {
            return;
        }

        // service succeeded in producing a response!
        // copy the result to out
        InputStream copy = null;

        try {
            // copy result to the real output stream
            copy = new BufferedInputStream(new FileInputStream(temp));

            OutputStream out = response.getOutputStream();
            out = new BufferedOutputStream(out, 1024 * 1024);

            byte[] buffer = new byte[BUFF_SIZE];
            int b;

            while ((b = copy.read(buffer, 0, BUFF_SIZE)) > 0) {
                out.write(buffer, 0, b);
            }
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            try {
                copy.close();
            } catch (Exception ex) {
            }
        }
    }
}
