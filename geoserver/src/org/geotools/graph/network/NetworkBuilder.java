/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geotools.graph.network;

import com.vividsolutions.jts.geom.Coordinate;

import org.geotools.feature.Feature;
import org.geotools.graph.*;
import org.geotools.graph.build.LineGraphBuilder;


/**
 * Network builder
 *
 * @author Justin Deoliveira
 * @version $Revision: 1.2 $
 */
public class NetworkBuilder extends LineGraphBuilder {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDirected() {
        return (true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     */
    public void removeNode(Node node) {
        super.removeNode(node);
    }

    /**
     * DOCUMENT ME!
     *
     * @param newEdge DOCUMENT ME!
     */
    public void addEdge(Edge newEdge) {
        ((InOutEdgeList) newEdge.getNodeA().getEdgeList()).addOut(newEdge);
        ((InOutEdgeList) newEdge.getNodeB().getEdgeList()).addIn(newEdge);
        getEdges().add(newEdge);
    }

    /**
     * DOCUMENT ME!
     *
     * @param edge DOCUMENT ME!
     */
    public void removeEdge(Edge edge) {
        ((InOutEdgeList) edge.getNodeA().getEdgeList()).removeOut(edge);
        ((InOutEdgeList) edge.getNodeB().getEdgeList()).removeIn(edge);
        getEdges().remove(edge);
    }

    /**
     * DOCUMENT ME!
     *
     * @param feature DOCUMENT ME!
     * @param coordinate DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Node buildNode(Feature feature, Coordinate coordinate) {
        return (new InOutNode(feature, new InOutEdgeList(), coordinate));
    }

    /**
     * DOCUMENT ME!
     *
     * @param feature DOCUMENT ME!
     * @param n1 DOCUMENT ME!
     * @param n2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Edge buildEdge(Feature feature, Node n1, Node n2) {
        return (new NetworkEdge(feature, n1, n2));
    }
}
