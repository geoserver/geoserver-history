/* Copyright (c) 2001 - 2007 TOPP - http://topp.openplans.org.
 * All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible in the
 * license.txt file of the documents directory off the root directory.
 */
package org.vfny.geoserver.services;

import org.vfny.geoserver.ExceptionHandler;
import org.vfny.geoserver.control.IPreferenceStore;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Provides a web interface for getting preferences.<p>To be invoked the
 * <b>service</b> parameter must be <em><b>wpss</b></em> and the
 * <b>request</b> paramter must be <em><b>getPreference</b></em></p>
 *  <p>A <b>key</b> parameter and a <b>value</b> parameter must be provided
 * or a <b>Service Exception</b> response will be thrown</p>
 *  <p>Example Request:
 * <pre>http://localhost:8080/geoserver/wfs?service=wpss&request=setPreference&key=pref</pre></p>
 *  <p>The preference <b>pref</b> will be returned.  Only strings are
 * returned or stored.</p>
 *  <p>If the preference has not set, the default value will be returned</p>
 *
 * @author Jesse
 *
 * @see SetPreference
 * @see SetDefault
 * @see IPreferenceStore
 */
public class GetPreference extends AbstractPreferenceService {
    private static final long serialVersionUID = 1L;

    public GetPreference() {
        super("wpss", "getPreference", null);
    }

    protected ExceptionHandler getExceptionHandler() {
        return null;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String key = request.getParameter("key");

        if (key == null) {
            throw new ServletException("A key attribute MUST be provided");
        }

        OutputStream stream = response.getOutputStream();
        stream.write(getController().getString(key).getBytes());
    }
}
