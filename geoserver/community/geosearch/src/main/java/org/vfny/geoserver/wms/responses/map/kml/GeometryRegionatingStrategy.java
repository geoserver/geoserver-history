package org.vfny.geoserver.wms.responses.map.kml;

import java.util.logging.Logger;

import org.opengis.feature.simple.SimpleFeature;
import org.vfny.geoserver.wms.WMSMapContext;
import org.geotools.map.MapLayer;

import com.vividsolutions.jts.geom.Geometry;

public class GeometryRegionatingStrategy implements RegionatingStrategy{

    Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");

    public void preProcess(WMSMapContext con, MapLayer layer){
        // dummied out. For now...
    }

    public boolean include(SimpleFeature feature){
        Geometry geom = (Geometry)feature.getDefaultGeometry();

        return geom.getLength() > 0.01;
    }
}
