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
 * @version $Id: GetMapResponse.java,v 1.1.2.1 2003/11/14 20:39:15 groldan Exp $
 */
public class GetMapResponse implements Response {
    /** DOCUMENT ME!  */
    private static final Map delegates = new HashMap();

    static {
        delegates.put("image/svg+xml", SVGMapResponse.class);
        delegates.put("image/svg xml", SVGMapResponse.class);

        /**
         * @task TODO: add JAI format handlers
         */
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
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType() {
        return delegate.getContentType();
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
        delegate.writeTo(out);
    }

    /**
     * Creates a GetMapDelegate specialized in generating the requested
     * map format
     *
     * @param request DOCUMENT ME!
     *
     * @throws WmsException if no specialization is configured for the output
     * format specified in <code>request</code> or if it can't be instantiated
     */
    private static GetMapDelegate getDelegate(GetMapRequest request)
        throws WmsException {
        String requestFormat = request.getFormat();
        Class delegateClass = (Class) delegates.get(requestFormat);

        if (delegateClass == null) {
            throw new WmsException(requestFormat
                + " is not recognized as an output format for this server. "
                + "Please consult the Capabilities document",
                "GetMapResponse.getDelegate");
        }

        GetMapDelegate delegate = null;

        try {
            delegate = (GetMapDelegate) delegateClass.newInstance();
        } catch (Exception ex) {
            throw new WmsException(ex,
                "Cannot obtain the map generator for the requested format",
                "GetMapResponse::getDelegate()");
        }

        return delegate;
    }
}
