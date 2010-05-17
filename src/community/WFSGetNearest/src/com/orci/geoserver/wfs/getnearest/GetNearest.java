/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package com.orci.geoserver.wfs.getnearest;

import org.vfny.geoserver.Response;
import org.vfny.geoserver.global.WFS;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wfs.servlets.WFService;

import java.util.Map;


/**
 * Implements the WFS GetNearest interface, which responds to requests for
 * GML. This servlet accepts a getFeatures request and returns GML2.1
 * structured XML docs.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @author Gabriel Rold?n
 * @version $Id: Feature.java,v 1.6 2004/02/09 23:29:46 dmzwiers Exp $
 */
public class GetNearest extends WFService {
    public GetNearest(WFS wfs) {
        super("GetNearest", wfs);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Response getResponseHandler() {
        return new GetNearestResponse();
    }

    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected KvpRequestReader getKvpReader(Map params) {
        return new GetNearestKvpReader(params, this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected XmlRequestReader getXmlRequestReader() {
        return new GetNearestXmlReader(this);
    }
}
