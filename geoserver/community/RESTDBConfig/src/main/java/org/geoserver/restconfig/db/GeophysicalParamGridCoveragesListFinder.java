package org.geoserver.restconfig.db;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.GeophysicParamInfo;
import org.geoserver.catalog.ModelInfo;
import org.geoserver.catalog.ModelRunInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.ows.kvp.TimeKvpParser;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.DataFormat;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.MapResource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.operation.TransformException;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.Position;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.restlet.Finder;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;
import org.vfny.geoserver.util.WCSUtils;

/**
 * Restlet for GeophysicalParamGridCoveragesListFinder resources
 *
 * @author Alessio Fabiani <alessio.fabiani@geo-solutions.it> , GeoSolutions S.a.S.
 */
public class GeophysicalParamGridCoveragesListFinder extends Finder {

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
        Resource r = new GeophysicalParamGridCoveragesList();
        r.init(getContext(), request, response);
        return r;
    }

    protected class GeophysicalParamGridCoveragesList extends MapResource{
        private Map myPostFormats;

        public GeophysicalParamGridCoveragesList(){
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
                        "HTMLTemplates/geophysicparamgridcoverages.ftl",
                        getClass(),
                        MediaType.TEXT_HTML)
                 );
            m.put("json", new JSONFormat());
            m.put("xml", new AutoXMLFormat("GeophysicParamGridCoverages"));
            m.put(null, m.get("html"));
            return m;
        }

        public Map getMap() {
            Map m = new HashMap();
            List<String> l = new ArrayList<String>();
            List gridcoverages = new ArrayList();
            Map geophysicParamGridCoverages = getVirtualGeophysicParamGridCoveragesMap(getRequest(), getCatalog());
            
            l.addAll(geophysicParamGridCoverages.keySet());
            Collections.sort(l);
            
            Map modelRun = new HashMap();
            for (String modelName : l) {
                Map modelCv = new HashMap();
                
                List<String> r = new ArrayList<String>();
                r.addAll(((Map)geophysicParamGridCoverages.get(modelName)).keySet());
                Collections.sort(r);
                
                for (String runName : r) {
                    modelCv.put(runName, ((Map)geophysicParamGridCoverages.get(modelName)).get(runName));
                }
                modelRun.put(modelName, modelCv);
                
            }
            gridcoverages.add(modelRun);
            
            m.put("GeophysicParamGridCoverages", gridcoverages);
            
            return m;
        }

        public boolean allowPost(){
            return true;
        }

        // TODO: POST support for folders/ url
        public void handlePost(){
            MediaType type = getRequest().getEntity().getMediaType();
            LOG.info("GeophysicParamGridCoverage posted, mediatype is:" + type);
            DataFormat format = (DataFormat)myPostFormats.get(type);
            LOG.info("Using post format: " + format);
            Map m = (Map)format.readRepresentation(getRequest().getEntity());
            LOG.info("Read data as: " + m);
        }
    }

    public static Map getVirtualGeophysicParamGridCoveragesMap(Request request, Catalog catalog){
        Map geophysicParamGridCoverages = new HashMap();
        Map attributes = request.getAttributes();
        Form form = request.getResourceRef().getQueryAsForm();
        
        String variableName = null;
        String time = form.getFirst("TIME") != null ? form.getFirstValue("TIME") : null;
        String elevation = form.getFirst("ELEVATION") != null ? form.getFirstValue("ELEVATION") : null;
        String bbox = form.getFirst("BBOX") != null ? form.getFirstValue("BBOX") : null;
        
        GeophysicParamInfo variable = null;
        if (attributes.containsKey("parameter")) {
            variableName = (String) attributes.get("parameter");
            
            variable = catalog.getGeophysicParamByName(variableName);
            
            // grouping gridcoverages by model
            if (variable != null && variable.getGridCoverages() != null) {
                for (ModelInfo model : catalog.getModels(variable)) {
                    Map runs = new HashMap();

                    for (ModelRunInfo modelRun : catalog.getModelRuns(model)) {
                        List<String> coverages = new ArrayList<String>();
                        
                        for (CoverageInfo cv : catalog.getGridCoverages(modelRun)) {
                            cv = catalog.getCoverage(cv.getId());
                            // perform T,Z,BBOX check here
                            boolean validRange = true;
                            if (validRange && time != null && cv.getTemporalExtent() != null) {
                                validRange = checkTemporalDomainRange(cv.getTemporalExtent(), time);
                            } else if (validRange && time != null) {
                                validRange = false;
                            }
                            
                            if (validRange && elevation != null && cv.getVerticalExtent() != null) {
                                validRange = checkVerticalDomainRange(cv.getVerticalExtent(), Double.valueOf(elevation));
                            } else if (validRange && elevation != null) {
                                validRange = false;
                            }

                            try {
                                if (validRange && bbox != null && modelRun.getOutline() != null) {
                                    String[] coords = bbox.split(",");
                                    Double[] coordinates = new Double[coords.length];
                                    for (int i=0; i<coords.length; i++) {
                                        coordinates[i] = Double.valueOf(coords[i]);
                                    }
                                    validRange = checkSpatialDomainRange(modelRun.getOutline(), coordinates);
                                } else if (validRange && bbox != null) {
                                    validRange = false;
                                }
                            } catch (Exception e) {
                                validRange = false;
                            }
                            
                            if (validRange) {
                                for (GeophysicParamInfo param : catalog.getGeophysicalParams(cv)) {
                                    if (variableName.equals(param.getName()) || param.getAlias().contains(variableName)) {
                                        coverages.add(cv.getName());
                                    }
                                }
                            }
                        }
                        
                        if (coverages.size() > 0)
                            runs.put(modelRun.getName(), coverages);
                    }
                    geophysicParamGridCoverages.put(model.getName(), runs);
                }
            }
        }
        
        return geophysicParamGridCoverages;
    }

    private static boolean checkSpatialDomainRange(ReferencedEnvelope outline, Double[] coordinates) {
        if (outline == null || coordinates == null)
            return false;
        
        if (coordinates.length != 4)
            return false;
        
        ReferencedEnvelope bbox = new ReferencedEnvelope(coordinates[0], coordinates[1], coordinates[2], coordinates[2], outline.getCoordinateReferenceSystem());

        BoundingBox bounds;
        try {
            bounds = outline.toBounds(outline.getCoordinateReferenceSystem());
        } catch (TransformException e) {
            return false;
        }
        
        return (bbox.intersects(bounds));
    }

    private static boolean checkVerticalDomainRange(Set<Envelope> verticalExtent, Double elevation) {
        if (verticalExtent == null || elevation == null)
            return false;
        
        double[] verticalBounds = WCSUtils.getVerticalExtentLimits(verticalExtent);
        
        if (verticalBounds[0] > elevation || verticalBounds[1] < elevation)
            return false;
                
        return true;
    }

    private static boolean checkTemporalDomainRange(Set<TemporalGeometricPrimitive> temporalExtent, String time) {
        if (temporalExtent == null || time == null)
            return false;
        
        TimeKvpParser timeParser = new TimeKvpParser("time");
        try {
            List<Date> requestedTimes = (List<Date>) timeParser.parse(time);
            
            if (requestedTimes == null || requestedTimes.size() ==0)
                return false;
            
            Position beginPosition = null;
            Position endPosition = null;
            
            for (TemporalGeometricPrimitive temporalObject : temporalExtent) {
                if (temporalObject instanceof Period) {
                    beginPosition = ((Period) temporalObject).getBeginning().getPosition();
                    endPosition = ((Period) temporalObject).getEnding().getPosition();
                } else if (temporalObject instanceof Instant) {
                    beginPosition = endPosition = ((Instant) temporalObject).getPosition();
                }
            }
            
            if (beginPosition == null || endPosition == null)
                return false;
            
            for (Date reqDate : requestedTimes) {
                DefaultInstant timeInstant = new DefaultInstant(new DefaultPosition(reqDate));

                if (!beginPosition.equals(endPosition)) {
                    DefaultPeriod timePeriod = new DefaultPeriod(new DefaultInstant(beginPosition), new DefaultInstant(endPosition));
                    return contains(timePeriod, timeInstant);
                } else {
                    return contains(new DefaultInstant(beginPosition), timeInstant);
                }
            }
        } catch (ParseException e) {
            return false;
        }
        
        return true;
    }
    
    public static boolean contains(TemporalGeometricPrimitive containing,
            TemporalGeometricPrimitive contained) {
        // //
        //
        // Instants should match to be taken
        // 
        // //
        if (containing instanceof Instant && contained instanceof Instant)
            return containing.equals(contained);
        // //
        //
        // If the first time is a period I will check if the second time (an
        // instant)
        // is between the beginning and the ending of the period.
        //
        // //
        else if (containing instanceof Period && contained instanceof Instant) {
            final Date position = ((DefaultInstant) contained).getPosition().getDate();
            final DefaultPeriod period = (DefaultPeriod) containing;
            final Date startPeriod = period.getBeginning().getPosition().getDate();
            final Date endPeriod = period.getEnding().getPosition().getDate();
            if (position.compareTo(startPeriod) >= 0 && position.compareTo(endPeriod) <= 0)
                return true;
        }
        // //
        //
        // In case both times are periods, I check for an intersection.
        //
        // //
        else if (containing instanceof Period && contained instanceof Period) {
            final DefaultPeriod containingPeriod = (DefaultPeriod) containing;
            final Date startContaining = containingPeriod.getBeginning().getPosition().getDate();
            final Date endContaining = containingPeriod.getEnding().getPosition().getDate();
            final DefaultPeriod containedPeriod = (DefaultPeriod) contained;
            final Date startContained = containedPeriod.getBeginning().getPosition().getDate();
            final Date endContained = containedPeriod.getEnding().getPosition().getDate();

            // Return false if the period which should be contained is totally
            // outside the containing one.
            // Silly example: a period between 3AM and 5AM isn't contained in a
            // period between 6AM and 8AM.
            // Instead, a period between 5AM and 7AM should be considered
            // contained within the period between 6AM and 8AM since part of
            // them are
            // intersecting.
            if (endContained.compareTo(startContaining) < 0 || startContained.compareTo(endContaining) > 0)
                return false;
            else
                return true;

        }
        return false;
    }
}