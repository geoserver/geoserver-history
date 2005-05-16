/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests.readers;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.requests.CoverageRequest;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class GetCoverageKvpReader extends KvpRequestReader {
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.readers");

    public GetCoverageKvpReader(Map kvPairs) {
        super(kvPairs);
    }

    public Request getRequest(HttpServletRequest request) throws ServiceException {
        return getCoverageRequest(request);
    }

    public CoverageRequest getCoverageRequest(HttpServletRequest srequest)
        throws WcsException {
        CoverageRequest currentRequest = new CoverageRequest();
        currentRequest.setHttpServletRequest(srequest);

        if (keyExists("REQUEST")) {
            String request = getValue("REQUEST");

            currentRequest.setRequest(request);
        }

        // set global request parameters
        LOGGER.finest("setting global request parameters");

        if (keyExists("VERSION")) {
            currentRequest.setVersion(getValue("VERSION"));
        }

        if (keyExists("REQUEST")) {
            currentRequest.setRequest(getValue("REQUEST"));
        }

        if (keyExists("SOURCECOVERAGE")) {
            currentRequest.setCoverage(getValue("SOURCECOVERAGE"));
        }

        if (keyExists("COVERAGEVERSION")) {
            currentRequest.setCoverageVersion(getValue("COVERAGEVERSION"));
        }

        if (keyExists("OUTPUTFORMAT")) {
            currentRequest.setOutputFormat(getValue("OUTPUTFORMAT"));
        }

        if (keyExists("ENVELOPE")) {
            currentRequest.setEnvelope(getValue("ENVELOPE"));
        }

        if (keyExists("INTERPOLATION")) {
            currentRequest.setInterpolation(getValue("INTERPOLATION"));
        }

        return currentRequest;
    }
}
