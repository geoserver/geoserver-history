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

/**
 * Collects all geometries from the specified vector layer into a single GeometryCollection
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

        Geometry[] array = (Geometry[]) result.toArray(new Geometry[result.size()]);
        return new GeometryFactory().createGeometryCollection(array);
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
