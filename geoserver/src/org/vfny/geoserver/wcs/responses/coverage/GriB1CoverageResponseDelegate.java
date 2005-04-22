/*
 * Created on Apr 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.vfny.geoserver.wcs.responses.coverage;

import java.io.IOException;
import java.io.OutputStream;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.grib1.GRIB1Writer;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.wcs.responses.CoverageResponseDelegate;

/**
 * @author giannecchini
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GriB1CoverageResponseDelegate implements CoverageResponseDelegate {

	private String outputFormat;
	private GridCoverage2D sourceCoverage;

	/**
	 * 
	 */
	public GriB1CoverageResponseDelegate() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.wcs.responses.CoverageResponseDelegate#canProduce(java.lang.String)
	 */
	public boolean canProduce(String outputFormat) {
		if(outputFormat.compareToIgnoreCase("GriB1")==0)
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
		// TODO Auto-generated method stub
		return "application/octet-stream";
	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.wcs.responses.CoverageResponseDelegate#getContentEncoding()
	 */
	public String getContentEncoding() {
		// TODO Auto-generated method stub
		return "grb";
	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.wcs.responses.CoverageResponseDelegate#encode(java.io.OutputStream)
	 * TODO
	 */
	public void encode(OutputStream output) throws ServiceException,
			IOException {
		if (sourceCoverage == null) {
			throw new IllegalStateException(
					"It seems prepare() has not been called"
					+ " or has not succeed");
		}
		//encode and write to the output
		GridCoverageWriter writer = new GRIB1Writer(output);
		if(writer!=null)
			writer.write(this.sourceCoverage,null);
		else
			throw new ServiceException("Could not instantiate a grib writer");
		output.flush();

	}

}
