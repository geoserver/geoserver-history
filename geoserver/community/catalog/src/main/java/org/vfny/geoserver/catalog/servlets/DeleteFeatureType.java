/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog.servlets;

import org.vfny.geoserver.Response;
import org.vfny.geoserver.catalog.requests.readers.DeleteFeatureTypeKvpReader;
import org.vfny.geoserver.catalog.requests.readers.DeleteFeatureTypeXmlReader;
import org.vfny.geoserver.catalog.responses.DeleteFeatureTypeResponse;
import org.vfny.geoserver.global.CATALOG;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import java.util.Map;


/**
 * ...
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public class DeleteFeatureType extends CATALOGService {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 6041201281690024053L;

    public DeleteFeatureType(CATALOG catalog) {
        super("DeleteFeatureType", catalog);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Response getResponseHandler() {
        return new DeleteFeatureTypeResponse();
    }

    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected KvpRequestReader getKvpReader(Map params) {
        return new DeleteFeatureTypeKvpReader(params, this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected XmlRequestReader getXmlRequestReader() {
        return new DeleteFeatureTypeXmlReader(this);
    }
}
