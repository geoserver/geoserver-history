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
 * @version $Id: AbstractService.java,v 1.1.2.7 2003/11/14 21:50:56 jive Exp $
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
    protected static final ServerConfig config = ServerConfig.getInstance();

    /** Specifies mime type */
    protected static final String MIME_TYPE = config.getGlobalConfig()
                                                    .getMimeType();
    private static Map context;

    public static final ServiceStratagy SPEED = new SpeedStratagy();
    public static final ServiceStratagy FILE = new FileStratagy();
    public static final ServiceStratagy BUFFER = new BufferStratagy();

    /**
     * Controls the Safty Mode used when using execute/writeTo.
     */
    private static ServiceStratagy saftyMode = BUFFER;

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
                             HttpServletResponse response,
                             Request serviceRequest) {
        saftyMode.doService( this, request, response, serviceRequest );
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
        }catch(IOException ioe){
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
     * <p>
     * While this interface resembles the Enum idiom in that only
     * three instances are available SPEED, BUFFER and FILE, we are
     * using this class to plug-in the implementation for our
     * doService request in the manner of the Stratagy pattern.
     * </p>
     * @author Jody Garnett, Refractions Research
     */
    static public interface ServiceStratagy {
        public void doService(AbstractService service,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              Request serviceRequest);
    }
}
/**
 * Fast and Dangeroud service stratagy.
 * <p>
 * Will fail when a ServiceException is encountered on writeTo, and will
 * not tell the user about it! This is the worst case scenario, you are trading
 * speed for danger by using this ServiceStratagy.
 * </p>
 * @throws IllegalStateException When encountering an error during writeTo
 * @author jgarnett
 */
class SpeedStratagy implements AbstractService.ServiceStratagy {
    public void doService(AbstractService service,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Request serviceRequest) {

        Response serviceResponse = service.getResponseHandler();
        OutputStream out = null;
        try {
            serviceResponse.execute(serviceRequest);
            out = response.getOutputStream();
            out = new BufferedOutputStream( out );

//            if (service.requestSupportsGzip(request))
//            {
//                response.setHeader("content-encoding", "gzip");
//                out = new GZIPOutputStream(out, 2048);
//            }
        } catch ( ServiceException serviceException){
            // we have not written anything, use sendError
            serviceResponse.abort();
            service.sendError(response, serviceException);
            return;

        } catch (IOException ioException){
            // we have not written anything, use sendError
            serviceResponse.abort();            
            service.sendError(response, ioException);
            return;
        }
        try {
            serviceResponse.writeTo( out );
            out.flush();
        }
        catch( ServiceException failed){
            // we have written something out
            // we cannot recover
            // this is the worst case scenario we warned you about
            // when using the SpeedStratagy
            serviceResponse.abort();            
            IllegalStateException stateException = new IllegalStateException("Speed Optimization Failed, cannot report error to user");
            stateException.initCause( failed );
            throw stateException;
        }
        catch( IOException ioException){
            // we could not communicate with the user
            serviceResponse.abort();            
            IllegalStateException stateException = new IllegalStateException("Communication to user failed");
            stateException.initCause( ioException );
            throw stateException;
        }
    }
};
/**
 * A safe Service stratagy that buffers output until writeTo completes.
 * <p>
 * This stratagy wastes memory, for saftey. It represents a middle ground
 * between SpeedStratagy and FileStratagy
 * </p>
 * @author jgarnett
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
class BufferStratagy implements AbstractService.ServiceStratagy {
    public void doService(AbstractService service,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Request serviceRequest) {

        ByteArrayOutputStream buffer = null;
        Response serviceResponse = service.getResponseHandler();        
        try {
            // execute request
            serviceResponse.execute(serviceRequest);

            // create buffer
            buffer = new ByteArrayOutputStream();

            // gather response
            serviceResponse.writeTo(buffer);
          }catch(IOException ioe){
            serviceResponse.abort();              
            //user just closed the socket stream, do nothing
            return;
          }catch( ServiceException serviceException ){
            serviceResponse.abort();              
            service.sendError( response, serviceException );
            return;
        }
        // service succeeded in producing a response!
        try {
            // (copy the result to out
            OutputStream out = response.getOutputStream();
            out = new BufferedOutputStream(out, 2 * 1024 * 1024);
            buffer.writeTo(out);
            out.flush();
        } catch (IOException ioException) {
            // something went wrong reporting to the user
            serviceResponse.abort();
            IllegalStateException stateException = new IllegalStateException("Communication to user failed");
            stateException.initCause( ioException );
            throw stateException;
        }
    }
};

class FileStratagy implements AbstractService.ServiceStratagy {
    static int sequence = 0;
    public void doService(AbstractService service,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Request serviceRequest) {
        File temp = null;
        Response serviceResponse = service.getResponseHandler();        
        try {
            
            // execute request
            serviceResponse.execute(serviceRequest);

            sequence++;
            temp = File.createTempFile( "wfs"+sequence,"tmp" );

            FileOutputStream safe = new FileOutputStream( temp );

            // accept response to temporary file
            serviceResponse.writeTo( safe );
            safe.close();
        } catch (IOException ioException) {
            serviceResponse.abort();            
            service.sendError(response, ioException);
            return;
        } catch( ServiceException serviceException ){
            serviceResponse.abort();            
            service.sendError( response, serviceException );
            return;
        }
        // service succeeded in producing a response!
        
        // copy the result to out
        try {
            // copy result to the real output stream
            InputStream copy = new BufferedInputStream( new FileInputStream( temp ) );
            OutputStream out = response.getOutputStream();

            // need to buffer this for performance
            // not sure if it has been done already
            int b;
            b = copy.read();
            while( b != -1 ){
                out.write( b );
                b = copy.read();
            }
            copy.close();
        } catch (IOException ioException) {
            serviceResponse.abort();            
            // something went wrong reporting to the user
            IllegalStateException stateException = new IllegalStateException("Communication to user failed");
            stateException.initCause( ioException );
            throw stateException;
        }
    }
}

