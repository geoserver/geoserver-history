/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wcs;

import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.InvalidParameterValue;
import it.geosolutions.imageio.stream.output.FileImageOutputStreamExtImpl;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.media.jai.Interpolation;

import net.opengis.gml.GridType;
import net.opengis.wcs10.AxisSubsetType;
import net.opengis.wcs10.DescribeCoverageType;
import net.opengis.wcs10.DomainSubsetType;
import net.opengis.wcs10.GetCapabilitiesType;
import net.opengis.wcs10.GetCoverageType;
import net.opengis.wcs10.InterpolationMethodType;
import net.opengis.wcs10.IntervalType;
import net.opengis.wcs10.OutputType;
import net.opengis.wcs10.RangeSubsetType;
import net.opengis.wcs10.TypedLiteralType;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.data.util.CoverageUtils;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.wcs.response.Wcs10CapsTransformer;
import org.geoserver.wcs.response.Wcs10DescribeCoverageTransformer;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.resources.CRSUtilities;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.vfny.geoserver.util.WCSUtils;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;
import org.vfny.geoserver.wcs.responses.CoverageResponseDelegate;
import org.vfny.geoserver.wcs.responses.CoverageResponseDelegateFactory;

/**
 * The Default WCS 1.0.0 Service implementation
 * 
 * @author Alessio Fabiani, GeoSolutions
 */
public class DefaultWebCoverageService100 implements WebCoverageService100 {

