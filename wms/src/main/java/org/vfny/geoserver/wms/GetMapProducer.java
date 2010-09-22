/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.request.GetMapRequest;
import org.geoserver.wms.response.Map;
import org.geotools.map.MapContext;

/**
 * Provides the skeleton for producers of map image, as required by the GetMap WMS request.
 * 
 * <p>
 * A GetMapProducer implementation is meant to produce one and only one type of content, whose
 * normative MIME-Type is advertised through the {@link #getContentType()} method. Yet, the
 * {@link #getOutputFormat()} method is meant to advertise the map format in the capabilities
 * document and may or may not match the mime type.
 * </p>
 * <p>
 * To incorporate a new producer specialized in a given output format, there must be a
 * {@linkplain org.vfny.geoserver.wms.responses.GetMapProducer} registered in the spring context
 * that can provide instances of that concrete implementation.
 * </p>
 * 
 * <p>
 * The methods defined in this interface respect the general parse request/produce response/get mime
 * type/write content workflow, so they should raise an exception if are called in the wrong order
 * (which is produceMap -> getContentType -> writeTo)
 * </p>
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @author Simone Giannecchini, GeoSolutions
 * @version $Id$
 */
public interface GetMapProducer {
    /**
     * Asks this map producer to create a map image for the passed {@linkPlain WMSMapContext}, which
     * contains enough information for doing such a process.
     * @param mapContext 
     * 
     * 
     * @throws ServiceException
     *             something goes wrong
     */
    Map produceMap(WMSMapContext mapContext) throws ServiceException;

    /**
     * Writes the map created in produceMap to the destination stream, though it could be used to
     * encode the map to the proper output format, provided that there are almost no risk that the
     * encoding fails.
     * 
     * @param out
     *            an open stream where to send the produced legend graphic to.
     * 
     * @throws ServiceException
     *             if something else goes wrong.
     * @throws IOException
     *             if something goes wrong in the actual process of writing content to
     *             <code>out</code>.
     */
    void writeTo(OutputStream out) throws ServiceException, IOException;

    /**
     * asks the legend graphic producer to stop processing since it will be no longer needed (for
     * example, because the request was interrupted by the user)
     */
    void abort();

    /**
     * Gets the {@link MapContext} for this MapProducer.
     * 
     * @return the {@link WMSMapContext} for this map producer.
     */
    public WMSMapContext getMapContext();

    /**
     * Returns the MIME type of the content to be written at <code>writeTo(OutputStream)</code>.
     * <p>
     * The value of this method is meant to be required after the map has been {@link #produceMap()
     * produced} in order to set the content type HTTP response header, yet this value is not going
     * to be exposed in the capabilities document. If the output format MIME type coincides with one
     * of the desired output format names for the GetCapabilities document, an implementation must
     * ensure its returned in the list of format names at {@link #getOutputFormatNames()}.
     * </p>
     * <p>
     * This is so to give implementations more freedom over what the actual MIME type for the
     * response is, since it may be the case this MIME Type may vary depending on certain conditions
     * only the GeMapProducer implementation may know.
     * </p>
     * 
     * @return the output format
     */
    public String getContentType() throws java.lang.IllegalStateException;

    /**
     * Gets the name of the output format the GetMap request was provided with.
     * <p>
     * If not explicitly set through {@link #setOutputFormat(String)}, defaults to
     * {@link #getContentType()}.
     * </p>
     * 
     * @return the name of the outputformat parameter issued by the GetMap request.
     * @see #getOutputFormatNames()
     */
    public String getOutputFormat();

    /**
     * Returns the list of content type aliases for this output format, that are the ones to be used
     * as Format elements in the GetCapabilities document.
     * <p>
     * Aliases are used to easy the task of writing a GetMap request, (for example, to type
     * &outputformat=svg instead of the full &outputformat=image/svg+xml)
     * </p>
     * 
     * @return the aliases a map of the content type this map producer creates content type can be
     *         asked by through a GetMap request.
     */
    public Set<String> getOutputFormatNames();

    /**
     * Sets the MIME type of the output image.
     * 
     * @param format
     *            the desired output map format, one of {@link #getContentType()} or
     *            {@link #getOutputFormatNames()}.
     * @throws IllegalArgumentException
     *             if format is not supported by this GetMapProducer
     */
    public void setOutputFormat(String format);


    void write(org.geoserver.wms.response.Map map, OutputStream output);
}
