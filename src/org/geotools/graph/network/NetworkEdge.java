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
 * An Edge connects two nodes in the network.
 *
 * @author Justin Deoliveira
 */
public class NetworkEdge extends Edge {
    /**
     * Creates a NetworkEdge instance.
     *
     * @param feature Underlying Feature represented by the edge.
     * @param nodeA Node at the source of the edge.
     * @param nodeB Node at the destination of the edge.
     */
    public NetworkEdge(Feature feature, Node nodeA, Node nodeB) {
        super(feature, nodeA, nodeB);
    }

    /**
     * Builds a Geometry object to represent the network edge spatially.
     *
     * @see Geometry
     */
    public Geometry buildGeometry() {
        return (getFeature().getDefaultGeometry());
    }
}
