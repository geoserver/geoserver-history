/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.request;

import javax.servlet.http.HttpServletRequest;

import org.geoserver.ows.Dispatcher;
import org.geoserver.ows.HttpServletRequestAware;
import org.geoserver.ows.Request;
import org.geoserver.ows.util.ResponseUtils;

/**
 * Defines a general Request type and provides accessor methods for universal request information.
 * <p>
 * Also provides access to the HttpRequest that spawned this GeoServer Request. This HttpRequest is
 * most often used to lookup information stored in the Web Container (such as the GeoServer Global
 * information).
 * </p>
 * <p>
 * <strong>NOTE</strong>: this class implements the deprecated {@link HttpServletRequestAware}
 * interface because I (gabriel) don't know what's the non deprecated way of obtaining the request
 * baseUrl, that some operation responses need to write the correct schema/DTD location on
 * responses. The request base URL is obtained in these cases through
 * {@link ResponseUtils#baseURL(javax.servlet.http.HttpServletRequest)}, hence the need for a
 * reference to the {@link HttpServletRequest}.
 * </p>
 * 
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldan
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id$
 */
public class WMSRequest extends Request  {

    public static final String WMS_SERVICE_TYPE = "WMS";

    private String baseUrl;

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
    public void setBaseUrl(final String baseUrl) {
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
