/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import org.geotools.filter.Filter;
import org.vfny.geoserver.responses.WfsException;
import java.util.List;
import java.util.logging.Logger;


/**
 * This utility reads in a Delete KVP request and turns it into an appropriate
 * internal Delete type request object.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: DeleteKvpReader.java,v 1.6 2003/09/12 18:37:56 cholmesny Exp $
 */
public class DeleteKvpReader extends RequestKvpReader {
    /** Class logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /**
     * Constructor with raw request string.  Calls parent.
     *
     * @param request The raw request string from the servlet.
     */
    public DeleteKvpReader(String request) {
        super(request);
    }

    /**
     * Returns Delete request object.
     *
     * @return Delete request objects
     *
     * @throws WfsException If no type is found, if filter length doesn't match
     *         feature length, or if no filter is found.  We don't want  users
     *         to accidentally delete their whole db.
     */
    public TransactionRequest getRequest() throws WfsException {
        TransactionRequest parentRequest = new TransactionRequest();
        boolean releaseAll = true;

        // set global request parameters
        LOGGER.finest("setting global request parameters");

        if (kvpPairs.containsKey("VERSION")) {
            parentRequest.setVersion((String) kvpPairs.get("VERSION"));
        }

        if (kvpPairs.containsKey("REQUEST")) {
            parentRequest.setRequest((String) kvpPairs.get("REQUEST"));
        }

        //REVISIT: This is not in spec, but really should be.  Waiting to hear
        //about features like this, that were just accidentally left out.
        if (kvpPairs.containsKey("LOCKID")) {
            parentRequest.setLockId((String) kvpPairs.get("LOCKID"));
        }

        // declare tokenizers for repeating elements
        LOGGER.finest("setting query request parameters");

        List typeList = readFlat((String) kvpPairs.get("TYPENAME"),
                INNER_DELIMETER);
        LOGGER.finest("type list size: " + typeList.size());

        List filterList = null;
        filterList = readFilters((String) kvpPairs.get("FEATUREID"),
                (String) kvpPairs.get("FILTER"), (String) kvpPairs.get("BBOX"));

        if (typeList.size() == 0) {
            typeList = getTypesFromFids((String) kvpPairs.get("FEATUREID"));

            if (typeList.size() == 0) {
                throw new WfsException("The typename element is mandatory if "
                    + "no FEATUREID is present");
            }
        }

        int featureSize = typeList.size();
        int filterSize = (filterList == null) ? 0 : filterList.size();

        // prepare the release action boolean for all delete transactions
        if (kvpPairs.containsKey("RELEASEACTION")) {
            String lockAction = (String) kvpPairs.get("RELEASEACTION");
            parentRequest.setReleaseAll(lockAction);
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
