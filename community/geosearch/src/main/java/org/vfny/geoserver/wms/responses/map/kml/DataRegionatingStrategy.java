package org.vfny.geoserver.wms.responses.map.kml;

import org.opengis.feature.simple.SimpleFeature;
import java.util.logging.Logger;

/**
 * Strategy for regionating based on algorithmic stuff related to the actual data.
 * This strategy is fairly ill-defined and should be considered highly experimental. 
 * 
 * @author David Winslow
 */
public class DataRegionatingStrategy implements RegionatingStrategy{

    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");

    public boolean include(double scaleDenominator, SimpleFeature feature){
        try{
            Object att = feature.getAttribute("cat");
            long l = ((Long) att).longValue();
            
            return l > 4;
        } catch (Exception e){
            LOGGER.info(e.getMessage());
        }
        return false;
    }

}
