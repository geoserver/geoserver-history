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
import org.geoserver.wms.response.Map;
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
        byte[] mapContents = map.getMapContents();
        output.write(mapContents);
        output.flush();
    }

}
