/* Copyright (c) 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.usermanagement;

import com.noelios.restlet.ext.servlet.ServletConverter;
import org.geoserver.security.EditableUserDAO;
import org.restlet.Restlet;
import org.restlet.Router;
import org.springframework.beans.BeansException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Simple AbstractController implementation that does the translation between
 * Spring requests and Restlet requests.
 * @author David Winslow <dwinslow@openplans.org>
 * @author Justin Deoliveira <jdeolive@openplans.org>
 */
public class WrappingController extends AbstractController {
    /**
     * The HTTP METHOD name for PUT operations.
     */
    public static String METHOD_PUT = "PUT";

    /**
     * The HTTP METHOD name for DELETE operations.
     */
    public static String METHOD_DELETE = "DELETE";

    /**
     * The ServletConverter object used to translate between Java Servlet Requests and Restlet Requests.
     */
    ServletConverter myConverter;

    /**
     * Default constructor overridden in order to allow non-default HTTP methods.
     */
    public WrappingController() {
        super();
        setSupportedMethods(new String[] { METHOD_GET, METHOD_POST, METHOD_PUT, METHOD_DELETE });
    }

    /**
     * Overridden to make the ServletConverter aware of the SpringContext
     */
    protected void initApplicationContext() throws BeansException {
        super.initApplicationContext();

        myConverter = new ServletConverter(getServletContext());
        myConverter.setTarget(createRoot());
    }

    /**
     * Actually handle a request by passing it off to the ServletConverter
     * @param req the HttpServletRequest to be handled
     * @param resp the HttpServletResponse to use for returning the response
     */
    protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse resp)
        throws Exception {
        myConverter.service(req, resp);

        return null;
    }

    /**
     * Create the Restlet that will handle the requests after they are translated.
     * @return the Restlet to handle requests
     */
    public Restlet createRoot() {
        Router router = new Router();

        EditableUserDAO eud = (EditableUserDAO) getApplicationContext()
                                                    .getBean("userDetailsService");
        // router.attach("/roles", new UserRestlet("Role Management Page"));
        router.attach("/user.{type}", new UserFinder(router.getContext(), eud));
        router.attach("/user", new UserFinder(router.getContext(), eud));
        router.attach("/user/{name}.{type}", new UserFinder(router.getContext(), eud));
        router.attach("/user/{name}", new UserFinder(router.getContext(), eud));
        router.attach("/dummy/{name}", new DummyRestlet(getApplicationContext()));

        // router.attach("/geoserver/users/{user}/roles", new
        // UserRestlet("getting role information"));
        return router;
    }
}
