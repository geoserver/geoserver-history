/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geotools.graph;

import com.vividsolutions.jts.geom.Coordinate;
import java.util.Collection;
import java.util.List;

import org.geotools.feature.Feature;


/**
 * A Node which sperates adjacent edges into two categories. Those in which the
 * node is the terminal node of the edge (in edges), and those in which the
 * node is the source node of the edge (out edges).
 *
 * @author Justin Deoliveira
 */
public class InOutNode extends PointNode {

	
    /**
     * InOutNode constructor.
     * 
     * <p>
     * super(feature,edgeList,coord);
     * </p>
     *
     * @param feature Feature
     * @param edgeList InOutEdgeList
     * @param coord Coordinate
     */
	public InOutNode(Feature feature, EdgeList edgeList, Coordinate coord) {
		super(feature, edgeList, coord);
	}

    /**
     * Returns a list of nodes in which the node is the terminal node. In other
     * words, it returns all the source nodes of all edges adjacent to the
     * node.
     *
     * @return List
     */
    public List getInNodes() {
        return (((InOutEdgeList) getEdgeList()).getOtherInNodes(this));
    }

    /**
     * Returns a list of edges in which the node is the terminal node.
     *
     * @return List
     */
    public List getInEdges() {
        return (((InOutEdgeList) getEdgeList()).getInEdges());
    }

    /**
     * Returns an edge in which the node is the terminal node, and the source
     * node is specified.
     *
     * @param source The source node of the edge to be returned.
     *
     * @return The edge (source,this).
     */
    public Edge getInEdge(Node source) {
        return ((InOutEdgeList) getEdgeList()).getInEdge(source, this);
    }

    /**
     * Returns a list of nodes in which the node is the source node of the edge
     * shared between them. In other words, it returns all the terminal nodes
     * of  all edges adjacent to the node.
     *
     * @return List
     */
    public List getOutNodes() {
        return (((InOutEdgeList) getEdgeList()).getOtherOutNodes(this));
    }

    /**
     * Returns a list of edges in which the node is the source node.
     *
     * @return List
     */
    public List getOutEdges() {
        return (((InOutEdgeList) getEdgeList()).getOutEdges());
    }

    /**
     * Returns an edge in which the node is the source node, and the terminal
     * node is specified.
     *
     * @param terminal The terminal node of the edge to be returned.
     *
     * @return The edge (this,terminal).
     */
    public Edge getOutEdge(Node terminal) {
        return ((InOutEdgeList) getEdgeList()).getOutEdge(this, terminal);
    }

    /**
     * Returns the in degree of the node. The number of edges adjacent to the
     * node in which the node is the terminal node of the edge.
     *
     * @return int
     */
    public int getInDegree() {
        return (((InOutEdgeList) getEdgeList()).getInEdges().size());
    }

    /**
     * Returns the out degree of the node. The number of edges adjacent to the
     * node in which the node is the source node of the edge.
     *
     * @return int
     */
    public int getOutDegree() {
        return (((InOutEdgeList) getEdgeList()).getOutEdges().size());
    }

    /**
     * Returns the terminal nodes of all edges adjacent to the node.
     *
     * @return Collection
     */
    public Collection getAdjacentElements() {
        return (getOutNodes());
    }
}
