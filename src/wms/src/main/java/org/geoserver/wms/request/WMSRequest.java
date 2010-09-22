/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.request;

import org.geoserver.ows.Dispatcher;
import org.geoserver.ows.Request;

/**
 * Defines a general Request type and provides accessor methods for universal request information.
 * <p>
 * Also provides access to the HttpRequest that spawned this GeoServer Request. This HttpRequest is
 * most often used to lookup information stored in the Web Container (such as the GeoServer Global
 * information).
 * </p>
 * 
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldan
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id$
 */
abstract class WMSRequest extends Request {

    public static final String WMS_SERVICE_TYPE = "WMS";

    protected String baseUrl;

    /**
     * Creates the new request, supplying the request name and the sevlet handling the request.
     * 
     * @param requestType
     *            name of hte request, (Example, GetCapabiliites)
     * @param wms
     *            The wms configuration object.
     * 
     */
    protected WMSRequest(final String request) {
        setRequest(request);
    }

    /**
     * Set by {@link Dispatcher}
     * 
     * @param baseUrl
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    /**
     * Setter for the 'WMTVER' parameter, which is an alias for 'VERSION'.�
     * 
     */
    public void setWmtVer(String version) {
        setVersion(version);
    }
}
