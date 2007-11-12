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
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;

import org.springframework.dao.DataAccessException;

import org.geoserver.security.EditableUserDAO;
import org.geoserver.restconfig.HTMLTemplate;

/**
 * First stab at representing user accounts as Restlet Resource objects.
 * 
 * @author David Winslow <dwinslow@openplans.org>
 */
public class UserResource extends Resource {

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

  public boolean allowGet() {
    return true;
  }

  public void handleGet() {
    if (myUserName == null) {
      List userList = getAllUserInfo();
      Map context = new HashMap();
      context.put("currentURL", getRequest().getResourceRef().getBaseRef());
      context.put("users", userList);
      getResponse().setEntity(
            HTMLTemplate.getHtmlRepresentation("HTMLTemplates/users.ftl", context)
	  );
    } else {
      Map details = getUserInfo(myUserName);

      if (details != null){		
	getResponse().setEntity(
	      HTMLTemplate.getHtmlRepresentation("HTMLTemplates/user.ftl", details)
	    );
      } else {
	getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
	getResponse().setEntity("Couldn't find requested resource", MediaType.TEXT_PLAIN);
      } 
    }
  }

  public boolean allowPut() {
    return true;
  }

  public void handlePut(){
    UserAttribute details = interpret(getRequest().getEntity());
    if (details != null){
      try{
	myUserService.setUserDetails(myUserName, details);
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
    info.put("name", name);
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
