package org.geotools.graph.traverse;

import java.util.Collection;
import java.util.Iterator;

import org.geotools.graph.Graph;
import org.geotools.graph.GraphComponent;



/**
 * A very simple traversal in which a single pass is made over the components
 * of the graph.
 *
 * @author Justin Deoliveira
 */
public class BasicGraphTraversal extends AbstractGraphTraversal {

  public BasicGraphTraversal(Graph graph, GraphWalker walker) {
    super(graph, walker);
  }
  
  /**
   * Iterates over all nodes in the graph.
   */
  public void walkNodes() {
    walk(getGraph().getNodes());
  }

  /**
   * Iterates over all edges in the graph.
   */
  public void walkEdges() {
    walk(getGraph().getEdges());
  }
  
  protected void walk(Collection elements) {
    for (Iterator itr = elements.iterator(); itr.hasNext();) {
      if (getWalker().visit((GraphComponent)itr.next(), this) == STOP) return;  
    }
  }
}