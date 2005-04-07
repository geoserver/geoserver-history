/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses.coverage;

import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.media.jai.IHSColorSpace;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;

import org.geotools.coverage.grid.GridCoverage2D;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.responses.CoverageResponseDelegate;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class IMGCoverageResponseDelegate implements CoverageResponseDelegate {
	private GridCoverage2D sourceCoverage;
	private String outputFormat;
	
	public IMGCoverageResponseDelegate() {
	}
	
	public boolean canProduce(String outputFormat) {
		return (ImageIO.getImageReadersByFormatName(outputFormat) != null);
	}
	
	public void prepare(String outputFormat, GridCoverage2D coverage)
	throws IOException {
		
		this.outputFormat = outputFormat;
		this.sourceCoverage = coverage;
	}
	
	public String getContentType(GeoServer gs) {
		return "image/" + outputFormat.toLowerCase();
	}
	
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContentEncoding() {
		return null;
	}
	
	public void encode(OutputStream output)
	throws ServiceException, IOException {
		if (sourceCoverage == null) {
			throw new IllegalStateException(
					"It seems prepare() has not been called"
					+ " or has not succeed");
		}
		try {
			RenderedImage image = sourceCoverage.geophysics(false).getRenderedImage();
			PlanarImage surrogateImage = null;

//			if( sourceCoverage.getSampleDimension(0).getColorInterpretation().name().equals("GRAY_INDEX") ) {
//				int width = image.getWidth();  
//				int height = image.getHeight();  
//				double[] dpixel = new double[image.getSampleModel().getNumBands()];   
//				// Which are the max and min of the image ? We need to know to create the   
//				// surrogate image.  
//				// Let's use the extrema operator to get them. 
//				ParameterBlock pbMaxMin = new ParameterBlock();  
//				pbMaxMin.addSource(image);  
//				RenderedOp extrema = JAI.create("extrema", pbMaxMin);  
//				// Must get the extrema of all bands !  
//				double[] allMins = (double[])extrema.getProperty("minimum");
//				double[] allMaxs = (double[])extrema.getProperty("maximum");
//				double minValue = Double.MAX_VALUE;
//				double maxValue = Double.MIN_VALUE;
//
//				if( !Double.isNaN(allMins[0]) ) {
//					minValue = allMins[0];
//				} else {
//					Double[] buffer = null;
//					if( image.getData().getDataBuffer() instanceof DataBufferFloat ) {
//						float[] tmp = ((DataBufferFloat) image.getData().getDataBuffer()).getData();
//						buffer = new Double[tmp.length];
//						for( int i=0; i<tmp.length; i++ )
//							buffer[i] = new Double(tmp[i]);
//					} else if( image.getData().getDataBuffer() instanceof DataBufferDouble ) {
//						double[] tmp = ((DataBufferDouble) image.getData().getDataBuffer()).getData();
//						buffer = new Double[tmp.length];
//						for( int i=0; i<tmp.length; i++ )
//							buffer[i] = new Double(tmp[i]);
//					}
//					
//					for( int i=0; i<buffer.length; i++)
//						if( minValue > buffer[i].doubleValue() )
//							minValue = buffer[i].doubleValue();
//				}
//
//				if( !Double.isNaN(allMaxs[0]) ) {
//					maxValue = allMaxs[0];
//				} else {
//					Double[] buffer = null;
//					if( image.getData().getDataBuffer() instanceof DataBufferFloat ) {
//						float[] tmp = ((DataBufferFloat) image.getData().getDataBuffer()).getData();
//						buffer = new Double[tmp.length];
//						for( int i=0; i<tmp.length; i++ )
//							buffer[i] = new Double(tmp[i]);
//					} else if( image.getData().getDataBuffer() instanceof DataBufferDouble ) {
//						double[] tmp = ((DataBufferDouble) image.getData().getDataBuffer()).getData();
//						buffer = new Double[tmp.length];
//						for( int i=0; i<tmp.length; i++ )
//							buffer[i] = new Double(tmp[i]);
//					}
//					
//					for( int i=0; i<buffer.length; i++)
//						if( maxValue < buffer[i].doubleValue() )
//							maxValue = buffer[i].doubleValue();
//				}
//
//				//minValue = ( !Double.isNaN(allMins[0]) ? allMins[0] : 0.0f );
//				//maxValue = ( !Double.isNaN(allMaxs[0]) ? allMaxs[0] : 0.0f );
//				
//				for(int v=1;v<allMins.length;v++) {  
//					if (allMins[v] < minValue) minValue = allMins[v];    
//					if (allMaxs[v] > maxValue) maxValue = allMaxs[v];    
//				}  
//				
//				
////				double minValue = sourceCoverage.getSampleDimension(0).getMinimumValue();
////				double maxValue = sourceCoverage.getSampleDimension(0).getMinimumValue();
//				
//				// Rescale the image with the parameters	    
//				double[] subtract = new double[1]; subtract[0] = minValue;  
//				double[] divide   = new double[1]; divide[0]   = 255./(maxValue-minValue);
//
//				// Now we can rescale the pixels gray levels:  
//				ParameterBlock pbRescale = new ParameterBlock();  
//				pbRescale.add(divide);  
//				pbRescale.add(subtract);  
//				pbRescale.addSource(image);  
//				surrogateImage = (PlanarImage)JAI.create("rescale", pbRescale, null);  
//				// Let's convert the data type for displaying.  
//				ParameterBlock pbConvert = new ParameterBlock();  
//				pbConvert.addSource(surrogateImage);  
//				pbConvert.add(DataBuffer.TYPE_BYTE);  
//				surrogateImage = JAI.create("format", pbConvert);
//				//surrogateImage = JAI.create("invert", surrogateImage);
//			} else {
				//surrogateImage = PlanarImage.wrapRenderedImage(image);
//			}
					
			/** Write image to disk and display it */
//			ImageIO.write(surrogateImage /*highlightImage(surrogateImage)*/, outputFormat.toLowerCase(), output);
			ImageIO.write(image /*highlightImage(surrogateImage)*/, outputFormat.toLowerCase(), output);
			output.flush();
			output.close();
		} catch (Exception e) {
			throw new WcsException("Problems Rendering Image: " + e.toString(), e);
		}
	}
	
	
	private RenderedImage highlightImage(RenderedImage stillImg) {
		
		RenderedImage dispImg = null;
		
		// Highlight the human pixels (detection results img)
		
		// Create a constant image
		
		Byte[] bandValues = new Byte[1];
		
		bandValues[0] = new Byte("65"); //32 -- orangeish, 65 -- greenish
		
		ParameterBlock pbConstant = new ParameterBlock();
		
		pbConstant.add(new Float(stillImg.getWidth())); // The width
		
		pbConstant.add(new Float(stillImg.getHeight())); // The height
		
		pbConstant.add(bandValues); // The band values
		
		PlanarImage imgConstant = (PlanarImage)JAI.create("constant",
				pbConstant);
		
		
		
		//System.out.println("Making multiply image");
		
		// Multiply the mask by 255 so the values are 0 or 255
		
		ParameterBlock pbMultiply = new ParameterBlock();
		
		pbMultiply.addSource((PlanarImage)stillImg);
		
		double[] multiplyArray = new double[] {255.0};
		
		pbMultiply.add(multiplyArray);
		
		PlanarImage imgMask = (PlanarImage)JAI.create("multiplyconst",
				pbMultiply);
		
		
		
		//System.out.println("Making IHS image");
		
		// Create a Intensity, Hue, Saturation image
		
		ParameterBlock pbIHS = new ParameterBlock();
		
		pbIHS.setSource(stillImg, 0); //still img is the intensity
		
		pbIHS.setSource(imgConstant, 1); //constant img is the hue
		
		pbIHS.setSource(imgMask, 2); //mask is the saturation
		
		//create rendering hint for IHS image to specify the color model
		
		ComponentColorModel IHS_model = new
		ComponentColorModel(IHSColorSpace.
				
				getInstance(),
				
				new int[] {8, 8, 8}
		
		,
		
		false, false,
		
		Transparency.OPAQUE,
		
		DataBuffer.TYPE_BYTE);
		
		ImageLayout layout = new ImageLayout();
		
		layout.setColorModel(IHS_model);
		
		RenderingHints rh = new RenderingHints(JAI.KEY_IMAGE_LAYOUT,
				layout);
		
		PlanarImage IHSImg = JAI.create("bandmerge", pbIHS, rh);
		
		
		
		//System.out.println("Making RGB image");
		
		// Convert IHS image to a RGB image
		
		ParameterBlock pbRGB = new ParameterBlock();
		
		//create rendering hint for RGB image to specify the color model
		
		ComponentColorModel RGB_model = new ComponentColorModel(ColorSpace.
				
				getInstance(ColorSpace.CS_sRGB),
				
				new int[] {8, 8, 8}
		
		,
		
		false, false,
		
		Transparency.OPAQUE,
		
		DataBuffer.TYPE_BYTE);
		
		pbRGB.addSource(IHSImg);
		
		pbRGB.add(RGB_model);
		
		dispImg = JAI.create("colorconvert", pbRGB);
		
		
		
		return dispImg;
		
	}
	
}
