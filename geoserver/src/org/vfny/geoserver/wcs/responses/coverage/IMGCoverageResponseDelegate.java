/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses.coverage;


import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;
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
		
		if(outputFormat.equalsIgnoreCase("bmp")||
				outputFormat.equalsIgnoreCase("tiff")||
				outputFormat.equalsIgnoreCase("png")||
				outputFormat.equalsIgnoreCase("jpeg")||
				outputFormat.equalsIgnoreCase("tif"))
			return true;
		return false;
		
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


					
			/** Write image to disk and display it */
			ImageIO.write(image /*highlightImage(surrogateImage)*/, outputFormat.toLowerCase(), output);
			output.flush();
			output.close();
		} catch (Exception e) {
			throw new WcsException("Problems Rendering Image: " + e.toString(), e);
		}
	}
	
	
}
