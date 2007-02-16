/**
 *
 */
package org.vfny.geoserver.wms.responses.map.geotiff;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;
import java.util.Collections;
import java.util.Map;
import java.util.Set;


/**
 *
DOCUMENT ME!
 *
 * @author Simone Giannecchini
 */
public class GeoTiffMapProducerFactory implements GetMapProducerFactorySpi {
    /** the only MIME type this map producer supports */
    static final String MIME_TYPE = "image/geotiff";

    /**
     * convenient singleton Set to expose the output format this
     * producer supports
     */
    private static final Set SUPPORTED_FORMATS = Collections.singleton(MIME_TYPE);

    /**
         *
         */
    public GeoTiffMapProducerFactory() {
        super();
    }

    public String getName() {
        return "Geographic tagged image format";
    }

    public Set getSupportedFormats() {
        return SUPPORTED_FORMATS;
    }

    public boolean isAvailable() {
        return true;
    }

    public boolean canProduce(String mapFormat) {
        return MIME_TYPE.equals(mapFormat);
    }

    public GetMapProducer createMapProducer(String mapFormat, WMS wms)
        throws IllegalArgumentException {
        if (!canProduce(mapFormat)) {
            throw new IllegalArgumentException(mapFormat + " not supported by this map producer");
        }

        return new GeoTiffMapProducer("image/geotiff", wms);
    }

    /* (non-Javadoc)
     * @see org.geotools.factory.Factory#getImplementationHints()
     * This just returns java.util.Collections.EMPTY_MAP
     */
    public Map getImplementationHints() {
        return java.util.Collections.EMPTY_MAP;
    }
}
