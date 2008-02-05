/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.usermanagement;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.userdetails.memory.UserAttribute;
import org.acegisecurity.userdetails.memory.UserAttributeEditor;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.MapResource;
import org.geoserver.security.EditableUserDAO;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.OutputRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.springframework.dao.DataAccessException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * First stab at representing user accounts as Restlet Resource objects.
 *
 * @author David Winslow <dwinslow@openplans.org>
 */
public class UserResource extends MapResource {
    private EditableUserDAO myUserService;

    public void setUserDAO(EditableUserDAO dao){
        myUserService = dao;
    }

    public EditableUserDAO getUserDAO(){
        return myUserService;
    }

    public Map getSupportedFormats() {
        Map theMap = new HashMap();
        theMap.put("json", new JSONFormat());
        theMap.put("html", new UserHTMLFormat("HTMLTemplates/user.ftl"));
        theMap.put("xml", new UserXMLFormat("XMLTemplates/user.ftl"));
        theMap.put(null, theMap.get("html"));

        return theMap;
    }

    public boolean allowGet() {
        return true;
    }

    public Map getMap() {
        String username = (String)getRequest().getAttributes().get("user");
        return getUserInfo(username);
    }

    public boolean allowPut() {
        return true;
    }

    protected void putMap(Map details) throws Exception {
        String username = (String)getRequest().getAttributes().get("user");
        UserAttribute attr = new UserAttribute();
        attr.setPassword(details.get("password").toString());
        attr.setEnabled(true);
        attr.setAuthoritiesAsString((List) details.get("roles"));

        myUserService.setUserDetails(username, attr);
    }

    public boolean allowDelete() {
        return true;
    }

    public void handleDelete() {
        String username = (String) getRequest().getAttributes().get("user");
        UserDetails details = myUserService.loadUserByUsername(username);

        if (details != null) {
            try {
                myUserService.deleteUser(username);
                getResponse()
                    .setEntity(new StringRepresentation(username + " deleted",
                        MediaType.TEXT_PLAIN));
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
    private Map getUserInfo(String name) {
        Map info = new HashMap();

        UserDetails user = myUserService.loadUserByUsername(name);

        if (user == null) {
            return null;
        }

        // info.put("name", name);
        info.put("password", user.getPassword());

        List roles = new ArrayList();
        GrantedAuthority[] auths = user.getAuthorities();

        for (int i = 0; i < auths.length; i++) {
            roles.add(auths[i].toString());
        }

        info.put("roles", roles);

        return info;
    }

    /**
     * TODO: Actually document this.
     * @author David Winslow
     */
    private List getAllUserInfo() {
        List users = new ArrayList();

        Iterator it = myUserService.getNameSet().iterator();

        while (it.hasNext()) {
            users.add(getUserInfo(it.next().toString()));
        }

        return users;
    }
}
