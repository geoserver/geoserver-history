/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.requests.Request;
import java.io.*;


/**
 * Process and write out Requests?
 * <p>
 * JG - This class seems to do two things, process requests and hold the
 * resulting Response ready to be written out.
 * </p>
 * @author Gabriel Roldán
 * @version 0.1
 */
public interface Response {
    /**
     * Performs the request.
     * <p>
     * We should clarify when a ServiceException is thrown? I would assume
     * that a "failed" request should still result in a Response that we
     * could write out. 
     * </p>
     * @param request
     * @throws ServiceException
     */
    public void execute(Request request) throws ServiceException;

    /**
     * MIME type of this Response - example <code>"text/xml"</code>.
     * <p>
     * There is some MIME stuff in JDK for reference:
     * <ul>
     * <li>java.awt.datatransfer package</li>
     * <li>javax.mail.internet</li>
     * <li>
     * and a few other places as well.
     * @return MIME Type
     */
    public String getContentType();

    /**
     * Write this respone to the provided output stream.
     * 
     * @param out
     * @throws ServiceException
     */
    public void writeTo(OutputStream out) throws ServiceException;
}
