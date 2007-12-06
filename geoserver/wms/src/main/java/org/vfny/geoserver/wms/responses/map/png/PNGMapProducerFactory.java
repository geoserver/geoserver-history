/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.png;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * DOCUMENT ME!
 *
 * @author Didier Richard, IGN-F
 * @author Simone Giannecchini
 * @version $Id$
 */
public class PNGMapProducerFactory implements GetMapProducerFactorySpi {
    /** the only MIME type this map producer supports */
    static final String MIME_TYPE = "image/png";

    /**
     * convenient singleton Set to expose the output format this producer
     * supports
     */
    private static final Set SUPPORTED_FORMATS;

    static {
        SUPPORTED_FORMATS = new HashSet(2);
        SUPPORTED_FORMATS.add(MIME_TYPE);
        SUPPORTED_FORMATS.add(MIME_TYPE + "8");
    }

    /**
     * Creates a new PNGMapProducerFactory object.
     */
    public PNGMapProducerFactory() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "Portable Network Graphics (PNG) map producer";
    }

    /**
     * Returns the Set of output format this producer supports
     *
     * @return Set of output format this producer supports (actually
     *         "image/png")
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
        return SUPPORTED_FORMATS.contains(mapFormat);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapFormat
     *            DOCUMENT ME!
     * @param wms
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

        return new PNGMapProducer(mapFormat, MIME_TYPE, wms);
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
