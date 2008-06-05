package org.vfny.geoserver.wms.responses.map.kml;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.FeatureSource;
import java.util.Comparator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.opengis.filter.FilterFactory;
import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geoserver.ows.HttpErrorCodeException;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.geotools.map.MapLayer;

import com.vividsolutions.jts.geom.Geometry;


public class GeometryRegionatingStrategy extends CachedHierarchyRegionatingStrategy{
    Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");

    protected String findCacheTable(WMSMapContext con, MapLayer layer){
        return super.findCacheTable(con, layer) + "_geometry_regionator";
    }

    public Comparator<SimpleFeature> getComparator(){
        return new GeometryComparator();
    }

    private class GeometryComparator implements Comparator<SimpleFeature> {
        public int compare(SimpleFeature a, SimpleFeature b){
            double valueA = findValue(a);
            double valueB = findValue(b);
            
            return (int)Math.signum(valueA - valueB);
        }
        
        private double findValue(SimpleFeature f){
            Geometry geom = (Geometry)f.getDefaultGeometry();
            
            double area = geom.getArea();
            if (area > 0) return area;
            
            return geom.getLength();
        }
    }
}

