/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses.coverage;

import java.io.IOException;
import java.io.OutputStream;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.coverage.grid.AbstractGridFormat;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.image.imageio.GeoToolsWriteParams;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.wcs.responses.CoverageResponseDelegate;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public class GeoTIFFCoverageResponseDelegate implements
		CoverageResponseDelegate {

	/**
	 * 
	 * @uml.property name="sourceCoverage"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private GridCoverage2D sourceCoverage;

	public GeoTIFFCoverageResponseDelegate() {
	}

	public boolean canProduce(String outputFormat) {

		if (outputFormat.equalsIgnoreCase("geotiff"))
			return true;
		return false;

	}

	public void prepare(String outputFormat, GridCoverage2D coverage)
			throws IOException {
		this.sourceCoverage = coverage;
	}

	public String getContentType(GeoServer gs) {
		return "image/tiff";
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
		return "attachment;filename=" + this.sourceCoverage.getName() + ".tiff";
	}

	public void encode(OutputStream output) throws ServiceException,
			IOException {
		if (sourceCoverage == null) {
			throw new IllegalStateException(
					"It seems prepare() has not been called"
							+ " or has not succeed");
		}		
		final GridCoverageWriter writer = new GeoTiffWriter(output);
		final GeoTiffWriteParams wp = new GeoTiffWriteParams();
		wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
		wp.setCompressionType("LZW");
		wp.setCompressionQuality(0.75F);
		wp.setTilingMode(GeoToolsWriteParams.MODE_EXPLICIT);
		wp.setTiling(256,256);

		final Format writerParams = writer.getFormat();
		writerParams.getWriteParameters().parameter(
				AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString())
				.setValue(wp);

		writer.write(sourceCoverage, (GeneralParameterValue[]) writerParams.getWriteParameters().values().toArray(new GeneralParameterValue[1]));

		writer.dispose();
		this.sourceCoverage.dispose();
		this.sourceCoverage = null;
	}
}