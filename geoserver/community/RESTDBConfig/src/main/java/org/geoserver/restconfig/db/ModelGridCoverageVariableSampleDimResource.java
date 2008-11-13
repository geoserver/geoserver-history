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
import org.geotools.coverage.io.range.FieldType;
import org.opengis.coverage.SampleDimension;
import org.restlet.data.MediaType;

/**
 * Restlet for ModelGridCoverageVariableSampleDim resources
 *
 * @author Alessio Fabiani <alessio.fabiani@geo-solutions.it> , GeoSolutions S.a.S.
 */
public class ModelGridCoverageVariableSampleDimResource extends MapResource {
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
    
    public ModelGridCoverageVariableSampleDimResource(GeoServer geoServer, Catalog catalog){
        super();
        setGeoServer(geoServer);
        setCatalog(catalog);
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/gridcoveragevariablesd.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("GridCoverageVariableSampleDim"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap(){
        return getMap(findCoverageVariableSampleDim());
    }

    public static Map getMap(SampleDimension sampleDimension) {
        Map m = new HashMap();
        
        m.put("Description", (sampleDimension.getDescription() != null ? sampleDimension.getDescription().toString() : "[None]"));
        m.put("Type", (sampleDimension.getSampleDimensionType() != null ? sampleDimension.getSampleDimensionType().name() : "[None]"));
        m.put("MinimumValue", String.valueOf(sampleDimension.getMinimumValue()));
        m.put("MaximumValue", String.valueOf(sampleDimension.getMaximumValue()));
        m.put("Offset", String.valueOf(sampleDimension.getOffset()));
        m.put("Scale", String.valueOf(sampleDimension.getScale()));
        m.put("NoDataValues", (sampleDimension.getNoDataValues() != null ? String.valueOf(sampleDimension.getNoDataValues()) : "[None]"));
        //m.put("Categories", getCategories(sampleDimension));
        
        Unit<?> uom = sampleDimension.getUnits();
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

    private static List<String> getCategories(SampleDimension sampleDimension){
        List<String> l = new ArrayList<String>();
        if(sampleDimension.getCategoryNames() == null)
            return l;

        for (int i=0; i<sampleDimension.getCategoryNames().length; i++)
            l.add(sampleDimension.getCategoryNames()[i].toString());
        
        return l;
    }
    
    private SampleDimension findCoverageVariableSampleDim() {
        Map attributes = getRequest().getAttributes();
        String modelName = null;
        String modelRunName = null;
        String gridcoverageName = null;
        String variableName = null;
        String sdName = null;

        ModelInfo theModel = null;
        ModelRunInfo theModelRun = null;
        CoverageInfo coverage = null;
        FieldType variable = null;
        SampleDimension sampleDim = null;
        if (attributes.containsKey("model") && attributes.containsKey("run") && attributes.containsKey("gridcoverage") && attributes.containsKey("variable")  && attributes.containsKey("sd")) {
            modelName = (String) attributes.get("model");
            modelRunName = (String) attributes.get("run");
            gridcoverageName = (String) attributes.get("gridcoverage");
            variableName = (String) attributes.get("variable");
            sdName = (String) attributes.get("sd");
            
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
            
            if (variable != null && variable.getSampleDimensions() != null) {
                Iterator<SampleDimension> i = variable.getSampleDimensions().iterator();
                while(i.hasNext()) {
                    SampleDimension sd = i.next();
                    if (sd.getDescription().toString().equals(sdName))
                        sampleDim = sd; 
                }
            }
        }

        return sampleDim;
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
