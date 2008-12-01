/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig.db;

import java.text.SimpleDateFormat;
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
import org.geotools.coverage.io.range.RangeType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.feature.type.Name;
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
 * Restlet for ModelGridCoverage resources
 *
 * @author Alessio Fabiani <alessio.fabiani@geo-solutions.it> , GeoSolutions S.a.S.
 */
public class ModelGridCoverageResource extends MapResource {
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
    
    public ModelGridCoverageResource(GeoServer geoServer, Catalog catalog){
        super();
        setGeoServer(geoServer);
        setCatalog(catalog);
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/gridcoverage.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("GridCoverage"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap(){
        return getMap(findCoverageInfo());
    }

    public static Map getMap(CoverageInfo theCoverage) {
        Map m = new HashMap();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:sss");

        m.put("Description", (theCoverage.getDescription() != null ? theCoverage.getDescription() : "[None]"));
        m.put("Name", (theCoverage.getName() != null ? theCoverage.getName() : "[None]"));
        m.put("Keywords", getKeywords(theCoverage));
        m.put("BoundingBox", getBoundingBox(theCoverage));
        m.put("Vdomain", getVdomain(theCoverage));
        m.put("Tdomain", getTdomain(theCoverage));
        m.put("Variables", getVariables(theCoverage));

        return m;
    }

    private static List getBoundingBox(CoverageInfo coverage){
        List l = new ArrayList();
        ReferencedEnvelope e = null;
        try {
            e = coverage.getBoundingBox();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (e == null)
            return l;
        
        l.add(String.valueOf(e.getMinX()));
        l.add(String.valueOf(e.getMaxX()));
        l.add(String.valueOf(e.getMinY()));
        l.add(String.valueOf(e.getMaxY()));
        
        l.add(e.getCoordinateReferenceSystem().toWKT());
        
        return l;
    }
    
    private static List getVdomain(CoverageInfo coverage){
        List l = new ArrayList();
        Set<Envelope> verticalExtent = coverage.getVerticalExtent();
        if (verticalExtent == null || verticalExtent.size() <= 0)
            return l;
        
        VerticalDatum verticalDatum = ((VerticalCRS)coverage.getVerticalCRS()).getDatum();
        CoordinateSystemAxis verticalAxis = ((VerticalCRS)coverage.getVerticalCRS()).getCoordinateSystem().getAxis(0);
        final double[] verticalLimits = (verticalExtent != null && verticalExtent.size() > 0 ? WCSUtils.getVerticalExtentLimits(verticalExtent) : null);
        final double lowerLimit = verticalLimits[0];
        final double upperLimit = verticalLimits[1];
        final double resolution = verticalLimits[2];
        
        StringBuilder verticalValues = new StringBuilder();
        for (Envelope vEnvelope : verticalExtent) {
            double lowerValue = vEnvelope.getLowerCorner().getOrdinate(0);
            double upperValue = vEnvelope.getUpperCorner().getOrdinate(0);
            
            if (verticalValues.length() > 0)
                verticalValues.append(",");
            
            if (lowerValue == upperValue)
                verticalValues.append(lowerValue);
            else
                verticalValues.append(lowerValue).append(",").append(upperValue);
        }
        
        l.add("lowerLimit=" + String.valueOf(lowerLimit));
        l.add("upperLimit=" + String.valueOf(upperLimit));
        l.add("resolution=" + String.valueOf(resolution));
        l.add("verticalValues=" + verticalValues.toString());
        l.add("verticalDatumType=" + verticalDatum.getVerticalDatumType().identifier());
        l.add("verticalAxisDimension=" + ((VerticalCRS)coverage.getVerticalCRS()).getCoordinateSystem().getDimension());
        l.add("verticalAxisAbbr=" + verticalAxis.getAbbreviation());
        l.add("verticalAxisDirection=" + verticalAxis.getDirection().identifier());
        l.add("verticalAxisUnit=" + verticalAxis.getUnit().toString());
        
        return l;
    }
    
    private static List getTdomain(CoverageInfo coverage){
        List l = new ArrayList();
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

        l.add("temporalExtentBegin=" + beginPosition.getDateTime().toString());
        l.add("temporalExtentEnd=" + endPosition.getDateTime().toString());
        
        l.add("timeOrigin=" + origin);
        l.add("timeDateOrigin=" + new DefaultPosition(new Date(origin)).getDateTime().toString());
        l.add("timeAxisDimension=" + ((TemporalCRS)coverage.getTemporalCRS()).getCoordinateSystem().getDimension());
        l.add("timeAxisDirection=" + timeAxis.getDirection().identifier());
        l.add("timeAxisUnit=" + timeAxis.getUnit().toString());

        return l;
    }
    
    private static List getKeywords(CoverageInfo coverage){
        List l = new ArrayList();
        if(coverage.getKeywords() == null)
            return l;
        
        l.addAll(coverage.getKeywords());
        return l;
    }
    
    private static List getVariables(CoverageInfo coverage){
        List l = new ArrayList();
        if(coverage.getFields() == null)
            return l;
        
        RangeType range = coverage.getFields();
        for (java.util.Iterator<Name> i = range.getFieldTypeNames().iterator(); i.hasNext();)
            l.add(i.next().getLocalPart());
        
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
