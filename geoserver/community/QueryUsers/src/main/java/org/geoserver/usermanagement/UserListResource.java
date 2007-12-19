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
import org.geoserver.restconfig.HTMLFormat;
import org.geoserver.restconfig.JSONFormat;
import org.geoserver.restconfig.MapResource;
import org.geoserver.restconfig.XMLFormat;
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
 * Resource for the user list.
 *
 * @author David Winslow <dwinslow@openplans.org>
 */
public class UserListResource extends MapResource {
    private EditableUserDAO myUserService;

    public UserListResource(Context context, Request request, Response response, EditableUserDAO eud) {
        super(context, request, response);
        myUserService = eud;
    }

    public Map getSupportedFormats() {
        Map theMap = new HashMap();
        theMap.put("json", new JSONFormat());
        theMap.put("html", new HTMLFormat("HTMLTemplates/users.ftl"));
        theMap.put("xml", new XMLFormat("XMLTemplates/users.ftl"));
        theMap.put(null, theMap.get("html"));

        return theMap;
    }

    public boolean allowGet() {
        return true;
    }

    public Map getMap() {
        Map theMap = new HashMap();
        theMap.put("users", getAllUserInfo());

        Iterator it = theMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
        }

        return theMap;
    }

    public boolean allowPut() {
        return false;
    }

    public boolean allowDelete() {
        return false;
    }

    /**
     * Build a list of the names of all users.
     *
     * @author David Winslow
     */
    private List getAllUserInfo() {
        List users = new ArrayList();

        Iterator it = myUserService.getNameSet().iterator();

        while (it.hasNext()) {
            users.add((it.next().toString()));
        }

        return users;
    }
}
