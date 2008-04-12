package org.vfny.geoserver.wms.responses.map.kml;

import org.opengis.feature.simple.SimpleFeature;

/**
 * Common interface for classes defining a mechanism for regionating KML placemarks.  
 * @author David Winslow
 */
public interface RegionatingStrategy {
    /**
     * Decide whether a feature should be displayed at a particular scale.
     * @param scaleDenominator the scale denominator for the tile being generated
     * @param feature the SimpleFeature to consider for inclusion
     * @return true if the feature should be displayed, false otherwise.
     */
    public boolean include(double scaleDenominator, SimpleFeature feature);
}
