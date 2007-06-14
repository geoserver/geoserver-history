/* Copyright (c) 2001, 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.palette;

public final class EfficientInverseColorMapComputation {
    protected final int bits;
    protected final int truncationBits;
    protected final int blueQuantizationMask;
    protected final int greenQuantizationMask;
    protected final int redQuantizationMask;
    protected final byte[] colorMap;
    protected final byte[] inverseColorMap; // inverse rgb color map

    public EfficientInverseColorMapComputation(byte[] rgbColorMap, final int quantizationBits) {
        colorMap = rgbColorMap;
        bits = quantizationBits;
        truncationBits = 8 - bits;
        blueQuantizationMask = (1 << bits) - 1;
        greenQuantizationMask = (blueQuantizationMask << bits);
        redQuantizationMask = (greenQuantizationMask << bits);

        //
        final int maximumQuantizationValue = 1 << bits;
        final int numberOfColors = colorMap.length / 3;
        inverseColorMap = new byte[maximumQuantizationValue * maximumQuantizationValue * maximumQuantizationValue];

        final int[] scracthRGBColorMap = new int[maximumQuantizationValue * maximumQuantizationValue * maximumQuantizationValue];
        final int x = (1 << truncationBits); // 8 the size of 1 Dimension of
                                             // each
                                             // quantized cel

        final int xsqr = 1 << (truncationBits * 2); // 64 - twice the smallest
                                                    // step size
                                                    // vale of quantized colors

        final int xsqr2 = xsqr + xsqr;

        for (int i = 0; i < numberOfColors; ++i) {
            final int i_ = i * 3;
            final int red = colorMap[i_] & 0xFF;
            final int green = colorMap[i_ + 1] & 0xFF;
            final int blue = colorMap[i_ + 2] & 0xFF;

            final int x_ = x / 2;
            int rdist = red - x_; // distance of red to center of
                                  // current cell

            int gdist = green - x_; // ditto for green
            int bdist = blue - x_; // ditto for blue
            rdist = (rdist * rdist) + (gdist * gdist) + (bdist * bdist);

            final int rinc = 2 * (xsqr - (red << truncationBits));
            final int ginc = 2 * (xsqr - (green << truncationBits));
            final int binc = 2 * (xsqr - (blue << truncationBits));

            for (int r = 0, rxx = rinc, rgbI = 0; r < maximumQuantizationValue;
                    rdist += rxx, ++r, rxx += xsqr2) {
                gdist = rdist;

                for (int g = 0, gxx = ginc; g < maximumQuantizationValue;
                        gdist += gxx, ++g, gxx += xsqr2) {
                    bdist = gdist;

                    for (int b = 0, bxx = binc; b < maximumQuantizationValue;
                            bdist += bxx, ++b, ++rgbI, bxx += xsqr2) {
                        if ((i == 0) || (scracthRGBColorMap[rgbI] > bdist)) {
                            scracthRGBColorMap[rgbI] = bdist;
                            inverseColorMap[rgbI] = (byte) i;
                        }
                    }
                }
            }
        }
    }

    public EfficientInverseColorMapComputation(byte[] rgbColorMap) {
        this(rgbColorMap, 5);
    }

    public final int getIndexNearest(int red, int green, int blue) {
        return inverseColorMap[((red << ((2 * bits) - truncationBits)) & redQuantizationMask)
        + ((green << ((1 * bits) - truncationBits)) & greenQuantizationMask)
        + ((blue >> (truncationBits)) & blueQuantizationMask)] & 0xFF;
    }
}
