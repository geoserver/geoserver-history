package org.geoserver.rest;

import org.restlet.Restlet;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

public class CatchAllRestlet extends Restlet{
    public void handle(Request req, Response res){
        res.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
    } 
}
