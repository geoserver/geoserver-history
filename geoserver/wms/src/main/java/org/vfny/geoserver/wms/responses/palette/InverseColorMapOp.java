package org.vfny.geoserver.wms.responses.palette;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;

/**
 * This class provide an Image oriented interface for the
 * {@link EfficientInverseColorMapComputation}. Specifically, it is designed in
 * order to implement the {@link BufferedImage} for processing
 * {@link BufferedImage}s efficiently accessgint eh raster pixels directly but
 * it also provide a method to process general {@link RenderedImage}s
 * implementations.
 * 
 * @author Simone Giannecchini - GeoSolutions SAS
 * @see EfficientInverseColorMapComputation
 * 
 */
public class InverseColorMapOp implements BufferedImageOp {

	protected final IndexColorModel icm;

	protected final EfficientInverseColorMapComputation invCM;

	/**
	 * @see EfficientInverseColorMapComputation#EfficientInverseColorMapComputation(IndexColorModel,
	 *      int, int).
	 */
	public InverseColorMapOp(final IndexColorModel destCM,
			final int quantizationColors, final int alphaThreshold) {
		this.icm = destCM;
		invCM = new EfficientInverseColorMapComputation(destCM,
				quantizationColors, alphaThreshold);

	}

	public InverseColorMapOp(final IndexColorModel destCM) {
		this(
				destCM,
				EfficientInverseColorMapComputation.DEFAULT_QUANTIZATION_COLORS,
				EfficientInverseColorMapComputation.DEFAULT_ALPHA_TH);
	}

	public BufferedImage createCompatibleDestImage(BufferedImage src,
			ColorModel destCM) {
		if (!(destCM instanceof IndexColorModel)
				|| ((IndexColorModel) destCM).getTransparency() == Transparency.TRANSLUCENT)
			return null;
		return new BufferedImage(src.getWidth(), src.getHeight(),
				BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel) destCM);
	}

	/**
	 * @see BufferedImageOp#filter(BufferedImage, BufferedImage)
	 */
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		// //
		//
		// Creating destination BufferedImage
		//
		// //
		if (dest == null)
			dest = new BufferedImage(src.getWidth(), src.getHeight(),
					BufferedImage.TYPE_BYTE_INDEXED, icm);

		// //
		//
		// Collecting info about the source and destination image
		//
		// //
		final WritableRaster wr = dest.getRaster();
		final Raster r = src.getRaster();
		final int minx = r.getMinX();
		final int miny = r.getMinY();
		final int maxx = minx + r.getWidth();
		final int maxy = miny + r.getHeight();

		// //
		//
		// Main loop
		//
		// //
		for (int j = miny; j < maxy; j++)
			for (int i = minx; i < maxx; i++) {

				// /////////////////////////////////////////////////////////////////////
				//
				// This is where the magic takes place
				//
				// /////////////////////////////////////////////////////////////////////
				// // get the pixel bands
				final int rgba[] = r.getPixel(i, j, (int[]) null);
				wr.setSample(i, j, 0, (invCM.getIndexNearest(rgba) & 0xff));

			}

		return dest;
	}



	public Rectangle2D getBounds2D(BufferedImage src) {
		return new Rectangle(src.getWidth(), src.getHeight());
	}

	public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
		if (dstPt == null)
			dstPt = new Point();
		dstPt.setLocation(srcPt);
		return dstPt;
	}

	public RenderingHints getRenderingHints() {
		return null;
	}

	/**
	 * Filter the source {@link RenderedImage} using the provided color map.
	 * 
	 * @param src
	 *            is the {@link RenderedImage} to process.
	 * @return a processed {@link RenderedImage}.
	 */
	public RenderedImage filterRenderedImage(RenderedImage src) {
		// //
		//
		// ShortCut for using bufferedimages and avoiding tiling
		//
		// //
		if (src instanceof BufferedImage)
			return filter((BufferedImage) src, null);

		// //
		//
		// Create the destination image
		//
		// //
		final BufferedImage dest = new BufferedImage(src.getWidth(), src
				.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, icm);
		final WritableRaster wr = dest.getRaster();
		// //
		//
		// Filter the image out
		//
		// //
		// /////////////////////////////////////////////////////////////////////
		//
		// The external loop is performed on the tiles the internal one on each
		// single tile. While we do these loops we set the pixels of the
		// destination image.
		//
		// /////////////////////////////////////////////////////////////////////
		final int minTileX = src.getMinTileX();
		final int minTileY = src.getMinTileY();
		// //
		//
		// Optimize the hell out of this code. WE have a single tile, let's go
		// fast!
		//
		// //
		if (src.getNumXTiles() == 1 && src.getNumYTiles() == 1) {
			
			final int srcMinx = src.getMinX();
			final int srcMiny =src.getMinY();
			final int srcMaxx = srcMinx + src.getWidth();
			final int srcMaxy = srcMiny + src.getHeight();
			final Raster r=src.getTile(minTileX, minTileY);
			// //
			//
			// Main loop
			//
			// //
			for (int j = srcMiny,jd=0; j < srcMaxy; j++,jd++)
				for (int i = srcMinx,id=0; i < srcMaxx; i++,id++) {

					// /////////////////////////////////////////////////////////////////////
					//
					// This is where the magic takes place
					//
					// /////////////////////////////////////////////////////////////////////
					// // get the pixel bands
					final int rgba[] = r.getPixel(i, j, (int[]) null);
					wr.setSample(id, jd, 0, (invCM.getIndexNearest(rgba) & 0xff));

				}
			return dest;
		}

		// if we got here we have more than one tile
		final int maxTileX = src.getNumXTiles() + minTileX;
		final int maxTileY = src.getNumYTiles() + minTileY;
		final int tileW = src.getTileWidth();
		final int tileH = src.getTileHeight();

		// //
		//
		// Collecting info about the source image
		//
		// //
		int xx = 0;
		final int srcW = src.getWidth();
		for (int ti = minTileX; ti < maxTileX; ti++)
			for (int tj = minTileY; tj < maxTileY; tj++) {
				// get the source raster tile
				final Raster r = src.getTile(ti, tj);

				// loop over it;
				final int minx = r.getMinX();
				final int miny = r.getMinY();
				final int maxx = minx + tileW;
				final int maxy = miny + tileH;
				for (int j = miny; j < maxy; j++)
					for (int i = minx; i < maxx; i++) {

						// /////////////////////////////////////////////////////////////////////
						//
						// This is where the magic takes place
						//
						// /////////////////////////////////////////////////////////////////////
						// // get the pixel bands
						wr.setSample(xx % srcW, xx / srcW, 0,
								(invCM.getIndexNearest(r.getPixel(i, j,
										(int[]) null)) & 0xff));

						// //
						//
						// Advance couner on the output image
						//
						// //
						xx++;
					}
			}
		return dest;
	}

}
