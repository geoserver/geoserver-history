/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.ModelInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.MapResource;
import org.restlet.data.MediaType;

/**
 * Restlet for Model resources
 *
 * @author Alessio Fabiani <alessio.fabiani@geo-solutions.it> , GeoSolutions S.a.S.
 */
public class ModelResource extends MapResource {
    private GeoServer geoServer;
    private Catalog rawCatalog;

    public void setGeoServer(GeoServer geoServer){
        this.geoServer = geoServer;
    }

    public GeoServer getGeoServer(){
        return this.geoServer;
    }

    public void setCatalog(Catalog catalog){
        this.rawCatalog = catalog;
    }

    public Catalog getCatalog(){
        return this.rawCatalog;
    }
    
    public ModelResource(GeoServer geoServer, Catalog catalog){
        super();
        setGeoServer(geoServer);
        setCatalog(catalog);
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/model.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("Model"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap(){
        return getMap(findModelInfo());
    }

    public static Map getMap(ModelInfo theModel) {
        Map m = new HashMap();

        m.put("Abstract", (theModel.getAbstract() != null ? theModel.getAbstract() : "[None]"));
        m.put("OriginatingCenter", (theModel.getCenter() != null ? theModel.getCenter() : "[None]"));
        try {
            m.put("CRS", (theModel.getCRS() != null ? /* CRS.lookupIdentifier(theModel.getCRS(), true) */ theModel.getCRS().toWKT() : "[None]"));
        } catch (Exception e) {
            m.put("CRS", "[None]");
        }
        m.put("Description", (theModel.getDescription() != null ? theModel.getDescription() : "[None]"));
        m.put("Discipline", (theModel.getDiscipline() != null ? theModel.getDiscipline().getDescription() : "[None]"));
        m.put("GridCRS", (theModel.getGridCRS() != null ? theModel.getGridCRS() : "[None]")); 
        m.put("GridCS", (theModel.getGridCS() != null ? theModel.getGridCS() : "[None]"));
        m.put("GridLowers", getGridLowers(theModel));
        m.put("GridOffsets", getGridOffsets(theModel));
        m.put("GridOrigin", getGridOrigin(theModel));
        m.put("GridType", (theModel.getGridType() != null ? theModel.getGridType() : "[None]"));
        m.put("GridUppers", getGridUppers(theModel));
        m.put("Keywords", getKeywords(theModel));
        m.put("InitParams", getInitParams(theModel));
        m.put("Metadata", getMetadata(theModel));
        m.put("MetadataLink", getMetadataLinks(theModel));
        m.put("Name", theModel.getName());
        m.put("OutParams", getOutParams(theModel));
        m.put("Products", getProducts(theModel));
        m.put("SubCenter", (theModel.getSubCenter() != null ? theModel.getSubCenter() : "[None]"));
        m.put("Title", (theModel.getTitle() != null ? theModel.getTitle() : "[None]"));
        m.put("TypeOfData", (theModel.getTypeOfData() != null ? theModel.getTypeOfData().getDescription() : "[None]"));
        m.put("Version", (theModel.getVersion() != null ? theModel.getVersion() : "[None]"));
        m.put("VerticalCoordinateMeaning", (theModel.getVerticalCoordinateMeaning() != null ? theModel.getVerticalCoordinateMeaning() : "[None]"));

        return m;
    }

    private static Object getMetadata(ModelInfo model) {
        List l = new ArrayList();
        if(model.getMetadata() == null)
            return null;
        
        Iterator i = model.getMetadata().entrySet().iterator();
        while (i.hasNext()) {
            Entry entry = (Entry) i.next();
            l.add(entry.getKey() + "=" + entry.getValue());
        }
        
        return l;
    }

    private static Object getProducts(ModelInfo model) {
        List l = new ArrayList();
        if(model.getProducts() == null)
            return l;
        
        l.addAll(model.getProducts());
        return l;
    }

    private static Object getInitParams(ModelInfo model) {
        List l = new ArrayList();
        if(model.getInitParams() == null)
            return null;
        
        Iterator i = model.getInitParams().entrySet().iterator();
        while (i.hasNext()) {
            Entry entry = (Entry) i.next();
            l.add(entry.getKey() + "=" + entry.getValue());
        }
        
        return l;
    }

    private static Object getOutParams(ModelInfo model) {
        List l = new ArrayList();
        if(model.getOutParams() == null)
            return null;
        
        Iterator i = model.getOutParams().entrySet().iterator();
        while (i.hasNext()) {
            Entry entry = (Entry) i.next();
            l.add(entry.getKey() + "=" + entry.getValue());
        }
        
        return l;
    }

    private static Object getGridOrigin(ModelInfo model) {
        List l = new ArrayList();
        if(model.getGridOrigin() == null)
            return l;
        
        l.addAll(Arrays.asList(model.getGridOrigin()));
        return l;
    }

    private static Object getGridOffsets(ModelInfo model) {
        List l = new ArrayList();
        if(model.getGridOffsets() == null)
            return l;
        
        l.addAll(Arrays.asList(model.getGridOffsets()));
        return l;
    }

    private static Object getGridLowers(ModelInfo model) {
        List l = new ArrayList();
        if(model.getGridLowers() == null)
            return l;
        
        l.addAll(Arrays.asList(model.getGridLowers()));
        return l;
    }

    private static Object getGridUppers(ModelInfo model) {
        List l = new ArrayList();
        if(model.getGridUppers() == null)
            return l;
        
        l.addAll(Arrays.asList(model.getGridUppers()));
        return l;
    }

    private static List getKeywords(ModelInfo model){
        List l = new ArrayList();
        if(model.getKeywords() == null)
            return l;
        
        l.addAll(model.getKeywords());
        return l;
    }

    private static List getMetadataLinks(ModelInfo model){
        List l = new ArrayList();
        if (model.getMetadataLink() == null)
            return l;
        
        l.addAll(model.getMetadataLink());
        return l;
    }

    private ModelInfo findModelInfo() {
        Map attributes = getRequest().getAttributes();
        String modelName = null;

        ModelInfo theModel = null;
        if (attributes.containsKey("model")) {
            modelName = (String) attributes.get("model");
            theModel  = rawCatalog.getModelByName(modelName);
        }

//        if ((theModel != null) && attributes.containsKey("run")) {
//            String ftid = (String) attributes.get("run");
//
//            // Append the datastore prefix
//            return myDC.getFeatureTypeConfig(modelName + ":" + ftid);
//        }

        return theModel;
    }

    public boolean allowGet() {
        return true;
    }
    
    public boolean allowPut() {
        return false;
    }

    public boolean allowDelete() {
        return false;
    }

}
