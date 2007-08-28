/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;


/**
 * A GetMapProducerFactorySpi just for testing GetMapResponse without depending
 * on a real one.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class TestMapProducerFactory implements GetMapProducerFactorySpi {
    /** a fictional image format mime type this factory claims to support */
    public static final String TESTING_MIME_TYPE = "image/unit+testing";

    /**
     * whether an instance returns it is available or not. Set from inside
     * GetMapResponseTest.
     */
    public static boolean available = true;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "GetMapProducerFactorySpi for unit testing GetMapResponse only";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getSupportedFormats() {
        return Collections.singleton(TESTING_MIME_TYPE);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
        return TESTING_MIME_TYPE.equals(mapFormat);
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
    public GetMapProducer createMapProducer(String mapFormat, WMS wms)
        throws IllegalArgumentException {
        if (!canProduce(mapFormat)) {
            throw new IllegalArgumentException();
        }

        return new TestingMapProducer();
    }

    /**
     * DOCUMENT ME!
     *
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private class TestingMapProducer extends AbstractGetMapProducer implements GetMapProducer {
        /**
         * DOCUMENT ME!
         *
         * @param map DOCUMENT ME!
         *
         * @throws WmsException DOCUMENT ME!
         */
        public void produceMap() throws WmsException {
        }

        /**
         * DOCUMENT ME!
         *
         * @param out DOCUMENT ME!
         *
         * @throws ServiceException DOCUMENT ME!
         * @throws IOException DOCUMENT ME!
         */
        public void writeTo(OutputStream out) throws ServiceException, IOException {
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws IllegalStateException DOCUMENT ME!
         */
        public String getContentType() throws IllegalStateException {
            return TestMapProducerFactory.TESTING_MIME_TYPE;
        }

        /**
         * DOCUMENT ME!
         */
        public void abort() {
        }

        public String getContentDisposition() {
            // can be null
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.geotools.factory.Factory#getImplementationHints()
     * This just returns java.util.Collections.EMPTY_MAP
     */
    public Map getImplementationHints() {
        return java.util.Collections.EMPTY_MAP;
    }
}
