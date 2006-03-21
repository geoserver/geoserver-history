/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.util;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GeneralGridRange;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.Operations;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.FactoryFinder;
import org.geotools.referencing.operation.transform.LinearTransform1D;
import org.geotools.resources.CRSUtilities;
import org.geotools.util.NumberRange;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.spatialschema.geometry.Envelope;
import org.opengis.spatialschema.geometry.MismatchedDimensionException;
import org.opengis.util.InternationalString;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.requests.CoverageRequest;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 */
public class CoverageUtils {
	public static final int TRANSPARENT = 0;
	
	public static final int OPAQUE = 1;
	
	
	public static URL getResource(String path, String baseDir) throws MalformedURLException{
		URL url = null;
		if (path.startsWith("file:data/")) {
			path = path.substring(5); // remove 'file:' prefix
			
			File file = new File(baseDir, path);
			url = file.toURL();
		} else {
			url = new URL(path);
		}
		
		return url;
	}
	
	/**
	 * @param paramValues
	 * @param key
	 * @param param
	 * @return
	 */
	public static Object getCvParamValue(final String key, ParameterValue param, final List paramValues, final int index) {
		Object value = null;
		try {
			if( key.equalsIgnoreCase("crs") ) {
				if( getParamValue(paramValues, index) != null && ((String) getParamValue(paramValues, index)).length() > 0 ) {
					//CRSFactory crsFactory = FactoryFinder.getCRSFactory(new Hints(Hints.CRS_AUTHORITY_FACTORY,EPSGCRSAuthorityFactory.class));
					CRSFactory crsFactory = FactoryFinder.getCRSFactory(new Hints(Hints.CRS_AUTHORITY_FACTORY,CRSAuthorityFactory.class));
					CoordinateReferenceSystem crs = crsFactory.createFromWKT((String) getParamValue(paramValues, index));
					value = crs;
				} else {
					//CRSAuthorityFactory crsFactory=FactoryFinder.getCRSAuthorityFactory("EPSG",new Hints(Hints.CRS_AUTHORITY_FACTORY,EPSGCRSAuthorityFactory.class));
					CRSAuthorityFactory crsFactory=FactoryFinder.getCRSAuthorityFactory("EPSG", new Hints(Hints.CRS_AUTHORITY_FACTORY, CRSAuthorityFactory.class));
					CoordinateReferenceSystem crs=(CoordinateReferenceSystem) crsFactory.createCoordinateReferenceSystem("EPSG:4326");
					value = crs;
				}
			} else if( key.equalsIgnoreCase("envelope") ) {
				if( getParamValue(paramValues, index) != null && ((String) getParamValue(paramValues, index)).length() > 0 ) {
					String tmp = (String) getParamValue(paramValues, index);
					if( tmp.indexOf("[") > 0 && tmp.indexOf("]") > tmp.indexOf("[") ) {
						tmp = tmp.substring(tmp.indexOf("[") + 1, tmp.indexOf("]")).trim();
						tmp = tmp.replaceAll(",","");
						String[] strCoords = tmp.split(" ");
						double[] coords = new double[strCoords.length];
						if( strCoords.length == 4 ) {
							for( int iT=0; iT<4; iT++) {
								coords[iT] = Double.parseDouble(strCoords[iT].trim());
							}
							
							value = (org.opengis.spatialschema.geometry.Envelope) 
							new GeneralEnvelope(
									new double[] {coords[0], coords[1]},
									new double[] {coords[2], coords[3]}
							);
						}
					}
				}
			} else if( key.equalsIgnoreCase("values_palette") ) {
				if( getParamValue(paramValues, index) != null && ((String) getParamValue(paramValues, index)).length() > 0 ) {
					String tmp = (String) getParamValue(paramValues, index);
					String[] strColors = tmp.split(";");
					Vector colors = new Vector();
					for( int col=0; col<strColors.length; col++) {
						if(Color.decode(strColors[col]) != null) {
							colors.add(Color.decode(strColors[col]));
						}
					}
					
					value = colors.toArray(new Color[colors.size()]);
				} else {
					value = "#000000;#3C3C3C;#FFFFFF";
				}
			} else {
				Class[] clArray = {getParamValue(paramValues, index).getClass()};
				Object[] inArray = {getParamValue(paramValues, index)};
				value = param.getValue().getClass().getConstructor(clArray).newInstance(inArray);
			}
			
			// Intentionally generic exception catched
		} catch (Exception e) {
			value = null;
			//            errors.add("paramValue[" + i + "]",
			//                    new ActionError("error.dataFormatEditor.param.parse", key,
			//                    		getParamValue(i).getClass(), e));
		}
		
		return value;
	}
	
