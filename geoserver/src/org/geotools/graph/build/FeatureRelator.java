/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geotools.graph.build;

import org.geotools.feature.Feature;




/**
 * A FeatureRelator determines if there is logical relationship between two
 * features at the graph level. A FeatureRelator uses a  {@link
 * FeatureComparator} to determine the kind of relationship present  between
 * two Features, and then determines if this relationship should  be
 * represented explicitly in thh graph.
 */
public abstract class FeatureRelator {
    /** FeatureComparator  */
    private FeatureComparator m_comparator;

    /**
     * Creates a new FeatureRelator object.
     *
     * @param comparator FeatureComparator
     * @see FeatureComparator
     */
    public FeatureRelator(FeatureComparator comparator) {
        m_comparator = comparator;
    }

    /**
     * Returns the FeatureComparator used by the relator.
     *
     * @return FeatureComparator
     * @see FeatureComparator
     */
    public FeatureComparator getComparator() {
        return (m_comparator);
    }

    /**
     * Relates two features.
     *
     * @return True if there is a relationship present, otherwise false.
     */
    public abstract boolean relate(Feature f1, Feature f2);
}
