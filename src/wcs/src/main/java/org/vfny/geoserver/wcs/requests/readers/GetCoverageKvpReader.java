/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests.readers;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.geoserver.platform.ServiceException;
import org.geoserver.wcs.WCSInfo;
import org.vfny.geoserver.Request;
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
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.requests.readers");

    public GetCoverageKvpReader(Map kvPairs, WCSInfo wcs) {
        super(kvPairs, wcs);
    }

    public Request getRequest(HttpServletRequest request)
        throws ServiceException {
        return getCoverageRequest(request);
    }

    public CoverageRequest getCoverageRequest(HttpServletRequest srequest)
        throws WcsException {
        CoverageRequest currentRequest = new CoverageRequest((WCSInfo)serviceConfig);
        currentRequest.setHttpServletRequest(srequest);

        Map parameters = new HashMap();
        parameters.putAll(kvpPairs);

        // set global request parameters
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("setting global request parameters");
        }

        if (keyExists("SERVICE")) {
            final String service = getValue("SERVICE");

            if (service.trim().toUpperCase().startsWith("WCS")) {
                currentRequest.setService(service);
            } else {
                throw new WcsException("SERVICE parameter is wrong.");
            }

            parameters.remove("SERVICE");
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

            parameters.remove("VERSION");
        } else {
            throw new WcsException("VERSION parameter is mandatory.");
        }

        if (keyExists("REQUEST")) {
            final String request = getValue("REQUEST");

            if (request.equalsIgnoreCase("GetCoverage")) {
                currentRequest.setRequest(request);
            } else {
                throw new WcsException("REQUEST parameter is wrong.");
            }

            parameters.remove("REQUEST");
        } else {
            throw new WcsException("REQUEST parameter is mandatory.");
        }

        if (keyExists("COVERAGE")) {
            currentRequest.setCoverage(getValue("COVERAGE"));
            parameters.remove("COVERAGE");
        } else {
            throw new WcsException("COVERAGE parameter is mandatory.");
        }

        if (keyExists("COVERAGEVERSION")) {
            currentRequest.setCoverageVersion(getValue("COVERAGEVERSION"));
            parameters.remove("COVERAGEVERSION");
        }

        if (keyExists("FORMAT")) {
            currentRequest.setOutputFormat(getValue("FORMAT"));
            parameters.remove("FORMAT");
        } else {
            throw new WcsException("FORMAT parameter is mandatory.");
        }

        if (keyExists("CRS")) {
            currentRequest.setCRS(getValue("CRS"));
            parameters.remove("CRS");
        } else {
            throw new WcsException("CRS parameter is mandatory.");
        }

        if (keyExists("RESPONSE_CRS")) {
            currentRequest.setResponseCRS(getValue("RESPONSE_CRS"));
            parameters.remove("RESPONSE_CRS");
        } else {
            currentRequest.setResponseCRS(getValue("CRS"));
        }

        if (keyExists("BBOX")) {
            currentRequest.setEnvelope(getValue("BBOX"));
            parameters.remove("BBOX");
        } else {
            throw new WcsException("BBOX parameter is mandatory.");
        }

        if (keyExists("WIDTH") && keyExists("HEIGHT")) {
            currentRequest.setGridOrigin(new Double[] { new Double(0.0), new Double(0.0) });
            currentRequest.setGridLow(new Double[] { new Double(0.0), new Double(0.0) });
            currentRequest.setGridHigh(new Double[] {
                    Double.valueOf(getValue("WIDTH")), Double.valueOf(getValue("HEIGHT"))
                });
            parameters.remove("WIDTH");
            parameters.remove("HEIGHT");
        } else if ((currentRequest.getEnvelope() != null)
                && (keyExists("RESX") && keyExists("RESY"))) {
            final Envelope envelope = currentRequest.getEnvelope();
            final double envWidth = Math.abs(envelope.getMaxX() - envelope.getMinX());
            final double envHeight = Math.abs(envelope.getMaxY() - envelope.getMinY());
            final double width = envWidth / Math.abs(Double.parseDouble(getValue("RESX")));
            final double height = envHeight / Math.abs(Double.parseDouble(getValue("RESY")));

            if ((width >= 1.0) && (height >= 1.0)) {
                currentRequest.setGridOrigin(new Double[] { new Double(0.0), new Double(0.0) });
                currentRequest.setGridLow(new Double[] { new Double(0.0), new Double(0.0) });
                currentRequest.setGridHigh(new Double[] { new Double(width), new Double(height) });
            }

            parameters.remove("RESX");
            parameters.remove("RESY");
        }

        if (keyExists("INTERPOLATION")) {
            currentRequest.setInterpolation(getValue("INTERPOLATION"));
            parameters.remove("INTERPOLATION");
        }

        currentRequest.setParameters(parameters);

        return currentRequest;
    }
    
    protected String getValue(String key) {
        //JD: this is probably something that the super method should do.
        String value = super.getValue(key);
        if ( value != null ) {
            value = value.trim();
        }
        
        return value;
    }
}
