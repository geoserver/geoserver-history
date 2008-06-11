package org.geoserver.geosearch;

import org.restlet.Restlet;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Method;
import org.restlet.data.MediaType;
import org.restlet.data.Status;

import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.NameSpaceInfo;
import org.vfny.geoserver.config.DataConfig;
import org.geoserver.ows.util.RequestUtils;

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

    public String getBaseURL(Request req){
        String baseURL = req.getResourceRef().getBaseRef().toString();
        baseURL = RequestUtils.proxifiedBaseURL(baseURL, getGeoServer().getProxyBaseUrl());
        baseURL = getParentUrl(baseURL);
        return baseURL;
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
