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
import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Restlet for DataStore resources
 *
 * @author Arne Kepp <ak@openplans.org> , The Open Planning Project
 */
public class FeatureTypeListResource extends MapResource {
    private DataConfig myDC;

    public void setDataConfig(DataConfig dc){
        myDC = dc;
    }

    public DataConfig getDataConfig(){
        return myDC;
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();

        m.put("html", new HTMLFormat("HTMLTemplates/featuretype.ftl"));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("FeatureTypes"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap() {
    	String dataStoreName = (String)getRequest().getAttributes().get("datastore");
    	
    	if (myDC.getDataStores().containsKey(dataStoreName)){
            Map m = new HashMap();
            List featureTypes = new ArrayList();
            
            Iterator it = myDC.getFeaturesTypes().values().iterator();
            while (it.hasNext()){
                FeatureTypeConfig ftc = (FeatureTypeConfig)it.next();
                if (ftc.getDataStoreId().equals(dataStoreName)){
                	featureTypes.add(ftc.getName());
                }
            }
            
            m.put("FeatureTypes", featureTypes);
            
            return m;    		
    	} else return null;    	
    }

    public boolean allowGet() {
        return true;
    }
}
