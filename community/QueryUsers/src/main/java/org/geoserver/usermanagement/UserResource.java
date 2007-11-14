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
 * First stab at representing user accounts as Restlet Resource objects.
 * 
 * @author David Winslow <dwinslow@openplans.org>
 */
public class UserResource extends Resource {

    private EditableUserDAO myUserService;
    private String myUserName;
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


			Map map = new HashMap();
			map.put("context", maybeMap); // TODO: figure out what to do rather than this kind of arbitrary thing

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

    public UserResource(Context context,
	    Request request,
	    Response response,
	    EditableUserDAO eud){
	super(context, request, response);
	myUserName = (String)request.getAttributes().get("name");
	myUserService = eud;
	myRequestFormat = (DataFormat)myFormatMap.get(request.getAttributes().get("type"));
    }

    public boolean allowGet() {
	return true;
    }

    public void handleGet() {


	if (myRequestFormat == null || 
		myUserName != null && 
		getUserInfo(myUserName) == null) {
	    getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
	    getResponse().setEntity(new StringRepresentation("Could not find requested resource; format was "+ getRequest().getAttributes().get("type"), MediaType.TEXT_PLAIN));
	    return;
	}
	
	if (myUserName == null) {
	    List userList = getAllUserInfo();
	    Map context = new HashMap();
	    context.put("currentURL", getRequest().getResourceRef().getBaseRef());
	    context.put("users", userList);
	    getResponse().setEntity(myRequestFormat.makeRepresentation(context));
	} else {
	    Map details = getUserInfo(myUserName); 
	    if (details != null){		
		getResponse().setEntity(myRequestFormat.makeRepresentation(details));
	    }
	}
    }

    public boolean allowPut() {
	return true;
    }

    public void handlePut(){
	if (myRequestFormat == null || 
		myUserName == null ) {
	    getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
	    getResponse().setEntity(new StringRepresentation("Could not find requested resource", MediaType.TEXT_PLAIN));
	    return;
	}

	Map details = myRequestFormat.readRepresentation(getRequest().getEntity());
	if (details != null){
	    try{
		putMap(details);
		getResponse().setStatus(Status.REDIRECTION_FOUND);
		getResponse().setRedirectRef(this.generateRef(""));
	    } catch (Exception e){
		e.printStackTrace();
		getResponse().setEntity(e.toString(), MediaType.TEXT_PLAIN);
		getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
	    }
	} else {
	    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
	    getResponse().setEntity("Request was badly formatted",
		    MediaType.TEXT_PLAIN);
	}
    }

    protected void putMap(Map details) throws Exception {
	UserAttribute attr = new UserAttribute();
	attr.setPassword(details.get("password").toString());
	attr.setEnabled(true);
	attr.setAuthoritiesAsString((List)details.get("roles"));

	myUserService.setUserDetails(myUserName, attr);
    }

    public boolean allowDelete() {
	return true;	
    }

    public void handleDelete(){
	UserDetails details = myUserService.loadUserByUsername(myUserName);
	if (details != null) {
	    try {
		myUserService.deleteUser(myUserName);
		getResponse().setEntity(
			new StringRepresentation(
			    myUserName + " deleted",
			    MediaType.TEXT_PLAIN)
			);
	    } catch (Exception e) {
		e.printStackTrace();
		getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
	    }
	} else {
	    getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
	    getResponse().setEntity("Couldn't find requested resource", MediaType.TEXT_PLAIN);

	}
    }


    /**
     * Convert from a Restlet Representation to a UserAttribute object, which can then be inserted into the DAO. 
     * @param Representation a Representation sent to this Restlet
     * @return a UserAttribute containing information from the request, or null if the format is invalid.
     */
    private UserAttribute interpret(Representation entity) {
	UserAttribute user = null;

	if (entity.getMediaType() == null ||
		entity.getMediaType().equals(MediaType.TEXT_PLAIN)){
	    UserAttributeEditor uae = new UserAttributeEditor();
	    try {
		uae.setAsText(entity.getText());
		user = (UserAttribute)uae.getValue();
	    } catch (IOException ioe){
		ioe.printStackTrace();
	    }
	}

	return user;
    }

    /**
     * Get user information from the UserDetailsService and return it as a String
     * containing the granted authorities for the user.
     */
    private String fetchDetailsByUserName(String username) {
	String message = "If you see this someone screwed up";

	try {
	    UserDetails user = myUserService.loadUserByUsername(username);
	    GrantedAuthority[] auths = user.getAuthorities();
	    message = user.getUsername() + ": ";
	    for (int i = 0; i < auths.length; i++) {
		message += auths[i].toString() + "; ";
	    }
	} catch (UsernameNotFoundException unfe) {
	    message = "User " + username + " does not exist.";
	} catch (DataAccessException dae) {
	    message = "Could not access database, please try again later.";
	}

	return message;
    }

    /**
     * TODO: Actually document this.
     * @author David Winslow
     */
    private String fetchUserList(){
	StringBuffer buff = new StringBuffer();
	Iterator it = myUserService.getNameSet().iterator();
	while (it.hasNext()){
	    buff.append(it.next().toString());
	}
	return buff.toString();
    }

    /**
     * TODO: Actually document this.
     * @author David Winslow
     */
    private Map getUserInfo(String name){
	Map info = new HashMap();

	UserDetails user = myUserService.loadUserByUsername(name);
	if (user == null) return null;
	// info.put("name", name);
	info.put("password", user.getPassword());

	List roles = new ArrayList();
	GrantedAuthority[] auths = user.getAuthorities();

	for (int i = 0; i < auths.length; i++){
	    roles.add(auths[i].toString());
	}

	info.put("roles", roles);


	return info;
    }

    /**
     * TODO: Actually document this.
     * @author David Winslow
     */
    private List getAllUserInfo(){
	List users = new ArrayList();

	Iterator it = myUserService.getNameSet().iterator();
	while (it.hasNext()){
	    users.add(getUserInfo(it.next().toString()));
	}

	return users;
    }

}
