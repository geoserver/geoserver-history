/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geotools.graph.traverse;

import java.util.Iterator;
import java.util.LinkedList;

import org.geotools.graph.Graph;
import org.geotools.graph.InOutNode;


/**
 * DOCUMENT ME!
 *
 * @author Justin Deoliveira
 * @version $Revision: 1.1 $
 */
public class TopologicalTraversal extends AbstractGraphTraversal {
    /** DOCUMENT ME!  */
    private LinkedList m_queue;

    /**
     * Creates a new TopologicalTraversal object.
     *
     * @param graph DOCUMENT ME!
     * @param walker DOCUMENT ME!
     */
    public TopologicalTraversal(Graph graph, GraphWalker walker) {
        super(graph, walker);
        m_queue = new LinkedList();
    }

    /**
     * DOCUMENT ME!
     */
    public void initNodes() {
        super.initNodes();

        for (Iterator itr = getGraph().getNodes().iterator(); itr.hasNext();) {
            InOutNode node = (InOutNode) itr.next();

            if (node.getInDegree() == 0) {
                m_queue.addLast(node);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void walkNodes() {
        walk();
    }

    /**
     * DOCUMENT ME!
     */
    public void walkEdges() {
        throw new UnsupportedOperationException("Edge walking not supported.");
    }

    /**
     * DOCUMENT ME!
     */
    protected void walk() {
        while (!m_queue.isEmpty()) {
            InOutNode node = (InOutNode) m_queue.removeFirst();
            getWalker().visit(node, this);

            Iterator itr;

            for (itr = node.getOutNodes().iterator(); itr.hasNext();) {
                InOutNode out = (InOutNode) itr.next();
                out.setCount(out.getCount() + 1);

                if (out.getCount() == out.getInNodes().size()) {
                    m_queue.addLast(out);
                }
            }
        }
    }
}
