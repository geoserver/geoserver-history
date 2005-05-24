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
			url = new File("C:\\Work\\NURC\\etopo.png").toURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Format format = new WorldImageFormat();
		GridCoverageReader reader = ((AbstractGridFormat) format).getReader(url);
		try {
			GridCoverage gc = reader.read(null);
			//getting crs from gc
			CoordinateReferenceSystem crs = gc.getCoordinateReferenceSystem();
			
			///////////////////////////////////////////////////////////////////////
			//
			// to be changed in the future
			//
			///////////////////////////////////////////////////////////////////////
			//getting the underlying raster
			java.awt.image.Raster data = ((GridCoverage2D) gc).geophysics(true)
			.getRenderedImage().getData();
			
			//getting the envelope
			GeneralEnvelope env = (GeneralEnvelope) ((GridCoverage2D) gc)
			.getEnvelope();
			
			//check if the coverage needs to be resampled
			gc = reShapeData(((GridCoverage2D) gc), data, env.getLength(0), //W
					env.getLength(1) //H
			);
			env = (GeneralEnvelope) ((GridCoverage2D) gc).getEnvelope();
			
			long height = data.getHeight();
			long width = data.getWidth();
			
			double xl = env.getLowerCorner().getOrdinate(0);
			double yl = env.getLowerCorner().getOrdinate(1);
			
			double cellsize = env.getUpperCorner().getOrdinate(0)
			- env.getLowerCorner().getOrdinate(0);
			
			cellsize = cellsize / width; //rivedi

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
	
	private static GridCoverage2D reShapeData(GridCoverage2D gc, Raster raster,
			double W, double H) {
		//resampling the image if needed
		int Nx = raster.getWidth();
		int Ny = raster.getHeight();
		double dx = W / Nx;
		double dy = H / Ny;
		GeneralEnvelope envelope = (GeneralEnvelope) gc.getEnvelope();
		GeneralEnvelope newEnvelope = null;
		
		if (Math.abs(dx - dy) <= GridCoverageReshape.EPS) {
			return gc;
		}
		
		double _Nx = 0.0;
		double _Ny = 0.0;
		
		if ((dx - dy) > GridCoverageReshape.EPS) {
			/**
			 * we have higher resolution on the Y axis we have to increase it
			 * on the X axis as well.
			 */
			
			//new number of columns
			_Nx = W / dy;
			
			double residual = _Nx - Math.floor(_Nx);
			double newLongitudeOfUpperCorner = 0.0;
			
			//check to see if we need to adjust the envelope
			if (((int) Math.round(_Nx) - (int) Math.floor(_Nx)) > 0) {
				//residual >0.5 we need to increase a bit the envelope
				//add the residual to the evelope which means extend the envelope
				//of the original gc
				newLongitudeOfUpperCorner = envelope.getUpperCorner()
				.getOrdinate(0)
				+ (dy * (1 - residual));
			} else {
				//residual <0.5 we need to decrease the envelope by the residual
				newLongitudeOfUpperCorner = envelope.getUpperCorner()
				.getOrdinate(0)
				- (dy * (residual));
			}
			
			newEnvelope = new GeneralEnvelope(new double[] {
					envelope.getLowerCorner().getOrdinate(0),
					envelope.getLowerCorner().getOrdinate(1)
			},
			new double[] {
					newLongitudeOfUpperCorner,
					envelope.getUpperCorner().getOrdinate(1)
			});
			newEnvelope.setCoordinateReferenceSystem(envelope
					.getCoordinateReferenceSystem());
			Nx = (int) Math.round(_Nx);
		} else {
			/**
			 * we have higher resolution on the X axis we have to increase it
			 * on the Y axis as well.
			 */
			
			//new number of rows
			_Ny = H / dx;
			
			double residual = _Ny - Math.floor(_Ny);
			double newLatitudeOfLowerCorner = 0.0;
			
			//check to see if we need to adjust the envelope
			if (((int) Math.round(_Ny) - (int) Math.floor(_Ny)) > 0) {
				//residual >0.5 we need to increase a bit the envelope
				//add the residual to the evelope which means extend the envelope
				//of the original gc
				newLatitudeOfLowerCorner = envelope.getUpperCorner()
				.getOrdinate(1)
				+ (dx * (1 - residual));
			} else {
				//residual <0.5 we need to decrease the envelope by the residual
				newLatitudeOfLowerCorner = envelope.getUpperCorner()
				.getOrdinate(1)
				+ (dx * (residual));
			}
			
			newEnvelope = new GeneralEnvelope(new double[] {
					envelope.getLowerCorner().getOrdinate(0),
					newLatitudeOfLowerCorner
			},
			new double[] {
					envelope.getUpperCorner().getOrdinate(0),
					envelope.getUpperCorner().getOrdinate(1)
			});
			newEnvelope.setCoordinateReferenceSystem(envelope
					.getCoordinateReferenceSystem());
			Ny = (int) Math.round(_Ny);
		}
		
		//new grid range
		GeneralGridRange newGridrange = new GeneralGridRange(new int[] { 0, 0 },
				new int[] { Nx, Ny });
		GridGeometry2D newGridGeometry = new GridGeometry2D(newGridrange,
				newEnvelope, new boolean[] { false, true });
		
		//getting the needed operation
		Resampler2D.Operation op = new Resampler2D.Operation();
		
		//getting parameters
		ParameterValueGroup group = op.getParameters();
		group.parameter("Source").setValue(gc.geophysics(true));
		group.parameter("CoordinateReferenceSystem").setValue(gc
				.getCoordinateReferenceSystem());
		group.parameter("GridGeometry").setValue(newGridGeometry);
		
		GridCoverageProcessor2D processor2D = GridCoverageProcessor2D
		.getDefault();
		GridCoverage2D gcOp = (GridCoverage2D) processor2D.doOperation(op, group);
		
		return gcOp;
	}
}