	private static String getParamValue(final List paramValues, final int index) {
		return (String) paramValues.get(index);
	}
	
	/**
	 * @param params
	 * @param key
	 * @param param
	 * @return
	 */
	public static Object getCvParamValue(final String key, ParameterValue param, final Map params) {
		Object value = null;
		try {
			if( key.equalsIgnoreCase("crs") ) {
				if( params.get(key) != null && ((String) params.get(key)).length() > 0 ) {
					//CRSFactory crsFactory = FactoryFinder.getCRSFactory(new Hints(Hints.CRS_AUTHORITY_FACTORY,EPSGCRSAuthorityFactory.class));
					CRSFactory crsFactory = FactoryFinder.getCRSFactory(new Hints(Hints.CRS_AUTHORITY_FACTORY,CRSAuthorityFactory.class));
					CoordinateReferenceSystem crs = crsFactory.createFromWKT((String) params.get(key));
					value = crs;
				} else {
					//CRSAuthorityFactory crsFactory=FactoryFinder.getCRSAuthorityFactory("EPSG",new Hints(Hints.CRS_AUTHORITY_FACTORY,EPSGCRSAuthorityFactory.class));
					CRSAuthorityFactory crsFactory=FactoryFinder.getCRSAuthorityFactory("EPSG", new Hints(Hints.CRS_AUTHORITY_FACTORY, CRSAuthorityFactory.class));
					CoordinateReferenceSystem crs=(CoordinateReferenceSystem) crsFactory.createCoordinateReferenceSystem("EPSG:4326");
					value = crs;
				}
			} else if( key.equalsIgnoreCase("envelope") ) {
				if( params.get(key) != null && ((String) params.get(key)).length() > 0 ) {
					String tmp = (String) params.get(key);
					if( tmp.indexOf("[") > 0 && tmp.indexOf("]") > tmp.indexOf("[") ) {
						tmp = tmp.substring(tmp.indexOf("[") + 1, tmp.indexOf("]")).trim();
						tmp = tmp.replaceAll(",","");
						String[] strCoords = tmp.split(" ");
						double[] coords = new double[strCoords.length];
						if( strCoords.length == 4 ) {
							for( int iT=0; iT<4; iT++) {
								coords[iT] = Double.parseDouble(strCoords[iT].trim());
							}
							
							value = (org.opengis.spatialschema.geometry.Envelope) 
							new GeneralEnvelope(
									new double[] {coords[0], coords[1]},
									new double[] {coords[2], coords[3]}
							);
						}
					}
				}
			} else if( key.equalsIgnoreCase("values_palette") ) {
				if( params.get(key) != null && ((String) params.get(key)).length() > 0 ) {
					String tmp = (String) params.get(key);
					String[] strColors = tmp.split(";");
					Vector colors = new Vector();
					final int colLength = strColors.length;
					Color tmpColor = null;
					for( int col=0; col<colLength; col++) {
						tmpColor = Color.decode(strColors[col]);
						if(tmpColor != null) {
							colors.add(tmpColor);
						}
					}
					
					value = colors.toArray(new Color[colors.size()]);
					//				} else {
					//					value = "#000000;#3C3C3C;#FFFFFF";
				}
			} else {
				Class[] clArray = {String.class};
				Object[] inArray = {params.get(key)};
				value = param.getValue().getClass().getConstructor(clArray).newInstance(inArray);
			}
		} catch (Exception e) {
			value = param.getValue();
		}
		
		return value;
	}
	
