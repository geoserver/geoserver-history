/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.wms.GetMapRequest;
import org.vfny.geoserver.responses.Response;
import org.vfny.geoserver.responses.wms.map.*;
import java.io.*;
import java.util.*;


/**
 * A GetMapResponse object is responsible for generating a map based on a
 * GetMap request. The way the map is generated is independent of this class,
 * wich will use a delegate object based on the output format requested
 *
 * @author Gabriel Roldán
 * @version $Id: GetMapResponse.java,v 1.2 2003/12/16 18:46:10 cholmesny Exp $
 */
public class GetMapResponse implements Response {
    /** DOCUMENT ME! */
    private static final List delegates = new LinkedList();

    private static final List supportedMimeTypes = new LinkedList();

    static {
        GetMapDelegate producer;

        producer = new SVGMapResponse();
        supportedMimeTypes.addAll(producer.getSupportedFormats());
        delegates.add(producer);

        producer = new JAIMapResponse();
        supportedMimeTypes.addAll(producer.getSupportedFormats());
        delegates.add(producer);
    }

    private GetMapDelegate delegate;

    /**
     * Creates a new GetMapResponse object.
     */
    public GetMapResponse() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     */
    public void execute(Request request) throws ServiceException {
        GetMapRequest getMapReq = (GetMapRequest) request;
        this.delegate = getDelegate(getMapReq);
        delegate.execute(request);
    }

    /**
     * asks the internal GetMapDelegate for the MIME type of the map that it
     * will generate or is ready to, and returns it
     *
     * @return the MIME type of the map generated or ready to generate
     *
     * @throws IllegalStateException if a GetMapDelegate is not setted yet
     */
    public String getContentType() throws IllegalStateException {
        if (delegate == null) {
            throw new IllegalStateException("No request has been proceced");
        }

        return delegate.getContentType();
    }

    /**
     * if a GetMapDelegate is set, calls it's abort method. Elsewere do nothing.
     */
    public void abort() {
        if (delegate != null) {
            delegate.abort();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        if(delegate == null)
        {
          throw new IllegalStateException(
          "No GetMapDelegate is setted, make sure you have called execute and it has succeed");
        }
        delegate.writeTo(out);
    }

    /**
     * Creates a GetMapDelegate specialized in generating the requested map
     * format
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws WmsException if no specialization is configured for the output
     *         format specified in <code>request</code> or if it can't be
     *         instantiated
     */
    private static GetMapDelegate getDelegate(GetMapRequest request)
        throws WmsException {
        String requestFormat = request.getFormat();
        GetMapDelegate delegate = null;
        Class delegateClass = null;

        for(Iterator it = delegates.iterator(); it.hasNext();)
        {
          delegate = (GetMapDelegate)it.next();
          if(delegate.canProduce(requestFormat))
          {
            delegateClass = delegate.getClass();
            break;
          }
        }

        if (delegateClass == null) {
            throw new WmsException(requestFormat
                + " is not recognized as an output format for this server. "
                + "Please consult the Capabilities document",
                "GetMapResponse.getDelegate");
        }

        try {
            delegate = (GetMapDelegate) delegateClass.newInstance();
        } catch (Exception ex) {
            throw new WmsException(ex,
                "Cannot obtain the map generator for the requested format",
                "GetMapResponse::getDelegate()");
        }

        return delegate;
    }

    /**
     * iterates over the registered Map producers and fills a list with
     * all the map formats' MIME types that the producers can handle
     */
    private static List getMapFormats()
    {
      return supportedMimeTypes;
    }
}
