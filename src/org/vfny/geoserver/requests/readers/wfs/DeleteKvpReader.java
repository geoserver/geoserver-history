/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers.wfs;

import org.geotools.filter.Filter;
import org.vfny.geoserver.WfsException;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.readers.KvpRequestReader;
import org.vfny.geoserver.requests.wfs.DeleteRequest;
import org.vfny.geoserver.requests.wfs.TransactionRequest;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 * This utility reads in a Delete KVP request and turns it into an appropriate
 * internal Delete type request object.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldán
 * @version $Id: DeleteKvpReader.java,v 1.5 2004/01/31 00:27:26 jive Exp $
 */
public class DeleteKvpReader extends KvpRequestReader {
    /** Class logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.readers");

    /**
     * Constructor with raw request string.  Calls parent.
     *
     * @param kvPairs The raw request string from the servlet.
     */
    public DeleteKvpReader(Map kvPairs) {
        super(kvPairs);
    }

    /**
     * Returns Delete request object.
     *
     * @param request the servlet request to get the GeoServer config from
     *
     * @return Delete request objects
     *
     * @throws WfsException If no type is found, if filter length doesn't match
     *         feature length, or if no filter is found.  We don't want  users
     *         to accidentally delete their whole db.
     */
    public Request getRequest(HttpServletRequest request) throws WfsException {
        TransactionRequest parentRequest = new TransactionRequest();
        parentRequest.setHttpServletRequest(request);
        boolean releaseAll = true;

        // set global request parameters
        LOGGER.finest("setting global request parameters");

        if (keyExists("VERSION")) {
            parentRequest.setVersion(getValue("VERSION"));
        }

        if (keyExists("REQUEST")) {
            parentRequest.setRequest(getValue("REQUEST"));
        }

        //REVISIT: This is not in spec, but really should be.  Waiting to hear
        //about features like this, that were just accidentally left out.
        if (keyExists("LOCKID")) {
            parentRequest.setLockId(getValue("LOCKID"));
        }

        // declare tokenizers for repeating elements
        LOGGER.finest("setting query request parameters");

        List typeList = readFlat(getValue("TYPENAME"), INNER_DELIMETER);
        LOGGER.finest("type list size: " + typeList.size());

        List filterList = null;
        filterList = readFilters(getValue("FEATUREID"), getValue("FILTER"),
                getValue("BBOX"));

        if (typeList.size() == 0) {
            typeList = getTypesFromFids(getValue("FEATUREID"));

            if (typeList.size() == 0) {
                throw new WfsException("The typename element is mandatory if "
                    + "no FEATUREID is present");
            }
        }

        int featureSize = typeList.size();
        int filterSize = (filterList == null) ? 0 : filterList.size();

        // prepare the release action boolean for all delete transactions
        if (keyExists("RELEASEACTION")) {
            String lockAction = getValue("RELEASEACTION");
            parentRequest.setReleaseAction(lockAction);
        }

        // check for errors in the request
        if (((filterSize != featureSize) && (filterSize > 0))
                || ((filterSize > 0) && (featureSize == 0))) {
            throw new WfsException("Filter size does not match"
                + " feature types.  Filter size: " + filterSize
                + " Feature size: " + featureSize);
        } else if (filterSize == featureSize) {
            for (int i = 0, n = featureSize; i < n; i++) {
                DeleteRequest childRequest = new DeleteRequest();
                childRequest.setTypeName((String) typeList.get(i));
                childRequest.setFilter((Filter) filterList.get(i));
                childRequest.setReleaseAll(releaseAll);
                parentRequest.addSubRequest(childRequest);
            }
        } else if (filterSize == 0) {
            String message = "No filter found.  If you are sure you want to "
                + "wipe out your database, then use a filter that is always true"
                + ".  We just don't want you to inadvertantly wipe everything "
                + "out without intending to";
            throw new WfsException(message);
        }

        return parentRequest;
    }
}
