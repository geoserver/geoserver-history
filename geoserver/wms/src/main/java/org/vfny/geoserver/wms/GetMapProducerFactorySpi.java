/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms;

import org.geotools.factory.Factory;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.global.WMS;
import java.util.Set;


/**
 * Constructs a live GetMapProducer.
 *
 * <p>
 * An instance of this interface should exist for all map producers which want
 * to take advantage of the dynamic plugin system. In addition to implementing
 * this interface GetMap producers should have a services file:
 * </p>
 *
 * <p>
 * <code>org.vfny.geoserver.responses.wms.GetMapProducerFactorySpi</code>
 * </p>
 *
 * <p>
 * The file should contain a single line which gives the full name of the
 * implementing class.
 * </p>
 *
 * <p>
 * example:<br/><code>e.g.
 * org.vfny.geoserver.wms.GIFLegendGraphicProducerSpi</code>
 * </p>
 *
 * <p>
 * The factories are never called directly by client code, instead the
 * GeoTools' FactoryFinder class is used.
 * </p>
 *
 * <p>
 * The following example shows how a user might obtain GetMapProducer capable
 * of generating a map image in GIF format and send the generated legend to a
 * file:
 * </p>
 *
 * <p>
 * <pre><code>
 *         WMSConfig config = getServletContext().getAttribute(WMSConfig.CONFIG_KEY);
 *
 *  GetMapProducerSpi gmpf = null;
 *  Iterator it = FactoryFinder.factories(GeMapProducerFactorySpi.class);
 *  while (it.hasNext()) {
 *          GeMapProducerFactorySpi tmpGmpf = (GeMapProducerFactorySpi) it.next();
 *          if (tmpGmpf.canProduce("image/gif")) {
 *                  gmpf = tmpGmpf;
 *                  break;
 *          }
 *  }
 *  ...
 *  GetMapProducer producer = gmpf.createMapProducer("image/gif");
 *         WMSMapContext ctx = ...
 *  producer.produceMap(ctx);
 *  OutputStream out = new FileOutputStream("/map.gif");
 *  producer.writeTo(out);
 * </code></pre>
 * </p>
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 * @deprecated to be removed and register the GetMapProducers directly through spring context
 */
public interface GetMapProducerFactorySpi extends Factory {
    /**
     * Returns a descriptive name for the factory instance.
     *
     * @return a descriptive name for the factory instance
     */
    String getName();

    /**
     * Returns a <code>java.util.Set&lt;String&gt;</code> of the MIME types the
     * map producers this factory can create are able to handle.
     *
     * @return the Set of supported output image mime types.
     */
    Set getSupportedFormats();

    /**
     * Checks if the GetMapProducer instances this factory serves will be able
     * of working properly (e.g., external dependencies are in place). This
     * method should be used to avoid asking for producer instances if they
     * are likely to fail.
     *
     * @return wether this factory is able to produce producer instances.
     */
    boolean isAvailable();

    /**
     * Returns wether the legend producers created by this factory can create
     * map images in the specified output format.
     *
     * @param mapFormat a MIME type string to check if this producer is able to
     *        handle.
     *
     * @return <code>true</code> if <code>mimeType</code> is an image format
     *         supported by the producers this factory serves.
     */
    boolean canProduce(String mapFormat);

    /**
     * Creates and instance of a GetMapProducer suitable to create map images
     * in the specified image format.
     *
     * @param mapFormat the MIME type of the desired image
     * @param wms the WMS module
     *
     * @return a GetMapProducer capable of creating maps in <code>format</code>
     *         image format.
     *
     * @throws IllegalArgumentException if <code>format</code> is not one of
     *         the MIME types this producer can create images in.
     */
    GetMapProducer createMapProducer(String mapFormat, WMS wms)
        throws IllegalArgumentException;
}
