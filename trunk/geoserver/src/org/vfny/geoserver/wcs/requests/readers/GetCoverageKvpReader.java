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

import com.vividsolutions.jts.geom.Envelope;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
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

        if (keyExists("SERVICE")) {
            currentRequest.setService(getValue("SERVICE"));
        }

        if (keyExists("VERSION")) {
            currentRequest.setVersion(getValue("VERSION"));
        }

        if (keyExists("REQUEST")) {
            currentRequest.setRequest(getValue("REQUEST"));
        }

        if (keyExists("COVERAGE")) {
            currentRequest.setCoverage(getValue("COVERAGE"));
        }

        if (keyExists("COVERAGEVERSION")) {
            currentRequest.setCoverageVersion(getValue("COVERAGEVERSION"));
        }

        if (keyExists("FORMAT")) {
            currentRequest.setOutputFormat(getValue("FORMAT"));
        }

        if (keyExists("BBOX")) {
            currentRequest.setEnvelope(getValue("BBOX"));
        }

        if (keyExists("ENVELOPE")) {
            currentRequest.setEnvelope(getValue("ENVELOPE"));
        }

        if (keyExists("WIDTH") && keyExists("HEIGHT")) {
            currentRequest.setGridOrigin(new Double[] {new Double(0.0), new Double(0.0)});
            currentRequest.setGridLow(new Double[] {new Double(0.0), new Double(0.0)});
            currentRequest.setGridHigh(new Double[] {Double.valueOf(getValue("WIDTH")), Double.valueOf(getValue("HEIGHT"))});
        } else if(currentRequest.getEnvelope() != null && (keyExists("RESX") && keyExists("RESY"))) {
        	final Envelope envelope = currentRequest.getEnvelope();
        	final double envWidth = Math.abs(envelope.getMaxX() - envelope.getMinX());
        	final double envHeight = Math.abs(envelope.getMaxY() - envelope.getMinY());
        	final double width = envWidth / Math.abs(Double.parseDouble(getValue("RESX")));
        	final double height = envHeight / Math.abs(Double.parseDouble(getValue("RESY")));
            currentRequest.setGridOrigin(new Double[] {new Double(0.0), new Double(0.0)});
            currentRequest.setGridLow(new Double[] {new Double(0.0), new Double(0.0)});
            currentRequest.setGridHigh(new Double[] {new Double(width), new Double(height)});
        }

        if (keyExists("INTERPOLATION")) {
            currentRequest.setInterpolation(getValue("INTERPOLATION"));
        }

        return currentRequest;
    }
}
