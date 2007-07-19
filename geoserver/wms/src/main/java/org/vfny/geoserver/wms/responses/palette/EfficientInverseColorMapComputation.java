package org.vfny.geoserver.wms.responses.palette;

import java.awt.image.IndexColorModel;

/**
 * This class is responsible for computing efficiently an inverse color map for
 * a given color map.
 * 
 * <p>
 * This algorithm is adapted from the algorithm found in Graphics Gems volume 2
 * by Spencer W. Thomas "Efficient Inverse Color Map Computation".
 * 
 * @author Simone Giannecchini - GeoSolutions SAS
 * 
 */
public final class EfficientInverseColorMapComputation {
	/**
	 * Default number of quantization colors used to build the index for the
	 * inverse color map.
	 */
	public static final int DEFAULT_QUANTIZATION_COLORS = 5;

	/**
	 * Default value for the threshold to decide whther a pixel is opaque (>=)
	 * or transparent (<).
	 */
	public static final int DEFAULT_ALPHA_TH = 1;


	/**
	 * Number of most significant bits we are going to use from the input color
	 * in order to quantize them.
	 */
	protected int bits;

	protected int truncationBits;

	protected int blueQuantizationMask;

	protected int greenQuantizationMask;

	protected int redQuantizationMask;

	/**
	 * Forward color map. Is a 3*numcolors array.
	 */
	protected byte[][] colorMap;

	/**
	 * inverse rgb color map
	 */
	protected byte[] mapBuf;

	protected IndexColorModel icm;

	protected int alphaThreshold;

	protected boolean hasAlpha;

	protected int transparencyIndex;

	/**
	 * Constructor.
	 * 
	 * @param icm
	 *            to use for the inversion.
	 */
	public EfficientInverseColorMapComputation(final IndexColorModel icm) {
		this(icm, DEFAULT_QUANTIZATION_COLORS, DEFAULT_ALPHA_TH);
	}

	/**
	 * /** Constructor.
	 * 
	 * @param destCM
	 *            to use for the inversion.
	 * @param quantizationBits
	 *            to use when builing the index in order to quantize rgb values.
	 * @param alphaThreshold
	 *            to decide whther a pixel is transparent or not.
	 */
	public EfficientInverseColorMapComputation(final IndexColorModel destCM,
			final int quantizationBits, final int alphaThreshold) {
		// /////////////////////////////////////////////////////////////////////
		//
		// Initial checks
		//
		// /////////////////////////////////////////////////////////////////////
		checkQuantizationBits(quantizationBits);

		// /////////////////////////////////////////////////////////////////////
		//
		// Initialization
		//
		// /////////////////////////////////////////////////////////////////////
		this.alphaThreshold = alphaThreshold;
		this.icm = destCM;
		hasAlpha = icm.hasAlpha();
		transparencyIndex = icm.getTransparentPixel();
		bits = quantizationBits;
		truncationBits = 8 - bits;
		blueQuantizationMask = (1 << bits) - 1;
		greenQuantizationMask = (blueQuantizationMask << bits);
		redQuantizationMask = (greenQuantizationMask << bits);
		final int mapSize = icm.getMapSize();
		colorMap = new byte[3][hasAlpha ? (mapSize - 1) : mapSize];

		if (hasAlpha) {
			final byte[] r = new byte[mapSize];
			final byte[] g = new byte[mapSize];
			final byte[] b = new byte[mapSize];
			icm.getReds(r);
			icm.getGreens(g);
			icm.getBlues(b);
			final int reducedMapSize = mapSize - 1;
			if (transparencyIndex == 0) {
				System.arraycopy(r, 1, colorMap[0], 0, reducedMapSize);
				System.arraycopy(g, 1, colorMap[1], 0, reducedMapSize);
				System.arraycopy(b, 1, colorMap[2], 0, reducedMapSize);
			} else if (transparencyIndex == mapSize - 1) {
				System.arraycopy(r, 0, colorMap[0], 0, reducedMapSize);
				System.arraycopy(g, 0, colorMap[1], 0, reducedMapSize);
				System.arraycopy(b, 0, colorMap[2], 0, reducedMapSize);
			} else {
				System.arraycopy(r, 0, colorMap[0], 0, transparencyIndex);
				System.arraycopy(g, 0, colorMap[1], 0, transparencyIndex);
				System.arraycopy(b, 0, colorMap[2], 0, transparencyIndex);

				System.arraycopy(r, transparencyIndex + 1, colorMap[0],
						transparencyIndex, reducedMapSize - transparencyIndex);
				System.arraycopy(g, transparencyIndex + 1, colorMap[1],
						transparencyIndex, reducedMapSize - transparencyIndex);
				System.arraycopy(b, transparencyIndex + 1, colorMap[2],
						transparencyIndex, reducedMapSize - transparencyIndex);
			}
		} else {
			icm.getReds(colorMap[0]);
			icm.getGreens(colorMap[1]);
			icm.getBlues(colorMap[2]);
		}
		init();
	}

	/**
	 * {@link EfficientInverseColorMapComputation} that allows us to specify the
	 * number of bits we are going to save from the quantization.
	 * 
	 * @param rgbColorMap
	 *            the input forward color map.
	 * @param quantizationBits
	 *            to use when builing the index in order to quantize rgb values.
	 * @param alphaThreshold
	 *            to decide whther a pixel is transparent or not.
	 * 
	 */
	public EfficientInverseColorMapComputation(byte[][] rgbColorMap,
			final int quantizationBits, final int alphaThreshold) {
		// /////////////////////////////////////////////////////////////////////
		//
		// Initial checks
		//
		// /////////////////////////////////////////////////////////////////////
		checkQuantizationBits(quantizationBits);

		// /////////////////////////////////////////////////////////////////////
		//
		// Initialization
		//
		// /////////////////////////////////////////////////////////////////////
		this.alphaThreshold = alphaThreshold;
		colorMap = rgbColorMap;
		bits = quantizationBits;
		truncationBits = 8 - bits;
		blueQuantizationMask = (1 << bits) - 1;
		greenQuantizationMask = (blueQuantizationMask << bits);
		redQuantizationMask = (greenQuantizationMask << bits);
		init();
	}

