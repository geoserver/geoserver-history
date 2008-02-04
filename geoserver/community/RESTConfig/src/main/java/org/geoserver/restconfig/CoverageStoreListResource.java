/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.util.HashMap;
import java.util.Map;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.vfny.geoserver.config.DataConfig;

import org.geoserver.rest.MapResource;
import org.geoserver.rest.HTMLFormat;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.JSONFormat;

public class CoverageStoreListResource extends MapResource {
    private DataConfig myDataConfig;

    public CoverageStoreListResource(){
        super();
    }

    public CoverageStoreListResource(Context context, Request request, Response response,
        DataConfig config) {
        super(context, request, response);
        myDataConfig = config;
    }

    public void setDataConfig(DataConfig dc){
        myDataConfig = dc;
    }

    public DataConfig getDataConfig(){
        return myDataConfig;
    }

    @Override
    public Map getMap() {
        Map m = new HashMap();
        m.put("coveragestores", myDataConfig.getDataFormatIds());

        return m;
    }

    @Override
    public Map getSupportedFormats() {
        Map m = new HashMap();
        m.put("html", new HTMLFormat("HTMLTemplates/coveragestores.ftl"));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("coveragestores"));
        m.put(null, m.get("html"));

        return m;
    }
}
