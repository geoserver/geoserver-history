/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.servlets;

import java.util.Map;

import org.vfny.geoserver.Response;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wcs.requests.readers.GetCoverageKvpReader;
import org.vfny.geoserver.wcs.requests.readers.GetCoverageXmlReader;
import org.vfny.geoserver.wcs.responses.CoverageResponse;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class Coverage extends WCService {

	protected Response getResponseHandler() {
        return new CoverageResponse();
    }

    protected KvpRequestReader getKvpReader(Map params) {
        return new GetCoverageKvpReader(params);
    }

    protected XmlRequestReader getXmlRequestReader() {
        return new GetCoverageXmlReader();
    }
}
