/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.wms.GetFeatureInfoRequest;
import org.vfny.geoserver.responses.Response;
import org.vfny.geoserver.responses.wms.featureInfo.GetFeatureInfoDelegate;
import org.vfny.geoserver.responses.wms.featureInfo.GmlFeatureInfoResponse;
import org.vfny.geoserver.responses.wms.featureInfo.HTMLTableFeatureInfoResponse;
import org.vfny.geoserver.responses.wms.featureInfo.TextFeatureInfoResponse;


/**
 * A GetFeatureInfoResponse object is responsible for generating GetFeatureInfo
 * content in the format specified. The way the content is generated is
 * independent of this class, wich will use a delegate object based on the
 * output format requested
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id: GetFeatureInfoResponse.java,v 1.3 2004/07/21 18:43:30 jmacgill Exp $
 */
public class GetFeatureInfoResponse implements Response {
    /** package logger   */
    private static final Logger LOGGER = Logger.getLogger(GetMapResponse.class.getPackage()
                                                                              .getName());

    /** list of output format specialists */
    private static final List delegates = new LinkedList();

    /**
     * The list of all the supported output formats
     */
    private static final List supportedMimeTypes = new LinkedList();

    static {
        GetFeatureInfoDelegate producer;

        producer = new TextFeatureInfoResponse();
        supportedMimeTypes.addAll(producer.getSupportedFormats());
        delegates.add(producer);

        producer = new HTMLTableFeatureInfoResponse();
        supportedMimeTypes.addAll(producer.getSupportedFormats());
        delegates.add(producer);

        producer = new GmlFeatureInfoResponse();
        supportedMimeTypes.addAll(producer.getSupportedFormats());
        delegates.add(producer);
    }

    /**
     * A delegate specialized in producing the required output format.
     */
    private GetFeatureInfoDelegate delegate;
    
    /**
     * Creates a new GetMapResponse object.
     */
    public GetFeatureInfoResponse() {
    }

    /**
     * Obtains a <code>GetFeatureInfoDelegate</code> for the requested output format,
     * and tells it to execute the request.
     *
     * @param request DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     */
    public void execute(Request request) throws ServiceException {
        LOGGER.entering(getClass().getName(), "execute",
            new Object[] { request });

        GetFeatureInfoRequest getFeatureInfoReq = (GetFeatureInfoRequest) request;
        this.delegate = getDelegate(getFeatureInfoReq);
        delegate.execute(request);
    }

    /**
     * Asks the internal GetFeatureInfoDelegate for the MIME type of the result that it
     * will generate or is ready to, and returns it
     *
     * @param gs the global app context
     *
     * @return the MIME type of the map generated or ready to generate
     *
     * @throws IllegalStateException if a GetMapDelegate is not setted yet
     */
    public String getContentType(GeoServer gs) throws IllegalStateException {
        if (delegate == null) {
            throw new IllegalStateException("No request has been proceced");
        }

        return delegate.getContentType(gs);
    }

    /**
     * Returns the content encoding of the internal delegate
     *
     * @return <code>null</code> since no content encoding (such as GZIP) is
     *         done.
     *
     * @throws IllegalStateException if this method is called before processing
     *         a request (i.e., execute() has not been called)
     */
    public String getContentEncoding() {
        if (delegate == null) {
            throw new IllegalStateException("No request has been proceced");
        }

        return delegate.getContentEncoding();
    }

    /**
     * if a GetFeatureInfoDelegate is set, calls it's abort method. Elsewere do
     * nothing.
     *
     * @param gs DOCUMENT ME!
     */
    public void abort(Service gs) {
        if (delegate != null) {
            LOGGER.fine("asking delegate for aborting the process");
            delegate.abort(gs);
        }
    }

    /**
     * delegates the writing and encoding of the results of the request to the
     * <code>GetMapDelegate</code> wich is actually processing it, and has
     * been obtained when <code>execute(Request)</code> was called
     *
     * @param out the output to where the map must be written
     *
     * @throws ServiceException if the delegate throws a ServiceException
     *         inside its <code>writeTo(OuptutStream)</code>, mostly due to
     * @throws IOException if the delegate throws an IOException inside its
     *         <code>writeTo(OuptutStream)</code>, mostly due to
     * @throws IllegalStateException if this method is called before
     *         <code>execute(Request)</code> has succeed
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        if (delegate == null) {
            throw new IllegalStateException(
                "No GetMapDelegate is setted, make sure you have called execute and it has succeed");
        }

        LOGGER.finer("asking delegate for write to " + out);
        delegate.writeTo(out);
    }

    /**
     * Creates a GetMapDelegate specialized in generating the requested map
     * format
     *
     * @param request a request parameter object wich holds the processed
     *        request objects, such as layers, bbox, outpu format, etc.
     *
     * @return A specialization of <code>GetMapDelegate</code> wich can produce
     *         the requested output map format
     *
     * @throws WmsException if no specialization is configured for the output
     *         format specified in <code>request</code> or if it can't be
     *         instantiated
     */
    private static GetFeatureInfoDelegate getDelegate(
        GetFeatureInfoRequest request) throws WmsException {
        String requestFormat = request.getInfoFormat();
        LOGGER.finer("request format is " + requestFormat);

        GetFeatureInfoDelegate curDelegate = null;
        Class delegateClass = null;

        for (Iterator it = delegates.iterator(); it.hasNext();) {
            curDelegate = (GetFeatureInfoDelegate) it.next();

            if (curDelegate.canProduce(requestFormat)) {
                delegateClass = curDelegate.getClass();
                LOGGER.finer("found GetFeatureInfoDelegate " + delegateClass);

                break;
            }
        }

        if (delegateClass == null) {
            throw new WmsException(requestFormat +
                " is not recognized as an output format for this server. " +
                "Please consult the Capabilities document",
                "GetMapResponse.getDelegate");
        }

        try {
            curDelegate = (GetFeatureInfoDelegate) delegateClass.newInstance();
        } catch (Exception ex) {
            throw new WmsException(ex,
                "Cannot obtain the map generator for the requested format",
                "GetMapResponse::getDelegate()");
        }

        return curDelegate;
    }

    /**
     * iterates over the registered Map producers and fills a list with all the
     * map formats' MIME types that the producers can handle
     *
     * @return DOCUMENT ME!
     */
    public static List getFormats() {
        return supportedMimeTypes;
    }
}
