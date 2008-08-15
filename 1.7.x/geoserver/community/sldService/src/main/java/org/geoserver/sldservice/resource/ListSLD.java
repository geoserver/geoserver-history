package org.geoserver.sldservice.resource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.global.Data;

public class ListSLD extends Resource {
    private Data dt;

    public ListSLD(Data dt) {
        this.dt = dt;
    }

    public void handleGet() {

        String message = this.xmlSldList(getRequest());
        getResponse().setEntity(
                new StringRepresentation(message, MediaType.TEXT_XML));
    }

    private String xmlSldList(Request req) {
        XMLSerializer xml = new XMLSerializer();
        xml.setRootName("slds");
        return xml.write(this.jsonSldList(req));
    }

    private JSONObject jsonSldList(Request req) {
        Reference ref = req.getResourceRef();
        String bUrl = ref.toString();
        Map out = new HashMap();
        Map styles = dt.getStyles();
        Iterator it = styles.keySet().iterator();
        String name;

        while (it.hasNext()) {
            name = (String) it.next();
            out.put(name, bUrl + "/" + name);
        }
        JSONObject json = JSONObject.fromObject(out);
        return json;
    }

}