	/**Creating a coverage from an Image.
	 * @param image
	 * @param crs
	 * @param envelope
	 * @param coverageName
	 * @return
	 * @throws MismatchedDimensionException
	 * @throws IOException
	 */
	public static GridCoverage createCoverage(
			RenderedImage image,
			CoordinateReferenceSystem crs, 
			Envelope envelope, 
			String coverageName
	) throws MismatchedDimensionException, IOException {
		//building up a coverage
		GridCoverage coverage = null;
		//deciding the number range
		NumberRange geophysicRange=null;
		switch(image.getSampleModel().getTransferType()){
		case DataBuffer.TYPE_BYTE:
			geophysicRange=new NumberRange(0, 255);
		break;
		case DataBuffer.TYPE_USHORT:
			geophysicRange=new NumberRange(0, 65535);
		break;
		case DataBuffer.TYPE_INT:
			geophysicRange=new NumberRange(-Integer.MAX_VALUE,Integer.MAX_VALUE);
		break;	
		default:
			throw new IOException("Data buffer type not supported! Use byte, ushort or int");
		}
		try {
			
			//convenieience category in order to 
			Category  values = new Category("values",new Color[]{Color.BLACK},geophysicRange,LinearTransform1D.IDENTITY );
			
			//creating bands
			GridSampleDimension bands[]=new GridSampleDimension[image.getSampleModel().getNumBands()];
			for(int i=0;i<image.getSampleModel().getNumBands();i++)
				bands[i]=new GridSampleDimension(new Category[] {values}, null).geophysics(true);
			
			//creating coverage
			coverage = new GridCoverage2D(coverageName, image, crs, envelope,bands,null,null);
		} catch (NoSuchElementException e1) {
			throw new IOException("Error when creating the coverage in world image"+e1.getMessage());
		}
		
		return coverage;
	}
	
