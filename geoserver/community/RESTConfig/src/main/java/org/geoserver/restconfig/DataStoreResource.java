/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.resource.Resource;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFactorySpi.Param;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Restlet for DataStore resources
 *
 * @author Arne Kepp <ak@openplans.org> , The Open Planning Project
 */
public class DataStoreResource extends MapResource {
    private DataConfig myDC;
    private DataStoreConfig myDSC;

    public DataStoreResource(){
        super();
    }

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

        Map storeSpecificParameters = new HashMap();
        Param[] parameters = factory.getParametersInfo();
        for (int i = 0; i < parameters.length; i++){
            Param p = parameters[i];
            if (!("namespace".equals(p.key))){
                Object value = myDSC.getConnectionParams().get(p.key);
                String text  = null;
                if (value == null) {
                    text = null;
                } else if (value instanceof String){
                    text = (String) value;
                } else {
                    text = p.text(value);
                }
                String key = p.key.substring(p.key.lastIndexOf(':') + 1);
                storeSpecificParameters.put(key, text);
            }
        }

        map.put(factory.getClass().getSimpleName(), storeSpecificParameters);

        return map;
    }
}

