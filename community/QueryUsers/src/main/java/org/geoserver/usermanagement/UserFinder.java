package org.geoserver.usermanagement;

import org.restlet.Context;
import org.restlet.Finder;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

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
