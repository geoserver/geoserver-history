/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.kvp;

import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.InvalidParameterValue;
import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.MissingParameterValue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.opengis.gml.CodeType;
import net.opengis.gml.DirectPositionType;
import net.opengis.gml.Gml4wcsFactory;
import net.opengis.gml.PointType;
import net.opengis.gml.RectifiedGridType;
import net.opengis.gml.TimePositionType;
import net.opengis.gml.VectorType;
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
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.VerticalCRS;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;
/**
 * GetCoverage request reader for WCS 1.0.0
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
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
                throw new WcsException("source coverage parameter is mandatory",MissingParameterValue, "source coverage");
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

    /**
     * 
     * @param kvp
     * @return
     */
    private DomainSubsetType parseDomainSubset(Map kvp) {
        final DomainSubsetType domainSubset = Wcs10Factory.eINSTANCE.createDomainSubsetType();
        final SpatialSubsetType spatialSubset = Wcs10Factory.eINSTANCE.createSpatialSubsetType();

        //
        // check for CRS
        //
        String crsName = (String) kvp.get("crs");
        CoordinateReferenceSystem crs = null;
        if (crsName == null)
            throw new WcsException("crs parameter is mandatory", MissingParameterValue, "crs");
        crs = decodeCRS100(crsName, crs);
        final VerticalCRS verticalCRS = CRS.getVerticalCRS(crs);
        final boolean hasVerticalCRS=verticalCRS!=null;


        //
        // at least one between BBOX and TIME must be there
        //
        final GeneralEnvelope bbox =  (GeneralEnvelope) kvp.get("BBOX");
        if (bbox == null)
            throw new WcsException("bbox parameter is mandatory", MissingParameterValue, "bbox");
        final GeneralEnvelope envelope = new GeneralEnvelope(crs);
        if(!hasVerticalCRS)
        	envelope.setEnvelope(
        			bbox.getLowerCorner().getOrdinate(0),
        			bbox.getLowerCorner().getOrdinate(1), 
        			bbox.getUpperCorner().getOrdinate(0), 
        			bbox.getUpperCorner().getOrdinate(1)
        			);
        else //3D
        	envelope.setEnvelope(
        			bbox.getLowerCorner().getOrdinate(0),
        			bbox.getLowerCorner().getOrdinate(1), 
        			bbox.getLowerCorner().getOrdinate(2),
        			bbox.getUpperCorner().getOrdinate(0), 
        			bbox.getUpperCorner().getOrdinate(1),
        			bbox.getUpperCorner().getOrdinate(2)
        			);
        //
        // TIME
        //
        TimeSequenceType timeSequence = null;
        Object time = kvp.get("TIME");
        if (time != null && time instanceof TimeSequenceType) {
            timeSequence = (TimeSequenceType) time;
        } else if (time != null && time instanceof List) {
            timeSequence = Wcs10Factory.eINSTANCE.createTimeSequenceType();
            for (Date tPos : (List<Date>) time) {
                final TimePositionType timePosition = Gml4wcsFactory.eINSTANCE.createTimePositionType();
                timePosition.setValue(tPos);
                timeSequence.getTimePosition().add(timePosition);
            }
        }
        if (timeSequence == null && bbox == null)
            throw new WcsException("Bounding box cannot be null, TIME has not been specified",WcsExceptionCode.MissingParameterValue, "BBOX");

        //
        // GRID management
        //
        final RectifiedGridType grid = Gml4wcsFactory.eINSTANCE.createRectifiedGridType();
        final Object w=kvp.get("width");
        final Object h=kvp.get("height");
        if ( w!= null && h != null) {
        	//
        	// normal grid management, we need to compute RESX, RESY, RESZ afterwards
        	//
        	
        	// get W and H
            int width = Integer.parseInt((String)w);
            int height = Integer.parseInt((String) h);
            grid.getAxisName().add("x");
            grid.getAxisName().add("y");
                       
            // now compute offset and origin
            final double resX = envelope.getSpan(0)/width;
            final double resY = envelope.getSpan(1)/height;
            
            
            // now compute offset vector for the transform from the envelope
            final double origX=envelope.getLowerCorner().getOrdinate(0);
            final double origY=envelope.getLowerCorner().getOrdinate(1);
            
            // create offset point
            final PointType origin= Gml4wcsFactory.eINSTANCE.createPointType();
            final DirectPositionType dp= Gml4wcsFactory.eINSTANCE.createDirectPositionType();
            origin.setPos(dp);
            origin.setSrsName(crsName);
            
            
            // create resolutions vector
            final VectorType resolutionVector=Gml4wcsFactory.eINSTANCE.createVectorType();
                                   

            final Object d=kvp.get("depth") ;
            if (d!= null) {

                // check that the envelope is 3D or throw an error
                if(bbox.getDimension()!=3)
                	throw new WcsException("Found depth but envelope is of dimension "+bbox.getDimension(), InvalidParameterValue, "");
                
            	// notice that as for the spec this element represents the number of ticks on the third dimension
                grid.getAxisName().add("z");

                final int depth = Integer.parseInt((String) d);
                grid.setDimension(BigInteger.valueOf(3));
                grid.setLimits(new GeneralGridEnvelope(new int[]{0, 0, depth}, new int[]{width, height, depth}));

                final double resZ = (bbox.getUpperCorner().getOrdinate(2)-bbox.getLowerCorner().getOrdinate(2))/depth;
                final double origZ = bbox.getLowerCorner().getOrdinate(2);
                
                // 3D grid
                grid.setDimension(BigInteger.valueOf(3));
                // set the origin position
                dp.setDimension(grid.getDimension());
                dp.setValue(Arrays.asList(origX,origY,origZ));
                grid.setOrigin(origin);
                
                // set the resolution vector
                resolutionVector.setDimension(grid.getDimension());
                resolutionVector.setValue(Arrays.asList(resX,resY,resZ));
            } else {
                // 2d grid
                grid.setDimension(BigInteger.valueOf(2));
                grid.setLimits(new GridEnvelope2D(0, 0,width, height));
                
                // set the origin position
                dp.setDimension(grid.getDimension());
                dp.setValue(Arrays.asList(origX,origY));
                grid.setOrigin(origin);
                
                // set the resolution vector
                resolutionVector.setDimension(grid.getDimension());
                resolutionVector.setValue(Arrays.asList(resX,resY));                
            }
        } else {
        		//
				// we might be working with a rectified grid request there we need
				// to try and use that type. we cannot build a raster grid at this
				// stage yet since we have no idea about how the envelope will be intersected with the native envelope for this raster
        		//
	            final Object rx=kvp.get("resx");
	            final Object ry=kvp.get("resy");
	        	if (rx != null && ry != null) {
	        
	        		// get resx e resy
		            final double resX = Double.parseDouble((String) rx);
		            final double resY = Double.parseDouble((String) ry);
		            
		            
		            // now compute offset vector for the transform from the envelope
		            final double origX=envelope.getLowerCorner().getOrdinate(0);
		            final double origY=envelope.getLowerCorner().getOrdinate(1);
		            
		            // create offset point
		            final PointType origin= Gml4wcsFactory.eINSTANCE.createPointType();
		            final DirectPositionType dp= Gml4wcsFactory.eINSTANCE.createDirectPositionType();
		            origin.setPos(dp);
		            origin.setSrsName(crsName);
		            
		            
		            // create resolutions vector
		            final VectorType resolutionVector=Gml4wcsFactory.eINSTANCE.createVectorType();
		                       
		            //
		            // Third dimension management
		            //
		            final Object rz=kvp.get("resz");
		            if (rz != null) {
		                // eventual depth
		                final double resZ = Double.parseDouble((String)rz);
		                // check that the envelope is 3D or throw an error
		                if(bbox.getDimension()!=3)
		                	throw new WcsException("Found ResZ but envelope is of dimension "+bbox.getDimension(), InvalidParameterValue, "");
		                final double origZ = bbox.getLowerCorner().getOrdinate(2);
		                
		                // 3D grid
		                grid.setDimension(BigInteger.valueOf(3));
		                // set the origin position
		                dp.setDimension(grid.getDimension());
		                dp.setValue(Arrays.asList(origX,origY,origZ));
		                grid.setOrigin(origin);
		                
		                // set the resolution vector
		                resolutionVector.setDimension(grid.getDimension());
		                resolutionVector.setValue(Arrays.asList(resX,resY,resZ));
		                
		                
		            } else {
		            	
		            	// 2d grid
		            	grid.setDimension(BigInteger.valueOf(2));
		                // set the origin position
		                dp.setDimension(grid.getDimension());
		                dp.setValue(Arrays.asList(origX,origY));
		                grid.setOrigin(origin);
		                
		                // set the resolution vector
		                resolutionVector.setDimension(grid.getDimension());
		                resolutionVector.setValue(Arrays.asList(resX,resY));

		            }

	            } else
	                throw new WcsException("Could not recognize grid resolution", InvalidParameterValue, "");
        	}
         

        spatialSubset.getEnvelope().add(envelope);
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
            crs = decodeCRS100(crsName, crs);

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
    private static CoordinateReferenceSystem decodeCRS100(String crsName, CoordinateReferenceSystem crs) {
        if ("WGS84(DD)".equals(crsName)) {
            crsName = "EPSG:4326";
        }

        try {
        	// in 100 we work with Lon,Lat always
            crs = CRS.decode(crsName, true);
        } catch (NoSuchAuthorityCodeException e) {
            throw new WcsException("Could not recognize crs " + crsName, InvalidParameterValue,"crs");
        } catch (FactoryException e) {
            throw new WcsException("Could not recognize crs " + crsName, InvalidParameterValue,"crs");
        }

        return crs;
    }

}
