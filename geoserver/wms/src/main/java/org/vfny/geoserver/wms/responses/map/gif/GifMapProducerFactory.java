/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.gif;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;
import java.util.Collections;
import java.util.Map;
import java.util.Set;


/**
 * DOCUMENT ME!
 *
 * @author Didier Richard, IGN-F *
 * @author Simone Giannechini
 * @version $Id$
 */
public final class GifMapProducerFactory implements GetMapProducerFactorySpi {
    /** the only MIME type this map producer supports */
    static final String MIME_TYPE = "image/gif";

    /**
     * convenient singleton Set to expose the output format this producer
     * supports
     */
    private static final Set SUPPORTED_FORMATS = Collections.singleton(MIME_TYPE);

    /**
     * Creates a new GifMapProducerFactory object.
     */
    public GifMapProducerFactory() {
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
     * Returns the Set of output format this producer supports
     *
     * @return Set of output format this producer supports (actually
     *         "image/gif")
     */
    public Set getSupportedFormats() {
        return SUPPORTED_FORMATS;
    }

    /**
     *
     * @return <code>true</code>
     */
    public boolean isAvailable() {
        try {
            Class.forName("com.sun.media.imageioimpl.plugins.gif.GIFImageWriterSpi");

            return true;
        } catch (ClassNotFoundException e) {
        }

        return false;
    }

    /**
     * Returns wether the map producers created by this factory can create maps
     * in the passed output format.
     *
     * @param mapFormat
     *            a MIME type string to check if this producer is able to
     *            handle.
     *
     * @return <code>true</code> if <code>mapFormat == "image/gif"</code>,
     *         <code>false</code> otherwise.
     */
    public boolean canProduce(String mapFormat) {
        return MIME_TYPE.equals(mapFormat);
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
            throw new IllegalArgumentException(mapFormat + " not supported by this map producer");
        }

        return new GIFMapProducer("image/gif", wms); // DJB: added "image/gif" or
                                                     // you'll get content encoded as
                                                     // image/png (the default)!!!
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
