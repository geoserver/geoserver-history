package org.vfny.geoserver.wms.responses.map.kml;

import org.opengis.feature.simple.SimpleFeature;
import java.util.logging.Logger;
import com.vividsolutions.jts.geom.Geometry;

public class GeometryRegionatingStrategy implements RegionatingStrategy{

    Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");
    public boolean include(double scaleDenominator, SimpleFeature feature){
        Geometry geom = (Geometry)feature.getDefaultGeometry();

        return geom.getLength() > 0.01;
    }
}
