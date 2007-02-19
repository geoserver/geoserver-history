/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.servlets;

import org.vfny.geoserver.Response;
import org.vfny.geoserver.global.WCS;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wcs.requests.readers.GetCoverageKvpReader;
import org.vfny.geoserver.wcs.requests.readers.GetCoverageXmlReader;
import org.vfny.geoserver.wcs.responses.CoverageResponse;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 */
public class Coverage extends WCService {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3257282552237797945L;

    public Coverage(WCS wcs) {
        super("GetCoverage", wcs);
    }

    protected Response getResponseHandler() {
        return new CoverageResponse();
    }

    protected KvpRequestReader getKvpReader(Map params) {
        return new GetCoverageKvpReader(params, this);
    }

    protected XmlRequestReader getXmlRequestReader() {
        return new GetCoverageXmlReader(this);
    }
}
