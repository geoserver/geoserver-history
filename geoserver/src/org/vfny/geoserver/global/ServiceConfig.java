/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.util.Map;

import org.w3c.dom.Element;


/**
 * default configuration for services
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: ServiceConfig.java,v 1.1.2.1 2003/12/30 23:08:26 dmzwiers Exp $
 */
public abstract class ServiceConfig extends BasicConfig {
    /** DOCUMENT ME! */
    private boolean enabled;

    /** DOCUMENT ME! */
    private String serviceType;

    /** DOCUMENT ME! */
    private String onlineResource;

    /** DOCUMENT ME! */
    protected String URL;

    public ServiceConfig(Element serviceRoot) throws ConfigurationException {
        super(serviceRoot);
        this.serviceType = getAttribute(serviceRoot, "type", true);
        this.enabled = getBooleanAttribute(serviceRoot, "enabled", true);
        this.onlineResource = getChildText(serviceRoot, "onlineResource", true);

        //this.URL = getChildText(serviceRoot, "URL");
    }
    /** Quick config for JUnit tests.
     * <ul>
     * <li>service.enabled: boolean (default true)</li>
     * <li>service.type: String (default test)</li>
     * <li>service.onlineResources (default localhost)</li>
     * </ul>
     */
    public ServiceConfig(Map config ){
        super( config );
        enabled = get( config, "service.enabled", true );
        serviceType = get( config, "service.type", "Test");
        onlineResource = get( config, "service.onlineResources", "localhost" );                        
    }
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getOnlineResource() {
        return onlineResource;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getURL() {
        return URL;
    }

    /**
     * Gets the base schema url.
     *
     * @return The url to use as the base for schema locations.
     *
     * @deprecated Use GlobalConfig.getSchemaBaseUrl()
     */
    public String getSchemaBaseUrl() {
        return GlobalConfig.getInstance().getSchemaBaseUrl();
    }
}
