/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geotools.graph.build;


import org.geotools.feature.Feature;

import com.vividsolutions.jts.geom.Polygon;



/**
 * An implentation of FeatureComparator used to compare Polygons.
 */
public class PolygonComparator extends FeatureComparator {
    /**
     * Implements the touches relationship
     * <p>
     * Touches is a bidirecitonal relationship - so the order in which the
     * features are added into the graph should not effect the resulting
     * outcome.
     * </p>
     * @param f1 DOCUMENT ME!
     * @param f2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare(Feature f1, Feature f2) {
        Polygon p1 = (Polygon) f1.getDefaultGeometry();
        Polygon p2 = (Polygon) f2.getDefaultGeometry();

        if (p1.touches(p2)) {
            return (1);
        }

        return (NO_RELATIONSHIP);
    }
}
