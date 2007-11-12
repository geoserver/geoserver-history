/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.usermanagement;

import org.restlet.Context;
import org.restlet.Finder;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

import org.geoserver.security.EditableUserDAO;

/**
 * A Finder for obtaining UserResources.
 * @author David Winslow <dwinslow@openplans.org>
 */
public class UserFinder extends Finder {

  private EditableUserDAO myUserService;

  public UserFinder(Context context, EditableUserDAO eud){
    super(context);
    myUserService = eud;
  }

  public Resource findTarget(Request request, Response response) {
    return new UserResource(getContext(), request, response, myUserService);
  }
}
