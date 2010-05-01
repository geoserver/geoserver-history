/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.servlets;

import java.util.Map;

import org.geoserver.wms.WMS;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wms.requests.GetLegendGraphicKvpReader;
import org.vfny.geoserver.wms.responses.GetLegendGraphicResponse;


/**
 * WMS service wich returns request and response handlers to manage a
 * GetLegendGraphic request
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class GetLegendGraphic extends WMService {
    /**
     *
     */
    private static final long serialVersionUID = 4957246942172056598L;

    public GetLegendGraphic(WMS wms) {
        super("GetLegendGraphic", wms);
    }

    /**
     * Returns a response handler to manage a GetLegendGraphic request
     *
     * @return a response handler to manage a GetLegendGraphic request
     */
    protected Response getResponseHandler() {
        return new GetLegendGraphicResponse(getApplicationContext());
    }

    /**
     * Returns GetLegendGraphic request parser
     *
     * @param params the kvp set of the request
     *
     * @return a GetLegendGraphic request parser
     */
    protected KvpRequestReader getKvpReader(Map params) {
        return new GetLegendGraphicKvpReader(params, getWMS());
    }

    /**
     * Throws an UnsupportedOperationException since GetLegendGraphic does
     * not defines an xml encoding for HTTP POST method.
     *
     * @return never, allways fails since it is an unsupported method for this
     *         request
     *
     * @throws UnsupportedOperationException
     */
    protected XmlRequestReader getXmlRequestReader() {
        throw new UnsupportedOperationException("request does not defines a POST encoding");
    }
}
