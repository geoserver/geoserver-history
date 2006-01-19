/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses.coverage;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.image.WorldImageWriter;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.responses.CoverageResponseDelegate;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 */
public class IMGCoverageResponseDelegate implements CoverageResponseDelegate {

	/**
	 * 
	 * @uml.property name="sourceCoverage"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private GridCoverage2D sourceCoverage;

	private String outputFormat;
	
	public IMGCoverageResponseDelegate() {
	}
	
	public boolean canProduce(String outputFormat) {
		
		if(outputFormat.equalsIgnoreCase("bmp")||
				outputFormat.equalsIgnoreCase("gif")||
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
	
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContentDisposition() {
		return outputFormat.equalsIgnoreCase("tiff") || outputFormat.equalsIgnoreCase("tif") ?
				"attachment;filename="+this.sourceCoverage.getName()+"."+outputFormat : null;
	}

	public void encode(OutputStream output)
	throws ServiceException, IOException {
		if (sourceCoverage == null) {
			throw new IllegalStateException(
					"It seems prepare() has not been called"
					+ " or has not succeed");
		}
		try {
			GridCoverageWriter writer= new WorldImageWriter(output);
		      //writing parameters for png
	        Format writerParams = writer.getFormat();
	        writerParams.getWriteParameters().parameter("Format").setValue(this.outputFormat);	
	        //	      writing
	        writer.write(sourceCoverage, null);
			//freeing everything
			//writer.dispose();
			//writer=null;
			//this.sourceCoverage.dispose();
			//this.sourceCoverage=null;
	        output.flush();
	        output.close();
		} catch (Exception e) {
			throw new WcsException("Problems Rendering Image: " + e.toString(), e);
		}
	}
}