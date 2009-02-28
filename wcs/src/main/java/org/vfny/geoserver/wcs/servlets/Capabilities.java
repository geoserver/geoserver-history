/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.servlets;

import java.util.Map;

import org.geoserver.config.GeoServer;
import org.geoserver.wcs.WCSInfo;
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
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id$
 */
public class Capabilities extends WCService {
    public Capabilities(GeoServer gs) {
        super("GetCapabilities", gs);
    }

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3258129176207636277L;

    /**
    * DOCUMENT ME!
    *
    * @param params DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    protected KvpRequestReader getKvpReader(Map params) {
        return new CapabilitiesKvpReader(params, getWCS());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected XmlRequestReader getXmlRequestReader() {
        return new CapabilitiesXmlReader(getWCS());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Response getResponseHandler() {
        return new WCSCapabilitiesResponse(getApplicationContext());
    }
}
