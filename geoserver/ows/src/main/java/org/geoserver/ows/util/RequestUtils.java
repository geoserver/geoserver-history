/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.geotools.util.Version;


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
        StringBuffer sb = new StringBuffer(req.getScheme());
        sb.append("://").append(req.getServerName()).append(":").append(req.getServerPort())
                .append(req.getContextPath()).append("/");
        return sb.toString();
    }

    /**
     * Given the actual <code>baseUrl</code> (may or may not include context) and a proxy base url,
     * returns the proxy base if not null or the actual one, ensuring the returned value ends with "/".
     * <p>
     * Be careful this does not account for a full reverse-proxy like url replacement, and is actually
     * meant only for OWS output that need some sort of schema information to be returned in the response
     * content.
     * </p>
     * 
     * @return proxyBase if given, baseUrl otherwise, either way ensuring it ends up with "/"
     */
    public static String proxifiedBaseURL(String baseUrl, String proxyBase) {
        if (proxyBase == null || proxyBase.trim().length() == 0) {
            if (!baseUrl.endsWith("/"))
                baseUrl += "/";
            return baseUrl;
        }
        return proxyBase.endsWith("/")? proxyBase : proxyBase + "/";
//        try {
//            URI baseUri = new URI(baseUrl);
//            if (proxyBase.endsWith("/"))
//                proxyBase = proxyBase.substring(0, proxyBase.length() - 1);
//
//            String proxifiedBaseUrl = proxyBase + baseUri.getPath();
//            if (!proxifiedBaseUrl.endsWith("/"))
//                proxifiedBaseUrl += "/";
//
//            return proxifiedBaseUrl;
//        } catch (URISyntaxException urise) {
//            // hmm...guess the proxy base must be invalid
//            throw new RuntimeException(
//                    "Invalid Proxy Base URL property is set in your GeoServer installation.", urise);
//        }
    }
    
    /**
     * Determines if the specified string is a valid "major,minor,patch"
     * version number. 
     */
    public static boolean isValidVersionNumber(String v) {
        return v.matches("[0-99]\\.[0-99]\\.[0-99]");
    }
    
    
    /**
     * Parses the specified string as a "major.minor.patch" version number.
     * <p>
     * This method will append 0 for minor and patch parts of the version number
     * in the event they are not specified. ie: 1.0 -> 1.0.0 and 1 -> 1.0.0
     * </p>
     * <p>
     * In the event of a null or empty string this method returns null.
     * </p>
     * @param v The version number string. 
     */
    public static Version version( String v ) {
        if (v == null || "".equals( v ) ) {
            return null;
        }
        
        if (!isValidVersionNumber(v)) {
            String[] parts = v.split("\\.");
            switch( parts.length ) {
                case 1: v = parts[0] + ".0.0"; break;
                case 2: v = parts[0] + "." + parts[1] + ".0"; break;
                default:
                    throw new IllegalArgumentException( "Invalid version number: " + v + "");
            }
        }
        
        return new Version( v );
    }
    
    /**
     * Matches the specified version to the highest available list of versions.
     * <p>
     * When <tt>version</tt> is null the highest available version is returned.
     * </p>
     * @param version The version to match, may be null.
     * @param versions The list of available versions, assumed to be sorted from 
     * lowest to highest.
     * 
     * @return The highest matching version.
     */
    public static Version matchHighestVersion( Version version, List versions ) {
        //match the version to the highest version provided by service
        // that is less than or equal to the "specified" version
        if ( version != null ) {
            Version last = null;
            for ( Iterator i = versions.iterator(); i.hasNext(); ) {
                Version v = (Version) i.next();
                if (version.compareTo( v ) < 0 ) {
                    break;
                }
                last = v;
            }
            
            if ( last == null ) {
                version = (Version) versions.get(0);
            }
            else {
                version = last;
            }
        }
        else {
            version = (Version) versions.get( versions.size() -1 );
        }
        
        return version;
    }
}
