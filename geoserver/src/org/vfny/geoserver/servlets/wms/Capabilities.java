/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets.wms;

import org.vfny.geoserver.*;
import org.vfny.geoserver.config.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.requests.readers.*;
import org.vfny.geoserver.requests.readers.wms.*;
import org.vfny.geoserver.responses.*;
import org.vfny.geoserver.responses.wms.*;
import org.vfny.geoserver.servlets.WMService;
import java.io.*;
import java.util.Map;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: Capabilities.java,v 1.2 2003/12/16 18:46:10 cholmesny Exp $
 */
public class Capabilities extends WMService {
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
        return new WMSCapabilitiesResponse();
    }
}
