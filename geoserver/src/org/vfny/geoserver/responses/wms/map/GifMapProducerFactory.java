/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.map;

import org.vfny.geoserver.responses.wms.GetMapProducerFactorySpi;
import java.util.Collections;
import java.util.Set;


/**
 * DOCUMENT ME!
 *
 * @author Didier Richard, IGN-F
 * @version $Id$
 */
public class GifMapProducerFactory implements GetMapProducerFactorySpi {
    /** DOCUMENT ME!  */
    private static final Set SUPPORTED_FORMATS = Collections.singleton(
            "image/gif");

    /**
     * Creates a new GifMapProducerFactory object.
     */
    public GifMapProducerFactory() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "Graphics Interchange Format (GIF) map producer";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getSupportedFormats() {
        return SUPPORTED_FORMATS;
    }

    /**
     * <b>FIXME</b> JAI dependency ?
     *
     * @return <code>true</code>
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapFormat DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canProduce(String mapFormat) {
        return SUPPORTED_FORMATS.contains(mapFormat);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapFormat DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public GetMapProducer createMapProducer(String mapFormat)
        throws IllegalArgumentException {
        return new GIFMapProducer();
    }
}
