/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.response;

import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetLegendGraphic;
import org.geoserver.wms.WMS;
import org.geoserver.wms.request.GetLegendGraphicRequest;
import org.springframework.util.Assert;
import org.vfny.geoserver.wms.GetLegendGraphicProducer;

/**
 * 
 * @author Gabriel Roldan
 * @version $Id$
 */
public class GetLegendGraphicResponse extends Response {

    private final WMS wms;

    /**
     * Creates a new GetLegendGraphicResponse object.
     * 
     * @param applicationContext
     */
    public GetLegendGraphicResponse(final WMS wms) {
        super(GetLegendGraphicRequest.class);
        this.wms = wms;
    }

    /**
     * @param value
     *            a {@link LegendGraphic}
     * @param operation
     *            a {@link GetLegendGraphic} operation
     * @return {@link LegendGraphic#getMimeType()}
     * @see org.geoserver.ows.Response#getMimeType(java.lang.Object,
     *      org.geoserver.platform.Operation)
     */
    @Override
    public String getMimeType(final Object value, final Operation operation)
            throws ServiceException {
        Assert.isTrue(value instanceof LegendGraphic);
        return ((LegendGraphic) value).getMimeType();
    }

    @Override
    public void write(final Object value, final OutputStream output, final Operation operation)
            throws IOException, ServiceException {

        Assert.isTrue(value instanceof LegendGraphic);

        final LegendGraphic legend = (LegendGraphic) value;
        final String mimeType = legend.getMimeType();
        final GetLegendGraphicProducer format = wms.getLegendGraphicOutputFormat(mimeType);
        if (format == null) {
            throw new ServiceException("No legend produced for output format " + mimeType
                    + " found.", "InvalidFormat");
        }
        format.write(legend, output);
    }

}
