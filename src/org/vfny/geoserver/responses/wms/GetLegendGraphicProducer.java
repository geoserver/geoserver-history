/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms;

import java.io.IOException;
import java.io.OutputStream;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.requests.wms.GetLegendGraphicRequest;

public interface GetLegendGraphicProducer {
    /**
     * Asks this map producer to render the passed map context to the specified
     * format.
     *
     * @param map
     *
     * @throws WmsException
     */
    void produceLegendGraphic(GetLegendGraphicRequest request) throws WmsException;

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    void writeTo(OutputStream out) throws ServiceException, IOException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws java.lang.IllegalStateException DOCUMENT ME!
     */
    String getContentType() throws java.lang.IllegalStateException;

    /**
     * DOCUMENT ME!
     */
    void abort();

}
