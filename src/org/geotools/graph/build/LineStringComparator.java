/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geotools.graph.build;


import org.geotools.feature.Feature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;



/**
 * An implentation of FeatureComparator used to compare Line Strings.
 */
public class LineStringComparator extends FeatureComparator {
    /** DOCUMENT ME!  */
    public static final int TIP_TO_TAIL = 0;

    /** DOCUMENT ME!  */
    public static final int TAIL_TO_TIP = 1;

    /** DOCUMENT ME!  */
    public static final int TIP_TO_TIP = 2;

    /** DOCUMENT ME!  */
    public static final int TAIL_TO_TAIL = 3;

    /**
     * Creates a new LineStringComparator object.
     */
    public LineStringComparator() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param f1 DOCUMENT ME!
     * @param f2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare(Feature f1, Feature f2) {
        LineString ls1 = (LineString) f1.getDefaultGeometry();
        LineString ls2 = (LineString) f2.getDefaultGeometry();

        Coordinate f1first = ls1.getCoordinateN(0);
        Coordinate f1last = ls1.getCoordinateN(ls1.getNumPoints() - 1);
        Coordinate f2first = ls2.getCoordinateN(0);
        Coordinate f2last = ls2.getCoordinateN(ls2.getNumPoints() - 1);

        if (f1last.equals2D(f2first)) {
            return (TIP_TO_TAIL);
        }

        if (f1first.equals2D(f2last)) {
            return (TAIL_TO_TIP);
        }

        if (f1last.equals2D(f2last)) {
            return (TIP_TO_TIP);
        }

        if (f1first.equals2D(f2first)) {
            return (TAIL_TO_TAIL);
        }

        return (-1); //no relationship
    }
}
