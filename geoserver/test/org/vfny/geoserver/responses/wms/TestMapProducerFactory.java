/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
/*
 * Created on Feb 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.vfny.geoserver.responses.wms;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.responses.wms.map.GetMapProducer;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
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
    public GetMapProducer createMapProducer(String mapFormat)
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
    private class TestingMapProducer implements GetMapProducer {
    	
        /**
         * DOCUMENT ME!
         *
         * @param map DOCUMENT ME!
         *
         * @throws WmsException DOCUMENT ME!
         */
        public void produceMap(WMSMapContext map) throws WmsException {
        }

        /**
         * DOCUMENT ME!
         *
         * @param out DOCUMENT ME!
         *
         * @throws ServiceException DOCUMENT ME!
         * @throws IOException DOCUMENT ME!
         */
        public void writeTo(OutputStream out)
            throws ServiceException, IOException {
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
    }
}
