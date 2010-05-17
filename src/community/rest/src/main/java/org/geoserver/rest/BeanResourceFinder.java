package org.geoserver.rest;

import org.restlet.Finder;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BeanResourceFinder extends Finder implements ApplicationContextAware{
    ApplicationContext myContext;
    String myBeanName;

    public void setApplicationContext(ApplicationContext con){
        myContext = con;
    }        

    public BeanResourceFinder(ApplicationContext con, String beanName){
        myContext = con;
        myBeanName = beanName;
    }

    public void setBeanToFind(String name){
        myBeanName = name;
    }

    public String getBeanToFind(){
        return myBeanName;
    }

    public Resource findTarget(Request request, Response response){
        Resource res = (Resource) myContext.getBean(getBeanToFind());
        res.init(getContext(), request, response);
        return res;
    }
}
