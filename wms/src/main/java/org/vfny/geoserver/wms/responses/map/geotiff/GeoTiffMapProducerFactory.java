/**
 *
 */
package org.vfny.geoserver.wms.responses.map.geotiff;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;


/**
 * {@link GetMapProducerFactorySpi} for producing images in geotiff format.
 *
 * @author Simone Giannecchini, GeoSolutions
 */
public class GeoTiffMapProducerFactory implements GetMapProducerFactorySpi {
    /** the only MIME type this map producer supports */
    static final String MIME_TYPE = "image/tiff";

    /**
     * convenient singleton Set to expose the output format this producer
     * supports
     */
    private static final Set SUPPORTED_FORMATS;

    static {
        SUPPORTED_FORMATS = new HashSet(2);
        SUPPORTED_FORMATS.add("image/geotiff");
        SUPPORTED_FORMATS.add("image/geotiff8");
    }
    /**
    /**
     * Default constructor.
     */
    public GeoTiffMapProducerFactory() {
    }

    public String getName() {
        return "Geographic tagged image format";
    }

    public Set getSupportedFormats() {
        return SUPPORTED_FORMATS;
    }

    public boolean isAvailable() {
        try {
            Class.forName("com.sun.media.imageio.plugins.tiff.GeoTIFFTagSet");

            return true;
        } catch (Exception e) {
        }

        return false;
    }

    public boolean canProduce(String mapFormat) {
        return SUPPORTED_FORMATS.contains(mapFormat);
    }

    public GetMapProducer createMapProducer(String mapFormat, WMS wms)
        throws IllegalArgumentException {
        if (!canProduce(mapFormat)) {
            throw new IllegalArgumentException(mapFormat + " not supported by this map producer");
        }

        return new GeoTiffMapProducer(mapFormat, MIME_TYPE, wms);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.factory.Factory#getImplementationHints() This just
     *      returns java.util.Collections.EMPTY_MAP
     */
    public Map getImplementationHints() {
        return java.util.Collections.EMPTY_MAP;
    }
}
