/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.georss;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class GeoRSSMapProducerFactory implements GetMapProducerFactorySpi {
    static Set formats = new HashSet();

    static {
        formats.add(AtomGeoRSSMapProducer.MIME_TYPE);
        formats.add(RSSGeoRSSMapProducer.MIME_TYPE);
    }

    public boolean canProduce(String mapFormat) {
        return formats.contains(mapFormat);
    }

    public GetMapProducer createMapProducer(String mapFormat, WMS wms)
        throws IllegalArgumentException {
        if (AtomGeoRSSMapProducer.MIME_TYPE.equals(mapFormat)
                || AtomGeoRSSMapProducer.FORMAT.equals(mapFormat)) {
            return new AtomGeoRSSMapProducer();
        }

        if (RSSGeoRSSMapProducer.MIME_TYPE.equals(mapFormat)
                || RSSGeoRSSMapProducer.FORMAT.equals(mapFormat)) {
            return new RSSGeoRSSMapProducer();
        }

        return null;
    }

    public String getName() {
        return "GeoRSS";
    }

    public Set getSupportedFormats() {
        return formats;
    }

    public boolean isAvailable() {
        return true;
    }

    public Map getImplementationHints() {
        return null;
    }
}