	/**
	 * @param request
	 * @param outputFormat
	 * @param meta
	 * @param coverage
	 * @return
	 * @throws WcsException
	 * @throws IOException
	 * @throws FactoryException
	 * @throws TransformException
	 * @throws IndexOutOfBoundsException
	 */
	public static GridCoverage2D getCroppedCoverage(
			CoverageRequest request, 
			CoverageInfo meta, 
			GridCoverage coverage
	) throws WcsException, IOException, IndexOutOfBoundsException, FactoryException, TransformException {
		final CRSAuthorityFactory crsFactory = FactoryFinder.getCRSAuthorityFactory("EPSG", new Hints(Hints.CRS_AUTHORITY_FACTORY, CRSAuthorityFactory.class));
		// This is the final Response CRS
		final String responseCRS = request.getResponseCRS();
		//  - first check if the responseCRS is present on the Coverage ResponseCRSs list
		if (!meta.getResponseCRSs().contains(responseCRS)) {
			throw new WcsException("This Coverage does not support the Response CRS requested.");
		}
		//  - then create the Coordinate Reference System
		final CoordinateReferenceSystem targetCRS = crsFactory.createCoordinateReferenceSystem(responseCRS);
		// This is the CRS of the requested Envelope
		final String requestCRS = request.getCRS();
		//  - first check if the requestCRS is present on the Coverage RequestCRSs list
		if (!meta.getResponseCRSs().contains(requestCRS)) {
			throw new WcsException("This Coverage does not support the CRS requested.");
		}
		//  - then create the Coordinate Reference System
		final CoordinateReferenceSystem sourceCRS = crsFactory.createCoordinateReferenceSystem(requestCRS);
		// This is the CRS of the Coverage Envelope
		final CoordinateReferenceSystem cvCRS = ((GeneralEnvelope) coverage.getEnvelope()).getCoordinateReferenceSystem();
		final MathTransform GCCRSTodeviceCRSTransformdeviceCRSToGCCRSTransform = CRS.transform(cvCRS, sourceCRS, true);
		final MathTransform deviceCRSToGCCRSTransform = GCCRSTodeviceCRSTransformdeviceCRSToGCCRSTransform.inverse();
		
		com.vividsolutions.jts.geom.Envelope envelope = request.getEnvelope();
		GeneralEnvelope destinationEnvelope;
		final boolean lonFirst = sourceCRS.getCoordinateSystem()
				.getAxis(0).getDirection().absolute()
				.equals(AxisDirection.EAST);
		// the envelope we are provided with is lon,lat always
		if (!lonFirst)
			destinationEnvelope = new GeneralEnvelope(new double[] {
					envelope.getMinY(), envelope.getMinX() }, new double[] {
					envelope.getMaxY(), envelope.getMaxX() });
		else
			destinationEnvelope = new GeneralEnvelope(new double[] {
					envelope.getMinX(), envelope.getMinY() }, new double[] {
					envelope.getMaxX(), envelope.getMaxY() });
		destinationEnvelope.setCoordinateReferenceSystem(sourceCRS);

		// this is the destination envelope in the coverage crs
		final GeneralEnvelope destinationEnvelopeInSourceCRS = 
			(!deviceCRSToGCCRSTransform.isIdentity()) ? 
					CRSUtilities.transform(deviceCRSToGCCRSTransform, destinationEnvelope)
					: 
					new GeneralEnvelope(destinationEnvelope);
		destinationEnvelopeInSourceCRS.setCoordinateReferenceSystem(cvCRS);

		// ///////////////////////////////////////////////////////////////////
		//
		// BAND SELECT
		//
		//
		// ///////////////////////////////////////////////////////////////////
		final Set params = request.getParameters().keySet();
		final int numDimensions = coverage.getNumSampleDimensions();
		final Map dims = new HashMap();
		final ArrayList selectedBands = new ArrayList();
		
		for (int d=0; d<numDimensions; d++) {
			dims.put(coverage.getSampleDimension(d).getDescription().toString(Locale.getDefault()).toUpperCase(), new Integer(d));
		}
		
		if (!params.isEmpty()) {
			for (Iterator p=params.iterator(); p.hasNext(); ) {
				final String param = (String) p.next();
				if (dims.containsKey(param)) {
					selectedBands.add(dims.get(param));
				}
			}
		}

		final int length = selectedBands.size();
		final int[] bands = new int[length];
		for (int b=0; b<length; b++) {
			bands[b] = ((Integer)selectedBands.get(b)).intValue();
		}
		
		Coverage bandSelectedCoverage;
		if (bands != null && bands.length > 0)
			bandSelectedCoverage = Operations.DEFAULT.selectSampleDimension(coverage, bands);
		else
			bandSelectedCoverage = coverage;

		// ///////////////////////////////////////////////////////////////////
		//
		// CROP
		//
		//
		// ///////////////////////////////////////////////////////////////////
		final GridCoverage2D croppedGridCoverage;
		final GeneralEnvelope oldEnvelope = (GeneralEnvelope) coverage.getEnvelope();
		// intersect the envelopes
		final GeneralEnvelope intersectionEnvelope = new GeneralEnvelope(
				destinationEnvelopeInSourceCRS);
		intersectionEnvelope.setCoordinateReferenceSystem(cvCRS);
		intersectionEnvelope.intersect((GeneralEnvelope) oldEnvelope);
		// dow we have something to show?
		if (intersectionEnvelope.isEmpty())
			throw new WcsException("The Intersection is null. Check the requested BBOX!");
		if (!intersectionEnvelope.equals((GeneralEnvelope) oldEnvelope)) {
			// get the cropped grid geometry
			// final GridGeometry2D cropGridGeometry = getCroppedGridGeometry(
			// intersectionEnvelope, gridCoverage);
			croppedGridCoverage = (GridCoverage2D) Operations.DEFAULT.crop(bandSelectedCoverage, intersectionEnvelope);
		} else
			croppedGridCoverage = (GridCoverage2D) bandSelectedCoverage;

		// prefetch to be faster afterwards.
		// This step is important since at this stage we might be loading tiles
		// from disk
		croppedGridCoverage.prefetch(intersectionEnvelope.toRectangle2D());

		// ///////////////////////////////////////////////////////////////////
		//
		// SCALE to the needed resolution
		// Let me now scale down to the EXACT needed resolution. This step does
		// not prevent from having loaded an overview of the original image
		// based on the requested scale.
		//
		// ///////////////////////////////////////////////////////////////////
		GridCoverage2D subCoverage = croppedGridCoverage;
		
		GridGeometry2D scaledGridGeometry = (GridGeometry2D) coverage.getGridGeometry();
		if(request.getGridLow()!=null && request.getGridHigh()!=null) {
			final int[] lowers = new int[] {request.getGridLow()[0].intValue(), request.getGridLow()[1].intValue()};
			final int[] highers = new int[] {request.getGridHigh()[0].intValue(), request.getGridHigh()[1].intValue()};
			//new grid range
			final GeneralGridRange newGridrange = new GeneralGridRange(lowers, highers);
			scaledGridGeometry = new GridGeometry2D(newGridrange, coverage.getEnvelope());
			final GridCoverage2D scaledGridCoverage = (GridCoverage2D) Operations.DEFAULT
			.resample(croppedGridCoverage, cvCRS, scaledGridGeometry,
					Interpolation
							.getInstance(Interpolation.INTERP_NEAREST));
			
			subCoverage = scaledGridCoverage;
		}
		
		// ///////////////////////////////////////////////////////////////////
		//
		// REPROJECT
		//
		//
		// ///////////////////////////////////////////////////////////////////
		if (!sourceCRS.equals(targetCRS)) {
			final GridCoverage2D reprojectedGridCoverage;
			reprojectedGridCoverage = (GridCoverage2D) Operations.DEFAULT
			.resample(subCoverage, targetCRS, null,
					Interpolation.getInstance(Interpolation.INTERP_NEAREST));
			
			subCoverage = reprojectedGridCoverage;
		}

		final String interp_requested = request.getInterpolation();
		if (interp_requested != null) {
			int interp_type = -1;
			
			if (interp_requested.equalsIgnoreCase("nearest_neighbor"))
				interp_type = Interpolation.INTERP_NEAREST;
			else if (interp_requested.equalsIgnoreCase("bilinear"))
				interp_type = Interpolation.INTERP_BILINEAR;
			else if (interp_requested.equalsIgnoreCase("bicubic"))
				interp_type = Interpolation.INTERP_BICUBIC;
			else if (interp_requested.equalsIgnoreCase("bicubic_2"))
				interp_type = Interpolation.INTERP_BICUBIC_2;
			else
				throw new WcsException("Unrecognized interpolation type. Allowed values are: nearest_neighbor, bilinear, bicubic, bicubic_2");

			subCoverage = (GridCoverage2D) Operations.DEFAULT
				.interpolate(subCoverage, Interpolation.getInstance(interp_type));
		}
		
		return subCoverage;
	}
	
