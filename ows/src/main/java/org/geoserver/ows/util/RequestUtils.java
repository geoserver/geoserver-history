/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows.util;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;


/**
 * Utility class performing operations related to http requests.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 * TODO: this class needs to be merged with org.vfny.geoserver.Requests.
 */
public class RequestUtils {
    static String forcedBaseUrl;
    
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
        if(forcedBaseUrl == null)
            return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()
                + req.getContextPath() + "/";
        else
            return forcedBaseUrl;
    }
    
    /**
     * Given a base URL and a proxy url (which may or may-not be null)
     * this method grafts the two together so that the proper 'proxified' or 'non-proxified' url is returned
     * 
     */
    public static String proxifiedBaseURL(String baseUrl, String proxyBase) {
        if (proxyBase == null || proxyBase.trim().length() == 0)
            return baseUrl;
        
        try {
            URI baseUri = new URI(baseUrl);
            if (proxyBase.endsWith("/")) proxyBase = proxyBase.substring(0, proxyBase.length() -1);
            
            String proxifiedBaseUrl = proxyBase + baseUri.getPath();
            if (!proxifiedBaseUrl.endsWith("/")) proxifiedBaseUrl += "/";
            
            return proxifiedBaseUrl;
        } catch (URISyntaxException urise) {
            //hmm...guess the proxy base must be invalid
            throw new RuntimeException("Invalid Proxy Base URL property is set in your GeoServer installation.",urise);
        }
    }
    
    /**
     * Forces the base url to a certain string. Useful to redirect the xml schema
     * locations to the local file system during testing (since no web server is running during unit tests)
     * @param url
     */
    public static void setForcedBaseUrl(String url) {
        forcedBaseUrl = url;
    }
}
