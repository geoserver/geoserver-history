/**
 * 
 */
package org.nato.nurc.utils;

import java.awt.image.Raster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.geotools.coverage.grid.GeneralGridRange;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.operation.Resampler2D;
import org.geotools.coverage.processing.GridCoverageProcessor2D;
import org.geotools.data.coverage.grid.AbstractGridFormat;
import org.geotools.gce.arcgrid.ArcGridFormat;
import org.geotools.gce.arcgrid.ArcGridRaster;
import org.geotools.gce.arcgrid.ArcGridWriter;
import org.geotools.gce.grib1.GRIB1Format;
import org.geotools.gce.image.WorldImageFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author Administrator
 *
 */
public class GridCoverageReshape {
	
	private final static double EPS = 1E-6;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		URL url = null;
		try {
			url = new File("d:\\java\\Work\\RadarSat_NURC_2005_05_18_Dummy_B1.grb").toURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Format format = new GRIB1Format();
		GridCoverageReader reader = ((AbstractGridFormat) format).getReader(url);
		try {
			GridCoverage gc = reader.read(null);
			
			///////////////////////////////////////////////////////////////////////
			//
			// to be changed in the future
			//
			///////////////////////////////////////////////////////////////////////
			
			((GridCoverage2D) gc).show();
		
			//create the file
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter("C:\\pippo.asc"));

			GridCoverageWriter writer = new ArcGridWriter(fileWriter);
			ParameterValueGroup params = writer.getFormat().getWriteParameters();
			writer.write(gc, null);
			
			//freeing everything
			writer.dispose();
			writer=null;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
