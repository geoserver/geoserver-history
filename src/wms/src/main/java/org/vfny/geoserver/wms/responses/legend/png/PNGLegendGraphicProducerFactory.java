/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.legend.png;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.vfny.geoserver.wms.GetLegendGraphicProducer;
import org.vfny.geoserver.wms.GetLegendGraphicProducerSpi;


/**
 * WMS GetLegendGraphic image producer for the formats supported by the
 * available JAI library.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class PNGLegendGraphicProducerFactory implements GetLegendGraphicProducerSpi {
    /**
     *
     */
    public PNGLegendGraphicProducerFactory() {

    }

    /**
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducerSpi#getName()
     */
    public String getName() {
        return "Legend graphic producer factory for PNG format";
    }

    /**
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducerSpi#getSupportedFormats()
     */
    public Set getSupportedFormats() {
        Set s = new TreeSet();
        s.add("image/png");

        return s;
    }

    /**
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducerSpi#isAvailable()
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducerSpi#canProduce(java.lang.String)
     */
    public boolean canProduce(String mimeType) {
        return mimeType.equalsIgnoreCase("image/png");
    }

    /**
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducerSpi#createLegendProducer(java.lang.String)
     */
    public GetLegendGraphicProducer createLegendProducer(String format)
        throws IllegalArgumentException {
        if (!canProduce(format)) {
            throw new IllegalArgumentException(format + " not supported by this legend producer");
        }

        return new PNGLegendGraphicProducer();
    }

    /* (non-Javadoc)
         * @see org.geotools.factory.Factory#getImplementationHints()
         * This just returns java.util.Collections.EMPTY_MAP
         */
    public Map getImplementationHints() {
        return java.util.Collections.EMPTY_MAP;
    }
}
