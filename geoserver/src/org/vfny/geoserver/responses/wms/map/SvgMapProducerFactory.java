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
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class SvgMapProducerFactory implements GetMapProducerFactorySpi {
    /** DOCUMENT ME!  */
    private static final Set SUPPORTED_FORMATS = Collections.singleton(
            "image/svg+xml");

    /**
     * Creates a new SvgMapProducerFactory object.
     */
    public SvgMapProducerFactory() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "Scalable Vector Graphics (SVG) map producer";
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
     * By now SVG map producer does not have external dependencied (such as
     * Batik), so just returns <code>true</code>.
     * 
     * <p>
     * It is most probable that this situation change in the future, like when
     * adding Styling support.
     * </p>
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
        return new SVGMapResponse();
    }
}
