package org.vfny.geoserver.wms.responses.map.kml;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;
import java.util.logging.Logger;


public class GeoSearchMapProducerFactory implements GetMapProducerFactorySpi {

    Logger LOG = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");

    public GeoSearchMapProducerFactory(){
        LOG.info("Created a " + getClass() + ".");

    }

    public static String BASE_URL;

    /**
     * this is just to check the requested mime type starts with this string,
     * since the most common error when performing the HTTP request is not to
     * escape the '+' sign in "kml+xml", which is decoded as a space
     * character at server side.
     */
    private static final String PRODUCE_TYPE = "geosearch-kml";

    /** Official KML mime type
     * @TODO add KMZ support
     */
    public static final String MIME_TYPE = "application/vnd.google-earth.kml+xml";

    /** Format name
    */
    public static final String FORMAT = "application/kml";

    /** Set of supported mime types for the producers made by this Factory
    */
    private static final Set SUPPORTED_FORMATS = Collections.singleton(PRODUCE_TYPE);

    public boolean canProduce(String mapFormat) {
        return PRODUCE_TYPE.equals(mapFormat) || MIME_TYPE.equals(mapFormat)
            || mapFormat.startsWith(FORMAT);
    }

    public GetMapProducer createMapProducer(String mapFormat, WMS wms)
        throws IllegalArgumentException {
            if (canProduce(mapFormat)) {
                return new GeoSearchMapProducer(mapFormat, MIME_TYPE);
            } else {
                System.out.println("Can't produce GeoSearchMapProducer!");
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
