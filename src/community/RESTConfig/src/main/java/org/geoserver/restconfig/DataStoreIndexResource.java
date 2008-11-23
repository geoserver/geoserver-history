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


/**
 * The DataStoreIndexResource class lists the paths available under a DataStore.
 * This is basically a static page that lists all the interesting pages for a 
 * datastore.
 *
 * @author David Winslow <dwinslow@openplans.org>
 */
class DataStoreIndexResource extends MapResource {
    public Map getSupportedFormats() {
        Map m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/datastoreindex.ftl", getClass(), MediaType.TEXT_HTML));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap() {
        return new HashMap();
    }
}
