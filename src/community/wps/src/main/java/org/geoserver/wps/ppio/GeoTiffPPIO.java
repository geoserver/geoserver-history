/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wps.ppio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.gce.geotiff.GeoTiffFormat;

/**
 * Decodes/encodes a GeoTIFF file
 * @author Andrea Aime - OpenGeo
 *
 */
public class GeoTiffPPIO extends BinaryPPIO {

	protected GeoTiffPPIO() {
		super(GridCoverage2D.class, GridCoverage2D.class, "image/tiff");
	}

	@Override
	public Object decode(InputStream input) throws Exception {
		// in order to read a grid coverage we need to first store it on disk
		File root = new File(System.getProperty("java.io.tmpdir", "."));
		File f = File.createTempFile("wps", "tiff", root);
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(f);
			IOUtils.copy(input, os);
		} finally {
			if(os != null) {
				os.close();
			}
		}
		
		// and then we try to read it as a geotiff
		AbstractGridFormat format = GridFormatFinder.findFormat(f);
		return format.getReader(f).read(null);
	}

	@Override
	public void encode(Object value, OutputStream os) throws Exception {
		GridCoverage2D coverage = (GridCoverage2D) value;
		
		// write the coverage to a temp file
		File root = new File(System.getProperty("java.io.tmpdir", "."));
		File f = File.createTempFile("wps", "tiff", root);
		
		GeoTiffFormat format = new GeoTiffFormat();
		format.getWriter(os).write(coverage, null);
	}

}
