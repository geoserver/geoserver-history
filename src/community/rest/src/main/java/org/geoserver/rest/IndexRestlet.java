/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.restlet.Context;
import org.restlet.Route;
import org.restlet.Router;
import org.restlet.Finder;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.MediaType;
import org.restlet.resource.Resource;


/**
 * The IndexResource class lists the paths available for a Router.
 * Specifically, it auto-generates an index page containing all 
 * non-templated paths relative to the router root.
 *
 * @author David Winslow <dwinslow@openplans.org>
 */
class IndexRestlet extends Finder{
    private Router myRouter;

    public IndexRestlet(Router r){
        myRouter = r;
    }

    public IndexRestlet(Context con, Router router) {
        super(con);
        myRouter = router;
    }

    public Resource findTarget(Request req, Response resp){
        Resource r = new IndexResource();
        r.init(getContext(), req, resp);
        return r;
    }

    private class IndexResource extends MapResource{

        public Map getSupportedFormats() {
            Map m = new HashMap();

            m.put("html", new FreemarkerFormat("HTMLTemplates/index.ftl", getClass(), MediaType.TEXT_HTML));
            m.put(null, m.get("html"));

            return m;
        }

        public Object getMap() {
            Map m = new HashMap();
            m.put("links", getLinkList());
            m.put("page", getPageDetails());

            return m;
        }

        private List getLinkList() {
            List l = new ArrayList();

            Iterator it = myRouter.getRoutes().iterator();

            while (it.hasNext()) {
                Route r = (Route) it.next();
                String pattern = r.getTemplate().getPattern();

                if (!pattern.contains("{") && (pattern.length() > 1)) {
                    l.add(pattern.substring(1)); // trim leading slash
                }
            }

            return l;
        }
    }
}
