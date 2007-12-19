/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.userdetails.memory.UserAttribute;
import org.acegisecurity.userdetails.memory.UserAttributeEditor;
import org.geoserver.restconfig.HTMLTemplate;
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
 * Base class that converts back and forth between varied data formats and a Java Map to
 * simplify the implementation of a RESTful web API.
 *
 * @author David Winslow <dwinslow@openplans.org>
 */
public abstract class MapResource extends Resource {
    private static Map myFormatMap;
    private DataFormat myRequestFormat;

    public MapResource(Context context, Request request, Response response) {
        super(context, request, response);
        myFormatMap = getSupportedFormats();
        myRequestFormat = (DataFormat) myFormatMap.get(request.getAttributes().get("type"));
    }

    public abstract Map getSupportedFormats();

    public void handleGet() {
        Map details = getMap();

        if ((myRequestFormat == null) | (details == null)) {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            getResponse()
                .setEntity(new StringRepresentation(
                    "Could not find requested resource; format was "
                    + getRequest().getAttributes().get("type"), MediaType.TEXT_PLAIN));

            return;
        }

        if (details != null) {
            Map page = getPageDetails();
            details.put("page", page);
            getResponse().setEntity(myRequestFormat.makeRepresentation(details));
        }
    }

    public Map getMap() {
        // nothing to do, this should be overridden by subclasses
        return new HashMap();
    }

    public Map getPageDetails() {
        Map map = new HashMap();
        String currentURL = getRequest().getResourceRef().getBaseRef().toString();
        String formatName = (String) getRequest().getAttributes().get("type");

        if (formatName != null) {
            currentURL = currentURL.substring(0, currentURL.length() - formatName.length() - 1);
        }

        map.put("currentURL", currentURL);

        return map;
    }

    public void handlePut() {
        Map details = myRequestFormat.readRepresentation(getRequest().getEntity());

        if ((myRequestFormat == null) || (details == null)) {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            getResponse()
                .setEntity(new StringRepresentation("Could not find  requested resource",
                    MediaType.TEXT_PLAIN));

            return;
        }

        try {
            putMap(details);
            getResponse().setStatus(Status.REDIRECTION_FOUND);
            getResponse().setRedirectRef(this.generateRef(""));
        } catch (Exception e) {
            e.printStackTrace();
            getResponse().setEntity(e.toString(), MediaType.TEXT_PLAIN);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        }
    }

    protected void putMap(Map details) throws Exception {
        // nothing to do; this should be overridden by subclasses
    }
}
