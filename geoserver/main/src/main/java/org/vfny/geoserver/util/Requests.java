/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.UserContainer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * Utility methods helpful when processing GeoServer Requests.<p>Provides
 * helper functions and classes useful when implementing your own Response
 * classes. Of significant importantance are the Request processing functions
 * that allow access to the WebContainer, GeoServer and the User's Session.</p>
 *  <p>If you are working with the STRUTS API the Action method is the
 * direct paralle of the Response classes. You may whish to look at how
 * ConfigAction is implemented, it is a super class which delegates to these
 * Request processing methods.</p>
 *
 * @author Jody Garnett
 */
public final class Requests {
    //JD: remove these methods that are commented out
    /**
     * Aquire GeoServer from Web Container.<p>In GeoServer is create by
     * a STRUTS plug-in and is available through the Web container.</p>
     *  <p>Test cases may seed the request object with a Mock
     * WebContainer and a Mock GeoServer.</p>
     */
    public static final String PROXY_PARAM = "PROXY_BASE_URL";

    /**
     * Aquire WFS from Web Container.<p>In WFS is create by a STRUTS
     * plug-in and is available through the Web container.</p>
     *  <p>Test cases may seed the request object with a Mock
     * WebContainer and a Mock GeoServer.</p>
     *
     * @param httpServletRequest HttpServletRequest used to aquire servlet
     *        context
     * @param geoserver DOCUMENT ME!
     *
     * @return WFS instance for the current Web Application
     */

    //JD: delete this
    //    public static WFS getWFS(HttpServletRequest request) {
    //    	ServletRequest req = request;
    //    	HttpSession session = request.getSession();
    //    	ServletContext context = session.getServletContext();
    //
    //    	return (WFS) context.getAttribute(WFS.WEB_CONTAINER_KEY);
    //    }
    /**
     * Aquire WMS from Web Container.<p>In WMS is create by a STRUTS
     * plug-in and is available through the Web container.</p>
     *  <p>Test cases may seed the request object with a Mock
     * WebContainer and a Mock GeoServer.</p>
     *
     * @param httpServletRequest HttpServletRequest used to aquire servlet
     *        context
     * @param geoserver DOCUMENT ME!
     *
     * @return WMS instance for the current Web Application
     */

    //	JD: delete this
    //    public static WMS getWMS(HttpServletRequest request) {
    //    	ServletRequest req = request;
    //    	HttpSession session = request.getSession();
    //    	ServletContext context = session.getServletContext();
    //
    //    	return (WMS) context.getAttribute(WMS.WEB_CONTAINER_KEY);
    //    }
    /**
     * Get base url used - it is not any more assumed to be
     * http://server:port/geoserver Removed the hardcoded "http://" and
     * replaced it with httpServletRequest.getScheme() because the https case
     * was not being handled.
     *
     * @param httpServletRequest
     * @param geoserver DOCUMENT ME!
     *
     * @return http://server:port/path-defined-context
     */
    public static String getBaseUrl(HttpServletRequest httpServletRequest, GeoServer geoserver) {
        // try with the web interface configuration, if it fails, look into
        // web.xml just to keep compatibility (should be removed next version)
        // and finally, if nothing is found, give up and return the default base URL
        String url = ((geoserver != null) ? geoserver.getProxyBaseUrl() : null);

        if ((geoserver != null) && (url != null)) {
            url = concatUrl(url, httpServletRequest.getContextPath());
        }

        if ((url == null) || (url.trim().length() == 0)) {
            if (httpServletRequest != null) {
                url = httpServletRequest.getSession().getServletContext()
                                        .getInitParameter(PROXY_PARAM);
            }

            if ((url == null) || (url.trim().length() == 0)) {
                url = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName()
                    + ":" + httpServletRequest.getServerPort()
                    + httpServletRequest.getContextPath() + "/";
            } else {
                url = concatUrl(url, httpServletRequest.getContextPath());
            }
        }

        // take care of incompletely setup path
        if (!url.endsWith("/")) {
            url = url + "/";
        }

        return url;
    }

    public static String getBaseJspUrl(HttpServletRequest httpServletRequest, GeoServer geoserver) {
        // try with the web interface configuration, if it fails, look into
        // web.xml just to keep compatibility (should be removed next version)
        // and finally, if nothing is found, give up and return the default base URL
        String url = geoserver.getProxyBaseUrl();

        if ((geoserver != null) && (url != null)) {
            url = concatUrl(url, httpServletRequest.getRequestURI());
        }

        if ((url == null) || (url.trim().length() == 0)) {
            if (httpServletRequest != null) {
                url = httpServletRequest.getSession().getServletContext()
                                        .getInitParameter(PROXY_PARAM);
            }

            if ((url == null) || (url.trim().length() == 0)) {
                url = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName()
                    + ":" + httpServletRequest.getServerPort() + httpServletRequest.getRequestURI()
                    + "/";
            } else {
                url = concatUrl(url, httpServletRequest.getRequestURI());
            }
        }

        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        return url;
    }

