/* Copyright (c) 2010 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.map;

import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.Map;
import org.springframework.util.Assert;

/**
 * A {@link Response} to handle a {@link RawMap}
 * 
 * @author Gabriel Roldan
 */
public class RawMapResponse extends Response {

    public RawMapResponse() {
        super(RawMap.class);
    }

    @Override
    public String getMimeType(Object value, Operation operation) throws ServiceException {
        Assert.isInstanceOf(RawMap.class, value);
        return ((Map) value).getMimeType();
    }

    @Override
    public void write(Object value, OutputStream output, Operation operation) throws IOException,
            ServiceException {
        Assert.isInstanceOf(RawMap.class, value);
        RawMap map = (RawMap) value;
        try {
            map.writeTo(output);
            output.flush();
        } finally {
            map.dispose();
        }
    }

    /**
     * Returns a 2xn array of Strings, each of which is an HTTP header pair to be set on the HTTP
     * Response. Can return null if there are no headers to be set on the response.
     * 
     * @param value
     *            must be a {@link Map}
     * @param operation
     *            The operation being performed.
     * 
     * @return {@link Map#getResponseHeaders()}: 2xn string array containing string-pairs of HTTP
     *         headers/values
     * @see Response#getHeaders(Object, Operation)
     * @see Map#getResponseHeaders()
     */
    @Override
    public String[][] getHeaders(Object value, Operation operation) throws ServiceException {
        Assert.isInstanceOf(Map.class, value);
        Map map = (Map) value;
        String[][] responseHeaders = map.getResponseHeaders();
        return responseHeaders;
    }

}
