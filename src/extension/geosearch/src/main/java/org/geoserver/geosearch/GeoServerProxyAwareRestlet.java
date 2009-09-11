package org.geoserver.geosearch;

import org.geoserver.config.GeoServer;
import org.geoserver.ows.util.RequestUtils;

import org.restlet.Restlet;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Method;
import org.restlet.data.MediaType;
import org.restlet.data.Status;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class GeoServerProxyAwareRestlet extends Restlet {

    private GeoServer myGeoserver;
    private Namespace SITEMAP = Namespace.getNamespace("http://www.sitemaps.org/schemas/sitemap/0.9");

    public GeoServer getGeoServer(){
        return myGeoserver;
    }

    public void setGeoServer(GeoServer gs){
        myGeoserver = gs;
    }

    public String getBaseURL(Request req) {
        return req.getRootRef().getParentRef().toString();
        
    }

    public static String getParentUrl(String url){
        while (url.endsWith("/")){
            url = url.substring(0, url.length() - 1);
        }

        int lastSlash = url.lastIndexOf('/');
        if (lastSlash != -1){
            url = url.substring(0, lastSlash);
        }

        return url;
    }
}
