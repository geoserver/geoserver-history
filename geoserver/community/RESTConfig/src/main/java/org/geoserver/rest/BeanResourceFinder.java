/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.rest;

import org.restlet.Finder;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

/**
 * The BeanResourceFinder class wraps a Resource and simply finds that Resource.
 * It allows you to wrap a Resource and use it as a Restlet.
 *
 * @author David Winslow <dwinslow@openplans.org>
 */
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
        return myBeanToFind;
    }
}
