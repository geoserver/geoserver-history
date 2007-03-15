/* Copyright (c) 2001 - 2007 TOPP - http://topp.openplans.org.
 * All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible in the
 * license.txt file of the documents directory off the root directory.
 */
package org.vfny.geoserver.services;

import org.vfny.geoserver.ExceptionHandler;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Provides a web interface for setting the default value for preferences.<p>To
 * be invoked the <b>service</b> parameter must be <b>wpss</b> and the
 * <b>request</b> paramter must be <b>setPreference</b></p>
 *  <p>A <b>key</b> parameter and a <b>value</b> parameter must be provided
 * or a <b>Service Exception</b> response will be thrown</p>
 *  <p>Example Request:
 * <pre>http://localhost:8080/geoserver/wfs?service=wpss&request=setDefault&key=pref&value=newValue</pre></p>
 *  <p>The default value of preference <b>pref</b> is being set to
 * <b>newValue</b>.</p>
 *
 * @author Jesse
 *
 * @see SetPreference
 * @see GetPreference
 * @see PreferenceController
 */
public class SetDefault extends AbstractPreferenceService {
    private static final long serialVersionUID = 1L;

    public SetDefault() {
        super("wpss", "setDefault", null);
    }

    protected ExceptionHandler getExceptionHandler() {
        return null;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String key = request.getParameter("key");
        String value = request.getParameter("value");

        if (key == null) {
            throw new ServletException("A key attribute MUST be provided");
        }

        if (value == null) {
            throw new ServletException("A value attribute MUST be provided");
        }

        getController().setDefault(key, value);
    }
}
