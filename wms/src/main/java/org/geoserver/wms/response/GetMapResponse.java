/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

import org.geoserver.ows.Request;
import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.WMS;
import org.geoserver.wms.request.GetMapRequest;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;

import com.vividsolutions.jts.util.Assert;

/**
 * A GetMapResponse object is responsible of generating a map based on a GetMap request. The way the
 * map is generated is independent of this class, wich will use a delegate object based on the
 * output format requested
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @author Simone Giannecchini - GeoSolutions SAS
 * @version $Id$
 */
public class GetMapResponse extends Response {

    static final Logger LOGGER = Logging.getLogger(GetMapResponse.class);

    /**
     * The map producer that will be used for the production of a map in the requested format.
     */
    private GetMapProducer delegate;

    /**
     * The map context
     */
    private WMSMapContext mapContext;

    /**
     * custom response headers
     */
    private HashMap<String, String> responseHeaders;

    String headerContentDisposition;

    private Collection<GetMapProducer> availableProducers;

    private final WMS wms;

    /**
     * Creates a new GetMapResponse object.
     * 
     * @param availableProducers
     *            the list of available map producers where to get one to handle the request format
     *            at {@link #execute(Request)}
     */
    public GetMapResponse(final WMS wms) {
        super(GetMapRequest.class);
        this.wms = wms;
    }

    @Override
    public String getMimeType(final Object value, final Operation operation)
            throws ServiceException {

        Assert.isTrue(value instanceof org.geoserver.wms.response.Map);

        return ((org.geoserver.wms.response.Map) value).getMimeType();
    }

    @Override
    public void write(Object value, OutputStream output, Operation operation) throws IOException,
            ServiceException {
        Assert.isTrue(value instanceof org.geoserver.wms.response.Map);

        final org.geoserver.wms.response.Map map = (org.geoserver.wms.response.Map) value;
        final String mimeType = map.getMimeType();
        GetMapProducer outputFormat = wms.getMapOutputFormat(mimeType);
        outputFormat.write(map, output);
    }

    @Override
    public String[][] getHeaders(final Object value, final Operation operation)
            throws ServiceException {
        Assert.isTrue(value instanceof org.geoserver.wms.response.Map);

        final org.geoserver.wms.response.Map map = (org.geoserver.wms.response.Map) value;

        String[][] extraHeaders = map.getResponseHeaders();

        return extraHeaders;
    }

}
