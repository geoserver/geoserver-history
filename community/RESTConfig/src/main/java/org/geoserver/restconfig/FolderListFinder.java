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

            myFileStoreTypes = new HashSet();
            myFileStoreTypes.add("Shapefile");
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
            l.addAll(getDataConfig().getDataFormatIds());

            Iterator it = getDataConfig().getDataStores().values().iterator();
            // Map folders = new HashMap(); // keep track of which files are in which folders (map folder->set(files))
            while(it.hasNext()){
                DataStoreConfig dsc = (DataStoreConfig)it.next();
//                if(myFileStoreTypes.contains(dsc.getFactory().getDisplayName()) && dsc.getConnectionParams().get("url") != null){
//                    String value = dsc.getConnectionParams().get("url").toString();
//                    String parent = findParentPath(value);
//                    Set files = (Set)folders.get(parent);
//                    files = (files == null ? new HashSet() : files);
//                    files.add(dsc);
//                    folders.put(parent, files);
//                } else {
                    l.add(dsc.getId());
//                }              
            }

            //LOG.info("File layers:" + folders);

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
            Map m = format.readRepresentation(getRequest().getEntity());
            LOG.info("Read data as: " + m);
        }

        private String findParentPath(String path){
            int lastSlashIndex = path.lastIndexOf("/");
            if (lastSlashIndex == -1){
                // TODO: no slashes? this seems weird
                return path;
            } else {
                return path.substring(0, lastSlashIndex);
            }
            
        }
    }
}
