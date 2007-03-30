/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.png;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;


/**
 * A simple extension to the PNG map producer that generates a paletted image with fixed palette
 *
 * @author Andrea Aime
 * @version $Id$
 */
public final class PNG8MapProducerFactory extends PNGMapProducerFactory {
    /** the only MIME type this map producer supports */
    static final String MIME_TYPE = "image/png";
    static final String FORMAT = "image/png8";

    /**
     * convenient singleton Set to expose the output format this producer
     * supports
     */
    private static final Set SUPPORTED_FORMATS = Collections.singleton(FORMAT);

    /**
     * Creates a new PNGMapProducerFactory object.
     */
    public PNG8MapProducerFactory() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "Portable Network Graphics (PNG) 8 bit map producer";
    }

    /**
     * Returns the Set of output format this producer supports
     *
     * @return Set of output format this producer supports (actually
     *         "image/png")
     */
    public Set getSupportedFormats() {
        return PNG8MapProducerFactory.SUPPORTED_FORMATS;
    }

    public boolean isAvailable() {
        return true;
    }

    public boolean canProduce(String mapFormat) {
        return FORMAT.equals(mapFormat);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapFormat
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException
     *             DOCUMENT ME!
     */
    public GetMapProducer createMapProducer(String mapFormat, WMS wms)
        throws IllegalArgumentException {
        if (!canProduce(mapFormat)) {
            throw new IllegalArgumentException(new StringBuffer(mapFormat).append(
                    " not supported by this map producer").toString());
        }

        return new PNG8MapProducer(FORMAT, wms);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.factory.Factory#getImplementationHints() This just
     *      returns java.util.Collections.EMPTY_MAP
     */
    public Map getImplementationHints() {
        return java.util.Collections.EMPTY_MAP;
    }
}
