/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.kvp;

import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.InvalidParameterValue;
import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.MissingParameterValue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.opengis.gml.CodeType;
import net.opengis.gml.Gml4wcsFactory;
import net.opengis.gml.GridType;
import net.opengis.gml.TimePositionType;
import net.opengis.wcs10.AxisSubsetType;
import net.opengis.wcs10.DomainSubsetType;
import net.opengis.wcs10.GetCoverageType;
import net.opengis.wcs10.IntervalType;
import net.opengis.wcs10.OutputType;
import net.opengis.wcs10.RangeSubsetType;
import net.opengis.wcs10.SpatialSubsetType;
import net.opengis.wcs10.TimeSequenceType;
import net.opengis.wcs10.TypedLiteralType;
import net.opengis.wcs10.Wcs10Factory;

import org.geoserver.catalog.Catalog;
import org.geoserver.ows.kvp.EMFKvpRequestReader;
import org.geoserver.ows.util.KvpUtils;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.ows.util.KvpUtils.Tokenizer;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;

public class Wcs10GetCoverageRequestReader extends EMFKvpRequestReader {

    Catalog catalog;

    public Wcs10GetCoverageRequestReader(Catalog catalog) {
        super(GetCoverageType.class, Wcs10Factory.eINSTANCE);
        this.catalog = catalog;
    }

    @Override
    public Object read(Object request, Map kvp, Map rawKvp) throws Exception {
        GetCoverageType getCoverage = (GetCoverageType) super.read(request, kvp, rawKvp);

        // grab coverage info to perform further checks
        if (getCoverage.getSourceCoverage() == null) {
            if (kvp.get("coverage") == null)
                throw new WcsException("source coverage parameter is mandatory",
                        MissingParameterValue, "source coverage");
            else
                getCoverage.setSourceCoverage((String) ((List) kvp.get("coverage")).get(0));
        }
        // if not specified, throw a resounding exception (by spec)
        if (!getCoverage.isSetVersion())
            throw new WcsException("Version has not been specified",
                    WcsExceptionCode.MissingParameterValue, "version");

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
            throw new WcsException("An invalid version number has been specified",
                    WcsExceptionCode.InvalidParameterValue, "version");
        }
        getCoverage.setVersion("1.0.0");

        // build the domain subset
        getCoverage.setDomainSubset(parseDomainSubset(kvp));

        // build the range subset
        getCoverage.setRangeSubset(parseRangeSubset(kvp, getCoverage.getSourceCoverage()));

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
        ReferencedEnvelope bbox = (ReferencedEnvelope) kvp.get("BBOX");
        if (bbox == null)
            throw new WcsException("bbox parameter is mandatory", MissingParameterValue, "bbox");
        GeneralEnvelope envelope = new GeneralEnvelope(crs);
        envelope.setEnvelope(bbox.getLowerCorner().getOrdinate(0), bbox.getLowerCorner()
                .getOrdinate(1), bbox.getUpperCorner().getOrdinate(0), bbox.getUpperCorner()
                .getOrdinate(1));
        TimeSequenceType timeSequence = null;
        Object time = kvp.get("TIME");
        if (time != null && time instanceof TimeSequenceType) {
            timeSequence = (TimeSequenceType) time;
        } else if (time != null && time instanceof List) {
            timeSequence = Wcs10Factory.eINSTANCE.createTimeSequenceType();
            for (Date tPos : (List<Date>) time) {
                TimePositionType timePosition = Gml4wcsFactory.eINSTANCE.createTimePositionType();
                timePosition.setValue(tPos);
                timeSequence.getTimePosition().add(timePosition);
            }
        }

        if (timeSequence == null && bbox == null)
            throw new WcsException("Bounding box cannot be null, TIME has not been specified",
                    WcsExceptionCode.MissingParameterValue, "BBOX");

        GridType grid = Gml4wcsFactory.eINSTANCE.createGridType();
        if (kvp.get("width") != null && kvp.get("height") != null) {
            double width = kvp.get("width") instanceof Integer ? ((Integer) kvp.get("width"))
                    .doubleValue() : Double.parseDouble((String) kvp.get("width"));
            double height = kvp.get("height") instanceof Integer ? ((Integer) kvp.get("height"))
                    .doubleValue() : Double.parseDouble((String) kvp.get("height"));

            grid.getAxisName().add("lon");
            grid.getAxisName().add("lat");

            if (kvp.get("depth") != null) {
                grid.getAxisName().add("elevation");

                double depth = kvp.get("depth") instanceof Integer ? ((Integer) kvp.get("depth"))
                        .doubleValue() : Double.parseDouble((String) kvp.get("depth"));
                grid.setDimension(BigInteger.valueOf(3));
                grid.setLimits(new GeneralEnvelope(new double[]{0.0, 0.0, depth}, new double[]{width, height, depth}));
            } else {
                grid.setDimension(BigInteger.valueOf(2));
                grid.setLimits(new GeneralEnvelope(new double[]{0.0, 0.0}, new double[]{width, height}));
            }
        } else if (kvp.get("resx") != null && kvp.get("resy") != null) {
            double resX = Double.parseDouble((String) kvp.get("resx"));
            double resY = Double.parseDouble((String) kvp.get("resy"));

            int width = (int) Math.round((envelope.getUpperCorner().getOrdinate(0) - envelope.getLowerCorner().getOrdinate(0)) / resX);
            int height = (int) Math.round((envelope.getUpperCorner().getOrdinate(1) - envelope.getLowerCorner().getOrdinate(1)) / resY);

            grid.getAxisName().add("lon");
            grid.getAxisName().add("lat");

            if (kvp.get("resz") != null) {
                grid.getAxisName().add("elevation");
                
                double resZ = Double.parseDouble((String) kvp.get("resz"));
                double depth = (envelope.getUpperCorner().getOrdinate(2) - envelope.getLowerCorner().getOrdinate(2)) / resZ;
                grid.setDimension(BigInteger.valueOf(3));
                grid.setLimits(new GeneralEnvelope(new double[]{0.0, 0.0, depth}, new double[]{width, height, depth}));
            } else {
                grid.setDimension(BigInteger.valueOf(2));
                grid.setLimits(new GeneralEnvelope(new double[]{0.0, 0.0}, new double[]{width, height}));
            }
        } else
            throw new WcsException("Could not recognize grid resolution", InvalidParameterValue, "");

