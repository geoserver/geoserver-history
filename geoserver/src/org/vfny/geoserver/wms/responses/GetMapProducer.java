/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Provides the skeleton for producers of map image, as required by the
 * GetMap WMS request.
 * 
 * <p>
 * To incorporate a new producer specialized in one or many output formats,
 * there must be a {@linkplain org.vfny.geoserver.responses.wms.GetMapProducerFactorySpi} registered
 * that can provide instances of that concrete implementation.
 * </p>
 * 
 * <p>
 * The methods defined in this interface respects the general parse
 * request/produce response/get mime type/write content workflow, so they
 * should raise an exception if are called in the wrong order (which is
 * produceMap -> getContentType -> writeTo)
 * </p>
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public interface GetMapProducer {
    /**
     * Asks this map producer to create a map image for the passed {@linkPlain
     * WMSMapContext}, which contains enough information for doing such a
     * process.
     *
     * @param map the map context that contains all the information required to
     *        create the map image.
     *
     * @throws WmsException something goes wrong
     */
    void produceMap(WMSMapContext map) throws WmsException;

    /**
     * Writes the map created in produceMap to the destination stream, though
     * it could be used to encode the map to the proper output format,
     * provided that there are almost no risk that the encoding fails.
     *
     * @param out an open stream where to send the produced legend graphic to.
     *
     * @throws ServiceException if something else goes wrong.
     * @throws IOException if something goes wrong in the actual process of
     *         writing content to <code>out</code>.
     */
    void writeTo(OutputStream out) throws ServiceException, IOException;

    /**
     * Returns the MIME type of the content to be writen at
     * <code>writeTo(OutputStream)</code>
     *
     * @return the output format
     *
     * @throws java.lang.IllegalStateException if this method is called before
     *         {@linkPlain #produceMap(WMSMapContext)},
     */
    String getContentType() throws java.lang.IllegalStateException;

    /**
     * asks the legend graphic producer to stop processing since it will be no
     * longer needed (for example, because the request was interrupted by the
     * user)
     */
    void abort();
}
