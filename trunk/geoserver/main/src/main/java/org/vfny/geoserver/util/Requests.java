/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.util;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.anonymous.AnonymousAuthenticationToken;
import org.acegisecurity.userdetails.UserDetails;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.UserContainer;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * Utility methods helpful when processing GeoServer Requests.
 *
 * <p>
 * Provides helper functions and classes useful when implementing your own
 * Response classes. Of significant importantance are the Request processing
 * functions that allow access to the WebContainer, GeoServer and the User's
 * Session.
 * </p>
 *
 * <p>
 * If you are working with the STRUTS API the Action method is the direct
 * paralle of the Response classes. You may whish to look at how ConfigAction
 * is implemented, it is a super class which delegates to these Request
 * processing methods.
 * </p>
 *
 * @author Jody Garnett
 */
public final class Requests {
    static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver");
    
    /*
     * This is the parameter used to get the proxy from the
     * web.xml file.  This is a bit hacky, it should be moved to
     * GeoServer.java and be a normal config parameter, but the
     * overhead of making a new config param is just too high,
     * so we're allowing this to just be read from the web.xml
     ( See GEOS-598 for more information
     */
    public static final String PROXY_PARAM = "PROXY_BASE_URL";

    /**
     * Get base url used - it is not any more assumed to be
     * http://server:port/geoserver
     *
     * GRR: it is not any more assumed the context path is /geoserver. If a proxyBaseUrl
     * was provided, then that's the full context path and thus the proxyBaseUrl is returned as is,
     * instead of appending /geosverver to it.
     *
     * Removed the hardcoded "http://" and replaced it with
     * httpServletRequest.getScheme() because the https case was not being
     * handled.
     *
     * @param httpServletRequest
     * @return http://server:port/path-defined-context
     * @deprecated use {@link RequestUtils#proxifiedBaseURL(String, String)} instead
     */
    public static String getBaseUrl(HttpServletRequest httpServletRequest, GeoServer geoserver) {
        // try with the web interface configuration, if it fails, look into
        // web.xml just to keep compatibility (should be removed next version)
        // and finally, if nothing is found, give up and return the default base URL
        String url = ((geoserver != null) ? geoserver.getProxyBaseUrl() : null);

        //if ((geoserver != null) && (url != null)) {
        //    url = appendContextPath(url, httpServletRequest.getContextPath());
        //}

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
                url = appendContextPath(url, httpServletRequest.getContextPath());
            }
        }

        // take care of incompletely setup path
        if (!url.endsWith("/")) {
            url = url + "/";
        }

        return url;
    }

    /**
     * @deprecated of no use
     */
    public static String getBaseJspUrl(HttpServletRequest httpServletRequest, GeoServer geoserver) {
        // try with the web interface configuration, if it fails, look into
        // web.xml just to keep compatibility (should be removed next version)
        // and finally, if nothing is found, give up and return the default base URL
        String url = geoserver.getProxyBaseUrl();

        if ((geoserver != null) && (url != null)) {
            url = appendContextPath(url, httpServletRequest.getRequestURI());
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
                url = appendContextPath(url, httpServletRequest.getRequestURI());
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
                    String url = appendContextPath(request.getScheme() + "://" + request.getServerName(),
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

    /**
     * Appends a context path to a base url.
     * 
     * @param url The base url.
     * @param contextPath The context path to be appended.
     * 
     * @return A full url with the context path appended.
     */
    public static String appendContextPath(String url, String contextPath) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        if (contextPath.startsWith("/")) {
            contextPath = contextPath.substring(1);
        }

        return url + "/" + contextPath;
    }
    
    /**
     * Appends a query string to a url.
     * <p>
     * This method checks <code>url</code> to see if the appended query string requires a '?' or
     * '&' to be prepended.
     * </p>
     *
     * @param url The base url.
     * @param queryString The query string to be appended, should not contain the '?' character.
     *
     * @return A full url with the query string appended.
     */
    public static String appendQueryString(String url, String queryString) {
        if (url.endsWith("?") || url.endsWith("&")) {
            return url + queryString;
        }

        if (url.indexOf('?') != -1) {
            return url + "&" + queryString;
        }

        return url + "?" + queryString;
    }

    /**
     * Get capabilities base url used
     *
     * @param httpServletRequest
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

            // acegi variation, login is performed by the acegi subsystem, we do get
            // the information we need from it
            if (user == null) {
                user = new UserContainer();
                
                //JD: for some reason there is sometimes a string here. doing
                // an instanceof check ... although i am not sure why this occurs.
                final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if(authentication == null) {
                    LOGGER.warning("Warning, Acegi security subsystem deactivated, no user checks will be made");
                    user.setUsername("admin");
                } else {
                    Object o = authentication.getPrincipal();
                    if ( o instanceof UserDetails ) {
                        UserDetails ud = (UserDetails) o;
                        user.setUsername(ud.getUsername());        
                    }
                    else if ( o instanceof String ) {
                        user.setUsername((String)o);
                    }
                }
                request.getSession().setAttribute(UserContainer.SESSION_KEY, user);
            }

            return user;
        }
    }

    public static boolean loggedIn(HttpServletRequest request) {
        return !getUserContainer(request).getUsername().equals("anonymous");
    }

    /**
     * Tests is user is loggin in.
     *
     * <p>
     * True if UserContainer exists has been created.
     * </p>
     *
     * @param request HttpServletRequest providing current Session
     *
     * @return
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        // check the user is not the anonymous one
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (authentication != null)
        && !(authentication instanceof AnonymousAuthenticationToken);
    }

    /**
     * Ensures a user is logged out.
     *
     * <p>
     * Removes the UserContainer, and thus GeoServers knowledge of the current
     * user attached to this Session.
     * </p>
     *
     * @param request HttpServletRequest providing current Session
     */
    public static void logOut(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(UserContainer.SESSION_KEY);
    }

    /**
     * This method gets the correct input stream for a URL.
     * If the URL is a http/https connection, the Accept-Encoding: gzip, deflate is added.
     * It the paramter is added, the response is checked to see if the response
     * is encoded in gzip, deflate or plain bytes. The correct input stream wrapper is then
     * selected and returned.
     *
     * This method was added as part of GEOS-420
     *
     * @param url The url to the sld file
     * @return The InputStream used to validate and parse the SLD xml.
     * @throws IOException
     */
    public static InputStream getInputStream(URL url) throws IOException {
        //Open the connection
        URLConnection conn = url.openConnection();

        //If it is the http or https scheme, then ask for gzip if the server supports it.
        if (conn instanceof HttpURLConnection) {
            //Send the requested encoding to the remote server.
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        }

        //Conect to get the response headers
        conn.connect();

        //Return the correct inputstream
        //If the connection is a url, connection, check the response encoding.
        if (conn instanceof HttpURLConnection) {
            //Get the content encoding of the server response
            String encoding = conn.getContentEncoding();

            //If null, set it to a emtpy string
            if (encoding == null) {
                encoding = "";
            }

            if (encoding.equalsIgnoreCase("gzip")) {
                //For gzip input stream, use a GZIPInputStream
                return new GZIPInputStream(conn.getInputStream());
            } else if (encoding.equalsIgnoreCase("deflate")) {
                //If it is encoded as deflate, then select the inflater inputstream.
                return new InflaterInputStream(conn.getInputStream(), new Inflater(true));
            } else {
                //Else read the raw bytes
                return conn.getInputStream();
            }
        } else {
            //Else read the raw bytes.
            return conn.getInputStream();
        }
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
