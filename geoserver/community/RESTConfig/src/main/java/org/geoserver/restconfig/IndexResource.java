/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.restlet.Context;
import org.restlet.Route;
import org.restlet.Router;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.MediaType;

import org.geoserver.rest.MapResource;
import org.geoserver.rest.FreemarkerFormat;

class IndexResource extends MapResource {
    private Router myRouter;

    public IndexResource(Router r){
        myRouter = r;
    }

    public IndexResource(Context con, Request req, Response res, Router router) {
        super(con, req, res);
        myRouter = router;
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/index.ftl", getClass(), MediaType.TEXT_HTML));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap() {
        Map m = new HashMap();
        m.put("links", getLinkList());

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
