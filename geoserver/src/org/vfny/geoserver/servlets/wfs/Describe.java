/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets.wfs;

import org.vfny.geoserver.*;
import org.vfny.geoserver.config.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.requests.readers.*;
import org.vfny.geoserver.requests.readers.wfs.*;
import org.vfny.geoserver.requests.wfs.*;
import org.vfny.geoserver.responses.*;
import org.vfny.geoserver.responses.wfs.*;
import org.vfny.geoserver.servlets.WFService;
import java.io.*;
import java.util.Map;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * Implements the WFS DescribeFeatureTypes inteface, which tells clients the
 * schema for each feature type. This servlet returns descriptions of all
 * feature types served by the server. Note that this assumes that the
 * possible schemas are only single tables, with no foreign key relationships
 * with other tables.
 *
 * @author Rob Hranac, TOPP
 * @version $Id: Describe.java,v 1.1.2.1 2003/11/04 23:31:11 cholmesny Exp $
 */
public class Describe extends WFService {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Response getResponseHandler() {
        return new DescribeResponse();
    }

    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected KvpRequestReader getKvpReader(Map params) {
        return new DescribeKvpReader(params);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected XmlRequestReader getXmlRequestReader() {
        return new DescribeXmlReader();
    }
}
