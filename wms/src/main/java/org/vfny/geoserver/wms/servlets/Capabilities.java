/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.servlets;

import java.util.Map;

import org.geoserver.wms.WMS;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wms.requests.CapabilitiesKvpReader;
import org.vfny.geoserver.wms.requests.CapabilitiesXmlReader;
import org.vfny.geoserver.wms.responses.WMSCapabilitiesResponse;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Rold?n
 * @version $Id$
 */
public class Capabilities extends WMService {
    
    public Capabilities(WMS wms) {
        super("GetCapabilities", wms);
    }

    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected KvpRequestReader getKvpReader(Map params) {
        return new CapabilitiesKvpReader(params, getWMS());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected XmlRequestReader getXmlRequestReader() {
        return new CapabilitiesXmlReader(getWMS());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Response getResponseHandler() {
        return new WMSCapabilitiesResponse(getApplicationContext());
    }
}
