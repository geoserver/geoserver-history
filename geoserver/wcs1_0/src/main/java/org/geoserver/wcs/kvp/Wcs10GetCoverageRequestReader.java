/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.kvp;

import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.InvalidParameterValue;
import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.MissingParameterValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.opengis.gml.CodeType;
import net.opengis.gml.Gml4wcsFactory;
import net.opengis.gml.GridType;
import net.opengis.gml.TimePositionType;
import net.opengis.wcs10.DomainSubsetType;
import net.opengis.wcs10.GetCoverageType;
import net.opengis.wcs10.OutputType;
import net.opengis.wcs10.RangeSubsetType;
import net.opengis.wcs10.SpatialSubsetType;
import net.opengis.wcs10.TimeSequenceType;
import net.opengis.wcs10.Wcs10Factory;

import org.geoserver.ows.kvp.EMFKvpRequestReader;
import org.geoserver.ows.util.RequestUtils;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class Wcs10GetCoverageRequestReader extends EMFKvpRequestReader {

    Data catalog;

    public Wcs10GetCoverageRequestReader(Data catalog) {
        super(GetCoverageType.class, Wcs10Factory.eINSTANCE);
        this.catalog = catalog;
    }

    @Override
    public Object read(Object request, Map kvp, Map rawKvp) throws Exception {
        GetCoverageType getCoverage = (GetCoverageType) super.read(request, kvp, rawKvp);

        // grab coverage info to perform further checks
        if (getCoverage.getSourceCoverage() == null) {
            if (kvp.get("coverage") == null)
                throw new WcsException("source coverage parameter is mandatory", MissingParameterValue, "source coverage");
            else
                getCoverage.setSourceCoverage((String) ((List)kvp.get("coverage")).get(0));
        }
        // if not specified, throw a resounding exception (by spec)
        if(!getCoverage.isSetVersion())
            throw new WcsException("Version has not been specified", WcsExceptionCode.MissingParameterValue, "version");
        
        // do the version negotiation dance
        List<String> provided = new ArrayList<String>();
        provided.add("1.0.0");
        List<String> accepted = null;
        if (getCoverage.getVersion() != null) {
            accepted = new ArrayList<String>();
            accepted.add(getCoverage.getVersion());
        }
        String version = RequestUtils.getVersionPreOws(provided, accepted);

        if (!"1.0.0".equals(version)) {
            throw new WcsException("An invalid version number has been specified", WcsExceptionCode.InvalidParameterValue, "version");
        }
        getCoverage.setVersion("1.0.0");
        
        // build the domain subset
        getCoverage.setDomainSubset(parseDomainSubset(kvp));

        // build the range subset
        getCoverage.setRangeSubset(parseRangeSubset(kvp));
        
        // build output element
        getCoverage.setOutput(parseOutputElement(kvp));

        return getCoverage;
    }

    private DomainSubsetType parseDomainSubset(Map kvp) {
        final DomainSubsetType domainSubset = Wcs10Factory.eINSTANCE.createDomainSubsetType();
        final SpatialSubsetType spatialSubset = Wcs10Factory.eINSTANCE.createSpatialSubsetType();
        
        // check for crs
        String crsName = (String) kvp.get("crs");
        CoordinateReferenceSystem crs = null;
        if (crsName == null)
            throw new WcsException("crs parameter is mandatory", MissingParameterValue, "crs");

        crs = decodeCRS(crsName, crs);

        // either bbox or timesequence must be there
        Envelope bbox = (Envelope) kvp.get("BBOX");
        if (bbox == null)
            throw new WcsException("bbox parameter is mandatory", MissingParameterValue, "bbox");
        GeneralEnvelope envelope = new GeneralEnvelope(crs);
        envelope.setEnvelope(bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY());
        TimeSequenceType timeSequence = null;
        Object time = kvp.get("TIME");
        if (time != null && time instanceof TimeSequenceType) {
            timeSequence = (TimeSequenceType) time;
        } else if (time != null && time instanceof List) {
            timeSequence = Wcs10Factory.eINSTANCE.createTimeSequenceType();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            for (Date tPos : (List<Date>)time) {
                TimePositionType timePosition = Gml4wcsFactory.eINSTANCE.createTimePositionType();
                timePosition.setValue(sdf.format(tPos));
                timeSequence.getTimePosition().add(timePosition);
            }
        }

        if (timeSequence == null && bbox == null)
            throw new WcsException(
                    "Bounding box cannot be null, TIME has not been specified",
                    WcsExceptionCode.MissingParameterValue, "BBOX");

        GridType grid = Gml4wcsFactory.eINSTANCE.createGridType();
        if (kvp.get("width") != null && kvp.get("height") != null) {
            double width  = kvp.get("width") instanceof Integer ? ((Integer) kvp.get("width")).doubleValue() : Double.parseDouble((String) kvp.get("width"));
            double height = kvp.get("height") instanceof Integer ? ((Integer) kvp.get("height")).doubleValue()  : Double.parseDouble((String) kvp.get("height"));
            
            grid.setLimits(new Envelope(0.0, width, 0.0, height));
        } else if (kvp.get("resx") != null && kvp.get("resy") != null) {
            double resX = Double.parseDouble((String) kvp.get("resx"));
            double resY = Double.parseDouble((String) kvp.get("resy"));
            
            int width = (int) Math.round((envelope.getUpperCorner().getOrdinate(0) - envelope.getLowerCorner().getOrdinate(0)) / resX);
            int height = (int) Math.round((envelope.getUpperCorner().getOrdinate(1) - envelope.getLowerCorner().getOrdinate(1)) / resY);

            grid.setLimits(new Envelope(0.0, width, 0.0, height));
        } else
            throw new WcsException("Could not recognize grid resolution", InvalidParameterValue, "");

        spatialSubset.getEnvelope().add(envelope);

        Double verticalPosition = (Double) kvp.get("ELEVATION");
        if (verticalPosition != null) {
            spatialSubset.getEnvelope().add(new GeneralEnvelope(verticalPosition, verticalPosition));
        }

        spatialSubset.getGrid().add(grid);
        domainSubset.setSpatialSubset(spatialSubset);
        domainSubset.setTemporalSubset(timeSequence);

        return domainSubset;
    }

    private RangeSubsetType parseRangeSubset(Map kvp) {
        final RangeSubsetType rangeSubset = Wcs10Factory.eINSTANCE.createRangeSubsetType();

        if (kvp.get("Band") != null)
            rangeSubset.getAxisSubset().add(kvp.get("Band"));
        
        return rangeSubset;
    }

    private OutputType parseOutputElement(Map kvp) throws Exception {
        final OutputType output = Wcs10Factory.eINSTANCE.createOutputType();
        final CodeType crsType = Gml4wcsFactory.eINSTANCE.createCodeType();
        final CodeType formatType = Gml4wcsFactory.eINSTANCE.createCodeType();

        // check and set format
        String format = (String) kvp.get("format");
        if (format == null)
            throw new WcsException("format parameter is mandatory", MissingParameterValue, "format");

        String crsName = (String) (kvp.get("response_crs") != null ? kvp.get("response_crs") : kvp.get("crs"));
        CoordinateReferenceSystem crs = null;
        if (crsName != null) {
            crs = decodeCRS(crsName, crs);

            crsType.setValue(CRS.lookupIdentifier(crs, false));

            output.setCrs(crsType);
        }

        formatType.setValue(format);
        
        output.setFormat(formatType);

        return output;
    }

    /**
     * @param crsName
     * @param crs
     * @return
     */
    private CoordinateReferenceSystem decodeCRS(String crsName, CoordinateReferenceSystem crs) {
        if("WGS84(DD)".equals(crsName) || "urn:ogc:def:crs:OGC:1.3:CRS84".equals(crsName)) {
            crsName = "EPSG:4326";
        }
        
        try {
            crs = CRS.decode(crsName, true);
        } catch (NoSuchAuthorityCodeException e) {
            throw new WcsException("Could not recognize crs " + crsName, InvalidParameterValue, "crs");
        } catch (FactoryException e) {
            throw new WcsException("Could not recognize crs " + crsName, InvalidParameterValue, "crs");
        }
        
        return crs;
    }

}
