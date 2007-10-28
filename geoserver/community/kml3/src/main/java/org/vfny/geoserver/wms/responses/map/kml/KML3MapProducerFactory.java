package org.vfny.geoserver.wms.responses.map.kml;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;


public class KML3MapProducerFactory implements GetMapProducerFactorySpi {
    
    
    /**
     * this is just to check the requested mime type starts with this string,
     * since the most common error when performing the HTTP request is not to
     * escape the '+' sign in "kml+xml", which is decoded as a space
     * character at server side.
     */
    private static final String PRODUCE_TYPE = "kml3";
    
    /** Official KML mime type
     * @TODO add KMZ support
     */
    public static final String MIME_TYPE = "application/kml+xml";

    /** Set of supported mime types for the producers made by this Factory
     */
    private static final Set SUPPORTED_FORMATS = Collections.singleton(PRODUCE_TYPE);

    public boolean canProduce(String mapFormat) {
        return PRODUCE_TYPE.equals(mapFormat) || MIME_TYPE.equals(mapFormat)
	|| MIME_TYPE.startsWith("application/kml");
    }

    public GetMapProducer createMapProducer(String mapFormat, WMS wms)
            throws IllegalArgumentException {
        if (canProduce(mapFormat)) {
            return new KML3MapProducer(mapFormat, MIME_TYPE);
        }

        throw new IllegalArgumentException("Unable to produce format " + mapFormat);
    }

    public String getName() {
        return "Experimental Keyhole Markup Language producer";
    }

    public Set getSupportedFormats() {
        return SUPPORTED_FORMATS;
    }

    public boolean isAvailable() {
        return true;
    }

    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }
    
}
