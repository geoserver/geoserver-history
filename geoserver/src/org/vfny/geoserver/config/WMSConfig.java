/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.w3c.dom.*;
import java.util.Date;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: WMSConfig.java,v 1.2 2003/12/16 18:46:07 cholmesny Exp $
 */
public class WMSConfig extends ServiceConfig {
    /** WMS version spec implemented */
    private static final String WMS_VERSION = "1.1.1";

    /** WMS spec specifies this fixed service name */
    private static final String FIXED_SERVICE_NAME = "OGC:WMS";

    /** DOCUMENT ME!  */
    private static final String[] EXCEPTION_FORMATS = {
        "application/vnd.ogc.se_xml", "application/vnd.ogc.se_inimage",
        "application/vnd.ogc.se_blank"
    };
    private Date updateTime = new Date();

    /**
     * Creates a new WMSConfig object.
     *
     * @param wmsRoot DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    public WMSConfig(Element wmsRoot) throws ConfigurationException {
        super(wmsRoot);
        URL = GlobalConfig.getInstance().getBaseUrl() + "/wms";
    }
    public WMSConfig( Map config ){
        super( config );
        URL = GlobalConfig.getInstance().getBaseUrl() + "/wms";
    }
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getVersion() {
        return WMS_VERSION;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean supportsSLD() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean supportsUserLayer() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean supportsUserStyle() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean supportsRemoteWFS() {
        return true;
    }
}
