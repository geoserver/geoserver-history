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
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.MediaType;
import org.vfny.geoserver.config.WMSConfig;

import org.geoserver.rest.MapResource;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.FreemarkerFormat;

/**
 * Restlet for Style resources
 *
 * @author David Winslow <dwinslow@openplans.org> , The Open Planning Project
 */
class LayerGroupListResource extends MapResource {
    private WMSConfig myWMSConfig;

    public LayerGroupListResource(){
        super();
    }

    public LayerGroupListResource(Context context, Request request, Response response,
        WMSConfig wmsConfig) {
        super(context, request, response);
        myWMSConfig = wmsConfig;
    }

    public void setWMSConfig(WMSConfig c){
        myWMSConfig = c;
    }

    public WMSConfig getWMSConfig(){
        return myWMSConfig;
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();
        m.put("html", new FreemarkerFormat("HTMLTemplates/layergroups.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("json", new JSONFormat());
        m.put("xml",  new AutoXMLFormat("layergroups"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap() {
        Map context = new HashMap();
        Map layerGroups = myWMSConfig.getBaseMapLayers();
        List layerNames = new ArrayList();

        if (layerGroups != null) {
            Iterator it = layerGroups.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                // addition.add(entry.getKey());
                // addition.put("members", Arrays.asList(entry.getValue().toString().split(",")));
                layerNames.add(entry.getKey());
            }

            context.put("layers", layerNames);
        }

        return context;
    }
}
