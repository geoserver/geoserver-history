/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers.wfs;

import org.vfny.geoserver.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.requests.readers.*;
import org.vfny.geoserver.requests.wfs.*;
import java.util.*;
import java.util.logging.*;


/**
 * This utility reads in a LockFeature KVP request and turns it into an
 * appropriate internal Lock type request object.
 *
 * @author Rob Hranac, TOPP
 * @author Gabriel Roldán
 * @version $Id: LockKvpReader.java,v 1.1.2.1 2003/11/04 22:46:52 cholmesny Exp $
 */
public class LockKvpReader extends KvpRequestReader {
    /** Class logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.readers");

    /**
     * Constructor with raw request string.  Calls parent.
     *
     * @param kvPairs key/value pairs ser to construct a Lock request
     */
    public LockKvpReader(Map kvPairs) {
        super(kvPairs);
    }

    /**
     * Returns Lock feature request object.
     *
     * @return LockRequest Lock request objects
     *
     * @throws ServiceException WfsException For any problems forming the
     *         request.
     * @throws WfsException DOCUMENT ME!
     */
    public Request getRequest() throws ServiceException {
        LockRequest currentRequest = new LockRequest();

        // set global request parameters
        LOGGER.finest("setting global request parameters");

        if (keyExists("VERSION")) {
            currentRequest.setVersion(getValue("VERSION"));
        }

        if (keyExists("REQUEST")) {
            currentRequest.setRequest(getValue("REQUEST"));
        }

        if (keyExists("EXPIRY")) {
            currentRequest.setExpiry(Integer.parseInt(getValue("EXPIRY")));
        }

        if (keyExists("LOCKACTION")) {
            String lockAction = getValue("LOCKACTION");

            if (lockAction == null) {
                currentRequest.setLockAll(true);
            } else if (lockAction.toUpperCase().equals("ALL")) {
                currentRequest.setLockAll(true);
            } else if (lockAction.toUpperCase().equals("SOME")) {
                currentRequest.setLockAll(false);
            } else {
                throw new WfsException("Illegal lock action: " + lockAction);
            }
        }

        // declare tokenizers for repeating elements
        LOGGER.finer("setting query request parameters");

        List typeList = readFlat(getValue("TYPENAME"), INNER_DELIMETER);
        LOGGER.finer("type list size: " + typeList.size());

        List filterList = readFilters(getValue("FEATUREID"),
                getValue("FILTER"), getValue("BBOX"));

        if (typeList.size() == 0) {
            typeList = getTypesFromFids(getValue("FEATUREID"));

            if (typeList.size() == 0) {
                throw new WfsException("The typename element is mandatory if "
                    + "no FEATUREID is present");
            }
        }

        int featureSize = typeList.size();
        int filterSize = filterList.size();

        // check for errors in the request
        if (((filterSize != featureSize) && (filterSize > 0))
                || ((filterSize > 0) && (featureSize == 0))) {
            throw new WfsException("Filter size does not match"
                + " feature types.  Filter size: " + filterSize
                + " Feature size: " + featureSize);
        } else {
            currentRequest.setLocks(typeList, filterList);

            return currentRequest;
        }
    }
}
