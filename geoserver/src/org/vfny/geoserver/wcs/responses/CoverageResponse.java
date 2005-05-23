/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses;
 
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.PropertySourceImpl;
import javax.media.jai.RenderedOp;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.coverage.grid.AbstractGridFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.FactoryFinder;
import org.geotools.referencing.operation.transform.LinearTransform1D;
import org.geotools.resources.GCSUtilities;
import org.geotools.util.NumberRange;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.spatialschema.geometry.Envelope;
import org.opengis.spatialschema.geometry.MismatchedDimensionException;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataFormatConfig;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.requests.CoverageRequest;

import com.vividsolutions.jts.geom.Coordinate;


/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class CoverageResponse implements Response {
	public static final int TRANSPARENT = 0;
	
	public static final int OPAQUE = 1;
	
	/** Standard logging instance for class */
	private static final Logger LOGGER = Logger.getLogger(
	"org.vfny.geoserver.responses");

	
	CoverageResponseDelegate delegate;
	
	/**
	 * This is the request provided to the execute( Request ) method.
	 * 
	 * <p>
	 * We save it so we can access the handle provided by the user for error
	 * reporting during the writeTo( OutputStream ) opperation.
	 * </p>
	 * 
	 * <p>
	 * This value will be <code>null</code> until execute is called.
	 * </p>
	 */
	private CoverageRequest request;
	
	/**
	 * Empty constructor
	 */
	public CoverageResponse() {
		request = null;
	}
	
	/**
	 * DOCUMENT ME!
	 *
	 * @param gs DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContentType(GeoServer gs) {
		return delegate.getContentType(gs);
	}
	
	public String getContentEncoding() {
		return delegate.getContentEncoding();
	}
	
	public String getContentDisposition() {
		return delegate.getContentDisposition();
	}

	/**
	 * Jody here with one pass replacement for writeTo.
	 * 
	 * <p>
	 * This code is a discussion point, when everyone has had there input we
	 * will try and set things up properly.
	 * </p>
	 * 
	 * <p>
	 * I am providing a mirror of the existing desing: - execute gathers the
	 * resultList - sets up the header
	 * </p>
	 *
	 * @param out DOCUMENT ME!
	 *
	 * @throws WcsException DOCUMENT ME!
	 * @throws IOException DOCUMENT ME!
	 * @throws IllegalStateException DOCUMENT ME!
	 */
	public void writeTo(OutputStream out) throws ServiceException, IOException {
		if ((request == null) || (delegate == null)) {
			throw new IllegalStateException(
			"execute has not been called prior to writeTo");
		}
		
		delegate.encode(out);
	}
	
	/**
	 * Executes FeatureRequest.
	 * 
	 * <p>
	 * Willing to execute a FetureRequest, or FeatureRequestWith Lock.
	 * </p>
	 *
	 * @param req DOCUMENT ME!
	 *
	 * @throws WcsException DOCUMENT ME!
	 */
	public void execute(Request req) throws WcsException {
		execute((CoverageRequest) req);
	}
	
	public void execute(CoverageRequest request) throws WcsException {
		LOGGER.finest("execute CoverageRequest response. Called request is: "
				+ request);
		this.request = request;
		
		String outputFormat = request.getOutputFormat();
		
		try {
			delegate = CoverageResponseDelegateFactory.encoderFor(outputFormat);
		} catch (NoSuchElementException ex) {
			throw new WcsException("output format: " + outputFormat + " not "
					+ "supported by geoserver", ex);
		}
		
		GeoServer config = request.getWCS().getGeoServer();
		Data catalog = request.getWCS().getData();
		CoverageInfo meta = null;
		GridCoverage2D coverage = null;
		
		try {
			meta = catalog.getCoverageInfo(request.getCoverage());

			String formatID = meta.getFormatId();
			DataConfig dataConfig = (DataConfig) request.getHttpServletRequest()
						.getSession()
						.getServletContext()
						.getAttribute(DataConfig.CONFIG_KEY);
			DataFormatConfig dfConfig = dataConfig.getDataFormat(formatID);

			String realPath = request.getHttpServletRequest().getRealPath("/");
			URL url = getResource(dfConfig.getUrl(), realPath);

			Format format = dfConfig.getFactory();
			GridCoverageReader reader = ((AbstractGridFormat) format).getReader(url);

			ParameterValueGroup params = format.getReadParameters();
			
			if( params != null ) {
				List list=params.values();
				Iterator it=list.iterator();
				while(it.hasNext())
				{
					ParameterValue param=((ParameterValue)it.next());
					ParameterDescriptor descr=(ParameterDescriptor)param.getDescriptor();
					
					Object value = null;
					String key = descr.getName().toString();
					
					try {
	    				if( key.equalsIgnoreCase("crs") ) {
							if( dfConfig.getParameters().get(key) != null && ((String) dfConfig.getParameters().get(key)).length() > 0 ) {
								CRSFactory crsFactory = FactoryFinder.getCRSFactory(null);
								CoordinateReferenceSystem crs = crsFactory.createFromWKT((String) dfConfig.getParameters().get(key));
								value = crs;
							} else {
								CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
								value = crs;
							}
						} else if( key.equalsIgnoreCase("envelope") ) {
							if( dfConfig.getParameters().get(key) != null && ((String) dfConfig.getParameters().get(key)).length() > 0 ) {
								String tmp = (String) dfConfig.getParameters().get(key);
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
							if( dfConfig.getParameters().get(key) != null && ((String) dfConfig.getParameters().get(key)).length() > 0 ) {
								String tmp = (String) dfConfig.getParameters().get(key);
								String[] strColors = tmp.split(";");
								Vector colors = new Vector();
								for( int i=0; i<strColors.length; i++) {
									if(Color.decode(strColors[i]) != null) {
										colors.add(Color.decode(strColors[i]));
									}
								}
								
								value = colors.toArray(new Color[colors.size()]);
							}
							reader.getFormat().getReadParameters().parameter("values_palette").setValue(value);
						} else {
							Class[] clArray = {String.class};
							Object[] inArray = {dfConfig.getParameters().get(key)};
							value = param.getValue().getClass().getConstructor(clArray).newInstance(inArray);
	    				}
					} catch (Exception e) {
						value = null;
					}
					
					if( value != null )
						params.parameter(key).setValue(value);
				}
			}
			
			coverage = (GridCoverage2D) reader.read(
					params != null ?
							(GeneralParameterValue[]) params.values().toArray(new GeneralParameterValue[params.values().size()])
							: null
			);
			
			getCroppedCoverage(request, outputFormat, meta, coverage);
		} catch (IOException e) {
			throw new WcsException(e, "problem with CoverageResults",
					request.getHandle());
		} catch (NoSuchElementException e) {
			throw new WcsException(e, "problem with CoverageResults",
					request.getHandle());
		} catch (IllegalArgumentException e) {
			throw new WcsException(e, "problem with CoverageResults",
					request.getHandle());
		} catch (SecurityException e) {
			throw new WcsException(e, "problem with CoverageResults",
					request.getHandle());
		}
	}

	/**
	 * @param request
	 * @param outputFormat
	 * @param meta
	 * @param coverage
	 * @throws WcsException
	 * @throws IOException
	 */
	private void getCroppedCoverage(
			CoverageRequest request, 
			String outputFormat, 
			CoverageInfo meta, 
			GridCoverage2D coverage
	) throws WcsException, IOException {
	
		if( request.getEnvelope() != null ) {
			com.vividsolutions.jts.geom.Envelope envelope = new com.vividsolutions.jts.geom.Envelope();
			GeneralEnvelope gEnvelope = (GeneralEnvelope) coverage.getEnvelope();
			envelope.init(
					gEnvelope.getLowerCorner().getOrdinate(0),
					gEnvelope.getUpperCorner().getOrdinate(0),
					gEnvelope.getLowerCorner().getOrdinate(1),
					gEnvelope.getUpperCorner().getOrdinate(1)
			);
			com.vividsolutions.jts.geom.Envelope subEnvelope = request.getEnvelope();
			GeneralEnvelope gSEnvelope = new GeneralEnvelope(new Rectangle2D.Double(
					subEnvelope.getMinX(),
					subEnvelope.getMinY(),
					subEnvelope.getWidth(),
					subEnvelope.getHeight())
			); 
			//getting raw image
			RenderedImage image = coverage.getRenderedImage();
			
			//getting dimensions of the raw image to evaluate the steps
			final int nX = image.getWidth();
			final int nY = image.getHeight();
			final double lo1 = envelope.getMinX();
			final double la1 = envelope.getMinY();
			final double lo2 = envelope.getMaxX();
			final double la2 = envelope.getMaxY();
			
			final double los1 = subEnvelope.getMinX();
			final double las1 = subEnvelope.getMinY();
			final double los2 = subEnvelope.getMaxX();
			final double las2 = subEnvelope.getMaxY();
			
			final double dX = (lo2 - lo1) / nX;
			final double dY = (la2 - la1) / nY;//we have to keep into account axis directions
			//when using the image

			final double lonIndex1 = java.lang.Math.ceil((los1 - lo1) / dX); 
			final double lonIndex2 = java.lang.Math.floor((los2 - lo1) / dX); 
			final double latIndex1 = java.lang.Math.floor((la2 - las2) / dY); 
			final double latIndex2 = java.lang.Math.ceil((la2 - las1) / dY); 
			
			final int cnX = new Double(lonIndex2 - lonIndex1).intValue();
			final int cnY = new Double(latIndex2 - latIndex1).intValue();

			if( !meta.getEnvelope().intersects(request.getEnvelope()) ) {
				/**
				 * In such a case we have a requested envelope that is completely outside
				 * the Coverage envelope.
				 */
				throw new WcsException("Invalid Requested Envelope: " + request.getEnvelope());
			} else if( !meta.getEnvelope().contains(request.getEnvelope()) ) {
				/**
				 * The requested envelope is bigger than the Coverage one or intersects that.
				 * We should perform the following steps:
				 * 
				 * - If the source coverage derives from an GRAYSCALE or RGB image without TRANSPARENCY
				 *   (i.e. the WritableRaster has an odd number of bands and a DataType like BYTE, USHORT and INT):
				 *   1. Add TRANSPARENCY to the source image, i.e. we have to add a new MAX-DATATYPE-VALUE constant 
				 *      band to the image. "MAX-DATATYPE-VALUE" means opaque (we want the original opaque image to
				 *      remain opaque). 
				 *   2. Create a 0-value constant image wich has dimesions equals to the union of the two envelopes, 
				 *      and the same number of bands of the source image+1.
				 *   3. Positioning through an Affine-Transform the original image to the background.
				 *   4. Overlaying the transparent background and the source image+transparency.
				 *   5. Create a new GridCoverage for the delegate.
				 * 
				 *   NOTE: if the original image has transparency yet, we simply don't need to add a new band to the
				 *         source image.
				 * 
				 * - if the source coverage derives from a raster
				 *   (i.e. the WritableRaster has DataType of type FLOAT, DOUBLE and SHORT):
				 *   1. Overlaying with a background filled with "DataType.NaN".
				 **/
				
				// The source coverage number of Bands
				final int numBands = image.getSampleModel().getNumBands();
				ParameterBlock pb = new ParameterBlock();
				Number[] bandValues = null;
				
				// Checking if the source coverage derives from an image.
				if( image.getSampleModel().getDataType() == DataBuffer.TYPE_BYTE ||
					image.getSampleModel().getDataType() == DataBuffer.TYPE_USHORT ||
					image.getSampleModel().getDataType() == DataBuffer.TYPE_INT	
				) {
					// Checking if we need to add transparency-band to the source image or not.
					if(numBands%2 != 0) {
						image = addTransparency(image, OPAQUE);
					}
					
					// Create the background Image.
					switch(image.getSampleModel().getDataType()){
						case DataBuffer.TYPE_BYTE:
							bandValues = new Byte[( numBands%2!=0 ? numBands+1 : numBands )];
							// Fill the array with a constant value.  
							for(int band=0;band<bandValues.length;band++)  
								bandValues[band] = new Byte((byte)(0));
						break;
						case DataBuffer.TYPE_USHORT:
							bandValues = new Short[( numBands%2!=0 ? numBands+1 : numBands )];
							// Fill the array with a constant value.  
							for(int band=0;band<bandValues.length;band++)  
								bandValues[band] = new Short((short)(0));
						break;
						case DataBuffer.TYPE_INT:
							bandValues = new Integer[( numBands%2!=0 ? numBands+1 : numBands )];
							// Fill the array with a constant value.  
							for(int band=0;band<bandValues.length;band++)  
								bandValues[band] = new Integer((int)(0));
						break;						
					}
					  
			        pb.removeParameters();
			        pb.removeSources();
					pb.add(new Float( subEnvelope.getWidth() / dX)).add(new Float( subEnvelope.getHeight() / dY));
			        pb.add(bandValues);
					PlanarImage imgBackground = JAI.create("constant", pb, null);
			
					/**translating the old one*/
			        pb.removeParameters();
			        pb.removeSources();
					pb.addSource(image);
					pb.add((float)((lo1-los1)/dX));
					pb.add((float)((las2-la2)/dY));
					RenderedImage renderableSource=JAI.create("translate",pb);
					
					/**overlaying images*/
			        pb.removeParameters();
			        pb.removeSources();
			        pb.addSource(imgBackground).addSource(renderableSource);
					RenderedImage destOverlayed = JAI.create("overlay", pb, null);

					//creating a copy of the given grid coverage2D
					GridCoverage2D subCoverage = (GridCoverage2D) createCoverage(destOverlayed, coverage.getCoordinateReferenceSystem(), gSEnvelope, coverage.getName().toString()); 
					
					delegate.prepare(outputFormat, subCoverage);
				}
				else {
			        // Create the background Image.
			        if( image.getSampleModel().getDataType() == DataBuffer.TYPE_FLOAT ) {
				        bandValues = new Float[numBands];  
				        // Fill the array with a constant value.  
				        for(int band=0;band<bandValues.length;band++)  
				        	bandValues[band] = new Float(Float.NaN);
			        } else if( image.getSampleModel().getDataType() == DataBuffer.TYPE_DOUBLE ) {
				        bandValues = new Double[numBands];  
				        // Fill the array with a constant value.  
				        for(int band=0;band<bandValues.length;band++)  
				        	bandValues[band] = new Double(Double.NaN);
			        } else if( image.getSampleModel().getDataType() == DataBuffer.TYPE_SHORT ) {
				        bandValues = new Short[numBands];  
				        // Fill the array with a constant value.  
				        for(int band=0;band<bandValues.length;band++)  
				        	//TODO this should be parameterized!!!!
				        	bandValues[band] = new Short((short)-9999);//quick hack for gtopo30 format!!!!!
			        }			 
				
			        pb.removeParameters();
			        pb.removeSources();
					pb.add(new Float( subEnvelope.getWidth() / dX)).add(new Float( subEnvelope.getHeight() / dY));
			        pb.add(bandValues);
					PlanarImage imgBackground = JAI.create("constant", pb, null);

					/**translating the old one*/
			        pb.removeParameters();
			        pb.removeSources();
					pb.addSource(image);
					pb.add((float)((lo1-los1)/dX));
					pb.add((float)((las2-la2)/dY));
					RenderedImage renderableSource=JAI.create("translate",pb);
					
					/**overlaying images*/
			        pb.removeParameters();
			        pb.removeSources();
			        pb.addSource(imgBackground).addSource(renderableSource);
					RenderedImage destOverlayed=JAI.create("overlay", pb, null);

					//creating a copy of the given grid coverage2D
					GridCoverage2D subCoverage = new GridCoverage2D(
							meta.getName(),
							destOverlayed,
							coverage.getCoordinateReferenceSystem(),
							gSEnvelope,
							coverage.getSampleDimensions(),
							null,
							((PropertySourceImpl)coverage).getProperties());
					
					delegate.prepare(outputFormat, subCoverage);
				}
			} else if( meta.getEnvelope().contains(request.getEnvelope()) ) {
				ParameterBlock pbCrop = new ParameterBlock();
				pbCrop.addSource((PlanarImage) image);
				pbCrop.add(new Double(lonIndex1).floatValue());//x origin
				pbCrop.add(new Double(latIndex1).floatValue());//y origin
				pbCrop.add(new Float(cnX).floatValue());//width
				pbCrop.add(new Float(cnY).floatValue());//height
				RenderedOp result = JAI.create("crop", pbCrop);
				
				pbCrop.removeParameters();
				pbCrop.removeSources();
				pbCrop.addSource((PlanarImage) result);
				pbCrop.add(new Double(-lonIndex1).floatValue());//x origin
				pbCrop.add(new Double(-latIndex1).floatValue());//y origin
				result=JAI.create("translate",pbCrop);
				
				//creating a copy of the given grid coverage2D
				GridCoverage2D subCoverage =null;
				final GridSampleDimension[] sampleDimensions=coverage.getSampleDimensions();
				/**
				 * This method checks if this coverage has a non geophysic
				 * view associated. In such a case it creates a new coverage based on this non
				 * geophysic view, otherwise it keeps the original view as it is provided.
				 */
				if(GCSUtilities.hasTransform(sampleDimensions))
					subCoverage= new GridCoverage2D(
						meta.getName(),
						result,
						coverage.getCoordinateReferenceSystem(),
						gSEnvelope,
						coverage.getSampleDimensions(),
						null,
						((PropertySourceImpl)coverage).getProperties());
				else
					subCoverage = (GridCoverage2D) createCoverage(
							result, 
							coverage.getCoordinateReferenceSystem(), 
							gSEnvelope, 
							coverage.getName().toString()
					); 
					
				delegate.prepare(outputFormat, subCoverage);
			}
		} else {
			RenderedImage image = coverage.geophysics(true).getRenderedImage();
			//creating a copy of the given grid coverage2D
			GridCoverage2D subCoverage = new GridCoverage2D(
					meta.getName(),
					image,
					coverage.getCoordinateReferenceSystem(),
					coverage.getEnvelope(),
					coverage.getSampleDimensions(),
					null,
					((PropertySourceImpl)coverage).getProperties());
			
			delegate.prepare(outputFormat, subCoverage);
		}
	}
	
	private static RenderedOp addTransparency(RenderedImage src, int transparency){
		Number[] bandValues = null;
		PlanarImage alphaPlane = null;

		/**
		 * We are trying to come up with a mask that should spot
		 * 0 or max as the transparency level
		 */
		switch(src.getSampleModel().getTransferType()){
		case DataBuffer.TYPE_BYTE:
			switch (transparency) {
			case TRANSPARENT :
				//fill with 0
				bandValues = new Byte[1];
				bandValues[0] = new Byte((byte)(0));
				break;
			case OPAQUE :
				//fill with DataType.MAX
				bandValues = new Byte[1];
				bandValues[0] = new Byte((byte)(255));
				break;
			}
		break;
		case DataBuffer.TYPE_USHORT:
			switch (transparency) {
			case TRANSPARENT :
				//fill with 0
				bandValues = new Short[1];
				bandValues[0] = new Short((short)(0));
				break;
			case OPAQUE :
				//fill with DataType.MAX
				bandValues = new Short[1];
				bandValues[0] = new Short((short)65535);
				break;
			}
		break;
		case DataBuffer.TYPE_INT:
			switch (transparency) {
			case TRANSPARENT :
				//fill with 0
				bandValues = new Integer[1];
				bandValues[0] = new Integer((byte)(0));
				break;
			case OPAQUE :
				//fill with DataType.MAX
				bandValues = new Integer[1];
				bandValues[0] = new Integer(Integer.MAX_VALUE);
				break;
			}
		break;
		default:
			return null;
		}

		ParameterBlock pb = new ParameterBlock();
		pb.add(new Float(src.getWidth())).add(new Float(src.getHeight()));
        pb.add(bandValues);
		alphaPlane = JAI.create("Constant", pb, null);

		//merging bands basing the decision on the transparency level
		pb.removeParameters();
		pb.removeSources();
		pb.addSource(src);
		pb.addSource(alphaPlane);
		
		RenderedOp dest= JAI.create("BandMerge",pb,null);

		return dest;
	}

	/**
	 * Release locks if we are into that sort of thing.
	 *
	 * @see org.vfny.geoserver.responses.Response#abort()
	 */
	public void abort(Service gs) {
		if (request == null) {
			return; // request was not attempted
		}
		
		Data catalog = gs.getData();
	}
	
	private URL getResource(String path, String baseDir) throws MalformedURLException{
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
	
    /**Creating a coverage from an Image.
     * @param image
     * @param crs
     * @param envelope
     * @param coverageName
     * @return
     * @throws MismatchedDimensionException
     * @throws IOException
     */
    private GridCoverage createCoverage(
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
}
