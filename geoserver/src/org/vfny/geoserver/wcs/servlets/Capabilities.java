/* Copyright (c) 2005 NATO - Undersea Research Centre.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.servlets;

import java.util.Map;

import org.vfny.geoserver.Response;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wcs.requests.readers.CapabilitiesKvpReader;
import org.vfny.geoserver.wcs.requests.readers.CapabilitiesXmlReader;
import org.vfny.geoserver.wcs.responses.WCSCapabilitiesResponse;

/**
 * Implements the WCS GetCapabilities interface, which tells clients what the
 * server can do.
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: Capabilities.java,v 0.1 Feb 15, 2005 12:15:13 PM $
 */
public class Capabilities extends WCService {
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
        return new WCSCapabilitiesResponse();
    }
}