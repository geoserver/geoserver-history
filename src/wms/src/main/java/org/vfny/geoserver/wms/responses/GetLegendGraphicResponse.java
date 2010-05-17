/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.ServiceException;
import org.springframework.context.ApplicationContext;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.wms.GetLegendGraphicProducer;
import org.vfny.geoserver.wms.GetLegendGraphicProducerSpi;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetLegendGraphicRequest;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class GetLegendGraphicResponse implements Response {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GetLegendGraphicResponse.class.getPackage()
                                                                                        .getName());

    /**
     * The legend graphic producer that will be used for the production of a legend in the
     * requested format.
     */
    private GetLegendGraphicProducer delegate;

    /**
     * Application Context
     */
    private ApplicationContext applicationContext;

    /**
     * Creates a new GetLegendGraphicResponse object.
     *
     * @param applicationContext
     */
    public GetLegendGraphicResponse(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Returns any extra headers that this service might want to set in the HTTP response object.
     * @see org.vfny.geoserver.Response#getResponseHeaders()
     */
    public HashMap getResponseHeaders() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param req DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     */
    public void execute(Request req) throws ServiceException {
        GetLegendGraphicRequest request = (GetLegendGraphicRequest) req;

        final String outputFormat = request.getFormat();
        this.delegate = getDelegate(outputFormat);
        this.delegate.produceLegendGraphic(request);
    }

    /**
     * @see Response#getContentType(GeoServer)
     */
    public String getContentType(GeoServer gs) throws IllegalStateException {
        if (this.delegate == null) {
            throw new IllegalStateException("No request has been processed");
        }

        return this.delegate.getContentType();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentEncoding() {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("returning content encoding null");
        }

        return null;
    }

    /**
     * Asks the GetLegendGraphicProducer obtained in execute() to abort the
     * process.
     *
     * @param gs not used.
     * @see Response#abort(ServiceInfo)
     */
    public void abort(ServiceInfo gs) {
        if (this.delegate != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("asking delegate for aborting the process");
            }

            this.delegate.abort();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        try { // mapcontext can leak memory -- we make sure we done (see
              // finally block)

            if (this.delegate == null) {
                throw new IllegalStateException(
                    "No GetMapDelegate is setted, make sure you have called execute and it has succeed");
            }

            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer(new StringBuffer("asking delegate for write to ").append(out).toString());
            }

            this.delegate.writeTo(out);
        } catch (Exception e) // we dont want to propogate a new error
         {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer(new StringBuffer("asking delegate for write to ").append(out).toString());
            }
        }
    }

    /**
     * Creates a GetMapDelegate specialized in generating the requested map
     * format
     *
     * @param outputFormat
     *            a request parameter object wich holds the processed request
     *            objects, such as layers, bbox, outpu format, etc.
     *
     * @return A specialization of <code>GetMapDelegate</code> wich can
     *         produce the requested output map format
     *
     * @throws WmsException
     *             if no specialization is configured for the output format
     *             specified in <code>request</code> or if it can't be
     *             instantiated
     */
    private GetLegendGraphicProducer getDelegate(String outputFormat)
        throws WmsException {
        Collection producers = GeoServerExtensions.extensions(GetLegendGraphicProducerSpi.class);
        GetLegendGraphicProducerSpi factory;

        for (Iterator iter = producers.iterator(); iter.hasNext();) {
            factory = (GetLegendGraphicProducerSpi) iter.next();

            if (factory.canProduce(outputFormat)) {
                return factory.createLegendProducer(outputFormat);
            }
        }

        throw new WmsException("There is no support for creating legends in " + outputFormat
            + " format", "InvalidFormat");
    }

    /**
     * Utility method to ask all the available legend graphic producer
     * factories if they support the production of a legend graphic in the
     * format specified.
     *
     * @param mimeType the MIME type of the desired legend format (e.g.
     *        "image/png").
     *
     * @return wether a legend producer can manage the specified format or not.
     * 
     * @deprecated use {@link #supportsFormat(String)}
     */
    public static boolean supportsFormat(String mimeType, ApplicationContext context) {
        return supportsFormat(mimeType);
    }

    /**
     * Utility method to ask all the available legend graphic producer
     * factories if they support the production of a legend graphic in the
     * format specified.
     *
     * @param mimeType the MIME type of the desired legend format (e.g.
     *        "image/png").
     *
     * @return wether a legend producer can manage the specified format or not.
     * 
     */
    public static boolean supportsFormat(String mimeType) {
        return loadLegendFormats().contains(mimeType);
    }
    
    /**
     * Convenient method to search and return all the supported image formats
     * for the creation of legend graphics.
     *
     * @return the set of all the supported legend graphic formats.
     * 
     * @deprecated use {@link #getFormats()}
     */
    public static Set getFormats(ApplicationContext context) {
        return getFormats();
    }

    /**
     * Convenient method to search and return all the supported image formats
     * for the creation of legend graphics.
     *
     * @return the set of all the supported legend graphic formats.
     */
    public static Set getFormats() {
        return loadLegendFormats();
    }
    
    /**
     * Convenience method for processing the GetLegendGraphicProducerSpi extension
     * point and returning the set of available image formats.
     */
    private static Set loadLegendFormats() {
        Collection producers = GeoServerExtensions.extensions(GetLegendGraphicProducerSpi.class);
        Set formats = new HashSet();
        GetLegendGraphicProducerSpi producer;

        for (Iterator iter = producers.iterator(); iter.hasNext();) {
            producer = (GetLegendGraphicProducerSpi) iter.next();
            formats.addAll(producer.getSupportedFormats());
        }

        return formats;
    }

    public String getContentDisposition() {
        return null;
    }
}
