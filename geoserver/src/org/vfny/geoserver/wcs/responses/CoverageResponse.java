/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses;
 
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.PropertySourceImpl;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.CompositeDescriptor;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.coverage.grid.AbstractGridFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.FactoryFinder;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
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


/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class CoverageResponse implements Response {
	public static final int TRANS_BLACK = 0;
	
	public static final int TRANS_WHITE = 1;
	
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
								CRSFactory crsFactory = FactoryFinder.getCRSFactory();
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
	private void getCroppedCoverage(CoverageRequest request, String outputFormat, CoverageInfo meta, GridCoverage2D coverage) throws WcsException, IOException {
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
				throw new WcsException("Invalid Requested Envelope: " + request.getEnvelope());
			} else if( !meta.getEnvelope().contains(request.getEnvelope()) ) {
				/**creating a a constant image for the overlay or composition*/
				final int numBands = image.getSampleModel().getNumBands();
				ParameterBlock pb = new ParameterBlock();
				Number[] bandValues = null;
				if( image.getSampleModel().getDataType() == DataBuffer.TYPE_BYTE ) {
					
					// Create the background Image.
					
					bandValues = new Byte[(numBands%2 != 0 ? numBands : numBands - 1)];  
					// Fill the array with a constant value.  
					for(int band=0;band<bandValues.length;band++)  
						bandValues[band] = new Byte((byte)(numBands%2 != 0 ? 0xFF : 0x00));
			        pb = new ParameterBlock();
					pb.add(new Float( subEnvelope.getWidth() / dX)).add(new Float( subEnvelope.getHeight() / dY));
			        pb.add(bandValues);
					PlanarImage imgBackground = 
						(numBands%2 != 0 ? JAI.create("constant", pb, null) : addTransparency(JAI.create("constant", pb, null), TRANS_BLACK));
					
//			        /**if we add a new band to the Coverage we have to add a new SampleDimension too*/
//			        GridSampleDimension[] sampleDimensions = new GridSampleDimension[(numBands%2 == 0 ? numBands : numBands+1)];
//			        for( int dim = 0; dim<numBands; dim++ ) {
//			        	sampleDimensions[dim] = (GridSampleDimension) coverage.getSampleDimension(dim);
//			        }
//			        
//					/**checking for the source alpha channel, adding one if necessary*/
//					if( numBands%2 != 0 ) {
//				        sampleDimensions[numBands] = new GridSampleDimension();
//					}
					try {
						ImageIO.write(imgBackground.createSnapshot(),"tiff",new File("c:/back.tif"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					/**translating the old one*/
			        pb = new ParameterBlock();
					pb.addSource(image);
					pb.add((float)((lo1-los1)/dX));
					pb.add((float)((las2-la2)/dY));
					RenderedImage renderableSource=JAI.create("translate",pb);
					
					/**overlaying images*/
			        pb = new ParameterBlock();
			        pb.addSource(imgBackground).addSource(renderableSource);
					RenderedImage destOverlayed=
						(numBands%2 == 0 ? JAI.create("overlay", pb, null) : addTransparency(JAI.create("overlay", pb, null), TRANS_BLACK));

					//creating a copy of the given grid coverage2D
					GridCoverage2D subCoverage = new GridCoverage2D(
							meta.getName(),
							destOverlayed,
							coverage.getCoordinateReferenceSystem(),
							gSEnvelope);//,
//							sampleDimensions,
//							null,
//							((PropertySourceImpl)coverage).getProperties());
					
					delegate.prepare(outputFormat, subCoverage);
				
				}else
				if( image.getSampleModel().getDataType() == DataBuffer.TYPE_USHORT ) {
					
					// Create the background Image.
					bandValues = new Short[(numBands%2 != 0 ? numBands : numBands - 1)];  
					// Fill the array with a constant value.  
					for(int band=0;band<bandValues.length;band++)  
						bandValues[band] = new Short((short)(0));
			        pb = new ParameterBlock();
					pb.add(new Float( subEnvelope.getWidth() / dX)).add(new Float( subEnvelope.getHeight() / dY));
			        pb.add(bandValues);

					
					//creating the background image for black transparency
					PlanarImage imgBackground = null;
					/**
					 * TODO make it work on more bands than 2
					 * 
					 * 
					 */
					if(numBands%2 != 0){
						//odd number of bands, adding transparency
						imgBackground=JAI.create("constant", pb, null);
						try {
							ImageIO.write(imgBackground.createSnapshot(),"tiff",new File("c:/back.tif"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
						
					}
					else{
						//we have an even number of bands we already have transparency
						imgBackground=JAI.create("constant", pb, null);
						PlanarImage imgBackground1  =JAI.create("constant", pb, null);
						ParameterBlock pb1 = new ParameterBlock();						
						pb1.removeParameters();
						pb1.removeSources();
						pb1.addSource(image);
						pb1.addSource(imgBackground1);
					    imgBackground = JAI.create("bandmerge",pb1,null);	
						try {
							ImageIO.write(imgBackground.createSnapshot(),"png",new File("c:/back.png"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}						
					}
						
					
					

//			        /**if we add a new band to the Coverage we have to add a new SampleDimension too*/
//			        GridSampleDimension[] sampleDimensions = new GridSampleDimension[(numBands%2 == 0 ? numBands : numBands+1)];
//			        for( int dim = 0; dim<numBands; dim++ ) {
//			        	sampleDimensions[dim] = (GridSampleDimension) coverage.getSampleDimension(dim);
//			        }
//			        
//					/**checking for the source alpha channel, adding one if necessary*/
//					if( numBands%2 != 0 ) {
//				        sampleDimensions[numBands] = new GridSampleDimension();
//					}
//					
					/**translating the old one*/
			        pb = new ParameterBlock();
					pb.addSource(image);
					pb.add((float)((lo1-los1)/dX));
					pb.add((float)((las2-la2)/dY));
					RenderedImage renderableSource=JAI.create("translate",pb);
					
					/**overlaying images*/
			        pb = new ParameterBlock();
			        pb.addSource(imgBackground).addSource(renderableSource);
					RenderedImage destOverlayed=
						(numBands%2 == 0 ? JAI.create("overlay", pb, null) : addTransparency(JAI.create("overlay", pb, null), TRANS_BLACK));

					//creating a copy of the given grid coverage2D
					GridCoverage2D subCoverage = new GridCoverage2D(
							meta.getName(),
							destOverlayed,
							coverage.getCoordinateReferenceSystem(),
							gSEnvelope);
//							null,
////							sampleDimensions,
//							null,
//							((PropertySourceImpl)coverage).getProperties());
					
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
			        }
			        else if( image.getSampleModel().getDataType() == DataBuffer.TYPE_SHORT ) {
				        bandValues = new Short[numBands];  
				        // Fill the array with a constant value.  
				        for(int band=0;band<bandValues.length;band++)  
				        	//TODO this should be parameterized!!!!
				        	bandValues[band] = new Short((short)-9999);//quick hack for gtopo30 format!!!!!
			        }			 
				
			        pb = new ParameterBlock();
					pb.add(new Float( subEnvelope.getWidth() / dX)).add(new Float( subEnvelope.getHeight() / dY));
			        pb.add(bandValues);
					PlanarImage imgBackground = JAI.create("constant", pb, null);

					/**translating the old one*/
			        pb = new ParameterBlock();
					pb.addSource(image);
					pb.add((float)((lo1-los1)/dX));
					pb.add((float)((las2-la2)/dY));
					RenderedImage renderableSource=JAI.create("translate",pb);
					
					/**overlaying images*/
			        pb = new ParameterBlock();
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
				
				//creating a copy of the given grid coverage2D
				GridCoverage2D subCoverage = new GridCoverage2D(
						meta.getName(),
						result,
						coverage.getCoordinateReferenceSystem(),
						gSEnvelope,
						coverage.getSampleDimensions(),
						null,
						((PropertySourceImpl)coverage).getProperties());
				
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
	
	private static RenderedOp addTransparency(RenderedImage src, int transparentColor){
		
		double[] low  = new double[1];
		double[] high = new double[1];
		double[] map  = new double[1];
		/**
		 * We are trying to come up with a mask that should spot
		 * 0 or max as the transparency level
		 * TODO adjust float and double support
		 * TODO rename white==max and black==0
		 */
		switch(src.getSampleModel().getTransferType()){
		case DataBuffer.TYPE_FLOAT:
		case DataBuffer.TYPE_DOUBLE:
		case DataBuffer.TYPE_BYTE:
			switch (transparentColor) {
			case TRANS_BLACK :
				//black to transparent
				low[0]  = 1.0F;
				high[0] = 255.0F;
				map[0]  = 255.0F;
				break;
			case TRANS_WHITE :
				//white to transparent
				low[0]  = 0.0F;
				high[0] = 254.0F;
				map[0]  = 0.0F;
				break;
			}
			break;
		case DataBuffer.TYPE_USHORT:
			switch (transparentColor) {
			case TRANS_BLACK :
				//black to transparent
				low[0]  = 1.0F;
				high[0] = 65535f;
				map[0]  =65535f;
				System.out.println("transparent black");
				break;
			case TRANS_WHITE :
				//white to transparent
				low[0]  = 0.0F;
				high[0] = 65534.0F;
				map[0]  = 0.0F;
				break;
			}
			break;
		case DataBuffer.TYPE_INT:
			return null;//TODO  make sure you also put the threshold values for integer databuffer type
		default:
			return null;
		}
		
		//thresholding to get a mask for alpha
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(src);
		pb.add(low);
		pb.add(high);
		pb.add(map);
		RenderedOp mask = JAI.create("threshold", pb, null);

		
		//inverted mask when needed to associate transparency to white
		RenderedOp invertedMask =null;
		if(transparentColor !=
			TRANS_BLACK)
			invertedMask=JAI.create("not", mask);
		
		//checking the case when having 3 bands
		//we have to combine them
		//and then to threshold again
		//TODO do it for inverted mask
		if(mask.getColorModel().getNumComponents()==3){
			//combine the three bands
			  double[][] matrix = {
                      { 1D, 1D, 1D,0D }
                   };

			  // Create the ParameterBlock.
			  pb = new ParameterBlock();
			  pb.addSource(mask);
			  pb.add(matrix);
			  
			  // Perform the band combine operation.
			  mask = JAI.create("bandcombine", pb, null);
			  
			  
			  //nw we need to threshold again otherwise we would miss
			  //the black points in the original image
			  
			  pb.removeSources();
			  pb.removeParameters();
			  pb.addSource(mask);
			  pb.add(low);
			  pb.add(high);
			  pb.add(map);
			  mask = JAI.create("threshold", pb, null);
			  System.out.println(mask.getColorModel().getNumComponents());
				
		}
		
		//merging bands basing the decision on the transparency level
		pb.removeParameters();
		pb.removeSources();
		pb.addSource(src);
		pb.addSource(transparentColor !=
			TRANS_BLACK?invertedMask.getRendering():mask.getRendering());
		
		RenderedOp dest= JAI.create("BandMerge",pb,null);
		try {
			ImageIO.write(dest,"tiff",new File("c:/aa.tif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
