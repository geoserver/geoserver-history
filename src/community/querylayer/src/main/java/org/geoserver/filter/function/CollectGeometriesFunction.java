/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 */
package org.geoserver.filter.function;

import java.util.Iterator;
import java.util.List;

import org.geotools.filter.FunctionImpl;
import org.geotools.geometry.jts.GeometryCollector;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Collects all geometries provided in a list into a single {@link GeometryCollection} object (a
 * type specific subclass of it if possible)
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 */
public class CollectGeometriesFunction extends FunctionImpl {

    long maxCoordinates;

    public CollectGeometriesFunction(String name, List<Expression> args, Literal fallback, long maxCoordinates) {
        setName(name);
        setFallbackValue(fallback);
        setParameters(args);
        this.maxCoordinates = maxCoordinates;

        if (args.size() != 1) {
            throw new IllegalArgumentException("CollectGeometries function requires a single"
                    + " argument, a collection of geometries");
        }
    }

    public Object evaluate(Object object) {
        List geometries = getParameters().get(0).evaluate(object, List.class);
        if (geometries == null || geometries.size() == 0) {
            return new GeometryCollection(null, new GeometryFactory());
        }

        // collect but don't clone, unfortunalely we're already stuck with a list, by cloning
        // we'd just increase memory usage
        GeometryCollector collector = new GeometryCollector();
        collector.setFactory(null);
        collector.setMaxCoordinates(maxCoordinates);
        for (Iterator it = geometries.iterator(); it.hasNext();) {
            Geometry geometry = (Geometry) it.next();
            collector.add(geometry);
        }

        return collector.collect();
    }

}
