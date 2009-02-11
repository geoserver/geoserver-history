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
    protected List<DataFormat> createSupportedFormats(Request request,
            Response response) {
        List l = new ArrayList();

        l.add(new FreemarkerFormat("HTMLTemplates/vfolder.ftl", getClass(), MediaType.TEXT_HTML));
        l.add(new MapXMLFormat());
        l.add(new MapJSONFormat());

        return l;
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
