package org.geoserver.usermanagement;

import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.StringRepresentation;
import org.springframework.context.ApplicationContext;

/**
  * The DummyRestlet is a very simple restlet that doesn't do anything useful.
  * I use it to verify my interpretation of the Restlet API docs without putting
  * weird and incorrect code into important classes.
  * @author David Winslow <dwinslow@openplans.org>
  */
public class DummyRestlet extends Restlet {
	ApplicationContext spring;

	public DummyRestlet(ApplicationContext sc) {
		spring = sc;
	}

	public void handle(Request request, Response response) {
		String message = "<html><head><title>hello</title></head><body>";
		
		
		message += "Base ref: " + request.getResourceRef().getBaseRef()
				+ "<br>";

		message += request.getMethod().getName();
		
		message += "</body></html>";
		response.setEntity(new StringRepresentation(message,
				MediaType.TEXT_HTML));
	}
}
