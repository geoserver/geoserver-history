package org.geotools.graph.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.graph.GraphComponent;

public class Path {
  ArrayList m_elements;
  
  public Path() {
    m_elements = new ArrayList();  
  }
  
  public void add(GraphComponent element) {
    m_elements.add(element);  
  }
  
  public List getElements() {
    return(m_elements);  
  }
  
  public int size() {
    return(m_elements.size());  
  }
  
  public Iterator iterator() {
    return(
      new Iterator() {
        int i = m_elements.size()-1;
        
        public void remove() {
          throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
          return(i > -1);
        }

        public Object next() {
          return(m_elements.get(i--));    
        }
      }
    );
  }
  
  public boolean isEmpty() {
    return(m_elements.isEmpty());  
  }
}