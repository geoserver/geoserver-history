package org.geoserver.restconfig.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.ModelInfo;
import org.geoserver.catalog.ModelRunInfo;
import org.geoserver.catalog.impl.CoverageInfoImpl;
import org.geoserver.config.GeoServer;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.DataFormat;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.MapResource;
import org.geotools.coverage.io.range.RangeType;
import org.opengis.feature.type.Name;
import org.restlet.Finder;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

/**
 * Restlet for ModelRunGridCoverageVariableList resources
 *
 * @author Alessio Fabiani <alessio.fabiani@geo-solutions.it> , GeoSolutions S.a.S.
 */
public class ModelRunGridCoverageVariableListFinder extends Finder {

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

    public Resource findTarget(Request request, Response response){
        Resource r = new ModelList();
        r.init(getContext(), request, response);
        return r;
    }

    protected class ModelList extends MapResource{
        private Map myPostFormats;

        public ModelList(){
            super();
            myPostFormats = new HashMap();
            myPostFormats.put(MediaType.TEXT_XML, new AutoXMLFormat());
            myPostFormats.put(MediaType.APPLICATION_JSON, new JSONFormat());
            myPostFormats.put(null, myPostFormats.get(MediaType.TEXT_XML));
        }

        public Map getSupportedFormats() {
            Map m = new HashMap();
            m.put("html",
                    new FreemarkerFormat(
                        "HTMLTemplates/modelruncoveragevariables.ftl",
                        getClass(),
                        MediaType.TEXT_HTML)
                 );
            m.put("json", new JSONFormat());
            m.put("xml", new AutoXMLFormat("ModelRunGridCoverageVariables"));
            m.put(null, m.get("html"));
            return m;
        }

        public Map getMap() {
            Map m = new HashMap();
            List l = new ArrayList();
            Map models = getVirtualModelRunGridCoverageVariablesMap(getRequest(), getCatalog());
            
            l.addAll(models.keySet());
            Collections.sort(l);
            m.put("ModelRunGridCoverageVariables", l);
            
            return m;
        }

        public boolean allowPost(){
            return true;
        }

        // TODO: POST support for folders/ url
        public void handlePost(){
            MediaType type = getRequest().getEntity().getMediaType();
            LOG.info("Model posted, mediatype is:" + type);
            DataFormat format = (DataFormat)myPostFormats.get(type);
            LOG.info("Using post format: " + format);
            Map m = (Map)format.readRepresentation(getRequest().getEntity());
            LOG.info("Read data as: " + m);
        }
    }

    public static Map getVirtualModelRunGridCoverageVariablesMap(Request request, Catalog catalog){
        Map modelRunGridCoverageVariables = new HashMap();

        Map attributes = request.getAttributes();
        String modelName = null;
        String modelRunName = null;
        String gridcoverageName = null;

        ModelInfo theModel = null;
        ModelRunInfo theModelRun = null;
        if (attributes.containsKey("model") && attributes.containsKey("run") && attributes.containsKey("gridcoverage")) {
            modelName = (String) attributes.get("model");
            modelRunName = (String) attributes.get("run");
            gridcoverageName = (String) attributes.get("gridcoverage");
            
            String coverageName = gridcoverageName.indexOf("@") > 0 ? gridcoverageName.substring(0, gridcoverageName.indexOf("@")) : gridcoverageName;
            String fieldName = gridcoverageName.indexOf("@") > 0 ? gridcoverageName.substring(gridcoverageName.indexOf("@")+1) : null;
            
            // stripping namespace
            gridcoverageName = coverageName.contains(":") ? coverageName.substring(coverageName.indexOf(":")+1) : coverageName;
            
            theModel  = catalog.getModelByName(modelName);
            
            if (theModel != null && theModel.getModelRuns() != null) {
                for (ModelRunInfo mr : catalog.getModelRuns(theModel)) {
                    if (mr.getName().equals(modelRunName))
                        theModelRun = mr;
                }
            }
            
            if (theModelRun != null && theModelRun.getGridCoverages() != null) {
                for (CoverageInfo ci : catalog.getGridCoverages(theModelRun)) {
                    if (ci.getName().equals(gridcoverageName)) {
                        CoverageInfo coverage = catalog.getCoverage(ci.getId());
                        if (coverage.getFields() != null) {
                            RangeType range = coverage.getFields();
                            Iterator<Name> it =  range.getFieldTypeNames().iterator();
                            while (it.hasNext()) {
                                Name entry = it.next();
                                modelRunGridCoverageVariables.put(entry.getLocalPart(), entry);
                            }
                        }
                    }
                }
            }
        }

        return modelRunGridCoverageVariables;
    }
}