package org.geoserver.geosearch;

import org.restlet.Restlet;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Method;
import org.restlet.data.MediaType;
import org.restlet.data.Status;

import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.NameSpaceInfo;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.rest.DataFormat;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.RestletException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class LayerAboutPage extends GeoServerProxyAwareRestlet {
    private final DataFormat format =
        new FreemarkerFormat("layerpage.ftl", getClass(), MediaType.TEXT_HTML);

    private Data catalog;

    public void setCatalog(Data cat){
        catalog = cat;
    }

    public Data getCatalog(){
        return catalog;
    }

    public void handle(Request request, Response response){
        if (request.getMethod().equals(Method.GET)) doGet(request, response);
        else response.setStatus(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    }

    public void doGet(Request request, Response response){
        String namespace = (String)request.getAttributes().get("namespace");
        String layer = (String)request.getAttributes().get("layer");

        response.setEntity(format.makeRepresentation(lookupType(namespace, layer)));
    }

    private FeatureTypeInfo lookupType(String namespace, String layer){

        NameSpaceInfo ns = catalog.getNameSpace(namespace);
        if ( ns == null ) {
            throw new RestletException(
                    "No such namespace: " + namespace, Status.CLIENT_ERROR_NOT_FOUND 
                    );
        }

        FeatureTypeInfo featureType = null;
        try {
            featureType = catalog.getFeatureTypeInfo(layer,ns.getUri());
        } catch( Exception e ) {
            throw new RestletException(
                    "No such featuretype: " + namespace + ":" + layer,
                     Status.CLIENT_ERROR_NOT_FOUND 
                    );
        }
        return featureType;
    }
}
