/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFactorySpi.Param;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;


/**
 * Restlet for DataStore resources
 *
 * @author Arne Kepp <ak@openplans.org> , The Open Planning Project
 */
public class DataStoreResource extends MapResource {
    private DataConfig myDC;
    private DataStoreConfig myDSC;

    public DataStoreResource(Context context, Request request, Response response, DataConfig dc) {
        super(context, request, response);
        myDC = dc;
        myDSC = findMyDataStore();
    }

    public void setDataConfig(DataConfig dc){
        myDC = dc;
    }

    public DataConfig getDataConfig(){
        return myDC;
    }

    private DataStoreConfig findMyDataStore() {
        Map attributes = getRequest().getAttributes();

        if (attributes.containsKey("datastore")) {
            String dsid = (String) attributes.get("datastore");

            return myDC.getDataStore(dsid);
        }

        return null;
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();
        m.put("html", new HTMLFormat("HTMLTemplates/datastore.ftl"));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("datastore"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap() {
        myDSC = findMyDataStore();

        // TODO: Fill this in better (what about different types of datastore?)
        // TODO: Handle parameters with spaces in their names
        Map map = new HashMap();
        map.put("Enabled", myDSC.isEnabled());
        map.put("Namespace", myDSC.getNameSpaceId());
        map.put("Description", (myDSC.getAbstract() == null ? "" : myDSC.getAbstract()));

        DataStoreFactorySpi factory = myDSC.getFactory();

        List storeSpecificParameters = new ArrayList();
        Param[] parameters = factory.getParametersInfo();
        for (int i = 0; i < parameters.length; i++){
            Param p = parameters[i];
            if (!("namespace".equals(p.key))){
                Object value = myDSC.getConnectionParams().get(p.key);
                Map entry = new HashMap();
                if (value == null) {
                    entry.put("value", "");
                } else if (value instanceof String){
                    entry.put("value", value);
                } else {
                    entry.put("value", p.text(value));
                }
                String key = p.key.substring(p.key.lastIndexOf(':') + 1);
                entry.put("name", key);
                entry.put("type", p.type.getName());
                entry.put("required", Boolean.toString(p.required));
                storeSpecificParameters.add(entry);
            }
        }

        map.put(factory.getClass().getSimpleName(), storeSpecificParameters);

        return map;
    }
}

