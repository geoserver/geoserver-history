/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import org.vfny.geoserver.servlets.Dispatcher;
import java.util.logging.Logger;


/**
 * Reads in a generic request and attempts to determine its type.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: DispatcherKvpReader.java,v 1.3 2003/09/12 21:34:16 cholmesny Exp $
 */
public class DispatcherKvpReader extends RequestKvpReader {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /**
     * Constructor with raw request string.  This constructor parses the entire
     * request string into a kvp hash table for quick access by sub-classes.
     *
     * @param rawRequest The raw request string from client.
     */
    public DispatcherKvpReader(String rawRequest) {
        super(rawRequest);
    }

    /**
     * Returns the request type for a given KVP string.
     *
     * @return Request type.
     */
    public int getRequestType() {
        String responseType = ((String) kvpPairs.get("REQUEST"));
        LOGGER.finer("dispatcher got request " + responseType);

        if (responseType != null) {
            responseType = responseType.toUpperCase();

            if (responseType.equals("GETCAPABILITIES")) {
                return Dispatcher.GET_CAPABILITIES_REQUEST;
            } else if (responseType.equals("DESCRIBEFEATURETYPE")) {
                return Dispatcher.DESCRIBE_FEATURE_TYPE_REQUEST;
            } else if (responseType.equals("GETFEATURE")) {
                return Dispatcher.GET_FEATURE_REQUEST;
            } else if (responseType.equals("TRANSACTION")) {
                return Dispatcher.TRANSACTION_REQUEST;
            } else if (responseType.equals("GETFEATUREWITHLOCK")) {
                return Dispatcher.GET_FEATURE_LOCK_REQUEST;
            } else if (responseType.equals("LOCKFEATURE")) {
                return Dispatcher.LOCK_REQUEST;
            } else {
                return Dispatcher.UNKNOWN;
            }
        } else {
            return Dispatcher.UNKNOWN;
        }
    }
}
