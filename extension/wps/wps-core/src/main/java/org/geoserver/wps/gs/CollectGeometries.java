/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wps.gs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geoserver.wps.jts.DescribeParameter;
import org.geoserver.wps.jts.DescribeProcess;
import org.geoserver.wps.jts.DescribeResult;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Collects all geometries from the specified vector layer into a single GeometryCollection (or
 * specialized subclass of it in case the geometries are uniform)
 * 
 * @author Andrea Aime - GeoSolutions
 */
@DescribeProcess(title = "collectGeometries", description = "Collects all the default geometries in the feature collection and returns them as a single geometry collection")
public class CollectGeometries implements GeoServerProcess {

    @DescribeResult(name = "result", description = "The reprojected features")
    public GeometryCollection execute(
            @DescribeParameter(name = "features", description = "The feature collection whose geometries will be collected") FeatureCollection features,
            ProgressListener progressListener) throws IOException {
        int count = features.size();
        float done = 0;

        List<Geometry> result = new ArrayList<Geometry>();
        FeatureIterator fi = null;
        try {
            fi = features.features();
            while(fi.hasNext()) {
                Geometry g = (Geometry) fi.next().getDefaultGeometryProperty().getValue();
                collect(g, result);
    
                // progress notification
                done++;
                if (progressListener != null && done % 100 == 0) {
                    progressListener.progress(done / count);
                }
            }
        } finally {
            if (fi != null) {
                fi.close();
            }
        }
        
        Class collectionClass = guessCollectionType(result);
        GeometryFactory gf = new GeometryFactory();
        if(collectionClass == MultiPoint.class) {
            Point[] array = (Point[]) result.toArray(new Point[result.size()]);
            return gf.createMultiPoint(array);
        } else if(collectionClass == MultiPolygon.class) {
            Polygon[] array = (Polygon[]) result.toArray(new Polygon[result.size()]);
            return gf.createMultiPolygon(array);
        } else if(collectionClass == MultiLineString.class) {
            LineString[] array = (LineString[]) result.toArray(new LineString[result.size()]);
            return gf.createMultiLineString(array);
        } else {
            Geometry[] array = (Geometry[]) result.toArray(new Geometry[result.size()]);
            return gf.createGeometryCollection(array);
        }
    }

    private Class guessCollectionType(List<Geometry> geometries) {
        // empty set? then we'll return an empty point collection
        if(geometries == null || geometries.size() == 0) {
            return GeometryCollection.class;
        }
        
        // see if all are of the same base geometric type
        Class result = baseType(geometries.get(0).getClass());
        for (int i = 1; i < geometries.size(); i++) {
            Class curr = geometries.get(i).getClass();
            if(curr != result && !(result.isAssignableFrom(curr))) {
                return GeometryCollection.class;
            }
        }
        
        // return the geometric collection associated with the base type
        if(result == Point.class) {
            return MultiPoint.class;
        } else if(result == LineString.class) {
            return MultiLineString.class;
        } else if(result == Polygon.class) {
            return MultiPolygon.class;
        } else {
            return GeometryCollection.class;
        }
            
    }
    
    private Class baseType(Class geometry) {
        if(Polygon.class.isAssignableFrom(geometry)) {
            return Polygon.class;
        } else if(LineString.class.isAssignableFrom(geometry)) {
            return LineString.class;
        } else if(Point.class.isAssignableFrom(geometry)) { 
            return Point.class;
        } else {
            return geometry;
        }
    }

    /**
     * Adds non null geometries to the results, flattening geometry collections in the process
     * 
     * @param g
     * @param result
     */
    void collect(Geometry g, List<Geometry> result) {
        if (g == null) {
            return;
        } else if (g instanceof GeometryCollection) {
            GeometryCollection gc = (GeometryCollection) g;
            for (int i = 0; i < gc.getNumGeometries(); i++) {
                collect(gc.getGeometryN(i), result);
            }
        } else {
            result.add(g);
        }
    }
}
