/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms;

import java.util.Set;

import org.geotools.factory.Factory;


/**
 * Constructs a live GetLegendGraphicProducer.
 *
 * <p>
 * An instance of this interface should exist for all legend producers which
 * want to take advantage of the dynamic plugin system. In addition to
 * implementing this interface GetLegendGraphic producers should have a
 * services file:
 * </p>
 *
 * <p>
 * <code>org.vfny.geoserver.responses.wms.GetLegendGraphicProducerSpi</code>
 * </p>
 *
 * <p>
 * The file should contain a single line which gives the full name of the
 * implementing class.
 * </p>
 *
 * <p>
 * example:<br/><code>e.g.
 * org.vfny.geoserver.responses.wms.GIFLegendGraphicProducerSpi</code>
 * </p>
 *
 * <p>
 * The factories are never called directly by client code, instead the
 * GeoTools' FactoryFinder class is used.
 * </p>
 *
 * <p>
 * The following example shows how a user might obtain GetLegendGraphicProducer
 * capable of generating a legend graphic image in GIF format and send the
 * generated legend to a file:
 * </p>
 *
 * <p>
 * <pre><code>
 *  GetLegendGraphicProducerSpi glf = null;
 *  Iterator it = FactoryFinder.factories(GetLegendGraphicProducerSpi.class);
 *  while (it.hasNext()) {
 *          GetLegendGraphicProducerSpi tmpGlf = (GetLegendGraphicProducerSpi) it.next();
 *          if (tmpGlf.canProduce("image/gif")) {
 *                  glf = tmpGlf;
 *                  break;
 *          }
 *  }
 *  GetLegendGraphicProducer producer = glf.createLegendProducer("image/gif");
 *         GetLegendGraphicRequest request = ...
 *  producer.produceLegendGraphic(request);
 *  OutputStream out = new FileOutputStream("/legend.gif");
 *  producer.writeTo(out);
 * </code></pre>
 * </p>
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Revision: 1.1 $
 */
public interface GetLegendGraphicProducerSpi extends Factory {
    /**
     * Returns a descriptive name for the factory instance.
     *
     * @return a descriptive name for the factory instance
     */
    String getName();

    /**
     * Returns a <code>java.util.Set&lt;String&gt;</code> of the MIME types the
     * legend producers this factory can create are able to handle.
     *
     * @return the Set of supported output image mime types.
     */
    Set getSupportedFormats();

    /**
     * Checks if the GetLegendGraphicProducer instances this factory serves
     * will be able of working properly (e.g., external dependencies are in
     * place). This method should be used to avoid asking for producer
     * instances if they are likely to fail.
     *
     * @return wether this factory is able to produce producer instances.
     */
    boolean isAvailable();

    /**
     * Returns wether the legend producers created by this factory can create
     * legend graphics in the specified output format.
     *
     * @param mimeType a MIME type string to check if this producer is able to
     *        handle.
     *
     * @return <code>true</code> if <code>mimeType</code> is an image format
     *         supported by the producers this factory serves.
     */
    boolean canProduce(String mimeType);

    /**
     * Creates and instance of a GetLegendGraphicProducer suitable to create
     * legend graphics in the specified image format.
     *
     * @param format the MIME type of the desired image
     *
     * @return a GetLegendGraphicProducer capable of creating legends in
     *         <code>format</code> image format.
     *
     * @throws IllegalArgumentException if <code>format</code> is not one of
     *         the MIME types this producer can create images in.
     */
    GetLegendGraphicProducer createLegendProducer(String format)
        throws IllegalArgumentException;
}
