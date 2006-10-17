package org.vfny.geoserver.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.media.jai.Interpolation;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.DefaultProcessor;
import org.geotools.coverage.processing.operation.Crop;
import org.geotools.coverage.processing.operation.Interpolate;
import org.geotools.coverage.processing.operation.Resample;
import org.geotools.coverage.processing.operation.SelectSampleDimension;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.resources.CRSUtilities;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridRange;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.wcs.WcsException;

/**
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 *
 */
public class WCSUtils {
	private final static Hints LENIENT_HINT = new Hints(
			Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);
	
	private static final Logger LOGGER = Logger
	.getLogger("org.vfny.geoserver.util");
	
	private final static SelectSampleDimension bandSelectFactory = new SelectSampleDimension();
	
	private final static Crop cropFactory = new Crop();
	
	private final static Interpolate interpolateFactory = new Interpolate();
	
	private final static Resample resampleFactory = new Resample();
	
	static {
		
		// ///////////////////////////////////////////////////////////////////
		//
		// Static Processors
		//
		// ///////////////////////////////////////////////////////////////////
		final DefaultProcessor processor = new DefaultProcessor(LENIENT_HINT);
		bandSelectParams 	= processor.getOperation("SelectSampleDimension").getParameters();
		cropParams 			= processor.getOperation("CoverageCrop").getParameters();
		interpolateParams 	= processor.getOperation("Interpolate").getParameters();
		resampleParams 		= processor.getOperation("Resample").getParameters();
	}
	
	private final static ParameterValueGroup bandSelectParams;
	
	private final static ParameterValueGroup cropParams;
	
	private final static ParameterValueGroup interpolateParams;
	
	private final static ParameterValueGroup resampleParams;
	
	private final static Hints hints = new Hints(new HashMap(5));
	
	static {
		hints.add(LENIENT_HINT);
	}
	
