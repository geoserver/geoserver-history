/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geotools.graph.network;

import org.geotools.feature.Feature;
import org.geotools.graph.Edge;
import org.geotools.graph.Node;

import com.vividsolutions.jts.geom.Geometry;





/**
 * DOCUMENT ME!
 *
 * @author Justin Deoliveira
 * @version $Revision: 1.1 $
 */
public class NetworkEdge extends Edge {
    /**
     * Creates a new NetworkEdge object.
     *
     * @param feature DOCUMENT ME!
     * @param nodeA DOCUMENT ME!
     * @param nodeB DOCUMENT ME!
     */
    public NetworkEdge(Feature feature, Node nodeA, Node nodeB) {
        super(feature, nodeA, nodeB);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Geometry buildGeometry() {
        return (getFeature().getDefaultGeometry());
    }
}
