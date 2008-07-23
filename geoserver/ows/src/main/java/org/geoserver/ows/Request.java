/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.ows;

import java.io.BufferedReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A collection of the informations collected and parsed by the
 * {@link Dispatcher} while doing its dispatching work. In case of dispatching
 * exceptions some fields may be left blank, depending how far the dispatching
 * went.
 * 
 * @author Justin DeOliveira
 * @author Andrea Aime
 */
public class Request {
    /**
     * Http request / response
     */
    HttpServletRequest httpRequest;

    HttpServletResponse httpResponse;

    /**
     * flag indicating if the request is get
     */
    boolean get;

    /**
     * Kvp parameters, only non-null if get = true
     */
    Map kvp;

    /**
     * raw kvp parameters, unparsed
     */
    Map rawKvp;

    /**
     * buffered input stream, only non-null if get = false
     */
    BufferedReader input;

    /**
     * The ows service,request,version
     */
    String service;

    String request;

    String version;

    /**
     * The output format of hte request
     */
    String outputFormat;

    /**
     * Any errors that occur tryinng to determine the service
     */
    Throwable error;

    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    public HttpServletResponse getHttpResponse() {
        return httpResponse;
    }

    public boolean isGet() {
        return get;
    }

    public Map getKvp() {
        return kvp;
    }

    public Map getRawKvp() {
        return rawKvp;
    }

    public BufferedReader getInput() {
        return input;
    }

    public String getService() {
        return service;
    }

    public String getRequest() {
        return request;
    }

    public String getVersion() {
        return version;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public Throwable getError() {
        return error;
    }

    public String toString() {
        return service + " " + version + " " + request;
    }
}