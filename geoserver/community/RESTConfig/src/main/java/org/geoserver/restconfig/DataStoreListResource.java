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
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;

import org.geoserver.rest.MapResource;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.FreemarkerFormat;

/**
 * Restlet for DataStore resources
 *
 * @author Arne Kepp <ak@openplans.org> , The Open Planning Project
 */
public class DataStoreListResource extends MapResource {
    private DataConfig myDC;

    public DataStoreListResource(){
        super();
    }

    public DataStoreListResource(Context context, Request request, Response response, DataConfig dc) {
        super(context, request, response);
        myDC = dc;
    }

    public void setDataConfig(DataConfig dc){
        myDC = dc;
    }

    public DataConfig getDataConfig(){
        return myDC;
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();
        m.put("html", new FreemarkerFormat("HTMLTemplates/datastores.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("datastores"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap() {
        return makeDataStoreMap();
    }

    /**
     * Finds information about all datastore and the associated
     * FeatureTypeConfigs, then creates map suitable for use with
     * FreeMarker templates.
     *
     * @return HashMap for use with FreeMarker templates
     */
    private HashMap makeDataStoreMap() {
        HashMap map = new HashMap();
        List datastores = new ArrayList();

        for (Iterator it = myDC.getDataStores().values().iterator(); it.hasNext();) {
            DataStoreConfig dsc = (DataStoreConfig)it.next();
            datastores.add(dsc.getId());
        }

        map.put("datastores", datastores);

        return map;
    }
}
