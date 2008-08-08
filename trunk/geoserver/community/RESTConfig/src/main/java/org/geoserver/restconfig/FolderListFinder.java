/* Copyright (c) 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import org.restlet.Finder;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.MediaType;
import org.restlet.resource.Resource;

import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.CoverageStoreConfig;

import org.geoserver.rest.MapResource;
import org.geoserver.rest.DataFormat;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.AutoXMLFormat;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;


public class FolderListFinder extends Finder {

    private DataConfig myDataConfig;
    private static Set FILE_DS_TYPES;
    private static Set FILE_CS_TYPES;

    static {
        FILE_DS_TYPES = new HashSet();
        FILE_DS_TYPES.add("Shapefile");

        FILE_CS_TYPES = new HashSet();
        FILE_CS_TYPES.add("GeoTIFF"); // TODO: Flesh out these lists properly.  Can we do it programmatically instead of hardcoding?
    }

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
        private Map myPostFormats;
        private Set myFileStoreTypes; // Display names of datastores that correspond to single files on the filesystem

        public FolderList(){
            super();
            myPostFormats = new HashMap();
            myPostFormats.put(MediaType.TEXT_XML, new AutoXMLFormat());
            myPostFormats.put(MediaType.APPLICATION_JSON, new JSONFormat());
            myPostFormats.put(null, myPostFormats.get(MediaType.TEXT_XML));
        }

        public Map getSupportedFormats() {
            Map m = new HashMap();
            m.put("html",
                    new FreemarkerFormat(
                        "HTMLTemplates/folders.ftl",
                        getClass(),
                        MediaType.TEXT_HTML)
                 );
            m.put("json", new JSONFormat());
            m.put("xml", new AutoXMLFormat("Folders"));
            m.put(null, m.get("html"));
            return m;
        }

        public Map getMap() {
            Map m = new HashMap();
            List l = new ArrayList();
            Map folders = getVirtualFolderMap(getDataConfig());
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
//            LOG.info("File layers:" + folders);
//
//            l.addAll(folders.keySet());
            
            l.addAll(folders.keySet());
            Collections.sort(l);
            m.put("Layers", l);
            
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
            Map m = (Map)format.readRepresentation(getRequest().getEntity());
            LOG.info("Read data as: " + m);
        }
    }

    private static String findParentPath(String value){
        int lastSlash = value.lastIndexOf("/");
        value = (lastSlash == -1) ? value : value.substring(0, lastSlash);
        lastSlash = value.lastIndexOf("/");
        value = value.substring(lastSlash + 1);
        return value;
    }

    public static Map getVirtualFolderMap(DataConfig dc){
        Map folders = new HashMap();
        Iterator it =  dc.getDataStores().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            DataStoreConfig dsc = (DataStoreConfig)entry.getValue();
            if (FILE_DS_TYPES.contains(dsc.getFactory().getDisplayName())
                    && dsc.getConnectionParams().get("url") != null) {
                String value = dsc.getConnectionParams().get("url").toString();
                value = findParentPath(value);
                Map files = (Map)folders.get(value);
                files = (files == null ? new HashMap() : files);
                files.put(dsc.getId(), dsc);
                folders.put(value, files);
            } else {
                folders.put(entry.getKey(), entry.getValue());
            }
        }

        it = dc.getDataFormats().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            CoverageStoreConfig csc = (CoverageStoreConfig)entry.getValue();
            // if (FILE_CS_TYPES.contains(csc.getFactory().getName());
            String path = findParentPath(csc.getUrl());
            Map files = (Map)folders.get(path);
            files = (files == null ? new HashMap() : files);
            files.put(csc.getId(), csc);
            folders.put(path, files);
        }

        return folders;
    }
}
