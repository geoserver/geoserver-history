/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses.coverage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.coverage.grid.stream.StreamGridCoverageExchange;
import org.geotools.gce.arcgrid.ArcGridFormat;
import org.opengis.coverage.grid.GridCoverageExchange;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.ParameterValueGroup;
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
public class AscCoverageResponseDelegate implements CoverageResponseDelegate {
	private GridCoverage2D sourceCoverage;
	private String outputFormat;
    private boolean compressOutput = false;
	
	public AscCoverageResponseDelegate() {
	}
	
	public boolean canProduce(String outputFormat) {
		return "ArcGrid".equalsIgnoreCase(outputFormat) 
			|| "ArcGrid-GZIP".equalsIgnoreCase(outputFormat);
	}
	
	public void prepare(String outputFormat, GridCoverage2D coverage)
	throws IOException {
        this.compressOutput = "ArcGrid-GZIP".equalsIgnoreCase(outputFormat);
		this.outputFormat = outputFormat;
		this.sourceCoverage = coverage;
	}
	
	public String getContentType(GeoServer gs) {
		// return gs.getMimeType();
		return compressOutput ? "application/x-gzip" : "text/plain";
	}
	
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContentEncoding() {
		return compressOutput ? "gzip" : null;
	}
	
	public void encode(OutputStream output)
	throws ServiceException, IOException {
		if (sourceCoverage == null) {
			throw new IllegalStateException(
					"It seems prepare() has not been called"
					+ " or has not succeed");
		}

        GZIPOutputStream gzipOut = null;

        if (compressOutput) {
            gzipOut = new GZIPOutputStream(output);
            output = gzipOut;
        }

		try {
			GridCoverageExchange gce = new StreamGridCoverageExchange();
			GridCoverageWriter writer = gce.getWriter(output, new ArcGridFormat());
			ParameterValueGroup params = writer.getFormat().getWriteParameters();
		    params.parameter("Compressed").setValue(compressOutput);
		    //params.parameter("GRASS").setValue(true);
//		    writer.write(sourceCoverage, (GeneralParameterValue[]) params.
//		    		values().toArray(new GeneralParameterValue[params.
		    writer.write(sourceCoverage, null);
			
            if (gzipOut != null) {
                gzipOut.finish();
                gzipOut.flush();
            }
		} catch (Exception e) {
			throw new WcsException("Problems Rendering Image", e);
		}
	}
}