	/**
	 * <strong>Reprojecting</strong><br>
	 * The new grid geometry can have a different coordinate reference system than the underlying
	 * grid geometry. For example, a grid coverage can be reprojected from a geodetic coordinate
	 * reference system to Universal Transverse Mercator CRS.
	 *
	 * @param coverage GridCoverage2D
	 * @param sourceCRS CoordinateReferenceSystem
	 * @param targetCRS CoordinateReferenceSystem
	 * @return GridCoverage2D
	 * @throws WcsException
	 */
	public static GridCoverage2D reproject(
			GridCoverage2D coverage,
			final CoordinateReferenceSystem sourceCRS,
			final CoordinateReferenceSystem targetCRS
	) throws WcsException {
		/////////////////////////////////////////////////////////////////////
		//
		//REPROJECT
		//
		//
		/////////////////////////////////////////////////////////////////////
		if (!CRSUtilities.equalsIgnoreMetadata(sourceCRS, targetCRS)) {
			/*Operations.DEFAULT.resample(
			 coverage, 
			 targetCRS, 
			 null, 
			 Interpolation.getInstance(Interpolation.INTERP_NEAREST))*/
			final ParameterValueGroup param = (ParameterValueGroup) resampleParams.clone();
			param.parameter("Source").setValue(coverage);
			param.parameter("CoordinateReferenceSystem").setValue(targetCRS);
			param.parameter("GridGeometry").setValue(null);
			param.parameter("InterpolationType").setValue(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
			
			coverage = (GridCoverage2D) resampleFactory.doOperation(param, hints);
		}
		
		return coverage;
	}
	
	/**
	 * <strong>Interpolating</strong><br>
	 * Specifies the interpolation type to be used to interpolate values for points which fall between
	 * grid cells. The default value is nearest neighbor. The new interpolation type operates on all
	 * sample dimensions. Possible values for type are: {@code "NearestNeighbor"}, {@code "Bilinear"}
	 * and {@code "Bicubic"} (the {@code "Optimal"} interpolation type is currently not supported).
	 * 
	 * @param coverage GridCoverage2D
	 * @param interpolation Interpolation
	 * @return GridCoverage2D
	 * @throws WcsException
	 */
	public static GridCoverage2D interpolate(
			GridCoverage2D coverage,			
			final Interpolation interpolation
	) throws WcsException {
		/////////////////////////////////////////////////////////////////////
		//
		//INTERPOLATE
		//
		//
		/////////////////////////////////////////////////////////////////////
		if (interpolation != null) {
			/*Operations.DEFAULT.interpolate(coverage, interpolation)*/
			final ParameterValueGroup param = (ParameterValueGroup) interpolateParams.clone();
			param.parameter("Source").setValue(coverage);
			param.parameter("Type").setValue(interpolation);
			
			coverage = (GridCoverage2D) interpolateFactory.doOperation(param, hints);
		}
		
		return coverage;
	}
	
	/**
	 * <strong>Scaling</strong><br>
	 * Let user to scale down to the EXACT needed resolution. This step does
	 * not prevent from having loaded an overview of the original image
	 * based on the requested scale.
	 * 
	 * @param coverage GridCoverage2D
	 * @param newGridRange GridRange
	 * @param sourceCoverage GridCoverage
	 * @param sourceCRS CoordinateReferenceSystem
	 * @return GridCoverage2D
	 */
	public static GridCoverage2D scale(
			final GridCoverage2D coverage,
			final GridRange newGridRange,
			final GridCoverage sourceCoverage, 
			final CoordinateReferenceSystem sourceCRS) {
		// ///////////////////////////////////////////////////////////////////
		//
		// SCALE to the needed resolution
		// Let me now scale down to the EXACT needed resolution. This step does
		// not prevent from having loaded an overview of the original image
		// based on the requested scale.
		//
		// ///////////////////////////////////////////////////////////////////
		GridGeometry2D scaledGridGeometry = new GridGeometry2D(newGridRange, sourceCoverage.getEnvelope());
		
		/*Operations.DEFAULT.resample(
		 coverage, 
		 sourceCRS, 
		 scaledGridGeometry,
		 Interpolation.getInstance(Interpolation.INTERP_NEAREST));*/
		final ParameterValueGroup param = (ParameterValueGroup) resampleParams.clone();
		param.parameter("Source").setValue(coverage);
		param.parameter("CoordinateReferenceSystem").setValue(sourceCRS);
		param.parameter("GridGeometry").setValue(scaledGridGeometry);
		param.parameter("InterpolationType").setValue(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
		
		final GridCoverage2D scaledGridCoverage = (GridCoverage2D) resampleFactory.doOperation(param, hints);
		
		return scaledGridCoverage;
	}
	
	/**
	 * <strong>Cropping</strong><br>
	 * The crop operation is responsible for selecting geographic subareas of the
	 * source coverage.
	 * 
	 * @param coverage Coverage
	 * @param sourceEnvelope GeneralEnvelope
	 * @param sourceCRS CoordinateReferenceSystem
	 * @param destinationEnvelopeInSourceCRS GeneralEnvelope
	 * @return GridCoverage2D
	 * @throws WcsException
	 */
	public static GridCoverage2D crop(
			final Coverage coverage,
			final GeneralEnvelope sourceEnvelope, 
			final CoordinateReferenceSystem sourceCRS, 
			final GeneralEnvelope destinationEnvelopeInSourceCRS) 
	throws WcsException {
		// ///////////////////////////////////////////////////////////////////
		//
		// CROP
		//
		//
		// ///////////////////////////////////////////////////////////////////
		final GridCoverage2D croppedGridCoverage;
		// intersect the envelopes
		final GeneralEnvelope intersectionEnvelope = new GeneralEnvelope(
				destinationEnvelopeInSourceCRS);
		intersectionEnvelope.setCoordinateReferenceSystem(sourceCRS);
		intersectionEnvelope.intersect((GeneralEnvelope) sourceEnvelope);
		// dow we have something to show?
		if (intersectionEnvelope.isEmpty())
			throw new WcsException(
			"The Intersection is null. Check the requested BBOX!");
		if (!intersectionEnvelope.equals((GeneralEnvelope) sourceEnvelope)) {
			// get the cropped grid geometry
			// final GridGeometry2D cropGridGeometry = getCroppedGridGeometry(
			// intersectionEnvelope, gridCoverage);
			
			/*Operations.DEFAULT.crop(coverage, intersectionEnvelope)*/
			final ParameterValueGroup param = (ParameterValueGroup) cropParams.clone();
			param.parameter("Source").setValue(coverage);
			param.parameter("Envelope").setValue(intersectionEnvelope);
			
			croppedGridCoverage = (GridCoverage2D) cropFactory.doOperation(param, hints);
		} else
			croppedGridCoverage = (GridCoverage2D) coverage;
		
		// prefetch to be faster afterwards.
		// This step is important since at this stage we might be loading tiles
		// from disk
		croppedGridCoverage.prefetch(intersectionEnvelope.toRectangle2D());
		return croppedGridCoverage;
	}
	
	/**
	 * <strong>Band Selecting</strong><br>
	 * Chooses <var>N</var> {@linkplain org.geotools.coverage.GridSampleDimension sample dimensions}
	 * from a grid coverage and copies their sample data to the destination grid coverage in the order
	 * specified. The {@code "SampleDimensions"} parameter specifies the source
	 * {@link org.geotools.coverage.GridSampleDimension} indices, and its size
	 * ({@code SampleDimensions.length}) determines the number of sample dimensions of the destination
	 * grid coverage. The destination coverage may have any number of sample dimensions, and a particular
	 * sample dimension of the source coverage may be repeated in the destination coverage by specifying
	 * it multiple times in the {@code "SampleDimensions"} parameter.
	 * 
	 * @param params Set
	 * @param coverage GridCoverage
	 * @return Coverage
	 */
	public static Coverage bandSelect(final Map params, final GridCoverage coverage) {
		// ///////////////////////////////////////////////////////////////////
		//
		// BAND SELECT
		//
		//
		// ///////////////////////////////////////////////////////////////////
		final int numDimensions = coverage.getNumSampleDimensions();
		final Map dims = new HashMap();
		final ArrayList selectedBands = new ArrayList();
		
		for (int d = 0; d < numDimensions; d++) {
			dims.put("band" + (d+1), new Integer(d));
		}
		
		if (!params.isEmpty()) {
			for (Iterator p = params.keySet().iterator(); p.hasNext();) {
				final String param = (String) p.next();
				if (param.equalsIgnoreCase("BAND")) {
					try {						
						final String values = (String)params.get(param);
						
						if (values.indexOf("/") > 0) {
							final String[] minMaxRes = values.split("/");
							final int min = (int) Math.round(Double.parseDouble(minMaxRes[0]));
							final int max = (int) Math.round(Double.parseDouble(minMaxRes[1]));
							final double res = (minMaxRes.length > 2 ? Double.parseDouble(minMaxRes[2]) : 0.0);

							for (int v=min;v<=max;v++) {
								final String key = param.toLowerCase() + v;
								if (dims.containsKey(key))
									selectedBands.add(dims.get(key));
							}
						} else {
							final String[] bands = values.split(",");
							for (int v=0;v<bands.length;v++) {
								final String key = param.toLowerCase() + bands[v];
								if (dims.containsKey(key))
									selectedBands.add(dims.get(key));
							}
						}
					} catch (Exception e) {
						LOGGER.severe("BAND SELECT: Param Incorrectly Specified.");
					}
				}
			}
		}
		
		final int length = selectedBands.size();
		final int[] bands = new int[length];
		for (int b = 0; b < length; b++) {
			bands[b] = ((Integer) selectedBands.get(b)).intValue();
		}
		
		Coverage bandSelectedCoverage;
		if (bands != null && bands.length > 0) {
			/*Operations.DEFAULT.selectSampleDimension(coverage, bands)*/
			final ParameterValueGroup param = (ParameterValueGroup) bandSelectParams.clone();
			param.parameter("Source").setValue(coverage);
			param.parameter("SampleDimensions").setValue(bands);
			
			bandSelectedCoverage = bandSelectFactory.doOperation(param, hints); 
		} else
			bandSelectedCoverage = coverage;
		return bandSelectedCoverage;
	}
}