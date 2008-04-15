package org.vfny.geoserver.wms.responses.map.kml;

import org.opengis.feature.simple.SimpleFeature;
import org.vfny.geoserver.wms.WMSMapContext;

/**
 * Common interface for classes defining a mechanism for regionating KML placemarks.  
 * @author David Winslow
 */
public interface RegionatingStrategy {
    /**
     * Many (most?) regionating strategies need some global information about the dataset, this
     * method provides a common way for them to acquire it.  Strategies can assume that the
     * provided FeatureCollection contains all elements in the dataset, NOT just the ones matched
     * by the bbox filter on the request.
     * @param collection a FeatureCollection containing the entire dataset
     */
    public void preProcess(WMSMapContext context, int layerIndex);

    /**
     * Decide whether a feature should be displayed at a particular scale.  This method should 
     * not worry about the bbox, just whether the feature should be included at the current zoomlevel.
     * @param feature the SimpleFeature to consider for inclusion
     * @return true if the feature should be displayed, false otherwise.
     */
    public boolean include(SimpleFeature feature);
}
