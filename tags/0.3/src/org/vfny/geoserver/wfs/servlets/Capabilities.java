/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.servlets;

import java.util.Map;

import org.vfny.geoserver.Response;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wfs.requests.readers.CapabilitiesKvpReader;
import org.vfny.geoserver.wfs.requests.readers.CapabilitiesXmlReader;
import org.vfny.geoserver.wfs.responses.WFSCapabilitiesResponse;


/**
 * Implements the WFS GetCapabilities interface, which tells clients what the
 * server can do.
 *
 * @author Rob Hranac, TOPP
 * @author Gabriel Rold?n
 * @version $Id: Capabilities.java,v 1.6 2004/02/09 23:29:46 dmzwiers Exp $
 */
public class Capabilities extends WFService {
    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected KvpRequestReader getKvpReader(Map params) {
        return new CapabilitiesKvpReader(params);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected XmlRequestReader getXmlRequestReader() {
        return new CapabilitiesXmlReader();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Response getResponseHandler() {
        return new WFSCapabilitiesResponse();
    }
}
