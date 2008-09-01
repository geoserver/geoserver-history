/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.StringRepresentation;


/**
  * The DummyRestlet is a very simple restlet that doesn't do anything useful.
  * I use it to verify my interpretation of the Restlet API docs without putting
  * weird and incorrect code into important classes.
  * @author David Winslow <dwinslow@openplans.org>
  */
public class DummyRestlet extends Restlet {
    public DummyRestlet() {
    }

    public void handle(Request request, Response response) {
        String message = "<html><head><title>hello</title></head><body>";

        message += ("Base ref: " + request.getResourceRef().getBaseRef() + "<br>");

        message += request.getMethod().getName();

        message += "</body></html>";
        response.setEntity(new StringRepresentation(message, MediaType.TEXT_HTML));
    }
}
