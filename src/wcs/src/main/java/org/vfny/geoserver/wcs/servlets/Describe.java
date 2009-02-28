/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.servlets;

import java.util.Map;

import org.geoserver.config.GeoServer;
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
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id$
 */
public class Describe extends WCService {
    public Describe(GeoServer gs) {
        super("DescribeCoverage", gs);
    }

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3257003241991714611L;

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    protected Response getResponseHandler() {
        return new DescribeResponse(getWCS());
    }

    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected KvpRequestReader getKvpReader(Map params) {
        return new DescribeKvpReader(params, getWCS());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected XmlRequestReader getXmlRequestReader() {
        return new DescribeXmlReader(getWCS());
    }
}
