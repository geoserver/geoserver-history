/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import java.util.Map;

import org.w3c.dom.Element;


/**
 * Represents a configuration of the WFS service.  Inherits most everything
 * from ServiceConfig.
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: WFSConfig.java,v 1.1.2.8 2003/11/22 02:02:18 cholmesny Exp $
 */
public class WFSConfig extends ServiceConfig {
    public static final String WFS_FOLDER = "wfs/1.0.0/";
    public static final String WFS_BASIC_LOC = WFS_FOLDER + "WFS-basic.xsd";
    public static final String WFS_CAP_LOC = WFS_FOLDER
        + "WFS-capabilities.xsd";
    private GlobalConfig global = GlobalConfig.getInstance();
    private String describeUrl;

    /**
     * Creates a new WFSConfig object.
     *
     * @param root DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    public WFSConfig(Element root) throws ConfigurationException {
        super(root);
        URL = GlobalConfig.getInstance().getBaseUrl() + "wfs/";
    }
    
    public WFSConfig(Map config ) {
        super( config );
        URL = GlobalConfig.getInstance().getBaseUrl() + "wfs/";
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
        if (this.describeUrl == null) {
            this.describeUrl = URL
                + "DescribeFeatureType?typeName=";
        }

        return describeUrl;
    }

    public String getDescribeUrl(String typeName) {
        return getDescribeBaseUrl() + typeName;
    }

    public String getWfsBasicLocation() {
        return global.getSchemaBaseUrl() + WFS_BASIC_LOC;
    }

    public String getWfsCapLocation() {
        return global.getSchemaBaseUrl() + WFS_CAP_LOC;
    }
}
