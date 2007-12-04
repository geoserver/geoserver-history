/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog.requests.readers;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.catalog.CatalogException;
import org.vfny.geoserver.catalog.requests.AddFeatureTypeRequest;
import org.vfny.geoserver.catalog.servlets.CATALOGService;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;


/**
 * This utility reads in a AddFeatureType KVP request and turns it into an
 * appropriate internal AddFeatureTypeRequest object.
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public class AddFeatureTypeKvpReader extends KvpRequestReader {
    /**
     * Constructor with raw request string.  Calls parent.
     *
     * @param kvPairs the key/value pairs containing DESCRIBE
     */
    public AddFeatureTypeKvpReader(Map kvPairs, CATALOGService service) {
        super(kvPairs, service);
    }

    /**
     * Returns a list of requested feature types..
     *
     * @param request the servlet request holding the server config
     *
     * @return AddFeatureTypeRequest request object.
     * @throws CatalogException
     */
    public Request getRequest(HttpServletRequest request)
        throws CatalogException {
        AddFeatureTypeRequest currentRequest = new AddFeatureTypeRequest((CATALOGService) service);

        if (keyExists("SERVICE")) {
            final String service = getValue("SERVICE");

            if (service.trim().toUpperCase().startsWith("CATALOG")) {
                currentRequest.setService(service);
            } else {
                throw new CatalogException("SERVICE parameter is wrong.");
            }
        } else {
            throw new CatalogException("SERVICE parameter is mandatory.");
        }

        if (keyExists("VERSION")) {
            final String version = getValue("VERSION");

            if (version.equals("1.0.0")) {
                currentRequest.setVersion(version);
            } else {
                throw new CatalogException("VERSION parameter is wrong.");
            }
        } else {
            throw new CatalogException("VERSION parameter is mandatory.");
        }

        if (keyExists("REQUEST")) {
            final String requestType = getValue("REQUEST");

            if (requestType.equalsIgnoreCase("AddFeatureType")) {
                currentRequest.setRequest(requestType);
            } else {
                throw new CatalogException("REQUEST parameter is wrong.");
            }
        } else {
            throw new CatalogException("REQUEST parameter is mandatory.");
        }

        if (!keyExists("DATA")) {
            throw new CatalogException("DATA parameter is mandatory.");
        }

        currentRequest.setHttpServletRequest(request);
        currentRequest.setVersion(getValue("VERSION"));
        currentRequest.setRequest(getValue("REQUEST"));
        currentRequest.setData(getValue("DATA"));

        return currentRequest;
    }
}
