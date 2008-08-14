/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms;

import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.platform.ServiceException;

/**
 * Provides the skeleton for producers of map image, as required by the GetMap
 * WMS request.
 * 
 * <p>
 * A GetMapProducer implementation is meant to produce one and only one type of
 * content, whose normative MIME-Type is advertised through the
 * {@link #getContentType()} method. Yet, the {@link #getOutputFormat()} method
 * is meant to advertise the map format in the capabilities document and may or
 * may not match the mime type.
 * </p>
 * <p>
 * To incorporate a new producer specialized in a given output format, there
 * must be a {@linkplain org.vfny.geoserver.wms.responses.GetMapProducer}
 * registered in the spring context that can provide instances of that concrete
 * implementation.
 * </p>
 * 
 * <p>
 * The methods defined in this interface respect the general parse
 * request/produce response/get mime type/write content workflow, so they should
 * raise an exception if are called in the wrong order (which is produceMap ->
 * getContentType -> writeTo)
 * </p>
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @author Simone Giannecchini, GeoSolutions
 * @version $Id$
 */
public interface GetMapProducer {
    /**
     * Asks this map producer to create a map image for the passed {@linkPlain
     * WMSMapContext}, which contains enough information for doing such a
     * process.
     * 
     * 
     * @throws WmsException
     *             something goes wrong
     */
    void produceMap() throws WmsException;

    /**
     * Writes the map created in produceMap to the destination stream, though it
     * could be used to encode the map to the proper output format, provided
     * that there are almost no risk that the encoding fails.
     * 
     * @param out
     *            an open stream where to send the produced legend graphic to.
     * 
     * @throws ServiceException
     *             if something else goes wrong.
     * @throws IOException
     *             if something goes wrong in the actual process of writing
     *             content to <code>out</code>.
     */
    void writeTo(OutputStream out) throws ServiceException, IOException;

    /**
     * asks the legend graphic producer to stop processing since it will be no
     * longer needed (for example, because the request was interrupted by the
     * user)
     */
    void abort();

    /**
     * Sets the {@link MapContext} for this MapProducer.
     * 
     * @param mapContext
     *            to use for producing a map.
     */
    public void setMapContext(WMSMapContext mapContext);

    /**
     * Gets the {@link MapContext} for this MapProducer.
     * 
     * @return the {@link WMSMapContext} for this map producer.
     */
    public WMSMapContext getMapContext();

    /**
     * Returns the MIME type of the content to be written at
     * <code>writeTo(OutputStream)</code>
     * 
     * @return the output format
     */
    public String getContentType() throws java.lang.IllegalStateException;

    /**
     * Sets the MIME Type to be used for this {@link GetMapProducer}.
     * 
     * @deprecated should be a read only property
     */
    public void setContentType(String mime);

    /**
     * Gets the name of the output format this map producer creates, as shown in
     * the capabilities document.
     * <p>
     * Note it may differ from the MIME-Type set as the HTTP response header.
     * For example, the capabilities advertised output format name may be
     * "image/png8" but the actual mime type written down as an HTTP header be
     * just "image/png"
     * </p>
     * 
     * @return the desired output map format.
     */
    public String getOutputFormat();

    /**
     * Sets the MIME type of the output image.
     * 
     * @param format
     *            the desired output map format.
     * @deprecated non sense
     */
    public void setOutputFormat(String format);

    /**
     * The content disposition is the file name of the returned result. If there
     * is no file name, null is returned. The returned string should be in the
     * form: "inline; filename=name.ext" You need the "inline;" prefix and the
     * filename can be whatever you want. An example would be: "inline;
     * filename=states.pdf"
     * 
     * @return Header information for setting the file name
     */
    String getContentDisposition();
}
