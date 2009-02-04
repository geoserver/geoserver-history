package org.geoserver.restconfig;

import org.geoserver.rest.MapResource;
import org.geoserver.rest.format.DataFormat;
import org.geoserver.rest.format.FreemarkerFormat;
import org.geoserver.rest.format.MapJSONFormat;
import org.geoserver.rest.format.MapXMLFormat;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class VirtualFolderResource extends MapResource {
    Map myMappedObjects;

    public VirtualFolderResource(Object o, Context context, Request request, Response response){
        super( context, request, response );
        if (o instanceof Map)
            myMappedObjects = (Map)o;
    }

    @Override
    protected Map<String, DataFormat> createSupportedFormats(Request request,
            Response response) {
        HashMap m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/vfolder.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("xml", new MapXMLFormat());
        m.put("json", new MapJSONFormat());
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap(){
        Map m = new HashMap();
        List l = new ArrayList();
        if (myMappedObjects != null) l.addAll(myMappedObjects.keySet());
        m.put("Layers", l);
        m.put("page", getPageInfo());
        return m;
    }
}
