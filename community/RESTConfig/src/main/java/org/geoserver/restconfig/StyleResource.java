package org.geoserver.restconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.FileRepresentation;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;

/**
 * Restlet for Style resources
 * 
 * @author David Winslow <dwinslow@openplans.org> , The Open Planning Project
 */
class StyleResource extends Resource {
    private DataConfig myDC;

    public StyleResource(Context context,
	    Request request,
	    Response response, 
	    DataConfig myDataConfig) {
        super(context, request, response);
        myDC = myDataConfig;
    }

    public void handleGet() {
	MediaType mt = null;
	Request req = getRequest();

	String styleName = (String)req.getAttributes().get("style");
	StyleConfig sc =(StyleConfig) myDC.getStyles().get(styleName);
	if (sc != null){
	    getResponse().setEntity(new FileRepresentation(sc.getFilename(), MediaType.APPLICATION_XML, 10));
	} else {
	    getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
	    getResponse().setEntity(new StringRepresentation("Error - Couldn't find the requested resource", MediaType.TEXT_PLAIN));
	}
    }
}
