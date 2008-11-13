/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.ModelInfo;
import org.geoserver.catalog.ModelRunInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.MapResource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.restlet.data.MediaType;

/**
 * Restlet for ModelRun resources
 *
 * @author Alessio Fabiani <alessio.fabiani@geo-solutions.it> , GeoSolutions S.a.S.
 */
public class ModelRunResource extends MapResource {
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
    
    public ModelRunResource(GeoServer geoServer, Catalog catalog){
        super();
        setGeoServer(geoServer);
        setCatalog(catalog);
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/modelrun.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("ModelRun"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap(){
        return getMap(findModelRunInfo());
    }

    public static Map getMap(ModelRunInfo theModelRun) {
        Map m = new HashMap();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:sss");

        m.put("BaseTime", (theModelRun.getBaseTime() != null ? sdf.format(theModelRun.getBaseTime()) : "[None]"));
        m.put("Description", (theModelRun.getDescription() != null ? theModelRun.getDescription() : "[None]"));
        m.put("ExecutionTime", (theModelRun.getExecutionTime() != null ? sdf.format(theModelRun.getExecutionTime()) : "[None]"));
        m.put("Name", (theModelRun.getName() != null ? theModelRun.getName() : "[None]"));
        m.put("Keywords", getKeywords(theModelRun));
        m.put("NumTAU", (theModelRun.getNumTAU() != null ? theModelRun.getNumTAU() : "[None]"));
        try {
            m.put("CRS", (theModelRun.getCRS() != null ? /* CRS.lookupIdentifier(theModel.getCRS(), true) */ theModelRun.getCRS().toWKT() : "[None]"));
        } catch (Exception e) {
            m.put("CRS", "[None]");
        }
        m.put("GridCRS", (theModelRun.getGridCRS() != null ? theModelRun.getGridCRS() : "[None]")); 
        m.put("GridCS", (theModelRun.getGridCS() != null ? theModelRun.getGridCS() : "[None]"));
        m.put("GridLowers", getGridLowers(theModelRun));
        m.put("GridOffsets", getGridOffsets(theModelRun));
        m.put("GridOrigin", getGridOrigin(theModelRun));
        m.put("GridType", (theModelRun.getGridType() != null ? theModelRun.getGridType() : "[None]"));
        m.put("GridUppers", getGridUppers(theModelRun));
        m.put("InitParams", getInitParams(theModelRun));
        m.put("OutParams", getOutParams(theModelRun));
        m.put("VerticalCoordinateMeaning", (theModelRun.getVerticalCoordinateMeaning() != null ? theModelRun.getVerticalCoordinateMeaning() : "[None]"));
        m.put("Outline", getBoundingBox(theModelRun));
        m.put("TAU", (theModelRun.getTAU() != null ? theModelRun.getTAU() : "[None]"));
        m.put("TAUunit", (theModelRun.getTAUunit() != null ? theModelRun.getTAUunit() : "[None]"));
        m.put("UpdateSequence", (theModelRun.getUpdateSequence() != null ? theModelRun.getUpdateSequence() : "[None]"));

        return m;
    }

    private static List getBoundingBox(ModelRunInfo modelRun){
        List l = new ArrayList();
        ReferencedEnvelope e = modelRun.getOutline();
        if (e == null)
            return l;
        
        l.add(String.valueOf(e.getMinX()));
        l.add(String.valueOf(e.getMaxX()));
        l.add(String.valueOf(e.getMinY()));
        l.add(String.valueOf(e.getMaxY()));
        l.add(e.getCoordinateReferenceSystem().toWKT());
        return l;
    }
    
    private static List getKeywords(ModelRunInfo modelRun){
        List l = new ArrayList();
        if(modelRun.getKeywords() == null)
            return l;
        
        l.addAll(modelRun.getKeywords());
        return l;
    }

    private static Object getInitParams(ModelRunInfo model) {
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

    private static Object getOutParams(ModelRunInfo model) {
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

    private static Object getGridOrigin(ModelRunInfo model) {
        List l = new ArrayList();
        if(model.getGridOrigin() == null)
            return l;
        
        l.addAll(Arrays.asList(model.getGridOrigin()));
        return l;
    }

    private static Object getGridOffsets(ModelRunInfo model) {
        List l = new ArrayList();
        if(model.getGridOffsets() == null)
            return l;
        
        l.addAll(Arrays.asList(model.getGridOffsets()));
        return l;
    }

    private static Object getGridLowers(ModelRunInfo model) {
        List l = new ArrayList();
        if(model.getGridLowers() == null)
            return l;
        
        l.addAll(Arrays.asList(model.getGridLowers()));
        return l;
    }

    private static Object getGridUppers(ModelRunInfo model) {
        List l = new ArrayList();
        if(model.getGridUppers() == null)
            return l;
        
        l.addAll(Arrays.asList(model.getGridUppers()));
        return l;
    }

    private ModelRunInfo findModelRunInfo() {
        Map attributes = getRequest().getAttributes();
        String modelName = null;
        String modelRunName = null;

        ModelInfo theModel = null;
        ModelRunInfo theModelRun = null;
        if (attributes.containsKey("model") && attributes.containsKey("run")) {
            modelName = (String) attributes.get("model");
            modelRunName = (String) attributes.get("run");
            
            theModel  = rawCatalog.getModelByName(modelName);
            
            if (theModel != null && theModel.getModelRuns() != null) {
                for (ModelRunInfo mr : rawCatalog.getModelRuns(theModel)) {
                    if (mr.getName().equals(modelRunName))
                        theModelRun = mr;
                }
            }
        }

        return theModelRun;
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
