package org.geoserver.restconfig;

import org.restlet.Finder;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.MediaType;
import org.restlet.resource.Resource;

import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;

import org.geoserver.rest.MapResource;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.AutoXMLFormat;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class FolderListFinder extends Finder {

    private DataConfig myDataConfig;

    public void setDataConfig(DataConfig dc){
        myDataConfig = dc;
    }

    public DataConfig getDataConfig(){
        return myDataConfig;
    }

    public Resource findTarget(Request request, Response response){
        Resource r = new FolderList();
        r.init(getContext(), request, response);
        return r;
    }

    protected class FolderList extends MapResource{

        public Map getSupportedFormats() {
            Map m = new HashMap();
            m.put("html",
                    new FreemarkerFormat(
                        "HTMLTemplates/datastores.ftl",
                        getClass(),
                        MediaType.TEXT_HTML)
                 );
            m.put("json", new JSONFormat());
            m.put("xml", new AutoXMLFormat("Folders"));
            m.put(null, m.get("html"));
            LOG.info("Setup format map for Folder list: " + m);
            return m;
        }

        public Map getMap() {
            Map m = new HashMap();
            List l = new ArrayList();
            l.addAll(getDataConfig().getDataFormatIds());

            Iterator it = getDataConfig().getDataStores().values().iterator();
            while(it.hasNext()){
                DataStoreConfig dsc = (DataStoreConfig)it.next();
                l.add(dsc.getId());
            }

            Collections.sort(l);
            m.put("Layers", l);
            
            return m;
        }

        // TODO: POST support for folders/ url
    }
}
