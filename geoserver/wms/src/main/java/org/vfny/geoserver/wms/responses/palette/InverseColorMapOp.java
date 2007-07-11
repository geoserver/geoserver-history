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
import java.awt.image.WritableRaster;

/**
 * 
 * @author Simone Giannecchini - GeoSolutions
 *
 */
public class InverseColorMapOp implements BufferedImageOp {

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
		this(destCM, 5, 1);
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

}
