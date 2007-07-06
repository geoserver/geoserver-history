/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.responses;

import org.vfny.geoserver.wfs.FeatureResponseDelegateProducerSpi;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 *
 */
public class JSONFeatureResponseDelegateFactory implements FeatureResponseDelegateProducerSpi {
    static HashSet supportedFormats = new HashSet();

    static {
        supportedFormats.add(JSONFeatureResponseDelegate.formatName); // eg. JSON
    }

    /**
     * Creates a new JSONFeatureResponseDelegateFactory object.
     */
    public JSONFeatureResponseDelegateFactory() {
        super();
    }

    /**
     * see interface def'n
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "JSON Feature Output";
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
     * Returns wether the  producers created by this factory can create output
     * in the passed output format.
     *
     * @param outputFormat a MIME type string to check if this producer is able
     *        to handle.
     *
     * @return <code>true</code>  or false
     */
    public boolean canProduce(String outputFormat) {
        return JSONFeatureResponseDelegate.formatName.equalsIgnoreCase(outputFormat);
    }

    /* (non-Javadoc)
     * @see org.geotools.factory.Factory#getImplementationHints()
     * This just returns java.util.Collections.EMPTY_MAP
     */
    public Map getImplementationHints() {
        return java.util.Collections.EMPTY_MAP;
    }

    /* (non-Javadoc)
     * @see org.vfny.geoserver.wfs.FeatureResponseDelegateProducerSpi#createFeatureDelegateProducer(java.lang.String)
     */
    public FeatureResponseDelegate createFeatureDelegateProducer(String format)
        throws IllegalArgumentException {
        if (canProduce(format)) {
            return new JSONFeatureResponseDelegate();
        }

        throw new IllegalArgumentException("cannot produce " + format);
    }
}