        spatialSubset.getEnvelope().add(envelope);

        if (bbox.getDimension() > 2) {
            spatialSubset.getEnvelope().add(
                    new GeneralEnvelope(bbox.getLowerCorner().getOrdinate(2), bbox.getUpperCorner()
                            .getOrdinate(2)));
        }

        spatialSubset.getGrid().add(grid);
        domainSubset.setSpatialSubset(spatialSubset);
        domainSubset.setTemporalSubset(timeSequence);

        return domainSubset;
    }

    private RangeSubsetType parseRangeSubset(Map kvp, String coverageName) {
        final RangeSubsetType rangeSubset = Wcs10Factory.eINSTANCE.createRangeSubsetType();

        if (kvp.get("Band") != null) {
            Object axis = kvp.get("Band");
            if (axis instanceof String) {
                checkStringTypeAxisRange(rangeSubset, axis);
            } else if (axis instanceof AxisSubsetType) {
                rangeSubset.getAxisSubset().add(axis);
            }
        }

        return rangeSubset;
    }

    /**
     * @param rangeSubset
     * @param axis
     */
    private void checkStringTypeAxisRange(final RangeSubsetType rangeSubset, Object axis) {
        String bands = (String) axis;
        if (bands != null) {
            if (bands.contains("/")) {
                List<String> unparsed = KvpUtils.readFlat(bands, new Tokenizer("/"));

                IntervalType interval = Wcs10Factory.eINSTANCE.createIntervalType();
                TypedLiteralType min = Wcs10Factory.eINSTANCE.createTypedLiteralType();
                TypedLiteralType max = Wcs10Factory.eINSTANCE.createTypedLiteralType();
                TypedLiteralType res = Wcs10Factory.eINSTANCE.createTypedLiteralType();
                if (unparsed.size() == 2) {
                    min.setValue(unparsed.get(0));
                    max.setValue(unparsed.get(1));

                    interval.setMin(min);
                    interval.setMax(max);
                } else {
                    min.setValue(unparsed.get(0));
                    max.setValue(unparsed.get(1));
                    res.setValue(unparsed.get(2));

                    interval.setMin(min);
                    interval.setMax(max);
                    interval.setRes(res);
                }

                final AxisSubsetType axisSubset = Wcs10Factory.eINSTANCE.createAxisSubsetType();

                axisSubset.setName("Band");

                axisSubset.getInterval().add(interval);

                rangeSubset.getAxisSubset().add(axisSubset);

            } else {
                List<String> unparsed = KvpUtils.readFlat(bands, KvpUtils.INNER_DELIMETER);

                if (unparsed.size() == 0) {
                    throw new WcsException(
                            "Requested axis subset contains wrong number of values (should have at least 1): "
                                    + unparsed.size(), WcsExceptionCode.InvalidParameterValue,
                            "band");
                }

                final AxisSubsetType axisSubset = Wcs10Factory.eINSTANCE.createAxisSubsetType();

                axisSubset.setName("Band");

                for (String bandValue : unparsed) {
                    TypedLiteralType singleValue = Wcs10Factory.eINSTANCE.createTypedLiteralType();
                    singleValue.setValue(bandValue);

                    axisSubset.getSingleValue().add(singleValue);

                }
                rangeSubset.getAxisSubset().add(axisSubset);
            }
        }
    }

    private OutputType parseOutputElement(Map kvp) throws Exception {
        final OutputType output = Wcs10Factory.eINSTANCE.createOutputType();
        final CodeType crsType = Gml4wcsFactory.eINSTANCE.createCodeType();
        final CodeType formatType = Gml4wcsFactory.eINSTANCE.createCodeType();

        // check and set format
        String format = (String) kvp.get("format");
        if (format == null)
            throw new WcsException("format parameter is mandatory", MissingParameterValue, "format");

        String crsName = (String) (kvp.get("response_crs") != null ? kvp.get("response_crs") : kvp
                .get("crs"));
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
        if ("WGS84(DD)".equals(crsName) || "urn:ogc:def:crs:OGC:1.3:CRS84".equals(crsName)) {
            crsName = "EPSG:4326";
        }

        try {
            crs = CRS.decode(crsName, true);
        } catch (NoSuchAuthorityCodeException e) {
            throw new WcsException("Could not recognize crs " + crsName, InvalidParameterValue,
                    "crs");
        } catch (FactoryException e) {
            throw new WcsException("Could not recognize crs " + crsName, InvalidParameterValue,
                    "crs");
        }

        return crs;
    }

}