    private final static Hints HINTS = new Hints();
    static {
        HINTS.add(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));
    }

    private WCSInfo wcs;

    private Catalog catalog;

    private GeoServer geoServer;

	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(DefaultWebCoverageService100.class);

    /**
     * 
     * @param geoServer
     */
    public DefaultWebCoverageService100(GeoServer geoServer) {
        this.wcs = geoServer.getService(WCSInfo.class);
        this.geoServer = geoServer;
        this.catalog = geoServer.getCatalog();
    }

    /**
     * 
     */
    public WCSInfo getServiceInfo() {
        return wcs;
    }

    /**
     * 
     */
    public Wcs10CapsTransformer getCapabilities(GetCapabilitiesType request) {
        // do the version negotiation dance
        List<String> provided = new ArrayList<String>();
        provided.add("1.0.0");
        List<String> accepted = null;
        if (request.getVersion() != null) {
            accepted = new ArrayList<String>();
            accepted.add(request.getVersion());
        }
        String version = RequestUtils.getVersionPreOws(provided, accepted);

        if ("1.0.0".equals(version)) {
            Wcs10CapsTransformer capsTransformer = new Wcs10CapsTransformer(geoServer);
            capsTransformer.setEncoding(Charset.forName((wcs.getGeoServer().getGlobal()
                    .getCharset())));
            return capsTransformer;
        }

        throw new WcsException("Could not understand version:" + version);
    }

    /**
     * 
     */
    public Wcs10DescribeCoverageTransformer describeCoverage(DescribeCoverageType request) {
        final String version = request.getVersion();
        if ("1.0.0".equals(version)) {
            Wcs10DescribeCoverageTransformer describeTransformer = new Wcs10DescribeCoverageTransformer(
                    wcs, catalog);
            describeTransformer.setEncoding(Charset.forName((wcs.getGeoServer().getGlobal()
                    .getCharset())));
            return describeTransformer;
        }

        throw new WcsException("Could not understand version:" + version);
    }

    /**
     * 
     */
    public GridCoverage[] getCoverage(GetCoverageType request) {
        CoverageInfo meta = null;
        GridCoverage2D coverage = null;
        List<GridCoverage> coverageResults = new ArrayList<GridCoverage>();
        try {

        	// acquire coverage info
            meta = catalog.getCoverageByName(request.getSourceCoverage());

            // first let's run some sanity checks on the inputs
            checkDomainSubset(meta, request.getDomainSubset());
            checkRangeSubset(meta, request.getRangeSubset());
            checkInterpolationMethod(meta, request.getInterpolationMethod());
            checkOutput(meta, request.getOutput());

            // grab the reader using the default params,
            final AbstractGridCoverage2DReader reader = (AbstractGridCoverage2DReader) meta.getGridCoverageReader(null, HINTS);
            if (reader != null) {
                // handle spatial domain subset, if needed
                final GeneralEnvelope nativeEnvelope = reader.getOriginalEnvelope();
                final CoordinateReferenceSystem nativeCRS = nativeEnvelope.getCoordinateReferenceSystem();

                // get requested crs
                String requestedCRS = null;
                if (request.getOutput().getCrs() != null)
                    requestedCRS = request.getOutput().getCrs().getValue();

                // Compute the target crs, the crs that the final coverage will be served into
                final CoordinateReferenceSystem targetCRS;
                if (requestedCRS == null) {
                    targetCRS = reader.getOriginalEnvelope().getCoordinateReferenceSystem();
                    requestedCRS = CRS.lookupIdentifier(targetCRS, false);
                } else
                    targetCRS = CRS.decode(requestedCRS,true);
                
                //
                // get requested envelope
                //
                final GeneralEnvelope requestedEnvelope ;
                Object reqEnvelope = request.getDomainSubset().getSpatialSubset().getEnvelope().get(0);
                if (reqEnvelope instanceof com.vividsolutions.jts.geom.Envelope)
                    requestedEnvelope = new GeneralEnvelope(new ReferencedEnvelope((com.vividsolutions.jts.geom.Envelope)reqEnvelope, targetCRS));
                else if (reqEnvelope instanceof Envelope)
                    requestedEnvelope =new GeneralEnvelope((Envelope) reqEnvelope);
                else
                	throw new IllegalArgumentException("Illegal bounding box requested");
                
                //
                // compute intersection envelope
                //
                final GeneralEnvelope destinationEnvelope = computeIntersectionEnvelope(requestedEnvelope,nativeEnvelope);
                     

                //
                // Raster destination size
                //
                Rectangle destinationSize = null;
                if (requestedCRS != null
                        && request.getDomainSubset().getSpatialSubset().getGrid().size() > 0) {
                    final GridType grid = (GridType) request.getDomainSubset().getSpatialSubset().getGrid().get(0);
                    
                    int[] lowers = new int[] { (int) grid.getLimits().getMinX(),(int) grid.getLimits().getMinY() };
                    int[] highers = new int[] { (int) grid.getLimits().getMaxX(),(int) grid.getLimits().getMaxY() };
                    destinationSize = new Rectangle(lowers[0], lowers[1], highers[0], highers[1]);
                }

            
                final Map parameters = CoverageUtils.getParametersKVP(reader.getFormat().getReadParameters());
                final GridGeometry2D requestedGridGeometry = new GridGeometry2D(
                		new GridEnvelope2D(destinationSize),
                		requestedEnvelope
                );
                parameters.put(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString(),requestedGridGeometry);
                coverage = (GridCoverage2D) reader.read(CoverageUtils.getParameters(reader.getFormat().getReadParameters(), parameters, true));
                if ((coverage == null) || !(coverage instanceof GridCoverage2D)) {
                    throw new IOException("The requested coverage could not be found.");
                }
                ImageIO.write(coverage.getRenderedImage(), "tiff", new FileImageOutputStreamExtImpl(new File("/media/ext_hd_01/work/data/wcs/test.tif")));

                //
                // Band Select (works on just one field)
                //
                GridCoverage2D bandSelectedCoverage = coverage;
                // ImageIOUtilities.visualize(coverage.getRenderedImage());
                String interpolationType = null;
                if (request.getRangeSubset() != null) {
                    if (request.getRangeSubset().getAxisSubset().size() > 1) {
                        throw new WcsException("Multi field coverages are not supported yet");
                    }

                    interpolationType = request.getInterpolationMethod().getLiteral();

                    // extract the band indexes
                    List axisSubset = request.getRangeSubset().getAxisSubset();
                    if (axisSubset.size() > 0) {
                        AxisSubsetType axis = (AxisSubsetType) axisSubset.get(0);

                        try {
                            String axisName = axis.getName();

                            int[] bands = null;
                            if (axisName.equals("Band") && axis.getSingleValue().size() > 0) {
                                bands = new int[axis.getSingleValue().size()];
                                for (int s = 0; s < axis.getSingleValue().size(); s++) {
                                    bands[s] = Integer.parseInt(((TypedLiteralType) axis.getSingleValue().get(s)).getValue()) - 1;
                                }
                            } else if (axis.getInterval().size() > 0) {
                                IntervalType interval = (IntervalType) axis.getInterval().get(0);
                                int min = Integer.parseInt(interval.getMin().getValue());
                                int max = Integer.parseInt(interval.getMax().getValue());
                                int res = (interval.getRes() != null ? Integer.parseInt(interval.getRes().getValue()) : 1);

                                bands = new int[(int) (Math.floor(max - min) / res + 1)];
                                for (int b = 0; b < bands.length; b++)
                                    bands[b] = (min + b * res) - 1;
                            }

                            // finally execute the band select
                            bandSelectedCoverage = (GridCoverage2D) WCSUtils.bandSelect(coverage,bands);
                        } catch (Exception e) {
                            // Warning: Axis not found!!!
                            throw new WcsException("Band Select Operation: "
                                    + e.getLocalizedMessage());
                        }
                    }
                }

                //
                // Checking for supported Interpolation Methods
                //
                Interpolation interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
                if (interpolationType != null) {
                    if (interpolationType.equalsIgnoreCase("bilinear")) {
                        interpolation = Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
                    } else if (interpolationType.equalsIgnoreCase("bicubic")) {
                        interpolation = Interpolation.getInstance(Interpolation.INTERP_BICUBIC);
                    } else if (interpolationType.equalsIgnoreCase("nearest neighbor")) {
                        interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
                    }
                }


                //
                // final step for the requested coverage
                //
                final GridGeometry2D destinationGridGeometry = new GridGeometry2D(
                		new GridEnvelope2D(destinationSize),
                		destinationEnvelope
                );
                final GridCoverage2D reprojectedCoverage = WCSUtils.resample(bandSelectedCoverage,nativeCRS, targetCRS,destinationGridGeometry, interpolation);
                ImageIO.write(reprojectedCoverage.getRenderedImage(), "tiff", new FileImageOutputStreamExtImpl(new File("/media/ext_hd_01/work/data/wcs/test1.tif")));
                coverageResults.add(reprojectedCoverage);
            }

            return coverageResults.toArray(new GridCoverage2D[] {});
        } catch (Exception e) {
            if (e instanceof WcsException)
                throw (WcsException) e;
            else
                throw new WcsException(e);
        }

    }

    private static GeneralEnvelope computeIntersectionEnvelope(
    		final GeneralEnvelope requestedEnvelope, 
    		final GeneralEnvelope nativeEnvelope) {

    	GeneralEnvelope retVal;
    	// get the crs for the requested bbox
        final CoordinateReferenceSystem requestCRS = CRS.getHorizontalCRS(requestedEnvelope.getCoordinateReferenceSystem());
        final CoordinateReferenceSystem nativeCRS = CRS.getHorizontalCRS(nativeEnvelope.getCoordinateReferenceSystem());
        
        try {
        	//
			// If this approach succeeds, either the request crs is the same of
			// the coverage crs or the request bbox can be reprojected to that
			// crs
        	//        	
            MathTransform destinationToSourceTransform=null;
			// STEP 1: reproject requested BBox to native CRS if needed
            if (!CRS.equalsIgnoreMetadata(requestCRS,nativeCRS))
                destinationToSourceTransform = CRS.findMathTransform(requestCRS,nativeCRS, true);
            // now transform the requested envelope to source crs
            if (destinationToSourceTransform != null && !destinationToSourceTransform.isIdentity())
            {
            	retVal = CRS.transform(destinationToSourceTransform,requestedEnvelope);
            	retVal.setCoordinateReferenceSystem(nativeCRS);
            	
            }
            else
            {
            	//we do not need to do anything, but we do this in order to aboid problems with the envelope checks
            	retVal= new GeneralEnvelope(requestedEnvelope);
            	
            }            


            //
            // STEP 2: intersect requested BBox in native CRS with coverage native bbox to get the crop bbox
        	//
            // intersect the requested area with the bounds of this
            // layer in native crs
            if (!retVal.intersects(nativeEnvelope,true))
            	return null;
            
            // intersect
            retVal.intersect(nativeEnvelope); 
            retVal.setCoordinateReferenceSystem(nativeCRS);
            return retVal;
            

        } catch (TransformException te) {
            // something bad happened while trying to transform this
            // envelope. let's try with wgs84
            if(LOGGER.isLoggable(Level.FINE))
            	LOGGER.log(Level.FINE,te.getLocalizedMessage(),te);
        } catch (FactoryException fe) {
            // something bad happened while trying to transform this
            // envelope. let's try with wgs84
            if(LOGGER.isLoggable(Level.FINE))
            	LOGGER.log(Level.FINE,fe.getLocalizedMessage(),fe);
        }
        

        try {

	        //
			// If we can not reproject the requested envelope to the native CRS,
			// we go back to reproject in the geographic crs of the native
			// coverage since this usually happens for conversions between CRS
			// whose area of definition is different
	        //              
        	final CoordinateReferenceSystem nativeGeoCRS=CRSUtilities.getStandardGeographicCRS2D(nativeCRS);
        	final GeneralEnvelope nativeGeoEnvelope= (GeneralEnvelope) CRS.transform(nativeEnvelope,nativeGeoCRS);
        	nativeGeoEnvelope.setCoordinateReferenceSystem(nativeGeoCRS);
        	
        	GeneralEnvelope requestedBBOXInNativeGeographicCRS=null;
			// STEP 1 reproject the requested envelope to the coverage geographic bbox
	        if(!CRS.equalsIgnoreMetadata(nativeCRS, requestCRS)){
	        	//try to convert the requested bbox to the coverage geocrs
	        	final MathTransform requestCRSToCoverageGeographicCRS2D = CRS.findMathTransform(requestCRS, nativeGeoCRS,true);
	        	if(!requestCRSToCoverageGeographicCRS2D.isIdentity())
	        	{
	        		requestedBBOXInNativeGeographicCRS=CRS.transform(requestCRSToCoverageGeographicCRS2D,requestedEnvelope);
	        		requestedBBOXInNativeGeographicCRS.setCoordinateReferenceSystem(nativeCRS);
	        	}
	        }
	        if(requestedBBOXInNativeGeographicCRS==null)
	        	requestedBBOXInNativeGeographicCRS= new GeneralEnvelope(requestCRS);
	
	
	        // STEP 2 intersection with the geographic bbox for this coverage
	        if (!requestedBBOXInNativeGeographicCRS.intersects(nativeEnvelope,true))
	        	return null;
	        
	        // intersect with the coverage native geographic bbox
	        // note that for the moment we got to use general envelope since there is no intersection otherwise
	        requestedBBOXInNativeGeographicCRS.intersect(nativeGeoEnvelope);
	        requestedBBOXInNativeGeographicCRS.setCoordinateReferenceSystem(nativeGeoCRS);
	        
	        // now go back to the coverage native CRS in order to compute an approximate requested resolution
	        final MathTransform transform = CRS.findMathTransform(nativeGeoCRS,requestCRS, true);
	        final GeneralEnvelope approximateRequestedBBox = CRS.transform(transform, requestedBBOXInNativeGeographicCRS);
	    	approximateRequestedBBox.setCoordinateReferenceSystem(requestCRS);
	    	return approximateRequestedBBox;
	    	
	    	
        } catch (TransformException te) {
            // something bad happened while trying to transform this
            // envelope. let's try with wgs84
            if(LOGGER.isLoggable(Level.FINE))
            	LOGGER.log(Level.FINE,te.getLocalizedMessage(),te);
        } catch (FactoryException fe) {
            // something bad happened while trying to transform this
            // envelope. let's try with wgs84
            if(LOGGER.isLoggable(Level.FINE))
            	LOGGER.log(Level.FINE,fe.getLocalizedMessage(),fe);
        }

        LOGGER.log(Level.INFO,"We did not manage to crop the requested envelope, we fall back onto loading the whole coverage.");
		return null;
	}

	private static void checkDomainSubset(CoverageInfo meta, DomainSubsetType domainSubset)
            throws Exception {
        ReferencedEnvelope bbox = null;
        Object requestedEnvelope = domainSubset.getSpatialSubset().getEnvelope().get(0);
        if (requestedEnvelope instanceof ReferencedEnvelope) {
            bbox = (ReferencedEnvelope) requestedEnvelope;
        } else if (requestedEnvelope instanceof GeneralEnvelope) {
            bbox = new ReferencedEnvelope((GeneralEnvelope) requestedEnvelope);
        }

        CoordinateReferenceSystem bboxCRs = bbox.getCoordinateReferenceSystem();
        // TODO: FIX THIS!!!
        Envelope gridEnvelope = /* meta.getCoverage().getEnvelope() */null;
        GeneralEnvelope gridEnvelopeBboxCRS = null;
        if (bboxCRs instanceof GeographicCRS) {
            try {
                CoordinateOperationFactory cof = CRS.getCoordinateOperationFactory(true);

                final CoordinateOperation operation = cof.createOperation(gridEnvelope
                        .getCoordinateReferenceSystem(), bboxCRs);
                gridEnvelopeBboxCRS = CRS.transform(operation, gridEnvelope);
            } catch (Exception e) {
                // this may happen, there is nothing we can do about it, we just
                // use the back transformed envelope to be more lenient about
                // which coordinate coorections to make on the longitude axis
                // should the antimeridian style be used
            }
        }

        // check the coordinates, but make sure the case 175,-175 is handled
        // as valid for the longitude axis in a geographic coordinate system
        // see section 7.6.2 of the WCS 1.1.1 spec)
        double[] lower = bbox.getLowerCorner().getCoordinate();
        double[] upper = bbox.getUpperCorner().getCoordinate();
        for (int i = 0; i < lower.length; i++) {
            if (lower[i] > upper[i]) {
                final CoordinateSystemAxis axis = bboxCRs.getCoordinateSystem().getAxis(i);
                // see if the coordinates can be fixed
                if (bboxCRs instanceof GeographicCRS && axis.getDirection() == AxisDirection.EAST) {

                    if (gridEnvelopeBboxCRS != null) {
                        // try to guess which one needs to be fixed
                        final double envMax = gridEnvelopeBboxCRS.getMaximum(i);
                        if (envMax >= lower[i])
                            upper[i] = upper[i] + (axis.getMaximumValue() - axis.getMinimumValue());
                        else
                            lower[i] = lower[i] - (axis.getMaximumValue() - axis.getMinimumValue());

                    } else {
                        // just fix the upper and hope...
                        upper[i] = upper[i] + (axis.getMaximumValue() - axis.getMinimumValue());
                    }
                }

                // if even after the fix we're in the wrong situation, complain
                if (lower[i] > upper[i]) {
                    throw new WcsException(
                            "illegal bbox, min of dimension " + (i + 1) + ": " + lower[i] + " is "
                                    + "greater than max of same dimension: " + upper[i],
                            WcsExceptionCode.InvalidParameterValue, "BoundingBox");
                }
            }

        }
    }

    private static void checkInterpolationMethod(CoverageInfo info,
            InterpolationMethodType interpolationMethod) {
        // check interpolation method
        String interpolation = interpolationMethod.getLiteral();
        if (interpolation != null) {
            boolean interpolationSupported = false;

            if (interpolation.startsWith("nearest")) {
                interpolation = "nearest neighbor";
            }
            for (Iterator it = info.getInterpolationMethods().iterator(); it.hasNext();) {
                String method = (String) it.next();
                if (interpolation.equalsIgnoreCase(method)) {
                    interpolationSupported = true;
                }
            }

            if (!interpolationSupported)
                throw new WcsException(
                        "The requested Interpolation method is not supported by this Coverage.",
                        InvalidParameterValue, "RangeSubset");
        }
    }

    /**
     * Checks that the elements of the Output part of the request do make sense by comparing them to
     * the coverage metadata
     * 
     * @param info
     * @param rangeSubset
     */
    private static void checkOutput(CoverageInfo meta, OutputType output) {
        if (output == null)
            return;

        // check output format
        String format = output.getFormat().getValue();
        String declaredFormat = getDeclaredFormat(meta.getSupportedFormats(), format);
        if (declaredFormat == null)
            throw new WcsException("format " + format + " is not supported for this coverage",
                    InvalidParameterValue, "format");

        // check requested CRS
        // if (output.getCrs() != null) {
        // String requestedCRS = output.getCrs().getValue();
        // if (getRequestResponseCRS(meta.getRequestCRSs(), requestedCRS) == null &&
        // getRequestResponseCRS(meta.getResponseCRSs(), requestedCRS) == null)
        // throw new WcsException("CRS " + requestedCRS + " is not supported for this coverage",
        // InvalidParameterValue, "CRS");
        // } else {
        // // The requested CRS was not specified ... what to do ???
        // }
    }

    /**
     * Extracts only the final part of an EPSG code allowing for a specification independent
     * comparison (that is, it removes the EPSG:, urn:xxx:, http://... prefixes)
     * 
     * @param srsName
     * @return
     */
    private String extractCode(String srsName) {
        if (srsName.startsWith("http://www.opengis.net/gml/srs/epsg.xml#"))
            return srsName.substring(40);
        else if (srsName.startsWith("urn:"))
            return srsName.substring(srsName.lastIndexOf(':') + 1);
        else if (srsName.startsWith("EPSG:"))
            return srsName.substring(5);
        else
            return srsName;
    }

    /**
     * Checks if the supported format string list contains the specified format, doing a case
     * insensitive search. If found the declared output format name is returned, otherwise null is
     * returned.
     * 
     * @param supportedFormats
     * @param format
     * @return
     */
    private static String getDeclaredFormat(List supportedFormats, String format) {
        // supported formats may be setup using old style formats, first scan
        // the configured list
        for (Iterator it = supportedFormats.iterator(); it.hasNext();) {
            String sf = (String) it.next();
            if (sf.equalsIgnoreCase(format.trim())) {
                return sf;
            } else {
                CoverageResponseDelegate delegate = CoverageResponseDelegateFactory.encoderFor(sf);
                if (delegate != null && delegate.canProduce(format))
                    return sf;
            }
        }
        return null;
    }

    /**
     * Checks if the request/response CRS list contains the requested output CRS, doing a case
     * insensitive search. If found the requested output CRS ID is returned, otherwise null is
     * returned.
     * 
     * @param requestResoinseCRSs
     * @param crs
     * @return
     */
    private String getRequestResponseCRS(List requestResoinseCRSs, String crs) {
        // supported formats may be setup using old style formats, first scan
        // the configured list
        for (Iterator it = requestResoinseCRSs.iterator(); it.hasNext();) {
            String reqResCRS = (String) it.next();
            if (reqResCRS.equalsIgnoreCase(crs)) {
                return reqResCRS;
            }
        }
        return null;
    }

    /**
     * Checks that the elements of the RangeSubset part of the request do make sense by comparing
     * them to the coverage metadata
     * 
     * @param info
     * @param rangeSubset
     */
    private static void checkRangeSubset(CoverageInfo info, RangeSubsetType rangeSubset) {
        // quick escape if no range subset has been specified (it's legal)
        if (rangeSubset == null)
            return;

        // check axis
        if (rangeSubset.getAxisSubset().size() > 1) {
            throw new WcsException("Multi axis coverages are not supported yet",
                    InvalidParameterValue, "RangeSubset");
        } else if (rangeSubset.getAxisSubset().size() == 0)
            return;

        AxisSubsetType axisSubset = (AxisSubsetType) rangeSubset.getAxisSubset().get(0);

        // prepare a support structure to quickly get the band index of a key
        // (and remember we replaced spaces with underscores in the keys to
        // avoid issues with the kvp parsing of indentifiers that include spaces)

        // check indexes
        int[] bands = null;
        if (axisSubset.getSingleValue().size() > 0) {
            bands = new int[1];
            bands[0] = Integer.parseInt(((TypedLiteralType) axisSubset.getSingleValue().get(0))
                    .getValue());
        } else if (axisSubset.getInterval().size() > 0) {
            IntervalType interval = (IntervalType) axisSubset.getInterval().get(0);
            int min = Integer.parseInt(interval.getMin().getValue());
            int max = Integer.parseInt(interval.getMax().getValue());
            int res = (interval.getRes() != null ? Integer.parseInt(interval.getRes().getValue())
                    : 1);

            bands = new int[(max - min) / res];
            for (int b = 0; b < bands.length; b++)
                bands[b] = min + (b * res);
        }

        if (bands == null)
            throw new WcsException("Invalid values for axis " + axisSubset.getName(),
                    InvalidParameterValue, "AxisSubset");
    }

}