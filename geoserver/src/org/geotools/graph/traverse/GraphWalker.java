package org.geotools.graph.traverse;

import org.geotools.graph.GraphComponent;

/**
 * A GraphWalker walks a graph via a {@link GraphTraversal}. As well as simply 
 * visiting components of the graph when received from a traversal, the walker
 * can also control the traversal by initializing the elements of a graph and 
 * determining when a component is considered to be visited.
 * 
 * @author Justin Deoliveira
 */
public interface GraphWalker {
  
  /**
   * Inializes a graph component
   */
  public void init(GraphComponent element);
  
  /**
   * Determines if a graph component has been visited. 
   */
  public boolean isVisited(GraphComponent element);
  
  /**
   * Visits a graph componenet.
   * @param element The component to visit.
   * @param traversal The traversal controlling the sequence of graph 
   * component visits.
   */
  public int visit(GraphComponent element, GraphTraversal traversal); 
  
  /**
   * Called when the graph traversal is completed.
   */
  public void finish();
  
}