/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets.wfs;

import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.requests.readers.KvpRequestReader;
import org.vfny.geoserver.requests.readers.XmlRequestReader;
import org.vfny.geoserver.requests.readers.wfs.LockKvpReader;
import org.vfny.geoserver.requests.readers.wfs.LockXmlReader;
import org.vfny.geoserver.responses.Response;
import org.vfny.geoserver.responses.wfs.LockResponse;
import org.vfny.geoserver.servlets.WFService;
import java.util.Map;
import javax.servlet.ServletContext;


/**
 * Implements the WFS Lock interface, which performs insert, update and delete
 * functions on the dataset. This servlet accepts a Lock request and returns a
 * LockResponse xml element.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: Lock.java,v 1.5 2004/01/31 00:27:26 jive Exp $
 */
public class Lock extends WFService {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Response getResponseHandler() {
        ServletContext context = getServletContext();

        return new LockResponse((GeoServer) context.getAttribute("GeoServer"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected KvpRequestReader getKvpReader(Map params) {
        return new LockKvpReader(params);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected XmlRequestReader getXmlRequestReader() {
        return new LockXmlReader();
    }
}
