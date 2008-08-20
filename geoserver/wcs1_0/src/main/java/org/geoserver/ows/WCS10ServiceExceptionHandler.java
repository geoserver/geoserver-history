/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.ows;

import java.util.List;

import org.geoserver.platform.Service;
import org.vfny.geoserver.global.GeoServer;


/**
 * A default implementation of {@link ServiceExceptionHandler} which outputs
 * as service exception in a <code>ows:ExceptionReport</code> document.
 * <p>
 * This service exception handler will generate an OWS exception report,
 * see {@linkplain http://schemas.opengis.net/ows/1.1.0/owsExceptionReport.xsd}.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WCS10ServiceExceptionHandler extends LegacyServiceExceptionHandler {
    protected boolean verboseExceptions = false;
    
    /**
     * Constructor to be called if the exception is not for a particular service.
     *
     */
    public WCS10ServiceExceptionHandler(Service service, OWS ows, GeoServer geoServer) {
        super(service, ows, geoServer);
    }

    /**
     * Constructor to be called if the exception is for a particular service.
     *
     * @param services List of services this handler handles exceptions for.
     */
    public WCS10ServiceExceptionHandler(List services, OWS ows, GeoServer geoServer) {
        super(services, ows, geoServer);
        contentType = "application/vnd.ogc.se_xml";
    }
}
