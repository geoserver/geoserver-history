/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.sld.requests;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.servlets.AbstractService;
import org.vfny.geoserver.sld.SldException;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;


public class PutStylesKvpReader extends KvpRequestReader {


    public PutStylesKvpReader(Map kvpPairs, WMS service) {
        super(kvpPairs, service);
    }

    public Request getRequest(HttpServletRequest httpRequest)
        throws ServiceException {
        PutStylesRequest request = new PutStylesRequest((WMS) serviceConfig);
        request.setHttpServletRequest(httpRequest);

        String version = getRequestVersion();
        request.setVersion(version);

        parseMandatoryParameters(request);
        parseOptionalParameters(request);

        return request;
    }

    public void parseMandatoryParameters(PutStylesRequest request)
        throws SldException {
        String req = getValue("REQUEST");

        if ((req != null) && !req.equals("")) {
            if (!req.equalsIgnoreCase("PutStyles")) {
                throw new SldException("Expecting 'request=PutStyles'");
            }
        }

        String mode = getValue("MODE");

        if ((mode != null) && !mode.equals("")) {
            if (mode.equalsIgnoreCase("InsertAndReplace") || mode.equalsIgnoreCase("ReplaceAll")) {
                request.setMode(mode);
            } else {
                throw new SldException("Parameter must be 'InsertAndReplace' or 'ReplaceAll'.");
            }
        }
    }

    public void parseOptionalParameters(PutStylesRequest request) {
        String sld = getValue("SLD");

        if ((sld != null) && !sld.equals("")) {
            request.setSLD(sld);
        }

        String sld_body = getValue("SLD_BODY");

        if ((sld_body != null) && !sld_body.equals("")) {
            request.setSldBody(sld_body);
        }
    }

    protected String getRequestVersion() {
        String version = getValue("VERSION");

        if (version == null) {
            version = getValue("WMTVER");
        }

        if (version == null) {
            version = WMS.getVersion();
        }

        return version;
    }
}
