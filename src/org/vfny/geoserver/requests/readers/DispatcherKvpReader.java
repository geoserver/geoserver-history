/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers;

import org.vfny.geoserver.servlets.*;
import java.util.*;
import java.util.logging.*;


/**
 * Reads in a generic request and attempts to determine its type.
 *
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldán
 * @version $Id: DispatcherKvpReader.java,v 1.2 2003/12/16 18:46:08 cholmesny Exp $
 */
public class DispatcherKvpReader {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.readers");

    /**
     * Returns the request type for a given KVP set.
     *
     * @param kvPairs DOCUMENT ME!
     *
     * @return Request type.
     */
    public static int getRequestType(Map kvPairs) {
        String responseType = ((String) kvPairs.get("REQUEST"));
        LOGGER.finer("dispatcher got request " + responseType);

        if (responseType != null) {
            responseType = responseType.toUpperCase();

            if (responseType.equals("GETCAPABILITIES")) {
                return Dispatcher.WFS_GET_CAPABILITIES_REQUEST;
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
