/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import org.geotools.factory.FactoryFinder;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.responses.Response;
import org.vfny.geoserver.wms.requests.GetLegendGraphicRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class GetLegendGraphicResponse implements Response {
    /** DOCUMENT ME! */
    private GetLegendGraphicProducer producer;

    /**
     *
     */
    public GetLegendGraphicResponse() {
        super();
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
        GetLegendGraphicProducerSpi glf = getProducerFactory(request.getFormat());
        this.producer = glf.createLegendProducer(request.getFormat());
        this.producer.produceLegendGraphic(request);
    }

    /**
     * DOCUMENT ME!
     *
     * @param gs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public String getContentType(GeoServer gs) throws IllegalStateException {
        if (this.producer == null) {
            throw new IllegalStateException("no legend producer");
        }

        return this.producer.getContentType();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentEncoding() {
        return null;
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
        if (this.producer == null) {
            throw new IllegalStateException("no legend producer");
        }

        this.producer.writeTo(out);
    }

    /**
     * Asks the GetLegendGraphicProducer obtained in execute() to abort the
     * process.
     *
     * @param gs not used.
     */
    public void abort(Service gs) {
        if (this.producer != null) {
            this.producer.abort();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param mimeType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static GetLegendGraphicProducerSpi getProducerFactory(
        String mimeType) {
        Iterator it = FactoryFinder.factories(GetLegendGraphicProducerSpi.class);
        GetLegendGraphicProducerSpi glf = null;

        while (it.hasNext()) {
            GetLegendGraphicProducerSpi tmpGlf = (GetLegendGraphicProducerSpi) it
                .next();

            if (tmpGlf.canProduce(mimeType)) {
                glf = tmpGlf;

                break;
            }
        }

        return glf;
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
     */
    public static boolean supportsFormat(String mimeType) {
        return getProducerFactory(mimeType) != null;
    }

    /**
     * Convenient method to search and return all the supported image formats
     * for the creation of legend graphics.
     *
     * @return the set of all the supported legend graphic formats.
     */
    public static Set getFormats() {
        Set allFormats = new HashSet();
        Iterator it = FactoryFinder.factories(GetLegendGraphicProducerSpi.class);

        while (it.hasNext()) {
            allFormats.addAll(((GetLegendGraphicProducerSpi) it.next())
                .getSupportedFormats());
        }

        return allFormats;
    }
}
