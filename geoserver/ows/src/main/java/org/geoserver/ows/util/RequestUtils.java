/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows.util;

import javax.servlet.http.HttpServletRequest;


/**
 * Utility class performing operations related to http requests.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * 
 * TODO: this class needs to be merged with org.vfny.geoserver.Requests.
 */
public class RequestUtils {
    /**
     * Returns the url which is hte base of schemas stored / served by
     * geoserver.
     * <p>
     *         This method returns:
     *         <pre>
     *        <code>
     *    baseURL( req ) + "schemas/"
     *  </code>
     *  </pre>
     * </p>
     *
     * @return A String of the form "<scheme>://<server>:<port>/<context>/schemas/"
     */
    public static String schemaBaseURL(HttpServletRequest req) {
        return baseURL(req) + "schemas/";
    }

    /**
     * Pulls out the base url ( from the client point of view ), from the
     * given request object.
     *
     * @return A String of the form "<scheme>://<server>:<port>/<context>/"
     *
     */
    public static String baseURL(HttpServletRequest req) {
        String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()
            + req.getContextPath() + "/";

        return url;
    }
}
