/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.sld.responses;

import org.geoserver.platform.ServiceException;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Service;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;


public class PutStylesResponse implements Response {
    public void execute(Request request) throws ServiceException {
        // TODO Auto-generated method stub
    }

    public String getContentType(GeoServer gs) throws IllegalStateException {
        // TODO Auto-generated method stub
        return null;
    }

    public String getContentEncoding() {
        // TODO Auto-generated method stub
        return null;
    }

    public void writeTo(OutputStream out) throws ServiceException, IOException {
        // TODO Auto-generated method stub
    }

    public void abort(Service gs) {
        // TODO Auto-generated method stub
    }

    public HashMap getResponseHeaders() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getContentDisposition() {
        // TODO Auto-generated method stub
        return null;
    }
}
