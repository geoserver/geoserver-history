

package org.geoserver.restconfig;

import org.restlet.resource.Resource;
import org.restlet.resource.InputRepresentation;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.MediaType;
import org.restlet.Router;
import org.restlet.Route;
import org.restlet.Context;

import java.io.InputStream;
import java.io.BufferedInputStream;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

class IndexResource extends MapResource{

    private Router myRouter;

    public IndexResource(Context con, Request req, Response res, Router router){
	super(con, req, res);
	myRouter = router;
    }

    public Map getSupportedFormats(){
	Map m = new HashMap();

	m.put("html", new HTMLFormat("HTMLTemplates/index.ftl"));
	m.put(null, m.get("html"));

	return m;
    }

    public Map getMap(){
	Map m = new HashMap();
	m.put("links", getLinkList());
	return m;
    }

    private List getLinkList(){
	List l = new ArrayList();

	Iterator it = myRouter.getRoutes().iterator();
	while (it.hasNext()){
	    Route r = (Route)it.next();
	    String pattern = r.getTemplate().getPattern();
	    if (!pattern.contains("{") && pattern.length() > 1){
		l.add(pattern.substring(1)); // trim leading slash
	    }
	}

	return l;
    }

}
