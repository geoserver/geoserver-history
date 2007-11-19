/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.usermanagement;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.io.BufferedWriter;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.userdetails.memory.UserAttribute;
import org.acegisecurity.userdetails.memory.UserAttributeEditor;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.OutputRepresentation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;

import org.springframework.dao.DataAccessException;

import org.geoserver.security.EditableUserDAO;
import org.geoserver.restconfig.HTMLTemplate;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.util.JSONUtils;

/**
 * Base class that converts back and forth between varied data formats and a Java Map to
 * simplify the implementation of a RESTful web API. 
 * 
 * @author David Winslow <dwinslow@openplans.org>
 */
public abstract class MapResource extends Resource {

    private static Map myFormatMap;
    private DataFormat myRequestFormat;

    static{
	myFormatMap = new HashMap();
	DataFormat json = 
	    new DataFormat(){
		public Representation makeRepresentation(final Map map){
		    return new OutputRepresentation(MediaType.APPLICATION_JSON){
			public void write(OutputStream os){
			    try{
				Writer outWriter = new BufferedWriter(new OutputStreamWriter(os));
				(new JSONObject(map)).write(outWriter);
				outWriter.flush();
			    } catch (IOException ioe){
				// how to handle?
			    }
			}
		    };
		}
		public Map readRepresentation(Representation rep){
		    try{
			JSONObject obj = new JSONObject(rep.getText()); 
			Object maybeMap = toMap(obj);
			if (maybeMap instanceof Map){
			    return (Map) maybeMap;
			}

			// TODO: figure out what to do rather than this kind of arbitrary thing
			Map map = new HashMap();
			map.put("context", maybeMap); 

			return map; 
		    } catch (IOException ioe){
			return new HashMap();
		    }
		}

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

		}
	    };

	DataFormat html = 
	    new DataFormat(){
		public Representation makeRepresentation(Map map){
		    return HTMLTemplate.getHtmlRepresentation("HTMLTemplates/user.ftl", map); 
		}

		public Map readRepresentation(Representation rep){
		    return new HashMap();
		}
	    };

	DataFormat xml = 
	    new DataFormat(){
		public Representation makeRepresentation(Map map){
		    return new StringRepresentation("XML not implemented", MediaType.TEXT_PLAIN);
		}
		public Map readRepresentation(Representation rep){
		    return new HashMap();
		}
	    };

	myFormatMap.put("json", json);
	myFormatMap.put("html", html);
	myFormatMap.put("xml", xml);
	myFormatMap.put(null, html);
    }

    public MapResource(Context context,
	    Request request,
	    Response response
	    ){
	super(context, request, response);
	myFormatMap = getSupportedFormats();
	myRequestFormat = (DataFormat)myFormatMap.get(request.getAttributes().get("type"));
    }

    public abstract Map getSupportedFormats();

    public void handleGet() {
        Map details = getMap();

	if (myRequestFormat == null | details == null){
	    getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
	    getResponse().setEntity(
		    new StringRepresentation(
			"Could not find requested resource; format was "
			+ getRequest().getAttributes().get("type"),
			MediaType.TEXT_PLAIN)
		    );
	    return;
	}
	
	if (details != null){		
	    getResponse().setEntity(myRequestFormat.makeRepresentation(details));
	}
    }

    public Map getMap(){
	// nothing to do, this should be overridden by subclasses
	return new HashMap();
    }

    public void handlePut(){
	Map details = myRequestFormat.readRepresentation(getRequest().getEntity());

	if (myRequestFormat == null || details == null){
	    getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
	    getResponse().setEntity(
		    new StringRepresentation("Could not find  requested resource",
			MediaType.TEXT_PLAIN)
		    );
	    return;
	}

	try{
	    putMap(details);
	    getResponse().setStatus(Status.REDIRECTION_FOUND);
	    getResponse().setRedirectRef(this.generateRef(""));
	} catch (Exception e){
	    e.printStackTrace();
	    getResponse().setEntity(e.toString(), MediaType.TEXT_PLAIN);
	    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
	}
    }

    protected void putMap(Map details) throws Exception {
	// nothing to do; this should be overridden by subclasses
    }
}
