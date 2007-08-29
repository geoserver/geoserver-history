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

import javax.media.jai.TiledImage;

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
public final class InverseColorMapOp implements BufferedImageOp {

	protected final InverseColorMapRasterOp rasterOp;

	protected final IndexColorModel icm;

	protected final int alphaThreshold;

	protected final boolean hasAlpha;

	protected final int transparencyIndex;

	public InverseColorMapOp(final IndexColorModel destCM,
			final int quantizationColors, final int alphaThreshold) {
		this.rasterOp = new InverseColorMapRasterOp(destCM, quantizationColors,
				alphaThreshold);
		this.icm = destCM;
		this.alphaThreshold = alphaThreshold;
		hasAlpha = icm.hasAlpha();
		transparencyIndex = icm.getTransparentPixel();

	}

	public InverseColorMapOp(final IndexColorModel destCM) {
		this(destCM, InverseColorMapRasterOp.DEFAULT_QUANTIZATION_COLORS,
				InverseColorMapRasterOp.DEFAULT_ALPHA_TH);
	}

	public BufferedImage createCompatibleDestImage(BufferedImage src,
			ColorModel destCM) {
		if (!(destCM instanceof IndexColorModel)
				|| ((IndexColorModel) destCM).getTransparency() == Transparency.TRANSLUCENT)
			return null;
		return new BufferedImage(src.getWidth(), src.getHeight(),
				BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel) destCM);
	}

	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		if (dest == null)
			dest = new BufferedImage(src.getWidth(), src.getHeight(),
					BufferedImage.TYPE_BYTE_INDEXED, icm);
		else {
			if (!(dest.getColorModel() instanceof IndexColorModel)
					|| ((IndexColorModel) dest.getColorModel())
							.getTransparency() != this.transparencyIndex)
				throw new IllegalArgumentException();
			if (((IndexColorModel) dest.getColorModel()).getTransparentPixel() != this.transparencyIndex)
				throw new IllegalArgumentException();
		}
		final WritableRaster wr = dest.getRaster();
		final Raster ir = src.getRaster();
		this.rasterOp.filter(ir, wr);
		return dest;
	}

	public BufferedImage filterRenderedImage(RenderedImage in) {
		// //
		//
		// ShortCut for using bufferedimages and avoiding tiling
		//
		// //
		if (in instanceof BufferedImage)
			return filter((BufferedImage) in, null);

		// //
		//
		// Create the destination image
		//
		// //
		final TiledImage src = new TiledImage(in, true);
		final BufferedImage dest = new BufferedImage(src.getWidth(), src
				.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, icm);
		final WritableRaster destWr = dest.getRaster();


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
			final Raster sourceR = src.getTile(minTileX, minTileY);
			rasterOp.filter(sourceR.createChild(src.getMinX(), src.getMinY(),
					src.getWidth(), src.getHeight(), 0, 0, null), destWr);
			return dest;
		}

		// //
		//
		// Test me more!!!
		//
		// //
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
		final int rgba[] = new int[src.getSampleModel().getNumBands()];
		final boolean sourceHasAlpha = rgba.length == 0;
		final EfficientInverseColorMapComputation invCM = rasterOp.getInvCM();
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
						for (int b = 0; b < rgba.length; b++)
							rgba[b] = src.getSample(i, j, b);
						if ((sourceHasAlpha && hasAlpha && rgba[3] >= this.alphaThreshold)
								|| !hasAlpha) {
							int val = invCM.getIndexNearest(rgba[0] & 0xff,
									rgba[1] & 0xff, rgba[2]);
							if (hasAlpha && val >= transparencyIndex)
								val++;
							destWr.setSample(xx % srcW, xx / srcW, 0, val);
						} else
							destWr.setSample(xx % srcW, xx / srcW, 0,
									transparencyIndex);

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

	public IndexColorModel getIcm() {
		return icm;
	}

}
