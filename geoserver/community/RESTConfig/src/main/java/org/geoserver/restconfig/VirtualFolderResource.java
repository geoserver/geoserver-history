package org.geoserver.restconfig;

import org.geoserver.rest.MapResource;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.JSONFormat;

import org.restlet.data.MediaType;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class VirtualFolderResource extends MapResource {
    Map myMappedObjects;

    public VirtualFolderResource(Object o){
        if (o instanceof Map)
            myMappedObjects = (Map)o;
    }

    public Map getSupportedFormats(){
        HashMap m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/vfolder.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("xml", new AutoXMLFormat());
        m.put("json", new JSONFormat());
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap(){
        Map m = new HashMap();
        List l = new ArrayList();
        if (myMappedObjects != null) l.addAll(myMappedObjects.keySet());
        m.put("Layers", l);
        return m;
    }
}