	/**
	 * Reformat the index color model to a component color model preserving
	 * transparency.
	 * Code from jai-interests archive.
	 * @param surrogateImage
	 *
	 * @return
	 *
	 * @throws IllegalArgumentException DOCUMENT ME!
	 */
	private static PlanarImage reformatColorModel2ComponentColorModel(
			PlanarImage surrogateImage) throws IllegalArgumentException {
		// Format the image to be expanded from IndexColorModel to
		// ComponentColorModel
		ParameterBlock pbFormat = new ParameterBlock();
		pbFormat.addSource(surrogateImage);
		pbFormat.add(surrogateImage.getSampleModel().getTransferType());
		
		ImageLayout layout = new ImageLayout();
		ColorModel cm1 = null;
		final int numBits;
		
		switch (surrogateImage.getSampleModel().getTransferType()) {
		case DataBuffer.TYPE_BYTE:
			numBits = 8;
		break;
		
		case DataBuffer.TYPE_USHORT:
			numBits = 16;
		break;
		case DataBuffer.TYPE_SHORT:
			numBits = 16;
		break;
		
		case DataBuffer.TYPE_INT:
			numBits = 32;
		break;			
		case DataBuffer.TYPE_FLOAT:
			numBits = 32;
		break;
		case DataBuffer.TYPE_DOUBLE:
			numBits=64;
		break;
		
		default:
			throw new IllegalArgumentException(
			"Unsupported data type for an index color model!");
		}
		
		//do we need alpha?
		final int transparency=surrogateImage.getColorModel().getTransparency();
		final int transpPixel=((IndexColorModel)surrogateImage.getColorModel()).getTransparentPixel();
		if (transparency!=Transparency.OPAQUE) {
			cm1 = new ComponentColorModel(ColorSpace.getInstance(
					ColorSpace.CS_sRGB),
					new int[] { numBits, numBits, numBits, numBits }, true,
					false,transparency,
					surrogateImage.getSampleModel().getTransferType());
		} else {
			cm1 = new ComponentColorModel(ColorSpace.getInstance(
					ColorSpace.CS_sRGB),
					new int[] { numBits, numBits, numBits }, false, false,
					transparency,
					surrogateImage.getSampleModel().getTransferType());
		}
		
		layout.setColorModel(cm1);
		layout.setSampleModel(cm1.createCompatibleSampleModel(
				surrogateImage.getWidth(), surrogateImage.getHeight()));
		
		RenderingHints hint = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
		RenderedOp dst = JAI.create("format", pbFormat, hint);
		surrogateImage = dst.createSnapshot();
		pbFormat.removeParameters();
		pbFormat.removeSources();
		dst.dispose();
		
		return surrogateImage;
	}	
	/**
	 * This method allows me to go from DirectColorModel to ComponentColorModel
	 * which seems to be well acepted from PNGEncoder and TIFFEncoder.
	 * 
	 * @param surrogateImage
	 * @return
	 */
	private static PlanarImage direct2ComponentColorModel(PlanarImage surrogateImage) {
		ParameterBlockJAI pb = new ParameterBlockJAI("ColorConvert");
		pb.addSource(surrogateImage);
		int numBits=8;
		if(DataBuffer.TYPE_INT==surrogateImage.getSampleModel().getTransferType())
			numBits=32;
		else
			if(DataBuffer.TYPE_USHORT==surrogateImage.getSampleModel().getTransferType()||
					DataBuffer.TYPE_SHORT==surrogateImage.getSampleModel().getTransferType())
				numBits=16;
			else
				if(DataBuffer.TYPE_FLOAT==surrogateImage.getSampleModel().getTransferType())
					numBits=32;
				else
					if(DataBuffer.TYPE_DOUBLE==surrogateImage.getSampleModel().getTransferType())
						numBits=64;
		ComponentColorModel colorModel = new ComponentColorModel(surrogateImage.getColorModel().getColorSpace(),
				new int[] { 
				numBits,
				numBits,
				numBits,
				numBits 
		},
		false,
		surrogateImage.getColorModel().hasAlpha(),
		surrogateImage.getColorModel().getTransparency(),
		surrogateImage.getSampleModel().getTransferType());
		pb.setParameter("colormodel", colorModel);
		ImageLayout layout = new ImageLayout();
		layout.setColorModel(colorModel);
		layout.setSampleModel(colorModel.createCompatibleSampleModel(surrogateImage.getWidth(), surrogateImage.getHeight()));
		RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
		surrogateImage = JAI.create("ColorConvert", pb, hints).createInstance();
		pb.removeParameters();
		pb.removeSources();
		
		return surrogateImage;
	}
	
