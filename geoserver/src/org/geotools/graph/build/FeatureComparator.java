/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geotools.graph.build;


import java.util.Comparator;

import org.geotools.feature.Feature;


/**
 * A FeatureComparator compares two features to one another.
 */
public abstract class FeatureComparator implements Comparator {
    /** constant NO_RELATIONSHIP */
    public static final int NO_RELATIONSHIP = -1;

    /**
     * Calls compare(Feature,Feature).
     *
     * @see Comparator#compare(Object, Object)
     */
    public int compare(Object o1, Object o2) {
        return (compare((Feature) o1, (Feature) o2));
    }

    /**
     * Compares one Feature to another.
     *
     * @param f1
     * @param f2
     *
     * @return An integer describing the relationship betweent the two
     *         features.
     */
    public abstract int compare(Feature f1, Feature f2);
}
