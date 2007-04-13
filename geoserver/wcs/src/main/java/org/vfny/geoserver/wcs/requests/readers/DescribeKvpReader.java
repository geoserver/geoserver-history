/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests.readers;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.requests.DescribeRequest;
import org.vfny.geoserver.wcs.servlets.WCService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;


/**
 * This utility reads in a DescribeCoverage KVP request and turns it into an
 * appropriate internal DescribeRequest object.
 *
 * @author Rob Hranac, TOPP
 * @author Gabriel Rold�n
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id$
 */
public class DescribeKvpReader extends KvpRequestReader {
    /**
     * Constructor with raw request string.  Calls parent.
     *
     * @param kvPairs the key/value pairs containing DESCRIBE
     */
    public DescribeKvpReader(Map kvPairs, WCService service) {
        super(kvPairs, service);
    }

    /**
     * Returns a list of requested feature types..
     *
     * @param request the servlet request holding the server config
     *
     * @return DescribeRequest request object.
     * @throws WcsException
     */
    public Request getRequest(HttpServletRequest request)
        throws WcsException {
        DescribeRequest currentRequest = new DescribeRequest((WCService) service);

        if (keyExists("SERVICE")) {
            final String service = getValue("SERVICE");

            if (service.trim().toUpperCase().startsWith("WCS")) {
                currentRequest.setService(service);
            } else {
                throw new WcsException("SERVICE parameter is wrong.");
            }
        } else {
            throw new WcsException("SERVICE parameter is mandatory.");
        }

        if (keyExists("VERSION")) {
            final String version = getValue("VERSION");

            if (version.equals("1.0.0")) {
                currentRequest.setVersion(version);
            } else {
                throw new WcsException("VERSION parameter is wrong.");
            }
        } else {
            throw new WcsException("VERSION parameter is mandatory.");
        }

        if (keyExists("REQUEST")) {
            final String requestType = getValue("REQUEST");

            if (requestType.equalsIgnoreCase("DescribeCoverage")) {
                currentRequest.setRequest(requestType);
            } else {
                throw new WcsException("REQUEST parameter is wrong.");
            }
        } else {
            throw new WcsException("REQUEST parameter is mandatory.");
        }

        currentRequest.setHttpServletRequest(request);
        currentRequest.setVersion(getValue("VERSION"));
        currentRequest.setRequest(getValue("REQUEST"));
        currentRequest.setOutputFormat(getValue("OUTPUTFORMAT"));
        currentRequest.setCoverages(readFlat(getValue("COVERAGE"), INNER_DELIMETER));

        return currentRequest;
    }
}
