package org.geotools.graph.traverse;

import org.geotools.graph.GraphComponent;


/**
 * A simple implentation of GraphWalker that decorates a  
 * GraphVisitor.
 * 
 * @author Justin Deoliveira
 */
public class SimpleGraphWalker implements GraphWalker {

  /** Underlying visitor */
  private GraphVisitor m_visitor;
  
  /**
   * Creates a GraphWalker from a preexising GraphVisitor 
   */
  public SimpleGraphWalker(GraphVisitor visitor) {
    m_visitor = visitor;  
  }
  
  /**
   * Resets visited flag and counter.
   * @see GraphWalker#init(GraphComponent)
   */
  public void init(GraphComponent element) {
    element.setVisited(false);
    element.setCount(0);
  }

  /**
   * Returns the visited flag of the graph component.
   * @see GraphWalker#isVisited(GraphComponent)
   */
  public boolean isVisited(GraphComponent element) {
    return(element.isVisited());  
  }

  /**
   * Sets the visted flag, and passes the element on to underlying visitor.
   */
  public int visit(GraphComponent element, GraphTraversal traversal) {
    element.setVisited(true);
    return(m_visitor.visit(element));
  }
  
  /**
   * Does nothing.
   * @see GraphWalker#finish()
   */
  public void finish() {}
}