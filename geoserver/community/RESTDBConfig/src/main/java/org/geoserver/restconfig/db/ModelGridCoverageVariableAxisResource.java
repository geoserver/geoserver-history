/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.measure.Measure;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.ModelInfo;
import org.geoserver.catalog.ModelRunInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.MapResource;
import org.geotools.coverage.io.range.Axis;
import org.geotools.coverage.io.range.FieldType;
import org.geotools.feature.NameImpl;
import org.restlet.data.MediaType;

/**
 * Restlet for ModelGridCoverageVariableAxis resources
 *
 * @author Alessio Fabiani <alessio.fabiani@geo-solutions.it> , GeoSolutions S.a.S.
 */
public class ModelGridCoverageVariableAxisResource extends MapResource {
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
    
    public ModelGridCoverageVariableAxisResource(GeoServer geoServer, Catalog catalog){
        super();
        setGeoServer(geoServer);
        setCatalog(catalog);
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/gridcoveragevariableaxis.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("GridCoverageVariableAxis"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap(){
        return getMap(findCoverageVariableAxis());
    }

    public static Map getMap(Axis axis) {
        Map m = new HashMap();
        
        m.put("Description", (axis.getDescription() != null ? axis.getDescription().toString() : "[None]"));
        m.put("Name", (axis.getName() != null ? axis.getName().getLocalPart() : "[None]"));
        m.put("CoordinateReferenceSystem", (axis.getCoordinateReferenceSystem() != null ? axis.getCoordinateReferenceSystem().getName().toString() : "[None]"));
        
        Unit<?> uom = axis.getUnitOfMeasure();
        if (uom == null)
            m.put("UnitOfMeasure", "[None]");
        else if (uom.isCompatible(Unit.ONE)) {
            m.put("UnitOfMeasure", uom.toString());
        } else {
            String uomUCUM = UnitFormat.getUCUMInstance().format(uom);
            if (uomUCUM != null) {
                m.put("UnitOfMeasure", uomUCUM);
            }
        }

        m.put("Keys", getKeys(axis));
        
        return m;
    }

    private static List<String> getKeys(Axis axis){
        List<String> l = new ArrayList<String>();
        if(axis.getKeys() == null)
            return l;
        
        Iterator<? extends Measure> i = axis.getKeys().iterator();
        while (i.hasNext()) {
            Measure measure = i.next();
            l.add(measure.getValue().toString());
        }
        
        return l;
    }
    
    private Axis findCoverageVariableAxis() {
        Map attributes = getRequest().getAttributes();
        String modelName = null;
        String modelRunName = null;
        String gridcoverageName = null;
        String variableName = null;
        String axisName = null;

        ModelInfo theModel = null;
        ModelRunInfo theModelRun = null;
        CoverageInfo coverage = null;
        FieldType variable = null;
        Axis axis = null;
        if (attributes.containsKey("model") && attributes.containsKey("run") && attributes.containsKey("gridcoverage") && attributes.containsKey("variable")  && attributes.containsKey("axis")) {
            modelName = (String) attributes.get("model");
            modelRunName = (String) attributes.get("run");
            gridcoverageName = (String) attributes.get("gridcoverage");
            variableName = (String) attributes.get("variable");
            axisName = (String) attributes.get("axis");
            
            theModel  = rawCatalog.getModelByName(modelName);
            
            if (theModel != null && theModel.getModelRuns() != null) {
                for (ModelRunInfo mr : theModel.getModelRuns()) {
                    if (mr.getName().equals(modelRunName))
                        theModelRun = mr;
                }
            }
            
            if (theModelRun != null && theModelRun.getGridCoverages() != null) {
                for (CoverageInfo ci : theModelRun.getGridCoverages()) {
                    if (ci.getName().equals(gridcoverageName)) {
                        coverage = ci;
                    }
                }
            }
            
            if (coverage != null && coverage.getFields() != null) {
                variable = coverage.getFields().getFieldType(variableName);
            }
            
            if (variable != null && variable.getAxes() != null) {
                axis = variable.getAxis(new NameImpl(axisName));
            }
        }

        return axis;
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
