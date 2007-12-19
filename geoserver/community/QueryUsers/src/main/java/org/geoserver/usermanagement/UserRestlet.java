/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.usermanagement;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.userdetails.memory.UserAttribute;
import org.acegisecurity.userdetails.memory.UserAttributeEditor;
import org.geoserver.security.EditableUserDAO;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.StringRepresentation;
import org.springframework.dao.DataAccessException;


/**
 * The UserRestlet provides the basic user query and editing features of the
 * user management API.  Basically, it handles the requests that actually deal
 * with users.
 * @note This class is currently not handling requests; see UserResource instead.
 * @author David Winslow <dwinslow@openplans.org>
 */
public class UserRestlet extends Restlet {
    private EditableUserDAO myUserService;

    /**
     * Currently, the UserRestlet constructor requires an EditableUserDAO rather
     * than any UserDetailsService.  Maybe it would make sense to have it hide the
     * user modification features when using other UserDetailsServices instead?
     * @param eud the EditableUserDAO to use for retrieving user information
     */
    public UserRestlet(EditableUserDAO eud) {
        myUserService = eud;
    }

    public void handle(Request request, Response response) {
        // what to do?
        String username = request.getAttributes().get("name").toString();

        if (request.getMethod().equals(Method.PUT)) {
            String roles;

            try {
                roles = request.getEntity().getText();

                UserAttributeEditor uae = new UserAttributeEditor();
                uae.setAsText(roles);
                myUserService.setUserDetails(username, (UserAttribute) uae.getValue());
            } catch (Exception e) {
                e.printStackTrace();
                roles = "failure";
            }

            response.setEntity(new StringRepresentation(roles, MediaType.TEXT_PLAIN));
        } else if (request.getMethod().equals(Method.GET)) {
            response.setEntity(new StringRepresentation(fetchDetailsByUserName(username),
                    MediaType.TEXT_PLAIN));
        } else if (request.getMethod().equals(Method.DELETE)) {
            String message;

            try {
                myUserService.deleteUser(username);
                message = username + " deleted";
            } catch (Exception e) {
                message = "couldn't delete " + username;
            }

            response.setEntity(new StringRepresentation(message, MediaType.TEXT_PLAIN));
        } else {
            response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }
    }

    /**
     * Get user information from the UserDetailsService and return it as a String
     * containing the granted authorities for the user.
     * @param username the name of the user whose details are to be fetched
     */
    private String fetchDetailsByUserName(String username) {
        String message = "Error fetching user details"; // should never be displayed 

        try {
            UserDetails user = myUserService.loadUserByUsername(username);
            GrantedAuthority[] auths = user.getAuthorities();
            message = user.getUsername() + ": ";

            for (int i = 0; i < auths.length; i++) {
                message += (auths[i].toString() + "; ");
            }
        } catch (UsernameNotFoundException unfe) {
            message = "User " + username + " does not exist.";
        } catch (DataAccessException dae) {
            message = "Could not access database, please try again later.";
        }

        return message;
    }
}
