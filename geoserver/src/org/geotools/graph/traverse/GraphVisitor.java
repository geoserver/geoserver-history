package org.geotools.graph.traverse;

import org.geotools.graph.GraphComponent;



/**
 * An interface in which to implement a visitor pattern with components of a 
 * graph.
 *
 * @author Justin Deoliveira
 */
public interface GraphVisitor {
  
  public int visit(GraphComponent element); 
}