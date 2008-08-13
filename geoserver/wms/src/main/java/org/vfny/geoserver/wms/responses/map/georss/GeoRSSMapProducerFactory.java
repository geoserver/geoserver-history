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

/**
 * 
 * @version $Id$
 * @deprecated to be removed and register the GetMapProducers directly through spring context
 */
public class GeoRSSMapProducerFactory implements GetMapProducerFactorySpi {
    static Set formats = new HashSet();
    static Set aliases = new HashSet();
    
    static {
        formats.add(AtomGeoRSSMapProducer.MIME_TYPE);
        formats.add(RSSGeoRSSMapProducer.MIME_TYPE2);
        
        aliases.addAll(AtomGeoRSSMapProducer.FORMATS);
        aliases.addAll(RSSGeoRSSMapProducer.FORMATS);
    }

    public GeoRSSMapProducerFactory() {
	}

	public boolean canProduce(String mapFormat) {
        return formats.contains(mapFormat) || aliases.contains(mapFormat);
    }

    public GetMapProducer createMapProducer(String mapFormat, WMS wms)
        throws IllegalArgumentException {
        if (AtomGeoRSSMapProducer.MIME_TYPE.equals(mapFormat)
                || AtomGeoRSSMapProducer.FORMATS.contains(mapFormat)) {
            return new AtomGeoRSSMapProducer(mapFormat);
        }

        if (RSSGeoRSSMapProducer.MIME_TYPE2.equals(mapFormat)
                || RSSGeoRSSMapProducer.FORMATS.contains(mapFormat)) {
            return new RSSGeoRSSMapProducer(mapFormat);
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
