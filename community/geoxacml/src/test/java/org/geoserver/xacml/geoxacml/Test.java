package org.geoserver.xacml.geoxacml;

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class Test {
   
    String[] testArray;
    
    public  static  void main(String arg[]) {
      
    BeanInfo bi = null;    
    try {
        bi = Introspector.getBeanInfo(Test.class);
    } catch (IntrospectionException e) {
        e.printStackTrace();
    }
   
    for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
        if ("testArray".equals(pd.getName())) {
            IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor) pd;
            System.out.println(ipd.getName());
            System.out.println(ipd.getIndexedReadMethod());
            System.out.println(ipd.getIndexedWriteMethod());
        }
        
    }
    
    }

    public String[] getTestArray() {
        return testArray;
    }

    public void setTestArray(String[] testArray) {
        this.testArray = testArray;
    }
    
    public void setTestArray(int index, String name) {
        testArray[index]=name;
        
    }
    public String getTestArray(int index)  {
        return testArray[index];
    }


}
