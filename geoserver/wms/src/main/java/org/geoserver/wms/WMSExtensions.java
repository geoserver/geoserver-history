/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import java.util.List;

import org.geoserver.platform.GeoServerExtensions;
import org.springframework.context.ApplicationContext;
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
}
