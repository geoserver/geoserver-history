package org.geotools.graph.traverse;
import java.util.Collection;

import org.geotools.graph.Graph;
import org.geotools.graph.GraphComponent;



/**
 * A GraphTraversal that is intened to start from a single graph componenet. 
 * This component is known as the source of the traversal.
 * 
 * @author Justin Deoliveira
 */
public abstract class SourceGraphTraversal extends AbstractGraphTraversal {

  /** source of traversal */
  private GraphComponent m_source;
  
  public SourceGraphTraversal(
    Graph graph, GraphWalker walker, GraphComponent source
  ) {
    super(graph, walker);
    m_source = source;
  }
  
  /**
   * Returns the source of the traversal.
   */
  public GraphComponent getSource() {
    return(m_source);  
  }
  
  /**
   * @see GraphTraversal#walkNodes()
   */
  public void walkNodes() {
    walk();
  }
  
  /**
   * @see GraphTraversal#walkEdges()
   */
  public void walkEdges() {
    walk();
  }
  
  /**
   * Returns the active elements of the traversal. That is elements that 
   * are in the process of being visited, or are queued to be visited.
   */
  public abstract Collection getActiveElements();
  
  protected abstract void walk();
}