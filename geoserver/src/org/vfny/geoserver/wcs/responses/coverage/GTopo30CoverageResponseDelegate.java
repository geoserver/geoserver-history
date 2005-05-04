/*
 * Created on Apr 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.vfny.geoserver.wcs.responses.coverage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.gtopo30.GTopo30Writer;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.wcs.responses.CoverageResponseDelegate;

/**
 * @author simone giannecchini
 * @author alesio fabiani
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GTopo30CoverageResponseDelegate implements
		CoverageResponseDelegate {
	/**
	 * the grid coverage to be used in this repsonse
	 */
	private GridCoverage2D sourceCoverage;
	private String outputFormat=null;;

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.wcs.responses.CoverageResponseDelegate#canProduce(java.lang.String)
	 */
	public boolean canProduce(String outputFormat) {
		
		if(outputFormat.compareToIgnoreCase("GTopo30")==0)
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.wcs.responses.CoverageResponseDelegate#prepare(java.lang.String, org.geotools.coverage.grid.GridCoverage2D)
	 */
	public void prepare(String outputFormat, GridCoverage2D coverage)
			throws IOException {
		this.outputFormat = outputFormat;
		this.sourceCoverage = coverage;

	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.wcs.responses.CoverageResponseDelegate#getContentType(org.vfny.geoserver.global.GeoServer)
	 */
	public String getContentType(GeoServer gs) {
		return "application/x-zip";
	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.wcs.responses.CoverageResponseDelegate#getContentEncoding()
	 */
	public String getContentEncoding() {
		//return "zip";
		return null;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContentDisposition() {
		return "attachment;filename="+this.sourceCoverage.getName()+".zip";
	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.wcs.responses.CoverageResponseDelegate#encode(java.io.OutputStream)
	 */
	public void encode(OutputStream output) throws ServiceException,
			IOException {
		//creating a zip outputstream
		//ByteArrayOutputStream outB = new ByteArrayOutputStream();
		ZipOutputStream outZ= new ZipOutputStream(output);
		output=outZ;
		
		//creating a writer
		GridCoverageWriter writer = new GTopo30Writer(outZ);
		
		//writing
		if(writer!=null)
			writer.write(this.sourceCoverage,null);
		else
			throw new ServiceException("Could not create a writer for the format Gtopo30!");
		outZ.flush();
		outZ.close();
	
	}

}