	private void checkQuantizationBits(final int quantizationBits) {
		if (quantizationBits < 3 || quantizationBits > 8)
			throw new IllegalArgumentException(
					"THe number of quantization bits is invalid. Any number in between 3 an 8 is valid..");
	}

	private void init() {
		// /////////////////////////////////////////////////////////////////////
		//
		// Initialization
		//
		// /////////////////////////////////////////////////////////////////////
		final int maximumQuantizationValue = 1 << bits;
		final int numberOfColors = colorMap[0].length;
		mapBuf = new byte[maximumQuantizationValue * maximumQuantizationValue
				* maximumQuantizationValue];
		final int[] distBuf = new int[maximumQuantizationValue
				* maximumQuantizationValue * maximumQuantizationValue];

		final int x = (1 << truncationBits);
		final int xsqr = x * x;
		final int txsqr = xsqr + xsqr;

		// /////////////////////////////////////////////////////////////////////
		//
		// This code visits every cell in the inverse color map for each
		// representative color.
		//
		// /////////////////////////////////////////////////////////////////////
		for (int i = 0; i < numberOfColors; ++i) {

			// //
			//
			// Get the representative color components
			//
			// //
			final int red = colorMap[0][i] & 0xFF;
			final int green = colorMap[1][i] & 0xFF;
			final int blue = colorMap[2][i] & 0xFF;

			// //
			//
			// Distances are measured to the center of each quantized cell, so
			// x/2 is used as the starting point. This is due to the fact that
			// we quantize by performing right shift.
			//
			// //
			final int x_ = x / 2;
			int rdist = red - x_;
			int gdist = green - x_;
			int bdist = blue - x_;
			// distance
			rdist = rdist * rdist + gdist * gdist + bdist * bdist;

			final int rinc = 2 * (xsqr - (red << truncationBits));
			final int ginc = 2 * (xsqr - (green << truncationBits));
			final int binc = 2 * (xsqr - (blue << truncationBits));

			// //
			//
			// Going to check for all the quantized space
			//
			// //
			for (int r = 0, rxx = rinc, rgbI = 0; r < maximumQuantizationValue; rdist += rxx, ++r, rxx += txsqr) {
				gdist = rdist;
				for (int g = 0, gxx = ginc; g < maximumQuantizationValue; gdist += gxx, ++g, gxx += txsqr) {
					bdist = gdist;
					for (int b = 0, bxx = binc; b < maximumQuantizationValue; bdist += bxx, ++b, ++rgbI, bxx += txsqr) {
						if (i == 0 || distBuf[rgbI] > bdist) {
							distBuf[rgbI] = bdist;
							mapBuf[rgbI] = (byte) i;
						}
					}
				}
			}
		}
	}

	/**
	 * Default constructor that does the quantization with 5 bits.
	 * 
	 * @param rgbColorMap
	 *            the input forward color map.
	 */
	public EfficientInverseColorMapComputation(byte[][] rgbColorMap) {
		this(rgbColorMap, DEFAULT_QUANTIZATION_COLORS, DEFAULT_ALPHA_TH);
	}

	/**
	 * This method is responsible for doing the actual lookup that given an rgb
	 * triple returns the best, taking into account quantization, index in the
	 * forward color map.
	 * 
	 * @param red
	 *            component.
	 * @param green
	 *            component.
	 * @param blue
	 *            component.
	 * @return the best, taking into account quantization, index in the forward
	 *         color map for the provided triple.
	 */
	public final int getIndexNearest(int red, int green, int blue) {
		return mapBuf[((red << (2 * bits - truncationBits)) & redQuantizationMask)
				+ ((green << (1 * bits - truncationBits)) & greenQuantizationMask)
				+ ((blue >> (truncationBits)) & blueQuantizationMask)] & 0xFF;
	}

	public final int getIndexNearest(int[] rgba) {
		// keep transparency into account
		int val = transparencyIndex;
		if ((rgba.length % 2 == 0 && hasAlpha && rgba[rgba.length - 1] >= this.alphaThreshold)
				|| !hasAlpha) {
			// do the color inversion
			val = mapBuf[(((rgba[0] & 0xff) << (2 * bits - truncationBits)) & redQuantizationMask)
					+ (((rgba[1] & 0xff) << (1 * bits - truncationBits)) & greenQuantizationMask)
					+ (((rgba[2] & 0xff) >> (truncationBits)) & blueQuantizationMask)] & 0xFF;
			// keep the transparent pixel into account
			if (val >= transparencyIndex)
				val++;

		}
		return val;

	}

	/**
	 * In case we provided an {@link IndexColorModel} to this
	 * {@link EfficientInverseColorMapComputation} object we'll allow you to
	 * access it again. In case you used the other constructors we do not even
	 * try to build a suitable {@link IndexColorModel} since you might have
	 * previously removed the transparency information.
	 * 
	 * @return the {@link IndexColorModel} provided at construction time to this
	 *         object or null if none was provided.
	 */
	public IndexColorModel getIcm() {
		return icm;

	}
}
