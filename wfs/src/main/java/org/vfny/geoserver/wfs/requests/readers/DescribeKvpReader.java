/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.requests.readers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.servlets.AbstractService;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.wfs.requests.DescribeRequest;
import org.vfny.geoserver.wfs.servlets.WFService;

/**
 * This utility reads in a DescribeFeatureType KVP request and turns it into an
 * appropriate internal DescribeRequest object.
 *
 * @author Rob Hranac, TOPP
 * @author Gabriel Rold?n
 * @version $Id: DescribeKvpReader.java,v 1.6 2004/02/09 23:29:40 dmzwiers Exp $
 */
public class DescribeKvpReader extends KvpRequestReader {
    

    /**
     * Constructor with raw request string and servlet handling
     * request. Calls parent.
     *
     * @param kvPairs the key/value pairs containing DESCRIBE
     * @param service the servlet / service handling the request.
     */
    public DescribeKvpReader(Map kvPairs, AbstractService service) {
    		super(kvPairs,service);
    }
    
    /**
     * Returns a list of requested feature types..
     *
     * @param request the servlet request holding the server config
     *
     * @return DescribeRequest request object.
     */
    public Request getRequest(HttpServletRequest request) {
        DescribeRequest currentRequest = new DescribeRequest((WFService) service);
        currentRequest.setHttpServletRequest(request);
        currentRequest.setVersion(getValue("VERSION"));
        currentRequest.setRequest(getValue("REQUEST"));
        currentRequest.setOutputFormat(getValue("OUTPUTFORMAT"));
        currentRequest.setFeatureTypes(readFlat(getValue("TYPENAME"),
                INNER_DELIMETER));

        return currentRequest;
    }
}
