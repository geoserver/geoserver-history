package org.geotools.graph.traverse;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

import org.geotools.graph.Graph;
import org.geotools.graph.GraphComponent;

public class ReverseDepthFirstTraversal extends SourceGraphTraversal {

  private Stack m_active;
  
  public ReverseDepthFirstTraversal(Graph graph, GraphWalker walker, GraphComponent source) {
    super(graph, walker, source);
  }

  public Collection getActiveElements() {
    return(m_active);  
  }
  
  protected void walk() {
    m_active = new Stack();
    
    m_active.push(getSource());
    if (getWalker().visit(getSource(), this) == STOP) return;
    
    while(!m_active.isEmpty()) {
      GraphComponent element = (GraphComponent)m_active.peek();
         
      boolean visited = true;
      Iterator itr = element.getAdjacentElements().iterator();
      while(visited && itr.hasNext()) {
        GraphComponent adjacent = (GraphComponent)itr.next();
        if (!getWalker().isVisited(adjacent)) {
          visited = false;  
          m_active.push(adjacent);
          if (getWalker().visit(adjacent, this) == STOP) return;
        }
      }
      
      if (visited) {
        m_active.pop();
      }
    }
  }
}