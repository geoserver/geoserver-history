/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.w3c.dom.*;
import java.util.Date;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
public class WMSConfig extends ServiceConfig {
    /** WMS version spec implemented */
    private static final String WMS_VERSION = "1.1.1";

    /** WMS spec specifies this fixed service name */
    private static final String FIXED_SERVICE_NAME = "OGC:WMS";
    private static final String[] EXCEPTION_FORMATS = {
        "application/vnd.ogc.se_xml", "application/vnd.ogc.se_inimage",
        "application/vnd.ogc.se_blank"
    };
    private Date updateTime = new Date();

    public WMSConfig(Element wmsRoot) throws ConfigurationException {
        super(wmsRoot);
        URL = GlobalConfig.getInstance().getBaseUrl() + "/wms";
    }

    public String[] getExceptionFormats() {
        return EXCEPTION_FORMATS;
    }

    /**
     * overrides getName() to return the fixed service name as specified by OGC
     * WMS 1.1 spec
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return FIXED_SERVICE_NAME;
    }

    /**
     * returns a GMT time string that represents the last modification time of
     * the capabilities aspects of the WMS service
     *
     * @return DOCUMENT ME!
     */
    public String getUpdateTime() {
        return updateTime.toGMTString();
    }

    public String getVersion() {
        return WMS_VERSION;
    }

    public boolean supportsSLD() {
        return true;
    }

    public boolean supportsUserLayer() {
        return true;
    }

    public boolean supportsUserStyle() {
        return true;
    }

    public boolean supportsRemoteWFS() {
        return true;
    }
}
