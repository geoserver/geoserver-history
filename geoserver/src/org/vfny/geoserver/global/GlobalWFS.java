/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.vfny.geoserver.config.wfs.*;

/**
 * Represents a configuration of the GlobalWFS service.  Inherits most everything
 * from ServiceConfig.
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: GlobalWFS.java,v 1.1.2.2 2004/01/03 00:20:15 dmzwiers Exp $
 */
public class GlobalWFS extends GlobalService {
    public static final String WFS_FOLDER = "wfs/1.0.0/";
    public static final String WFS_BASIC_LOC = WFS_FOLDER + "GlobalWFS-basic.xsd";
    public static final String WFS_CAP_LOC = WFS_FOLDER
        + "GlobalWFS-capabilities.xsd";
    private WFSConfig config;

    public GlobalWFS(WFSConfig config){
    	super(config.getService());
		URL = GlobalServer.getInstance().getGlobalData().getBaseUrl() + "wfs/";
		this.config = config;
    }
    /**
     * Gets the base url of a describe request.
     *
     * @return DOCUMENT ME!
     *
     * @task REVISIT: consider using the /wfs? base, as it makes things a bit
     *       clearer.  Right now, however, I'm getting problems with the & in
     *       returned xml, having to put a &amp; in, and not sure if clients
     *       will process it correctly.
     */
    public String getDescribeBaseUrl() {
        if (config.getDescribeUrl() == null) {
			config.setDescribeUrl(URL + "DescribeFeatureType?typeName=");
        }

        return config.getDescribeUrl();
    }

    public String getDescribeUrl(String typeName) {
        return getDescribeBaseUrl() + typeName;
    }

    public String getWfsBasicLocation() {
        return GlobalServer.getInstance().getGlobalData().getSchemaBaseUrl() + WFS_BASIC_LOC;
    }

    public String getWfsCapLocation() {
        return GlobalServer.getInstance().getGlobalData().getSchemaBaseUrl() + WFS_CAP_LOC;
    }
}
