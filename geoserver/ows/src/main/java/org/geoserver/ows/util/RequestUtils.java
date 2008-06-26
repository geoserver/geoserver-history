/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.geoserver.platform.ServiceException;
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
     * Given a list of provided versions, and a list of accepted versions, this method will
     * return the negotiated version to be used for response according to the pre OWS 1.1 specifications,
     * that is, WMS 1.1, WMS 1.3, WFS 1.0, WFS 1.1 and WCS 1.0
     * @param providedList a non null, non empty list of provided versions (in "x.y.z" format)
     * @param acceptedList a list of accepted versions, eventually null or empty (in "x.y.z" format)
     * @return the negotiated version to be used for response
     */
    public static String getVersionPreOws(List<String> providedList, List<String> acceptedList) {
        //first figure out which versions are provided
        TreeSet<Version> provided = new TreeSet<Version>();
        for (String v : providedList) {
            provided.add(new Version(v));
        }
        
        // if no accept list provided, we return the biggest
        if(acceptedList == null || acceptedList.isEmpty())
            return provided.last().toString();
    
        //next figure out what the client accepts (and check they are good version numbers)
        TreeSet<Version> accepted = new TreeSet<Version>();
        for (String v : acceptedList) {
            checkVersionNumber(v, null);
            
            accepted.add(new Version(v));
        }
    
        // prune out those not provided
        for (Iterator<Version> v = accepted.iterator(); v.hasNext();) {
            Version version = (Version) v.next();
    
            if (!provided.contains(version)) {
                v.remove();
            }
        }
    
        // lookup a matching version
        String version = null;
        if (!accepted.isEmpty()) {
            //return the highest version provided
            version = ((Version) accepted.last()).toString();
        } else {
            for (String v : acceptedList) {
                accepted.add(new Version(v));
            }
    
            //if highest accepted less then lowest provided, send lowest
            if ((accepted.last()).compareTo(provided.first()) < 0) {
                version = (provided.first()).toString();
            }
    
            //if lowest accepted is greater then highest provided, send highest
            if ((accepted.first()).compareTo(provided.last()) > 0) {
                version = (provided.last()).toString();
            }
    
            if (version == null) {
                //go through from lowest to highest, and return highest provided 
                // that is less than the highest accepted
                Iterator<Version> v = provided.iterator();
                Version last = v.next();
    
                for (; v.hasNext();) {
                    Version current = v.next();
    
                    if (current.compareTo(accepted.last()) > 0) {
                        break;
                    }
    
                    last = current;
                }
    
                version = last.toString();
            }
        }
        
        return version;
    }
    
    /**
     * Given a list of provided versions, and a list of accepted versions, this method will
     * return the negotiated version to be used for response according to the OWS 1.1 specification
     * (at the time of writing, only WCS 1.1.1 is using it)
     * @param providedList a non null, non empty list of provided versions (in "x.y.z" format)
     * @param acceptedList a list of accepted versions, eventually null or empty (in "x.y.z" format)
     * @return the negotiated version to be used for response
     */
    public static String getVersionOws11(List<String> providedList, List<String> acceptedList) {
        //first figure out which versions are provided
        TreeSet<Version> provided = new TreeSet<Version>();
        for (String v : providedList) {
            provided.add(new Version(v));
        }
        
        // if no accept list provided, we return the biggest supported version
        if(acceptedList == null || acceptedList.isEmpty())
            return provided.last().toString();
            
    
        // next figure out what the client accepts (and check they are good version numbers)
        List<Version> accepted = new ArrayList<Version>();
        for (String v : acceptedList) {
            checkVersionNumber(v, "AcceptVersions");
            
            accepted.add(new Version(v));
        }
    
        // from the specification "The server, upon receiving a GetCapabilities request, shall scan 
        // through this list and find the first version number that it supports"
        Version negotiated = null;
        for (Iterator<Version> v = accepted.iterator(); v.hasNext();) {
            Version version = (Version) v.next();
    
            if (provided.contains(version)) {
                negotiated = version;
                break;
            }
        }
        
        // from the spec: "If the list does not contain any version numbers that the server 
        // supports, the server shall return an Exception with 
        // exceptionCode="VersionNegotiationFailed"
        if(negotiated == null)
            throw new ServiceException("Could not find any matching version", "VersionNegotiationFailed");
        
        return negotiated.toString();
    }

    /**
     * Determines if the specified string is a valid "major,minor,patch"
     * version number. 
     */
    public static boolean isValidVersionNumber(String v) {
        return v.matches("[0-99]\\.[0-99]\\.[0-99]");
    }
    
    /**
     * Checks the validity of a version number (the specification version numbers, three dot
     * separated integers between 0 and 99). Throws a ServiceException if the version number
     * is not valid.
     * @param v the version number (in string format)
     * @param the locator for the service exception (may be null)
     */
    public static void checkVersionNumber(String v, String locator) throws ServiceException {
        if (!isValidVersionNumber(v)) {
            String msg = v + " is an invalid version number";
            throw new ServiceException(msg, "VersionNegotiationFailed", locator);
        }
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
}
