/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.rest;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geoserver.config.GeoServer;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.GeoServerExtensions;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;
import org.restlet.util.Template;
import org.springframework.beans.BeansException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.noelios.restlet.ext.servlet.ServletConverter;

/**
 * Simple AbstractController implementation that does the translation between
 * Spring requests and Restlet requests.
 */
public class RESTDispatcher extends AbstractController {
    /** HTTP method "PUT" */
    public static String METHOD_PUT = "PUT";
    /** HTTP method "DELETE" */
    public static String METHOD_DELETE = "DELETE";
    
    /**
     * logger
     */
    static Logger LOG = org.geotools.util.logging.Logging.getLogger("org.geoserver.rest");
    
    /**
     * converter for turning servlet requests into resetlet requests.
     */
    ServletConverter myConverter;
    
    /**
     * the root restlet router
     */
    Router myRouter;
    
    /**
     * geoserver configuration
     */
    GeoServer gs;
    
    public RESTDispatcher(GeoServer gs) {
        this.gs = gs;
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

        try {
            myConverter.service(req, resp);
        }
        catch( Exception e ) {
            RestletException re = null;
            if ( e instanceof RestletException ) {
                re = (RestletException) e;
            }
            if ( re == null && e.getCause() instanceof RestletException ) {
                re = (RestletException) e.getCause();
            }
            
            if ( re != null ) {
                resp.setStatus( re.getStatus().getCode() );
                
                String reStr = re.getRepresentation().getText();
                if ( reStr != null ) {
                    LOG.severe( reStr );
                    resp.getOutputStream().write(reStr.getBytes());    
                }
            }
            else {
                LOG.log( Level.SEVERE, "", e );
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                
                if ( e.getMessage() != null ) {
                    resp.getOutputStream().write( e.getMessage().getBytes() );    
                }
            }
            resp.getOutputStream().flush();
        }
            
        return null;
    }

    public void addRoutes(Map m, Router r){
        Iterator it = m.entrySet().iterator();

        while (it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();

            // LOG.info("Found mapping: " + entry.getKey().toString());

            if (getApplicationContext().getBean(entry.getValue().toString()) instanceof Resource){
                r.attach(entry.getKey().toString(), 
                        new BeanResourceFinder(
                            getApplicationContext(),
                            entry.getValue().toString()
                            )
                        ).getTemplate().setMatchingMode(Template.MODE_EQUALS);
            } else {
                r.attach(entry.getKey().toString(), 
                        new BeanDelegatingRestlet(
                            getApplicationContext(),
                            entry.getValue().toString()
                            )
                        ).getTemplate().setMatchingMode(Template.MODE_EQUALS);
            }
        }
    }

    public Restlet createRoot() {
        if (myRouter == null){
            myRouter = new Router() {
                @Override
                protected synchronized void init(Request request,
                        Response response) {
                    super.init(request, response);

                    //set the base url, and proxy it if a proxy set
                    String baseURI = null;
                    if ( request.getResourceRef().getBaseRef() != null ) {
                        baseURI = request.getResourceRef().getBaseRef().toString();
                    }
                    else {
                        baseURI = request.getResourceRef().toString();
                    }
                    
                    String pageURI = request.getResourceRef().toString();
                    if ( gs.getGlobal().getProxyBaseUrl() != null ) {
                        baseURI = RequestUtils.proxifiedBaseURL(baseURI, gs.getGlobal().getProxyBaseUrl());
                        pageURI = ResponseUtils.appendPath( baseURI, request.getResourceRef().getPath() );
                    }
                    
                    //strip off the extension
                    String extension = ResponseUtils.getExtension(pageURI);
                    if ( extension != null ) {
                        pageURI = pageURI.substring(0,pageURI.length() - extension.length() - 1);
                    }
                    
                    //trim leading slash
                    if ( pageURI.endsWith( "/" ) ) {
                        pageURI = pageURI.substring(0,pageURI.length()-1);
                    }
                    //create a page info object and put it into a request attribute
                    PageInfo pageInfo = new PageInfo();
                    pageInfo.setBaseURI( baseURI );
                    pageInfo.setPageURI( pageURI );
                    pageInfo.setExtension( extension );
                    request.getAttributes().put( PageInfo.KEY, pageInfo );
                    
                }
            };

            //load all the rest mappings and register them with the router
            Iterator i = 
                GeoServerExtensions.extensions(RESTMapping.class).iterator();

            while (i.hasNext()){
                RESTMapping rm = (RESTMapping)i.next();
                addRoutes(rm.getRoutes(), myRouter);
            }

            //create a root mapping
            myRouter.attach("", new IndexRestlet(myRouter));
        }

        return myRouter;
   }
}
