/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.w3c.dom.*;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
public class WFSConfig extends ServiceConfig {
    /**
     * Creates a new WFSConfig object.
     *
     * @param root DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    public WFSConfig(Element root) throws ConfigurationException {
        super(root);
        URL = GlobalConfig.getInstance().getBaseUrl() + "/wfs";
    }
}
