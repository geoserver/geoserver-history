/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers.wfs;

import java.util.Map;

import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.readers.KvpRequestReader;
import org.vfny.geoserver.requests.wfs.DescribeRequest;


/**
 * This utility reads in a DescribeFeatureType KVP request and turns it into an
 * appropriate internal DescribeRequest object.
 *
 * @author Rob Hranac, TOPP
 * @author Gabriel Roldán
 * @version $Id: DescribeKvpReader.java,v 1.3 2004/01/12 21:01:25 dmzwiers Exp $
 */
public class DescribeKvpReader extends KvpRequestReader {
    /**
     * Constructor with raw request string.  Calls parent.
     *
     * @param kvPairs the key/value pairs containing DESCRIBE
     */
    public DescribeKvpReader(Map kvPairs) {
        super(kvPairs);
    }

    /**
     * Returns a list of requested feature types..
     *
     * @return DescribeRequest request object.
     */
    public Request getRequest() {
        DescribeRequest currentRequest = new DescribeRequest();
        currentRequest.setVersion(getValue("VERSION"));
        currentRequest.setRequest(getValue("REQUEST"));
        currentRequest.setOutputFormat(getValue("OUTPUTFORMAT"));
        currentRequest.setFeatureTypes(readFlat(getValue("TYPENAME"),
                INNER_DELIMETER));

        return currentRequest;
    }
}
