/* Copyright (c) 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import org.restlet.Context;
import org.restlet.Finder;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.MediaType;
import org.restlet.resource.Resource;

import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.config.DataConfig;

import org.geoserver.rest.MapResource;
import org.geoserver.rest.format.DataFormat;
import org.geoserver.rest.format.FreemarkerFormat;
import org.geoserver.rest.format.MapJSONFormat;
import org.geoserver.rest.format.MapXMLFormat;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
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
        Resource r = new FolderList(getContext(),request,response);
        r.init(getContext(), request, response);
        return r;
    }

    protected class FolderList extends MapResource{
        private Map myPostFormats;
        private Set myFileStoreTypes; // Display names of datastores that correspond to single files on the filesystem

        public FolderList(Context context, Request request, Response response){
            super(context,request,response);
            myPostFormats = new HashMap();
            myPostFormats.put(MediaType.TEXT_XML, new MapXMLFormat());
            myPostFormats.put(MediaType.APPLICATION_JSON, new MapJSONFormat());
            myPostFormats.put(null, myPostFormats.get(MediaType.TEXT_XML));
        }

        @Override
        protected Map<String, DataFormat> createSupportedFormats(
                Request request, Response response) {
            Map m = new HashMap();
            m.put("html",
                    new FreemarkerFormat(
                        "HTMLTemplates/folders.ftl",
                        getClass(),
                        MediaType.TEXT_HTML)
                 );
            m.put("json", new MapJSONFormat());
            m.put("xml", new MapXMLFormat("Folders"));
            m.put(null, m.get("html"));
            return m;
        }

        public Map getMap() {
            Map m = new HashMap();
            List l = new ArrayList();
            Map folders = RESTUtils.getVirtualFolderMap(getDataConfig());
//            l.addAll(getDataConfig().getDataFormatIds());
//
//            Iterator it = getDataConfig().getDataStores().values().iterator();
//
//            Map folders = new HashMap(); 
//            while(it.hasNext()){
//                DataStoreConfig dsc = (DataStoreConfig)it.next();
//                if(myFileStoreTypes.contains(dsc.getFactory().getDisplayName()) 
//                        && dsc.getConnectionParams().get("url") != null) {
//                    String value = dsc.getConnectionParams().get("url").toString();
//                    int lastSlash = value.lastIndexOf("/");
//                    value = (lastSlash == -1) ? value : value.substring(0, lastSlash);
//                    lastSlash = value.lastIndexOf("/");
//                    value = value.substring(lastSlash + 1);
//                    String parent = findParentPath(value);
//                    Set files = (Set)folders.get(parent);
//                    files = (files == null ? new HashSet() : files);
//                    files.add(dsc);
//                    folders.put(parent, files);
//                } else {
//                    l.add(dsc.getId());
//                }              
//            }
//
//            LOGGER.info("File layers:" + folders);
//
//            l.addAll(folders.keySet());
            
            l.addAll(folders.keySet());
            Collections.sort(l);
            m.put("Layers", l);
            m.put("page", getPageInfo());
            return m;
        }

        public boolean allowPost(){
            return true;
        }

        // TODO: POST support for folders/ url
        public void handlePost(){
            MediaType type = getRequest().getEntity().getMediaType();
            LOG.info("Folder posted, mediatype is:" + type);
            DataFormat format = (DataFormat)myPostFormats.get(type);
            LOG.info("Using post format: " + format);
            Map m = (Map)format.toObject(getRequest().getEntity());
            LOG.info("Read data as: " + m);
        }
    }
}