    /**
     * Returns the full url to the tile cache used by GeoServer ( if any ).
     * <p>
     * If the tile cache set in the configuration ({@link GeoServer#getTileCache()})
     * is set to an asbsolute url, it is simply returned. Otherwise the value
     * is appended to the scheme and host of the supplied <tt>request</tt>.
     * </p>
     * @param request The request.
     * @param geoServer The geoserver configuration.
     *
     * @return The url to the tile cache, or <code>null</code> if no tile
     * cache set.
     */
    public static String getTileCacheBaseUrl(HttpServletRequest request, GeoServer geoServer) {
        //first check if tile cache set
        String tileCacheBaseUrl = geoServer.getTileCache();

        if (tileCacheBaseUrl != null) {
            //two possibilities, local path, or full remote path
            try {
                new URL(tileCacheBaseUrl);

                //full url, return it
                return tileCacheBaseUrl;
            } catch (MalformedURLException e1) {
                //try relative to the same host as request
                try {
                    String url = concatUrl(request.getScheme() + "://" + request.getServerName(),
                            tileCacheBaseUrl);
                    new URL(url);

                    //cool return it
                    return url;
                } catch (MalformedURLException e2) {
                    //out of guesses
                }
            }
        }

        return null;
    }
    
    public static String concatUrl(String url, String contextPath) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        if (contextPath.startsWith("/")) {
            contextPath = contextPath.substring(1);
        }

        return url + "/" + contextPath;
    }

    /**
     * Get capabilities base url used
     *
     * @param httpServletRequest
     * @param geoserver DOCUMENT ME!
     *
     * @return http://server:port/path-defined-context/data/capabilities
     */
    public static String getSchemaBaseUrl(HttpServletRequest httpServletRequest, GeoServer geoserver) {
        return getBaseUrl(httpServletRequest, geoserver) + "schemas/";
    }

    /**
     * Aquire type safe session information in a UserContainer.
     *
     * @param request Http Request used to aquire session reference
     *
     * @return UserContainer containing typesafe session information.
     */
    public static UserContainer getUserContainer(HttpServletRequest request) {
        HttpSession session = request.getSession();

        synchronized (session) {
            UserContainer user = (UserContainer) session.getAttribute(UserContainer.SESSION_KEY);

            return user;
        }
    }

    /**
     * Tests is user is loggin in.<p>True if UserContainer exists has
     * been created.</p>
     *
     * @param request HttpServletRequest providing current Session
     *
     * @return
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession();

        synchronized (session) {
            UserContainer user = (UserContainer) session.getAttribute(UserContainer.SESSION_KEY);

            return user != null;
        }
    }

    /**
     * Ensures a user is logged out.<p>Removes the UserContainer, and
     * thus GeoServers knowledge of the current user attached to this Session.</p>
     *
     * @param request HttpServletRequest providing current Session
     */
    public static void logOut(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(UserContainer.SESSION_KEY);
    }

    /**
     * Parses an 'option-holding' parameters in the following form
     * FORMAT_OPTIONS=multiKey:val1,val2,val3;singleKey:val
     * 
     * Useful for parsing out the FORMAT_OPTIONS and LEGEND_OPTIONS parameters
     */
    public static Map parseOptionParameter(String rawOptionString) throws IllegalArgumentException {
        HashMap map = new HashMap();
        if (rawOptionString == null) {
            return map;
        }
        
        StringTokenizer semiColonSplitter = new StringTokenizer(rawOptionString, ";");
        while (semiColonSplitter.hasMoreElements()) {
            String curKVP = semiColonSplitter.nextToken();
            
            final int cloc = curKVP.indexOf(":");
            if (cloc <= 0) {
                throw new IllegalArgumentException("Key-value-pair: '" + curKVP + "' isn't properly formed.  It must be of the form 'Key:Value1,Value2...'");
            }
            String key = curKVP.substring(0, cloc);
            String values = curKVP.substring(cloc + 1, curKVP.length());
            if (values.indexOf(",") != -1) {
                List valueList = new ArrayList();
                StringTokenizer commaSplitter = new StringTokenizer(values, ",");
                while (commaSplitter.hasMoreElements())
                    valueList.add(commaSplitter.nextToken());
                
                map.put(key, valueList);
            } else {
                map.put(key, values);
            }
        }
        
        return map;
    }
}
