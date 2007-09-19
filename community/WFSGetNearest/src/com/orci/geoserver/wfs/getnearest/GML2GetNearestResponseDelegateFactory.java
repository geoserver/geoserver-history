/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package com.orci.geoserver.wfs.getnearest;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 *
 */
public class GML2GetNearestResponseDelegateFactory implements GetNearestResponseDelegateProducerSpi {
    static HashSet supportedFormats = new HashSet();

    static {
        supportedFormats.add(GML2GetNearestResponseDelegate.formatName); // eg. GML2
        supportedFormats.add(GML2GetNearestResponseDelegate.formatNameCompressed); // eg. GML2-GZIP
    }

    /**
             * Creates a new GifMapProducerFactory object.
             */
    public GML2GetNearestResponseDelegateFactory() {
        super();
    }

    /**
     * see interface def'n
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "GML2 Ouput (uncompressed or gziped)";
    }

    /**
     * Returns the Set of output format this producer supports
     *
     * @return
     */
    public Set getSupportedFormats() {
        return supportedFormats;
    }

    /**
     * DOCUMENT ME!
     *
     * @return <code>true</code>
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * Returns wether the  producers created by this factory can create
     * output in the passed output format.
     *
     * @param outputFormat a MIME type string to check if this producer is able
     *        to handle.
     *
     * @return <code>true</code>  or false
     */
    public boolean canProduce(String outputFormat) {
        return GML2GetNearestResponseDelegate.formatName.equalsIgnoreCase(outputFormat)
        || GML2GetNearestResponseDelegate.formatNameCompressed.equalsIgnoreCase(outputFormat);
    }

    /* (non-Javadoc)
     * @see org.geotools.factory.Factory#getImplementationHints()
     * This just returns java.util.Collections.EMPTY_MAP
     */
    public Map getImplementationHints() {
        return java.util.Collections.EMPTY_MAP;
    }

    /* (non-Javadoc)
     * @see org.vfny.geoserver.wfs.GetNearestResponseDelegateProducerSpi#createFeatureDelegateProducer(java.lang.String)
     */
    public GetNearestResponseDelegate createFeatureDelegateProducer(String format)
        throws IllegalArgumentException {
        if (canProduce(format)) {
            return new GML2GetNearestResponseDelegate();
        }

        throw new IllegalArgumentException("cannot produce " + format);
    }
}
