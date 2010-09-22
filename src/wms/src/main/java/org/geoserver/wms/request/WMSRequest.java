/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.request;

import java.util.Map;

import org.geoserver.ows.Dispatcher;

/**
 * Defines a general Request type and provides accessor methods for universal request information.
 * 
 * 
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldan
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id$
 */
public abstract class WMSRequest {

    public static final String WMS_SERVICE_TYPE = "WMS";

    private String baseUrl;

    private Map<String, String> rawKvp;

    /**
     * flag indicating if the request is get
     */
    protected boolean get;

    /**
     * The ows service,request,version
     */
    protected String service;

    protected String request;

    protected String version;

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

    public boolean isGet() {
        return get;
    }

    public void setGet(boolean get) {
        this.get = get;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setRawKvp(Map<String, String> rawKvp) {
        this.rawKvp = rawKvp;
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
     * Gets the raw kvp parameters which were used to create the request.
     */
    public Map<String, String> getRawKvp() {
        return rawKvp;
    }

    /**
     * Setter for the 'WMTVER' parameter, which is an alias for 'VERSION'.�
     * 
     */
    public void setWmtVer(String version) {
        setVersion(version);
    }
}
