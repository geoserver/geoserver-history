/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.servlets;

import java.util.Map;

import org.vfny.geoserver.Response;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wcs.requests.readers.DescribeKvpReader;
import org.vfny.geoserver.wcs.requests.readers.DescribeXmlReader;
import org.vfny.geoserver.wcs.responses.DescribeResponse;


/**
 * Implements the WFS DescribeFeatureTypes inteface, which tells clients the
 * schema for each feature type. This servlet returns descriptions of all
 * feature types served by the server. Note that this assumes that the
 * possible schemas are only single tables, with no foreign key relationships
 * with other tables.
 *
 * @author Rob Hranac, TOPP
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: Describe.java,v 1.6 2004/02/09 23:29:46 dmzwiers Exp $
 */
public class Describe extends WCService {
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
