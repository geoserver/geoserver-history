/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms;

import java.util.Set;

import org.vfny.geoserver.responses.wms.helpers.JAISupport;


/**
 * WMS GetLegendGraphic image producer for the formats supported by the
 * available JAI library.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class JaiLegendGraphicProducerFactory
    implements GetLegendGraphicProducerSpi {
    /**
     *
     */
    public JaiLegendGraphicProducerFactory() {
        super();
    }

    /**
     * @see org.vfny.geoserver.responses.wms.GetLegendGraphicProducerSpi#getName()
     */
    public String getName() {
        return "Legend graphic producer factory for JAI formats";
    }

    /**
     * @see org.vfny.geoserver.responses.wms.GetLegendGraphicProducerSpi#getSupportedFormats()
     */
    public Set getSupportedFormats() {
        return JAISupport.getSupportedFormats();
    }

    /**
     * @see org.vfny.geoserver.responses.wms.GetLegendGraphicProducerSpi#isAvailable()
     */
    public boolean isAvailable() {
        return JAISupport.isJaiAvailable();
    }

    /**
     * @see org.vfny.geoserver.responses.wms.GetLegendGraphicProducerSpi#canProduce(java.lang.String)
     */
    public boolean canProduce(String mimeType) {
        return JAISupport.getSupportedFormats().contains(mimeType);
    }

    /**
     * @see org.vfny.geoserver.responses.wms.GetLegendGraphicProducerSpi#createLegendProducer(java.lang.String)
     */
    public GetLegendGraphicProducer createLegendProducer(String format)
        throws IllegalArgumentException {
        if (!canProduce(format)) {
            throw new IllegalArgumentException(format
                + " not supported by this legend producer");
        }

        return new JaiLegendGraphicProducer(format);
    }
}
