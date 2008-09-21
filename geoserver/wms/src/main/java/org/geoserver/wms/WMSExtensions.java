/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.geoserver.platform.GeoServerExtensions;
import org.springframework.context.ApplicationContext;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WmsException;

/**
 * Utility class uses to process GeoServer WMS extension points.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 */
public class WMSExtensions {

    /**
     * Finds out the registered GetMapProducers in the application context.
     */
    public static List<GetMapProducer> findMapProducers(final ApplicationContext context) {
        return GeoServerExtensions.extensions(GetMapProducer.class, context);
    }

    /**
     * Finds out a {@link GetMapProducer} specialized in generating the
     * requested map format, registered in the spring context.
     * 
     * @param outputFormat
     *            a request parameter object wich holds the processed request
     *            objects, such as layers, bbox, outpu format, etc.
     * 
     * @return A specialization of <code>GetMapDelegate</code> wich can
     *         produce the requested output map format, or {@code null} if none
     *         is found
     * 
     * @throws WmsException
     *             if no specialization is configured for the output format
     *             specified in <code>request</code> or if it can't be
     *             instantiated
     */
    public static GetMapProducer findMapProducer(final String outputFormat,
            final ApplicationContext applicationContext) {
        final Collection<GetMapProducer> producers;
        producers = WMSExtensions.findMapProducers(applicationContext);

        return findMapProducer(outputFormat, producers);
    }

    public static GetMapProducer findMapProducer(String outputFormat,
            Collection<GetMapProducer> producers) {

        Set<String> producerFormats;
        for (GetMapProducer producer : producers) {
            producerFormats = producer.getOutputFormatNames();
            Set<String> caseInsensitiveFormats = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            caseInsensitiveFormats.addAll(producerFormats);
            if (caseInsensitiveFormats.contains(outputFormat)) {
                return producer;
            }
        }
        return null;
    }
}
