/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geotools.graph;

import java.util.List;


/**
 * Represents the edge adjacency list for a node.
 *
 * @author Justin Deoliveira
 */
public interface EdgeList {
    /**
     * Returns the edges contained in the edge list.
     *
     * @return List
     */
    public List getEdges();

    /**
     * Adds an edge to the edge list.
     *
     * @param edge the edge to be added.
     */
    public void add(Edge edge);

    /**
     * Removed an edge from the edge list.
     *
     * @param edge the edge to be removed.
     */
    public void remove(Edge edge);

    /**
     * Returns an edge in the list ended by specfic nodes.
     *
     * @param n1 Starting Edge Node
     * @param n2 Ending Edge Node
     *
     * @return The Edge
     */
    public Edge getEdge(Node n1, Node n2);

    /**
     * Returns the size of the edge list.
     *
     * @return The number of edges contained in the list.
     */
    public int getSize();

    /**
     * Returns the edges in the list minus a specific edge.
     *
     * @param edge The edge not to be returned.
     *
     * @return List
     */
    public List getOtherEdges(Edge edge);

    /**
     * Returns a collection of nodes adjacent to edges in the list minus a
     * specifc node.
     *
     * @param node The node not to be returned.
     *
     * @return List
     */
    public List getOtherNodes(Node node);

    /**
     * Determines if the edge list contains a certain edge.
     *
     * @return True if the edge is contained, false otherwise.
     */
    public boolean contains(Edge edge);
}
