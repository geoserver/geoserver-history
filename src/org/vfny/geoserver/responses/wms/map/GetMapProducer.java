/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vfny.geoserver.responses.wms.map;

import java.io.IOException;
import java.io.OutputStream;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.responses.wms.WMSMapContext;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public interface GetMapProducer {
    /**
     * Asks this map producer to render the passed map context to the specified
     * format.
     *
     * @param map
     * @param format DOCUMENT ME!
     *
     * @throws WmsException
     */
    void produceMap(WMSMapContext map)
        throws WmsException;

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    void writeTo(OutputStream out)
        throws ServiceException, IOException;

    /**
     * DOCUMENT ME!
     *
     * @param gs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws java.lang.IllegalStateException DOCUMENT ME!
     */
    String getContentType()
        throws java.lang.IllegalStateException;

    /**
     * DOCUMENT ME!
     *
     * @param gs DOCUMENT ME!
     */
    void abort();
}
