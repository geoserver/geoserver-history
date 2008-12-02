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
import org.opengis.coverage.SampleDimension;
import org.restlet.data.MediaType;

/**
 * Restlet for ModelGridCoverageVariable resources
 *
 * @author Alessio Fabiani <alessio.fabiani@geo-solutions.it> , GeoSolutions S.a.S.
 */
public class ModelGridCoverageVariableResource extends MapResource {
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
    
    public ModelGridCoverageVariableResource(GeoServer geoServer, Catalog catalog){
        super();
        setGeoServer(geoServer);
        setCatalog(catalog);
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/gridcoveragevariable.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("GridCoverageVariable"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap(){
        return getMap(findCoverageVariable());
    }

    public static Map getMap(FieldType fieldType) {
        Map m = new HashMap();
        
        m.put("Description", (fieldType.getDescription() != null ? fieldType.getDescription() : "[None]"));
        m.put("Name", (fieldType.getName() != null ? fieldType.getName().getLocalPart() : "[None]"));
        m.put("Axes", getAxes(fieldType));
        m.put("SampleDimensions", getSampleDimensions(fieldType));
        
        Unit<?> uom = fieldType.getUnitOfMeasure();
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

        return m;
    }

    private static Object getAxes(FieldType fieldType) {
        List l = new ArrayList();
        if(fieldType.getAxes() == null)
            return null;
        
        Iterator<Axis<?, ?>> i = fieldType.getAxes().iterator();
        while (i.hasNext()) {
            Axis axis = i.next();
            l.add(axis.getName().getLocalPart());
        }
        
        return l;
    }
    
    private static Object getSampleDimensions(FieldType fieldType) {
        List l = new ArrayList();
        if(fieldType.getSampleDimensions() == null)
            return null;
        
        Iterator<SampleDimension> i = fieldType.getSampleDimensions().iterator();
        while (i.hasNext()) {
            SampleDimension sd = i.next();
            l.add(sd.getDescription().toString());
        }
        
        return l;
    }
    
    private FieldType findCoverageVariable() {
        Map attributes = getRequest().getAttributes();
        String modelName = null;
        String modelRunName = null;
        String gridcoverageName = null;
        String variableName = null;

        ModelInfo theModel = null;
        ModelRunInfo theModelRun = null;
        CoverageInfo coverage = null;
        FieldType variable = null;
        if (attributes.containsKey("model") && attributes.containsKey("run") && attributes.containsKey("gridcoverage") && attributes.containsKey("variable")) {
            modelName = (String) attributes.get("model");
            modelRunName = (String) attributes.get("run");
            gridcoverageName = (String) attributes.get("gridcoverage");
            
            String coverageName = gridcoverageName.indexOf("@") > 0 ? gridcoverageName.substring(0, gridcoverageName.indexOf("@")) : gridcoverageName;
            String fieldName = gridcoverageName.indexOf("@") > 0 ? gridcoverageName.substring(gridcoverageName.indexOf("@")+1) : null;
            
            // stripping namespace
            gridcoverageName = coverageName.contains(":") ? coverageName.substring(coverageName.indexOf(":")+1) : coverageName;

            variableName = (String) attributes.get("variable");
            
            theModel  = rawCatalog.getModelByName(modelName);
            
            if (theModel != null && theModel.getModelRuns() != null) {
                for (ModelRunInfo mr : rawCatalog.getModelRuns(theModel)) {
                    if (mr.getName().equals(modelRunName))
                        theModelRun = mr;
                }
            }
            
            if (theModelRun != null && theModelRun.getGridCoverages() != null) {
                for (CoverageInfo ci : rawCatalog.getGridCoverages(theModelRun)) {
                    if (ci.getName().equals(gridcoverageName)) {
                        coverage = rawCatalog.getCoverage(ci.getId());
                    }
                }
            }
            
            if (coverage != null && coverage.getFields() != null) {
                variable = coverage.getFields().getFieldType(variableName);
            }
        }

        return variable;
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
