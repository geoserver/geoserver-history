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
import org.geoserver.wms.response.GetFeatureInfoOutputFormat;
import org.springframework.context.ApplicationContext;
import org.vfny.geoserver.wms.GetLegendGraphicProducer;
import org.vfny.geoserver.wms.GetMapProducer;

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
     * Finds out a {@link GetMapProducer} specialized in generating the requested map format,
     * registered in the spring context.
     * 
     * @param outputFormat
     *            a request parameter object wich holds the processed request objects, such as
     *            layers, bbox, outpu format, etc.
     * 
     * @return A specialization of <code>GetMapDelegate</code> wich can produce the requested output
     *         map format, or {@code null} if none is found
     */
    public static GetMapProducer findMapProducer(final String outputFormat,
            final ApplicationContext applicationContext) {

        final Collection<GetMapProducer> producers;
        producers = WMSExtensions.findMapProducers(applicationContext);

        return findMapProducer(outputFormat, producers);
    }

    /**
     * @return {@link GetMapProducer} for the requested outputFormat, or {@code null}
     */
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

    /**
     * @return the configured {@link GetFeatureInfoOutputFormat}s
     */
    public static List<GetFeatureInfoOutputFormat> findFeatureInfoFormats(
            ApplicationContext applicationContext) {
        return GeoServerExtensions.extensions(GetFeatureInfoOutputFormat.class, applicationContext);
    }

    public static GetLegendGraphicProducer findLegendGraphicFormat(final String outputFormat,
            final ApplicationContext applicationContext) {

        List<GetLegendGraphicProducer> formats = findLegendGraphicFormats(applicationContext);

        for (GetLegendGraphicProducer format : formats) {
            if (format.getContentType().startsWith(outputFormat)) {
                return format;
            }
        }
        return null;
    }

    public static List<GetLegendGraphicProducer> findLegendGraphicFormats(
            final ApplicationContext applicationContext) {
        List<GetLegendGraphicProducer> formats = GeoServerExtensions.extensions(
                GetLegendGraphicProducer.class, applicationContext);
        return formats;
    }
}
