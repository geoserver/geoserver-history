/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog.servlets;

import org.vfny.geoserver.Response;
import org.vfny.geoserver.catalog.requests.readers.AddFeatureTypeKvpReader;
import org.vfny.geoserver.catalog.requests.readers.AddFeatureTypeXmlReader;
import org.vfny.geoserver.catalog.responses.AddFeatureTypeResponse;
import org.vfny.geoserver.global.CATALOG;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import java.util.Map;


/**
 * ...
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public class AddFeatureType extends CATALOGService {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 318046797469240553L;

    public AddFeatureType(CATALOG catalog) {
        super("AddFeatureType", catalog);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Response getResponseHandler() {
        return new AddFeatureTypeResponse();
    }

    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected KvpRequestReader getKvpReader(Map params) {
        return new AddFeatureTypeKvpReader(params, this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected XmlRequestReader getXmlRequestReader() {
        return new AddFeatureTypeXmlReader(this);
    }
}
