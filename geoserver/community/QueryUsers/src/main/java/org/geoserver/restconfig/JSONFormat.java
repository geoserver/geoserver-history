/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.resource.OutputRepresentation;
import org.restlet.resource.Representation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * DataFormat for JSON output/input.
 * @author David Winslow <dwinslow@openplans.org>
 */
public class JSONFormat implements DataFormat {
    public Representation makeRepresentation(final Map map) {
        return new OutputRepresentation(MediaType.APPLICATION_JSON) {
                public void write(OutputStream os) {
                    try {
                        Iterator it = map.entrySet().iterator();

                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            System.out.println(entry.getKey() + " : " + entry.getValue());
                        }

                        Writer outWriter = new BufferedWriter(new OutputStreamWriter(os));
                        map.remove("page");

                        outWriter.flush();

                        JSONObject json = new JSONObject();
                        json.accumulateAll(map);
                        /*            if (obj instanceof JSONObject){
                                        json = (JSONObject)obj;
                                    } else {
                                        json = new JSONObject();
                                        json.put("context", obj);
                                    } */
                        json.write(outWriter);
                        outWriter.flush();
                    } catch (Exception e) {
                        try {
                            // how to handle?
                            os.write(("Couldn't write json because of " + e.toString()).getBytes());
                        } catch (IOException ioe) {
                        }
                    }
                }

                /* public Object toJSONObject(Object obj){
                    if (obj instanceof Map){
                        Map m = (Map) obj;
                        JSONObject json = new JSONObject();
                        Iterator it = m.entrySet().iterator();
                        while (it.hasNext()){
                            Map.Entry entry = (Map.Entry)it.next();
                            json.put((String)entry.getKey(), toJSONObject(entry.getValue()));
                        }
                        return json;
                    } else if (obj instanceof Collection){
                        Collection col = (Collection)obj;
                        JSONArray json = new JSONArray();
                        Iterator it = col.iterator();
                        while (it.hasNext()){
                            json.put(toJSONObject(it.next()));
                        }
                        return json;
                    } else {
                        return obj.toString();
                    }
                } */
            };
    }

    public Map readRepresentation(Representation rep) {
        try {
            JSONObject obj = JSONObject.fromObject(rep.getText());

            return obj;

            /*
            Object maybeMap = toMap(obj);
            if (maybeMap instanceof Map){
                return (Map) maybeMap;
            }

            // TODO: figure out what to do rather than this kind of arbitrary thing
            Map map = new HashMap();
            map.put("context", maybeMap);

            return map; */
        } catch (IOException ioe) {
            return new HashMap();
        }
    }

    /*
        protected Object toMap(Object json){
            if (json instanceof JSONObject){
                Map m = new HashMap();
                Iterator it = ((JSONObject)json).keys();
                while (it.hasNext()){
                    String key = (String)it.next();
                    m.put(key, toMap(((JSONObject)json).get(key)));
                }
                return m;
            } else if (json instanceof JSONArray){
                List l = new ArrayList();
                for (int i = 0; i < ((JSONArray)json).length(); i++){
                    l.add(toMap(((JSONArray)json).get(i)));
                }
                return l;
            } else if (json instanceof JSONNull){
                return null;
            } else {
                return json;
            }
        } */
}
