/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers.wfs;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.geotools.filter.Filter;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WfsException;
import org.vfny.geoserver.requests.Query;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.readers.KvpRequestReader;
import org.vfny.geoserver.requests.wfs.FeatureRequest;
import org.vfny.geoserver.requests.wfs.FeatureWithLockRequest;

/**
 * This utility reads in a GetFeature KVP request and turns it into a
 * GetFeature type request object.
 *
 * <p>
 * If you pass this utility a KVP request (everything after the '?' in the
 * URI), it will translate this into a GetFeature type request object.  Note
 * that you  must check for validity before passing the request.
 * </p>
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldán
 * @version $Id: GetFeatureKvpReader.java,v 1.6 2004/02/09 23:29:40 dmzwiers Exp $
 */
public class GetFeatureKvpReader extends KvpRequestReader {
    /** Class logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.readers");

    /**
     * Constructor with raw request string.  Calls parent.
     *
     * @param kvPairs kay/value pairs for a GetFeature request
     */
    public GetFeatureKvpReader(Map kvPairs) {
        super(kvPairs);
    }

    public Request getRequest(HttpServletRequest request) throws ServiceException {
        return getRequest(false, request);
    }

    /**
     * Returns GetFeature request object.
     *
     * @param withLock Whether this should be a GetFeatureWithLock request.
     * @param srequest to set the request's servlet request
     * @return Feature request object.
     *
     * @throws WfsException If no typename or featureid is present, or if the
     *         filter list size doesn't match the feature list size.
     */
    public FeatureRequest getRequest(boolean withLock, HttpServletRequest srequest)
        throws WfsException {
        FeatureRequest currentRequest = new FeatureRequest();
        currentRequest.setHttpServletRequest(srequest);

        boolean useLock = false;

        if (withLock || keyExists("REQUEST")) {
            String request = getValue("REQUEST");
            useLock = withLock
                || request.equalsIgnoreCase("GETFEATUREWITHLOCK");

            if (useLock) {
                currentRequest = new FeatureWithLockRequest();
            }

            currentRequest.setRequest(request);
        }

        // set global request parameters
        LOGGER.finest("setting global request parameters");

        if (keyExists("MAXFEATURES")) {
            currentRequest.setMaxFeatures(getValue("MAXFEATURES"));
        }

        if (keyExists("VERSION")) {
            currentRequest.setVersion(getValue("VERSION"));
        }

        if (keyExists("REQUEST")) {
            currentRequest.setRequest(getValue("REQUEST"));
        }

        if (keyExists("FEATUREVERSION")) {
            currentRequest.setFeatureVersion(getValue("FEATUREVERSION"));
        }

        if (keyExists("OUTPUTFORMAT")) {
            currentRequest.setOutputFormat(getValue("OUTPUTFORMAT"));
        }

        if (useLock && keyExists("EXPIRY")) {
            ((FeatureWithLockRequest) currentRequest).setExpiry(Integer
                .parseInt(getValue("EXPIRY")));
        }

        // declare tokenizers for repeating elements
        LOGGER.finest("setting query request parameters");

        List typeList = readFlat(getValue("TYPENAME"), INNER_DELIMETER);
        List propertyList = readNested(getValue("PROPERTYNAME"));
        String fidKvps = getValue("FEATUREID");
        List filterList = readFilters(fidKvps, getValue("FILTER"),
                getValue("BBOX"));

        int propertySize = propertyList.size();
        int filterSize = filterList.size();

        if (typeList.size() == 0) {
            typeList = getTypesFromFids(fidKvps);

            if (typeList.size() == 0) {
                throw new WfsException("The typename element is mandatory if "
                    + "no FEATUREID is present");
            }
        }

        int featureSize = typeList.size();

        // check for errors in the request
        if (((propertySize != featureSize) && (propertySize > 1))
                || ((filterSize != featureSize) && (filterSize > 1))) {
            throw new WfsException("Properties or filter sizes do not match"
                + " feature types.  Property size: " + propertySize
                + " Filter size: " + filterSize + " Feature size: "
                + featureSize);
        } else {
            // loops through feature types, and creates queries based on them
            LOGGER.finest("setting query request parameters");

            for (int i = 0; i < featureSize; i++) {
                String featureType = (String) typeList.get(i);
                List properties;
                Filter filter;

                // permissive logic: lets one property list apply to all types
                LOGGER.finest("setting properties: " + i);

                if (propertySize == 0) {
                    properties = null;
                } else if (propertySize == 1) {
                    properties = (List) propertyList.get(0);
                } else {
                    properties = (List) propertyList.get(i);
                }

                // permissive logic: lets one filter apply to all types
                LOGGER.finest("setting filters: " + i);

                if (filterSize == 0) {
                    filter = null;
                } else if (filterSize == 1) {
                    filter = (Filter) filterList.get(0);
                } else {
                    filter = (Filter) filterList.get(i);
                }

                LOGGER.finest("query filter: " + filter);

                // add query
                currentRequest.addQuery(makeQuery(featureType, properties,
                        filter));
            }

            return currentRequest;
        }
    }

    /**
     * Returns a list of requested queries.
     *
     * @param featureType The featureType to query.
     * @param propertyNames The names of the properties to query.
     * @param filter What to filter features on.
     *
     * @return List of requested queries
     */
    private static Query makeQuery(String featureType, List propertyNames,
        Filter filter) {
        Query currentQuery = new Query();

        currentQuery.setTypeName(featureType);

        if (propertyNames != null) {
            for (int i = 0; i < propertyNames.size(); i++) {
                currentQuery.addPropertyName((String) propertyNames.get(i));
            }
        }

        if (filter != null) {
            currentQuery.addFilter(filter);
        }

        return currentQuery;
    }
}
