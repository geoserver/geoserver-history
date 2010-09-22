/* Copyright (c) 2010 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */package org.geoserver.wms;

import static org.geoserver.ows.util.ResponseUtils.baseURL;

import java.nio.charset.Charset;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.request.DescribeLayerRequest;
import org.geoserver.wms.response.DescribeLayerTransformer;

/**
 * DescribeLayer WMS operation default implementation.
 * 
 * @author Gabriel Roldan
 */
public class DescribeLayer {

    private WMS wms;

    public DescribeLayer(final WMS wms) {
        this.wms = wms;
    }

    /**
     * @see org.geoserver.wms.DescribeLayer#run(org.geoserver.wms.request.DescribeLayerRequest)
     */
    public DescribeLayerTransformer run(DescribeLayerRequest request) throws ServiceException {

        DescribeLayerTransformer transformer;
        transformer = new DescribeLayerTransformer(baseURL(request.getHttpRequest()));
        transformer.setNamespaceDeclarationEnabled(false);
        Charset encoding = wms.getCharSet();
        transformer.setEncoding(encoding);
        if (wms.getGeoServer().getGlobal().isVerbose()) {
            transformer.setIndentation(2);
        }
        return transformer;
    }

}
