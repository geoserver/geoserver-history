/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms;

import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.request.GetLegendGraphicRequest;
import org.geoserver.wms.response.LegendGraphic;

/**
 * Provides the skeleton for producers of a legend image, as required by the GetLegendGraphic WMS
 * request.
 * 
 * <p>
 * To incorporate a new producer specialized in one or many output formats, there must be a
 * {@linkPlain org.vfny.geoserver.responses.wms.GetLegendGraphicProducerSpi} registered that can
 * provide instances of that concrete implementation.
 * </p>
 * 
 * <p>
 * The methods defined in this interface respects the general parse request/produce response/get
 * mime type/write content workflow, so they should raise an exception if are called in the wrong
 * order (which is produceLegendGraphic -> getContentType -> writeTo)
 * </p>
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public interface GetLegendGraphicProducer {
    /**
     * Asks this legend graphic producer to create a graphic for the GetLegenGraphic request
     * parameters held in <code>request</code>
     * 
     * @param request
     *            the "parsed" request, where "parsed" means that it's properties are already
     *            validated so this method must not take care of verifying the requested layer
     *            exists and the like.
     * 
     * @throws WmsException
     *             something goes wrong
     */
    LegendGraphic produceLegendGraphic(GetLegendGraphicRequest request) throws ServiceException;

    /**
     * Writes the given legend graphic created to the destination stream, though it could be used to
     * encode the legend to the proper output format, provided that there are almost no risk that
     * the encoding fails.
     * 
     * @param legend
     *            the legend to encode
     * @param output
     *            an open stream where to send the produced legend graphic to.
     * 
     * @throws IOException
     *             if something goes wrong in the actual process of writing content to
     *             <code>out</code>.
     * @throws ServiceException
     *             if something else goes wrong.
     */
    void write(LegendGraphic legend, OutputStream output) throws IOException, ServiceException;

    /**
     * Returns the MIME type of the content supported by this format
     * 
     * @return the output format
     * 
     * @throws java.lang.IllegalStateException
     *             if this method is called before
     *             {@linkplain #produceLegendGraphic(GetLegendGraphicRequest)}.
     */
    String getContentType();

}
