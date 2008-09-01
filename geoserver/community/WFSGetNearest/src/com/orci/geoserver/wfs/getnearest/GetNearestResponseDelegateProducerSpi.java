/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package com.orci.geoserver.wfs.getnearest;

import org.geotools.factory.Factory;
import java.util.Set;


public interface GetNearestResponseDelegateProducerSpi extends Factory {
    /**
     * Returns a descriptive name for the factory instance.
     *
     * @return a descriptive name for the factory instance
     */
    String getName();

    /**
     * Returns a <code>java.util.Set&lt;String&gt;</code> of the MIME
     * types the map producers this factory can create are able to handle.
     *
     * @return the Set of supported output image mime types.
     */
    Set getSupportedFormats();

    /**
     * Checks if the GetNearestResponseDelegate instances this factory
     * serves will be able of working properly (e.g., external dependencies
     * are in place). This method should be used to avoid asking for producer
     * instances if they are likely to fail.
     *
     * @return wether this factory is able to produce producer instances.
     */
    boolean isAvailable();

    /**
     * Returns wether the GetNearestResponseDelegate created by this
     * factory can create output in the specified output format.
     *
     * @param format a MIME type string to check if this producer is able to
     *        handle.
     *
     * @return <code>true</code> if <code>mimeType</code> is an  format
     *         supported by the producers this factory serves.
     */
    boolean canProduce(String format);

    /**
     * Creates and instance of a FeatureDelegateProducer suitable to
     * create output in the specified  format.
     *
     * @param format the MIME type of the desired image
     *
     * @return a FeatureDelegateProducer capable of creating putput in
     *         <code>format</code> format.
     *
     * @throws IllegalArgumentException if <code>format</code> is not one of
     *         the MIME types this producer can create images in.
     */
    GetNearestResponseDelegate createFeatureDelegateProducer(String format)
        throws IllegalArgumentException;
}
