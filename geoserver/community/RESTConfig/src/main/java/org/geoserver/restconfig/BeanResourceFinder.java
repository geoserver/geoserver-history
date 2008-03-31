package org.geoserver.restconfig;

import org.restlet.Finder;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

public class BeanResourceFinder extends Finder{
    Resource myBeanToFind;

    public BeanResourceFinder(){}

    public BeanResourceFinder(Resource res){
        myBeanToFind = res; 
    }

    public void setBeanToFind(Resource name){
        myBeanToFind = name;
    }

    public Resource getBeanToFind(){
        return myBeanToFind;
    }

    public Resource findTarget(Request request, Response response){
        myBeanToFind.init(getContext(), request, response);
        System.out.println("BeanResourceFinder found " + myBeanToFind);
        return myBeanToFind;
    }
}
