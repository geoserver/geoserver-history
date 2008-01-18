/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import org.geotools.data.DataStoreFactorySpi;
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
public class DataStoreListResource extends MapResource {
    private DataConfig myDC;
    private DataStoreConfig myDSC;

    public DataStoreListResource(){
        super();
    }

    public DataStoreListResource(Context context, Request request, Response response, DataConfig dc) {
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

    public Map getSupportedFormats() {
        Map m = new HashMap();
        m.put("html", new HTMLFormat("HTMLTemplates/datastores.ftl"));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("datastores"));
        m.put(null, m.get("html"));

        return m;
    }

    private DataStoreConfig findMyDataStore() {
        Map attributes = getRequest().getAttributes();

        if (attributes.containsKey("datastore")) {
            String dsid = (String) attributes.get("datastore");

            return myDC.getDataStore(dsid);
        }

        return null;
    }

    public Map getMap() {
        myDSC = findMyDataStore();
        return makeDataStoreMap();
    }

    /**
     * Sorts the FeatureTypeConfigs according to the datastore to which they are associated.
     *
     * @return HashMap where datastore id is key and value is list of associated feature types
     */
    private HashMap getSortedFeatureTypes() {
        HashMap map = new HashMap();

        for (Iterator it = myDC.getFeaturesTypes().values().iterator(); it.hasNext();) {
            FeatureTypeConfig ftc = (FeatureTypeConfig) it.next();
            List ftcs = (ArrayList) map.get(ftc.getDataStoreId());

            if (ftcs == null) {
                ftcs = new ArrayList();
            }

            ftcs.add(ftc.getName());
            map.put(ftc.getDataStoreId(), ftcs);
        }

        return map;
    }

    /**
     * Get the FeatureTypeConfigs for given DataStoreId
     *
     * @return list of FeatureTypeConfigs in given DataStore
     */
    private List getFeatureTypes(String dsId) {
        List ftcs = new ArrayList();

        for (Iterator it = myDC.getFeaturesTypes().values().iterator(); it.hasNext();) {
            FeatureTypeConfig ftc = (FeatureTypeConfig) it.next();

            if (ftc.getDataStoreId().equals(dsId)) {
                ftcs.add(ftc);
            }
        }

        return ftcs;
    }

    /**
     * Extracts information from FeatureTypeConfigs and creates a Map
     * suitable for use with FreeMarker templates.
     *
     * @param featureTypeConfigs list with FeatureTypeConfigs
     * @return HashMap for use with FreeMarker templates
     */
    private HashMap makeFeatureTypeMap(List featureTypeConfigs) {
        HashMap map = new HashMap();
        List fts = new ArrayList();

        for (Iterator it = featureTypeConfigs.iterator(); it.hasNext();) {
            FeatureTypeConfig ftc = (FeatureTypeConfig) it.next();
            fts.add(ftc.getName());
        }

        map.put("featuretypes", fts);

        return map;
    }

    /**
     * Finds information about all datastore and the associated
     * FeatureTypeConfigs, then creates map suitable for use with
     * FreeMarker templates.
     *
     * @return HashMap for use with FreeMarker templates
     */
    private HashMap makeDataStoreMap() {
        // Get all the featuretypes, sorted according to datastore
        Map sortedFeatureTypes = getSortedFeatureTypes();

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
