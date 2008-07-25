/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.kvp;

import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.InvalidParameterValue;
import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.MissingParameterValue;

import java.util.List;
import java.util.Map;

import net.opengis.gml.CodeType;
import net.opengis.gml.Gml4wcsFactory;
import net.opengis.gml.GridType;
import net.opengis.wcs.DomainSubsetType;
import net.opengis.wcs.GetCoverageType;
import net.opengis.wcs.OutputType;
import net.opengis.wcs.RangeSubsetType;
import net.opengis.wcs.SpatialSubsetType;
import net.opengis.wcs.TimeSequenceType;
import net.opengis.wcs.Wcs10Factory;

import org.geoserver.ows.kvp.EMFKvpRequestReader;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;

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

        if("urn:ogc:def:crs:OGC:1.3:CRS84".equals(crsName)) {
            crsName = "EPSG:4326";
        }
        
        try {
            crs = CRS.decode(crsName, true);
        } catch (NoSuchAuthorityCodeException e) {
            throw new WcsException("Could not recognize crs " + crsName, InvalidParameterValue, "crs");
        } catch (FactoryException e) {
            throw new WcsException("Could not recognize crs " + crsName, InvalidParameterValue, "crs");
        }

        // either bbox or timesequence must be there
        Envelope bbox = (Envelope) kvp.get("BBOX");
        GeneralEnvelope envelope = new GeneralEnvelope(crs);
        envelope.setEnvelope(bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY());
        TimeSequenceType timeSequence = (TimeSequenceType) kvp.get("TIME");
        if (timeSequence == null && bbox == null)
            throw new WcsException(
                    "Bounding box cannot be null, TIME has not been specified",
                    WcsExceptionCode.MissingParameterValue, "BBOX");

        GridType grid = Gml4wcsFactory.eINSTANCE.createGridType();
        if (kvp.get("width") != null && kvp.get("height") != null) {
            double width  = ((Integer) kvp.get("width")).doubleValue();
            double height = ((Integer) kvp.get("height")).doubleValue();
            
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
            if("urn:ogc:def:crs:OGC:1.3:CRS84".equals(crsName)) {
                crsName = "EPSG:4326";
            }
            
            try {
                crs = CRS.decode(crsName, true);
            } catch (NoSuchAuthorityCodeException e) {
                throw new WcsException("Could not recognize crs " + crsName, InvalidParameterValue, "crs");
            } catch (FactoryException e) {
                throw new WcsException("Could not recognize crs " + crsName, InvalidParameterValue, "crs");
            }

            crsType.setValue(CRS.lookupIdentifier(crs, false));

            output.setCrs(crsType);
        }

        formatType.setValue(format);
        
        output.setFormat(formatType);

        return output;
    }

}
