/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses;
 
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
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

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.PropertySourceImpl;
import javax.media.jai.RenderedOp;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GeneralGridRange;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.DefaultProcessor;
import org.geotools.coverage.processing.GridCoverageProcessor2D;
import org.geotools.coverage.processing.operation.Resample;
import org.geotools.data.coverage.grid.AbstractGridFormat;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.FactoryFinder;
import org.geotools.referencing.operation.transform.LinearTransform1D;
import org.geotools.resources.image.CoverageUtilities;
import org.geotools.util.NumberRange;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CRSAuthorityFactory;
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

	/**
	 * 
	 * @uml.property name="delegate"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
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
	 * 
	 * @uml.property name="request"
	 * @uml.associationEnd multiplicity="(0 1)"
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
								//CRSFactory crsFactory = FactoryFinder.getCRSFactory(new Hints(Hints.CRS_AUTHORITY_FACTORY,EPSGCRSAuthorityFactory.class));
								CRSFactory crsFactory = FactoryFinder.getCRSFactory(new Hints(Hints.CRS_AUTHORITY_FACTORY,CRSAuthorityFactory.class));
								CoordinateReferenceSystem crs = crsFactory.createFromWKT((String) dfConfig.getParameters().get(key));
								value = crs;
							} else {
								//CRSAuthorityFactory crsFactory=FactoryFinder.getCRSAuthorityFactory("EPSG",new Hints(Hints.CRS_AUTHORITY_FACTORY,EPSGCRSAuthorityFactory.class));
								CRSAuthorityFactory crsFactory=FactoryFinder.getCRSAuthorityFactory("EPSG", new Hints(Hints.CRS_AUTHORITY_FACTORY, CRSAuthorityFactory.class));
								CoordinateReferenceSystem crs=(CoordinateReferenceSystem) crsFactory.createCoordinateReferenceSystem("EPSG:4326");
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
	
		GridCoverage2D subCoverage =null;

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
					//checking color model
					if (image.getColorModel() instanceof DirectColorModel ) 
						image = direct2ComponentColorModel((PlanarImage)image);
					else
						/**
						 * IndexColorModel with more than 8 bits for sample might be a problem because GIF allows only 8 bits based palette 
						 * therefore I prefere switching to component color model in order to handle this properly.
						 * 
						 * NOTE. The only transfert types avalaible for IndexColorModel are byte and ushort.
						 */
						if (image.getColorModel() instanceof IndexColorModel &&
		                        (image.getSampleModel().getTransferType() != DataBuffer.TYPE_BYTE)) {
							image = this.reformatColorModel2ComponentColorModel((PlanarImage)image);
		                }								
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
					subCoverage = (GridCoverage2D) createCoverage(destOverlayed, coverage.getCoordinateReferenceSystem(), gSEnvelope, coverage.getName().toString()); 
					
					//delegate.prepare(outputFormat, subCoverage);
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
					subCoverage = new GridCoverage2D(
							meta.getName(),
							destOverlayed,
							coverage.getCoordinateReferenceSystem(),
							gSEnvelope,
							coverage.getSampleDimensions(),
							null,
							((PropertySourceImpl)coverage).getProperties());
					
					//delegate.prepare(outputFormat, subCoverage);
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
				final GridSampleDimension[] sampleDimensions=coverage.getSampleDimensions();
				/**
				 * This method checks if this coverage has a non geophysic
				 * view associated. In such a case it creates a new coverage based on this non
				 * geophysic view, otherwise it keeps the original view as it is provided.
				 */
				if(CoverageUtilities.hasTransform(sampleDimensions))
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
					
				//delegate.prepare(outputFormat, subCoverage);
			}
		} else {
			RenderedImage image = coverage.getRenderedImage();
			//creating a copy of the given grid coverage2D
			subCoverage = new GridCoverage2D(
					meta.getName(),
					image,
					coverage.getCoordinateReferenceSystem(),
					coverage.getEnvelope(),
					coverage.getSampleDimensions(),
					null,
					((PropertySourceImpl)coverage).getProperties());
		}

	
		if(request.getGridLow()!=null && request.getGridHigh()!=null) {
			final int[] lowers = new int[] {request.getGridLow()[0].intValue(), request.getGridLow()[1].intValue()};
			final int[] highers = new int[] {request.getGridHigh()[0].intValue(), request.getGridHigh()[1].intValue()};
	        //new grid range
	        GeneralGridRange newGridrange = new GeneralGridRange(lowers, highers);
	        GridGeometry2D newGridGeometry = new GridGeometry2D(newGridrange,
	        		subCoverage.getEnvelope()/*, new boolean[] { false, true }, true*/);

	        //getting the needed operation
	        Resample op = new Resample();

	        //getting parameters
	        ParameterValueGroup group = op.getParameters();
	        group.parameter("Source").setValue(subCoverage);
	        group.parameter("CoordinateReferenceSystem").setValue(subCoverage
	            .getCoordinateReferenceSystem());
	        group.parameter("GridGeometry").setValue(newGridGeometry);
	        if(request.getInterpolation()!=null && request.getInterpolation().length()>0) {
	        	group.parameter("InterpolationType").setValue(request.getInterpolation());
	        }

	        DefaultProcessor processor2D = new DefaultProcessor(null);
	        GridCoverage2D gcOp = (GridCoverage2D) processor2D.doOperation(/*op, */group);
			delegate.prepare(outputFormat, gcOp);				
		}else {
			delegate.prepare(outputFormat, subCoverage);
		}
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
    private PlanarImage reformatColorModel2ComponentColorModel(
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
		private PlanarImage direct2ComponentColorModel(PlanarImage surrogateImage) {
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
