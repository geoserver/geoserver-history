/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets.wfs;

import java.util.Map;

import org.vfny.geoserver.requests.readers.KvpRequestReader;
import org.vfny.geoserver.requests.readers.XmlRequestReader;
import org.vfny.geoserver.requests.readers.wfs.CapabilitiesKvpReader;
import org.vfny.geoserver.requests.readers.wfs.CapabilitiesXmlReader;
import org.vfny.geoserver.responses.Response;
import org.vfny.geoserver.responses.wfs.WFSCapabilitiesResponse;
import org.vfny.geoserver.servlets.WFService;


/**
 * Implements the GlobalWFS GetCapabilities interface, which tells clients what the
 * server can do.
 *
 * @author Rob Hranac, TOPP
 * @author Gabriel Roldán
 * @version $Id: Capabilities.java,v 1.2.2.3 2004/01/02 17:53:28 dmzwiers Exp $
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
