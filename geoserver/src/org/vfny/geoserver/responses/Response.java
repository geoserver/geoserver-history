/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.requests.Request;
import java.io.*;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
public interface Response {
    public void execute(Request request) throws ServiceException;

    public String getContentType();

    public void writeTo(OutputStream out) throws ServiceException;
}
