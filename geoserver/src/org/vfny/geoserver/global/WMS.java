/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.util.*;
import java.text.*;

import org.vfny.geoserver.global.dto.*;

/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: WMS.java,v 1.1.2.2 2004/01/05 23:26:26 dmzwiers Exp $
 */
public class WMS extends Service {
    /** WMS version spec implemented */
    private static final String WMS_VERSION = "1.1.1";

    /** WMS spec specifies this fixed service name */
    private static final String FIXED_SERVICE_NAME = "OGC:GlobalWMS";

    /** DOCUMENT ME!  */
    private static final String[] EXCEPTION_FORMATS = {
        "application/vnd.ogc.se_xml", "application/vnd.ogc.se_inimage",
        "application/vnd.ogc.se_blank"
    };

	private WMSDTO config = null;

    public WMS(WMSDTO config){
    	super(config.getService());
    	this.config = config;
    	URL = GeoServer.getInstance().getBaseUrl() + "/wms";
    }

	Object getDTO(){
		return config;
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
    	DateFormat df = DateFormat.getInstance();
    	df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(config.getUpdateTime());
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
