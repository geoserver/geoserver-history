/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.w3c.dom.*;
import java.util.*;


/**
 * default configuration for services
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
public abstract class ServiceConfig extends BasicConfig {
    /** DOCUMENT ME! */
    private boolean enabled;

    /** DOCUMENT ME! */
    private String serviceType;

    /** DOCUMENT ME! */
    private String onlineResource;

    /** DOCUMENT ME! */
    private String URL;

    public ServiceConfig(Element serviceRoot) throws ConfigurationException {
        super(serviceRoot);
        this.serviceType = getAttribute(serviceRoot, "type", true);
        this.enabled = getBooleanAttribute(serviceRoot, "enabled", true);
        this.onlineResource = getChildText(serviceRoot, "onlineResource", true);
        this.URL = getChildText(serviceRoot, "URL");
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
     */
    public String getSchemaBaseUrl() {
        return getURL() + "/data/capabilities/";
    }
}
