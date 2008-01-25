/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.Restlet;
import org.restlet.Router;
import org.springframework.beans.BeansException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.noelios.restlet.ext.servlet.ServletConverter;

/**
 * Simple AbstractController implementation that does the translation between
 * Spring requests and Restlet requests.
 */
public class WrappingController extends AbstractController {
    public static String METHOD_PUT = "PUT";
    public static String METHOD_DELETE = "DELETE";
    ServletConverter myConverter;
    private Map myRouteMap;
    private Router myRouter;

    public WrappingController() {
        super();
        setSupportedMethods(new String[] {
                METHOD_GET, METHOD_POST, METHOD_PUT, METHOD_DELETE, METHOD_HEAD
            });
    }

    protected void initApplicationContext() throws BeansException {
        super.initApplicationContext();

        myConverter = new ServletConverter(getServletContext());
        myConverter.setTarget(createRoot());
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse resp)
        throws Exception {

            myConverter.service(req, resp);

            return null;
    }

    public void setMapping(Map m){
        if (m == null) return;
        if (myRouter == null) myRouter = new Router();

        myRouter.getRoutes().clear();

        myRouter.attach("", new BeanResourceFinder(new IndexResource(myRouter)));

        try{
            Iterator it = m.keySet().iterator();

            while (it.hasNext()){
                String key = (String)it.next();

                myRouter.attach(key, (Restlet)m.get(key));
            }
        } catch (ClassCastException cce){
            if (m != myRouteMap){
                setMapping(myRouteMap);
            } else {
                myRouter.getRoutes().clear();
            }
            return;
        }
        myRouteMap = m;
    }

    public Map getMapping(){
        return myRouteMap;
    }

    public Restlet createRoot() {
        if (myRouter == null) myRouter = new Router();
        return myRouter;
/*
        Router router = new Router();
        ApplicationContext context = getApplicationContext();

        router.attach("",
            new ResourceFinder(ResourceFinder.RESOURCE_INDEX, router.getContext(), context, router));
        router.attach("/datastores.{type}",
            new ResourceFinder(ResourceFinder.RESOURCE_DATASTORE, router.getContext(), context,
                router));
        router.attach("/datastores",
            new ResourceFinder(ResourceFinder.RESOURCE_DATASTORE, router.getContext(), context,
                router));
        router.attach("/datastores/{datastore}.{type}",
            new ResourceFinder(ResourceFinder.RESOURCE_DATASTORE, router.getContext(), context,
                router));
        router.attach("/datastores/{datastore}",
            new ResourceFinder(ResourceFinder.RESOURCE_DATASTORE, router.getContext(), context,
                router));
        // This rule messes everything up: router.attach("/datastores/{datastore}/featuretypes", new ResourceFinder(ResourceFinder.RESOURCE_DATASTORE, router.getContext(), dc));
        router.attach("/datastores/{datastore}/featuretypes/{featuretype}.{type}",
            new ResourceFinder(ResourceFinder.RESOURCE_FEATURETYPE, router.getContext(), context,
                router));
        router.attach("/datastores/{datastore}/featuretypes/{featuretype}",
            new ResourceFinder(ResourceFinder.RESOURCE_FEATURETYPE, router.getContext(), context,
                router));
        router.attach("/styles/{style}.{type}",
            new ResourceFinder(ResourceFinder.RESOURCE_STYLE, router.getContext(), context, router));
        router.attach("/styles/{style}",
            new ResourceFinder(ResourceFinder.RESOURCE_STYLE, router.getContext(), context, router));
        router.attach("/styles.{type}",
            new ResourceFinder(ResourceFinder.RESOURCE_STYLE, router.getContext(), context, router));
        router.attach("/styles",
            new ResourceFinder(ResourceFinder.RESOURCE_STYLE, router.getContext(), context, router));
        router.attach("/coveragestores.{type}",
            new ResourceFinder(ResourceFinder.RESOURCE_COVERAGESTORE, router.getContext(), context,
                router));
        router.attach("/coveragestores",
            new ResourceFinder(ResourceFinder.RESOURCE_COVERAGESTORE, router.getContext(), context,
                router));
        router.attach("/coveragestores/{coveragestore}.{type}",
            new ResourceFinder(ResourceFinder.RESOURCE_COVERAGESTORE, router.getContext(), context,
                router));
        router.attach("/coveragestores/{coveragestore}",
            new ResourceFinder(ResourceFinder.RESOURCE_COVERAGESTORE, router.getContext(), context,
                router));
        router.attach("/coveragestores/{coveragestore}/coverages/{coverage}.{type}",
            new ResourceFinder(ResourceFinder.RESOURCE_COVERAGE, router.getContext(), context,
                router));
        router.attach("/coveragestores/{coveragestore}/coverages/{coverage}",
            new ResourceFinder(ResourceFinder.RESOURCE_COVERAGE, router.getContext(), context,
                router));
        router.attach("/dummy/{name}", new DummyRestlet(getApplicationContext()));
        router.attach("/layergroups/{group}.{type}",
            new ResourceFinder(ResourceFinder.RESOURCE_LAYERGROUP, router.getContext(), context,
                router));
        router.attach("/layergroups/{group}",
                new ResourceFinder(ResourceFinder.RESOURCE_LAYERGROUP, router.getContext(), context,
                    router));
        router.attach("/layergroups.{type}",
            new ResourceFinder(ResourceFinder.RESOURCE_LAYERGROUP, router.getContext(), context,
                router));
        router.attach("/layergroups",
            new ResourceFinder(ResourceFinder.RESOURCE_LAYERGROUP, router.getContext(), context,
                router));
        router.attach("/projections", new DummyRestlet(getApplicationContext()));
        router.attach("/projections/{projection}", new DummyRestlet(getApplicationContext()));

        return router; */
    }
}
