/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms;

import java.util.Collections;
import java.util.Set;

import org.vfny.geoserver.responses.wms.helpers.JAISupport;


/**
 * Factory of legend graphic producers for the <code>"image/gif"</code> format.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class GIFLegendGraphicProducerFactory
    implements GetLegendGraphicProducerSpi {
    /**
     * Empty constructor, as required by the factory strategy.
     */
    public GIFLegendGraphicProducerFactory() {
        super();
    }

    /**
     * @see org.vfny.geoserver.responses.wms.GetLegendGraphicProducerSpi#getName()
     */
    public String getName() {
        return "Graphics Interchange Format (GIF) legend graphics producer";
    }

    /**
     * DOCUMENT ME!
     *
     * @return a singleton Set with the supported mime type.
     *
     * @see org.vfny.geoserver.responses.wms.GetLegendGraphicProducerSpi#getSupportedFormats()
     */
    public Set getSupportedFormats() {
        return Collections.singleton(GifLegendGraphicProducer.MIME_TYPE);
    }

    /**
     * Returns wether the gif legend producer is available to be used.
     *
     * @return <code>true</code> iif JAI is available, since the actual image generation
     * depends on JAI availability.
     *
     * @see org.vfny.geoserver.responses.wms.GetLegendGraphicProducerSpi#isAvailable()
     */
    public boolean isAvailable() {
        return JAISupport.isJaiAvailable();
    }

    /**
     * Evaluates if the prioducer this factory serves can create images in
     * <code>mimeType</code> format.
     *
     * @param mimeType DOCUMENT ME!
     *
     * @return true iif <code>mimeType == "image/gif"</code>
     *
     * @see org.vfny.geoserver.responses.wms.GetLegendGraphicProducerSpi#canProduce(java.lang.String)
     */
    public boolean canProduce(String mimeType) {
        return GifLegendGraphicProducer.MIME_TYPE.equals(mimeType);
    }

    /**
     * Creates a legend graphics producer for the given format, which in this
     * case must be <code>"image/gif"</code>
     *
     * @see org.vfny.geoserver.responses.wms.GetLegendGraphicProducerSpi#createLegendProducer(java.lang.String)
     */
    public GetLegendGraphicProducer createLegendProducer(String format)
        throws IllegalArgumentException {
        if (!canProduce(format)) {
            throw new IllegalArgumentException(format
                + " not supported by this legend producer");
        }

        return new GifLegendGraphicProducer();
    }
}
