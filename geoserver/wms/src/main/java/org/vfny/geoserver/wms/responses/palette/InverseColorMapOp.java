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
 * This class implements a simple {@link BufferedImageOp} subclass to apply
 * inversion of color map to a given {@link BufferedImage}.
 * 
 * <p>
 * Note that the {@link EfficientInverseColorMapComputation} works on opaque
 * pixels and does not take into account transparency. The magic is done at the
 * {@link InverseColorMapRasterOp} level using the threshold we provide ere.
 * 
 * 
 * @author Simone Giannecchini - GeoSolutions
 * @see EfficientInverseColorMapComputation
 */
public class InverseColorMapOp implements BufferedImageOp {

	protected final InverseColorMapRasterOp rasterOp;

	protected final IndexColorModel icm;

	protected final int alphaThreshold;

	protected final boolean hasAlpha;

	protected final int transparencyIndex;

	/**
	 * This constructor allows users to specify not only the final
	 * {@link IndexColorModel} but also the number of quantization colors and
	 * the threshold to use for deciding whether translucent pixels should be
	 * maded opaque or transparent.
	 * 
	 * @param destCM
	 *            is the destination {@link IndexColorModel}.
	 * @param quantizationColors
	 *            for quantization of input colors.
	 * @param alphaThreshold
	 *            for translucent pixels.
	 */
	public InverseColorMapOp(final IndexColorModel destCM,
			final int quantizationColors, final int alphaThreshold) {
		this.rasterOp = new InverseColorMapRasterOp(destCM, quantizationColors,
				alphaThreshold);
		this.icm = destCM;
		this.alphaThreshold = alphaThreshold;
		hasAlpha = icm.hasAlpha();
		transparencyIndex = icm.getTransparentPixel();

	}

	/**
	 * This constructor use default values for the number of quantization colors
	 * and for the transparency threshold. In particular, all the input pixels
	 * which are not fully opaque will become transparent.
	 * 
	 * @param destCM
	 *            destination color model.
	 */
	public InverseColorMapOp(final IndexColorModel destCM) {
		this(destCM, 5, 255);
	}

	/**
	 * @see BufferedImageOp#createCompatibleDestImage(BufferedImage, ColorModel)
	 */
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
		if (dest == null)
			dest = new BufferedImage(src.getWidth(), src.getHeight(),
					BufferedImage.TYPE_BYTE_INDEXED, icm);
		else {
			if (!(dest.getColorModel() instanceof IndexColorModel)
					|| ((IndexColorModel) dest.getColorModel())
							.getTransparency() != this.transparencyIndex)
				throw new IllegalArgumentException(
						"Unable to filter the provided image due to inconsistencies in the color models");
			if (((IndexColorModel) dest.getColorModel()).getTransparentPixel() != this.transparencyIndex)
				throw new IllegalArgumentException(
						"Unable to filter the provided image due to inconsistencies in the color models");
		}
		final WritableRaster wr = dest.getRaster();
		final Raster ir = src.getRaster();
		this.rasterOp.filter(ir, wr);
		return dest;
	}

	/**
	 * @see BufferedImageOp#getBounds2D(BufferedImage)
	 */
	public Rectangle2D getBounds2D(BufferedImage src) {
		return new Rectangle(src.getWidth(), src.getHeight());
	}

	/**
	 * @see BufferedImageOp#getPoint2D(Point2D, Point2D)
	 */
	public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
		if (dstPt == null)
			dstPt = new Point();
		dstPt.setLocation(srcPt);
		return dstPt;
	}

	/**
	 * @see BufferedImageOp#getRenderingHints()
	 */
	public RenderingHints getRenderingHints() {
		return null;
	}

}