	private static RenderedOp addTransparency(RenderedImage src, int transparency){
		Number[] bandValues = null;
		PlanarImage alphaPlane = null;
		final int transferType = src.getSampleModel().getTransferType();
		/**
		 * We are trying to come up with a mask that should spot
		 * 0 or max as the transparency level
		 */
		switch(transferType){
		case DataBuffer.TYPE_BYTE:
			//fill with 0
			bandValues = new Byte[1];
		bandValues[0] = new Byte((byte)(0));
		
		break;
		case DataBuffer.TYPE_USHORT:
			
			//fill with 0
			bandValues = new Short[1];
		bandValues[0] = new Short((short)(0));
		break;
		case DataBuffer.TYPE_INT:
			//fill with 0
			bandValues = new Integer[1];
		bandValues[0] = new Integer((byte)(0));
		break;
		default:
			return null;
		}
		
		ParameterBlock pb = new ParameterBlock();
		pb.add(new Float(src.getWidth())).add(new Float(src.getHeight()));
		pb.add(bandValues);
		alphaPlane = JAI.create("Constant", pb, null);
		
		
		/**
		 * In case we are asking for adding the alpha channel
		 * we need to ensure that we add transparency at the maximum level.
		 * 
		 * NOTE: handling transparenxy with Transfert Type USHORT is tricky because
		 * if you try to create a constant image directly values 65535 it comes out
		 * as a SHORT valued image instead of a USHORT because of Java's limitation 
		 * with unsigned values.
		 * The trick i:
		 * 1>Create a constant image type USHORT
		 * 2>Rescale it using scale 0 or 1 or whatever you like (this value will be multiplied by 0 so...)
		 * and offset 65535. In such a case everything works fine!!!
		 */
		if(transparency==OPAQUE){
			
			/**
			 * RESCSALE
			 */
			pb.removeSources();
			pb.removeParameters();
			
			//get the transfer type and set the levels for the dynamic
			double dynamicAcme = 0.0;
			
			switch (transferType) {
			case DataBuffer.TYPE_BYTE:
				dynamicAcme = 255;
			break;
			case DataBuffer.TYPE_USHORT:
				dynamicAcme = 65535;
			break;
			case DataBuffer.TYPE_INT:
				dynamicAcme = Integer.MAX_VALUE;
			break;
			}
			
			pb.addSource(alphaPlane);
			
			//rescaling each band
			double[] scale = new double[1];
			double[] offset = new double[1];
			scale[0]=1;
			offset[0]=dynamicAcme;
			
			pb.add(scale);
			pb.add(offset);
			alphaPlane = JAI.create("rescale", pb);
			
			
		}
		
		//merging bands basing the decision on the transparency level
		pb.removeParameters();
		pb.removeSources();
		pb.addSource(src);
		pb.addSource(alphaPlane);
		
		RenderedOp dest= JAI.create("BandMerge",pb,null);
		
		return dest;
	}
}
