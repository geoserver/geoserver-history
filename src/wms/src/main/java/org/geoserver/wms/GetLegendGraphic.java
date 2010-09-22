/* Copyright (c) 2010 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.request.GetLegendGraphicRequest;
import org.geoserver.wms.response.LegendGraphic;

/**
 * WMS GetLegendGraphic operation default implementation.
 * 
 * @author Gabriel Roldan
 */
public class GetLegendGraphic {

    private final WMS wms;

    public GetLegendGraphic(final WMS wms) {
        this.wms = wms;
    }

    public LegendGraphic run(final GetLegendGraphicRequest request) throws ServiceException {

        final String outputFormat = request.getFormat();
        final GetLegendGraphicOutputFormat format = wms.getLegendGraphicOutputFormat(outputFormat);
        if (format == null) {
            throw new ServiceException("There is no support for creating legends in "
                    + outputFormat + " format", "InvalidFormat");
        }
        LegendGraphic legend = format.produceLegendGraphic(request);
        return legend;
    }

}
