/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.SI;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.ModelInfo;
import org.geoserver.catalog.ModelRunInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.MapResource;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.crs.VerticalCRS;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.datum.TemporalDatum;
import org.opengis.referencing.datum.VerticalDatum;
import org.opengis.temporal.Duration;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.Position;
import org.opengis.temporal.RelativePosition;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.restlet.data.MediaType;
import org.vfny.geoserver.util.WCSUtils;

/**
 * Restlet for ModelGridCoverageVerticalDomain resources
 *
 * @author Alessio Fabiani <alessio.fabiani@geo-solutions.it> , GeoSolutions S.a.S.
 */
public class ModelGridCoverageTemporalDomainResource extends MapResource {
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
    
    public ModelGridCoverageTemporalDomainResource(GeoServer geoServer, Catalog catalog){
        super();
        setGeoServer(geoServer);
        setCatalog(catalog);
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/gridcoveragetdomain.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("TemporalDomain"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap(){
        return getMap(findCoverageInfo());
    }

    public static Map getMap(CoverageInfo theCoverage) {
        Map m = new HashMap();
        
        m.put("CoverageName", (theCoverage.getName() != null ? theCoverage.getName() : "[None]"));
        
        List<String> tDomain = getTdomain(theCoverage);
        m.put("temporalExtentBegin", tDomain.get(0) != null ? tDomain.get(0) : "[None]");
        m.put("temporalExtentEnd", tDomain.get(1) != null ? tDomain.get(1) : "[None]");
        m.put("timeOrigin", tDomain.get(2) != null ? tDomain.get(2) : "[None]");
        m.put("timeDateOrigin", tDomain.get(3) != null ? tDomain.get(3) : "[None]");
        m.put("timeAxisDimension", tDomain.get(4) != null ? tDomain.get(4) : "[None]");
        m.put("timeAxisDirection", tDomain.get(5) != null ? tDomain.get(5) : "[None]");
        m.put("timeAxisUnit", tDomain.get(6) != null ? tDomain.get(6) : "[None]");

        return m;
    }

    private static List<String> getTdomain(CoverageInfo coverage){
        List<String> l = new ArrayList<String>();
        Set<TemporalGeometricPrimitive> temporalExtent = coverage.getTemporalExtent();
        if (temporalExtent == null || temporalExtent.isEmpty())
            return l;
        
        TemporalDatum temporalDatum = ((TemporalCRS)coverage.getTemporalCRS()).getDatum();
        CoordinateSystemAxis timeAxis = ((TemporalCRS)coverage.getTemporalCRS()).getCoordinateSystem().getAxis(0);
        
        long origin = temporalDatum.getOrigin().getTime();
        UnitConverter toMillis = timeAxis.getUnit().getConverterTo(SI.MILLI(SI.SECOND));
        
        Position beginPosition = null;
        Position endPosition = null;
        Duration duration = null;
        for (Iterator<TemporalGeometricPrimitive> i=temporalExtent.iterator(); i.hasNext();) {
            TemporalGeometricPrimitive temporalObject = i.next();
            
            if (temporalObject instanceof Period) {
                Position tmp = ((Period) temporalObject).getBeginning().getPosition();
                if (beginPosition != null) {
                    DefaultInstant beginInstant = new DefaultInstant(beginPosition);
                    DefaultInstant tmpInstant = new DefaultInstant(tmp);
                    
                    if (tmpInstant.relativePosition(beginInstant).equals(RelativePosition.BEFORE)) {
                        beginPosition = tmp;
                    } else if (duration == null)
                        duration = beginInstant.distance(tmpInstant);
                } else
                    beginPosition = tmp;
                
                tmp = ((Period) temporalObject).getEnding().getPosition();
                if (endPosition != null) {
                    DefaultInstant endInstant = new DefaultInstant(endPosition);
                    DefaultInstant tmpInstant = new DefaultInstant(tmp);
                    
                    if (tmpInstant.relativePosition(endInstant).equals(RelativePosition.AFTER)) {
                        endPosition = tmp;
                    }
                } else
                    endPosition = tmp;
            } else if (temporalObject instanceof Instant) {
                Position tmp = ((Instant) temporalObject).getPosition();
                if (beginPosition != null) {
                    DefaultInstant beginInstant = new DefaultInstant(beginPosition);
                    DefaultInstant tmpInstant = new DefaultInstant(tmp);
                    
                    if (tmpInstant.relativePosition(beginInstant).equals(RelativePosition.BEFORE)) {
                        beginPosition = tmp;
                    } else if (duration == null)
                        duration = beginInstant.distance(tmpInstant);
                } else
                    beginPosition = tmp;
                
                if (endPosition != null) {
                    DefaultInstant endInstant = new DefaultInstant(endPosition);
                    DefaultInstant tmpInstant = new DefaultInstant(tmp);
                    
                    if (tmpInstant.relativePosition(endInstant).equals(RelativePosition.AFTER)) {
                        endPosition = tmp;
                    }
                } else
                    endPosition = tmp;
            }
        }

        l.add(beginPosition.getDateTime().toString());
        l.add(endPosition.getDateTime().toString());
        
        l.add(String.valueOf(origin));
        l.add(new DefaultPosition(new Date(origin)).getDateTime().toString());
        l.add(String.valueOf(((TemporalCRS)coverage.getTemporalCRS()).getCoordinateSystem().getDimension()));
        l.add(timeAxis.getDirection().identifier());
        l.add(timeAxis.getUnit().toString());

        return l;
    }
    
    private CoverageInfo findCoverageInfo() {
        Map attributes = getRequest().getAttributes();
        String modelName = null;
        String modelRunName = null;
        String gridcoverageName = null;

        ModelInfo theModel = null;
        ModelRunInfo theModelRun = null;
        CoverageInfo coverage = null;
        if (attributes.containsKey("model") && attributes.containsKey("run") && attributes.containsKey("gridcoverage")) {
            modelName = (String) attributes.get("model");
            modelRunName = (String) attributes.get("run");
            gridcoverageName = (String) attributes.get("gridcoverage");
            
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
        }

        return coverage;
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
