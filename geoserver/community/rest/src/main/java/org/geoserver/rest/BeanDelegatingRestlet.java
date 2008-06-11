package org.geoserver.rest;

import org.restlet.Restlet;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.springframework.context.ApplicationContext;

public class BeanDelegatingRestlet extends Restlet{
    String myBeanName;
    ApplicationContext myContext;


    public BeanDelegatingRestlet(ApplicationContext con, String beanName){
       myContext = con;
       myBeanName = beanName; 
    }

    public void handle(Request req, Response res){
        Object bean = myContext.getBean(myBeanName);
        Restlet restlet = (Restlet)bean;
        restlet.handle(req, res);
    }
}
