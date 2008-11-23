/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.rest;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geoserver.platform.GeoServerExtensions;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.springframework.beans.BeansException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.noelios.restlet.ext.servlet.ServletConverter;

/**
 * Simple AbstractController implementation that does the translation between
 * Spring requests and Restlet requests.
 */
public class RESTDispatcher extends AbstractController {
    public static String METHOD_PUT = "PUT";
    public static String METHOD_DELETE = "DELETE";
    ServletConverter myConverter;
    private Router myRouter;
    private Logger LOG = org.geotools.util.logging.Logging.getLogger("org.geoserver.rest");

    public RESTDispatcher() {
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
                // This does not actually write anything?
                //re.getRepresentation().write(resp.getOutputStream());
                
                String reStr = re.getRepresentation().getText();
                resp.getOutputStream().write(reStr.getBytes());
                
                resp.getOutputStream().flush();
            }
            else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                new StringRepresentation( e.getMessage() ).write( resp.getOutputStream() );
                resp.getOutputStream().flush();
            }
        }
            
        return null;
    }

    public void addRoutes(Map m, Router r){
        Iterator it = m.entrySet().iterator();

        while (it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();

            // LOG.info("Found mapping: " + entry.getKey().toString());

            if (getApplicationContext().getBean(entry.getValue().toString()) instanceof Resource){
                r.attach(entry.getKey().toString(), new BeanResourceFinder(getApplicationContext(), entry.getValue().toString()));
            } else {
                r.attach(entry.getKey().toString(), new BeanDelegatingRestlet(getApplicationContext(), entry.getValue().toString()));
            }
        }
    }

    public Restlet createRoot() {
        if (myRouter == null){
            myRouter = new Router();

            Iterator i = 
                GeoServerExtensions.extensions(RESTMapping.class).iterator();

            while (i.hasNext()){
                RESTMapping rm = (RESTMapping)i.next();
                addRoutes(rm.getRoutes(), myRouter);
            }

            myRouter.attach("", new IndexRestlet(myRouter));
        }

        return myRouter;
   }
}
