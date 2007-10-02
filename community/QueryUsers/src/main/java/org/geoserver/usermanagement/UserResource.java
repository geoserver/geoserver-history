package org.geoserver.usermanagement;

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
		myUserName = request.getAttributes().get("name").toString();
		myUserService = eud;
	}

	public boolean allowGet() {
		return true;
	}
	
	public void handleGet() {
		UserDetails details = myUserService.loadUserByUsername(myUserName);
		
		if (details != null){		
			getResponse().setEntity(
				new StringRepresentation(fetchDetailsByUserName(myUserName), 
						MediaType.TEXT_PLAIN)
				);
		} else {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			getResponse().setEntity("Couldn't find requested resource", MediaType.TEXT_PLAIN);
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
	
	
}
