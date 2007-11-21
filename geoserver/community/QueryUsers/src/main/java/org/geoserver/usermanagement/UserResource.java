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
public class UserResource extends MapResource {

    private EditableUserDAO myUserService;
    private String myUserName;

    public UserResource(Context context,
	    Request request,
	    Response response,
	    EditableUserDAO eud){
	super(context, request, response);
	myUserName = (String)request.getAttributes().get("name");
	myUserService = eud;
    }

    public Map getSupportedFormats(){
	Map theMap = new HashMap();
	theMap.put("json", new JSONFormat());
	theMap.put("html", new HTMLFormat("HTMLTemplates/user.ftl"));
	theMap.put("xml", new XMLFormat());
	theMap.put(null, theMap.get("html"));
	return theMap;
    }

    public boolean allowGet() {
	return true;
    }

    public Map getMap(){
	return getUserInfo(myUserName);
    }
    

    public boolean allowPut() {
	return true;
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
